package CMS::Action::ImportMediaResources::Test;
use strict;
use warnings;
use Data::Dumper;

use lib qw(/home/value/src/CMS-App/t/lib);
use lib qw(/home/value/src/CMS-App/lib);
use lib qw(/home/value/perl5/bricolage/lib);
use local::lib;

BEGIN {
$ENV{BRICOLAGE_ROOT} = '/home/value/perl5/bricolage';
$ENV{APACHE_BIN} = '/usr/sbin/httpd';
}

use Bric::Biz::Asset::Template;
use Carp;
use File::Slurp qw(slurp);
use File::Spec::Functions;
use Test::Able;
use Test::More;
use Timestamp::Simple qw(stamp);
use Workflow::Factory qw( FACTORY );
use Workflow::Persister::File;
use XML::Simple;

use CMS::Workflow::CreateCmsSite;
use CMS::Action::ImportMediaResources;
use CMS::TestUtils;

with 'CMS::TestUtils';


has cms_site_config => ( 
			 is => 'rw', 
			 default => '/home/value/websites/search123.com/conf/search123_cms_config.xml',
			 );

has storage_path => (
                         is => 'rw',
                         default => '/home/value/tmp/persistence'
                         );

sub delete_test_data {
    my ($self, $args) = @_;
    my $dbh = $self->cms_db;

    my $wf = $self->{wf};
    my @row_ary  = $dbh->selectrow_array("select id from site where name = 'search123'");
    my $site_id = $row_ary[0] if @row_ary;
    return unless $site_id;
#    return unless $wf;

    my @statements = (
               q{delete from workflow where site__id = ?},
               q{delete from template where site__id = ?},
               q{delete from output_channel where site__id = ?},
               q{delete from category where site__id = ?},
               q{delete from server_type where site__id = ?},
               q{delete from media where site__id = ?},
               q{delete from story where site__id = ?},
               q{delete from story_uri where site__id = ?},                                                                  
               q{delete from site where id = ?},
           );
    for my $sql (@statements) {
        $dbh->do($sql, {}, $site_id);
    }
    $dbh->do('delete from desk where name like "%s123%"');
    $dbh->do('delete from element_type');
    

=pod    
    eval {
    # user group and workflow related tables.
    my $ctx = $self->init_context();
    my $wflws_ref =$ctx->param('workflows');
    for my $wf_ref ( @{ $wflws_ref } ) {
	for my $desk ( @{$wf_ref->{desks}} ) {
	    $dbh->do('delete from desk where name = ?', {} , ($desk->{name}));
	}	
    }
    
    my $oc_assets = $wf->context->param("output_channel_assets");    
    for my $oc (@{$oc_assets}) {
	my $sql = 'delete from story_instance where primary_oc__id = ?';
	$dbh->do($sql, {}, ($oc->get_id()));	
    }

    if ($ctx->param('users')) {
	$dbh->do("delete from usr where login = ?", {}, ($_->{login})) for @{$ctx->param('users')};
    }
    if ($ctx->param('groups')) {
	$dbh->do("delete from grp where name = ?", {}, ($_->{name})) for @{$ctx->param('groups')};
    }

    # element related
    my $path = $ctx->param('element_types_dir');
    if(-d $path) {
	my $file; my $dir;
	unless (opendir $dir, $path) { warn "Could not open directory $path: Error $!\n"; return;}
	while($file = readdir $dir) {
	    next if $file eq '.' || $file eq '..';
	    $file =~ s/^(.*)\.xml$/$1/g;
	    my $sql = "delete from element_type where key_name = ?";
	    eval { $dbh->do($sql, {}, ($file)) };
	    warn "unable to delete element_type with keyname: $file " if($@);
	}
    }

    # site related.
    my @tables = (qw{ template category output_channel workflow server_type media story});
    if($wf) {
	for my $t (@tables) {
	    my $assets = $wf->context->param("$t" . "_assets");
	    if( $assets ) { # use cms object
		$dbh->do("delete from $t where id = " . $_->get_id()) for (@$assets);
	    }
	    elsif ($site_id) { # use relation to site
		$dbh->do("delete from $t where site__id = $site_id");
	    }
	}
	$dbh->do("delete from site where id = $site_id");
    }
};
    if($@) {
	$dbh->do("delete from site where id = $site_id");
    }

=cut

    $dbh->disconnect();
}

sub last_run {
    my ($self) = @_;
    my $persist_dir = $self->storage_path;
    my $token_path = catfile($persist_dir, 'last_workflow_id');

    my $workflow_id;
    if ( -e $token_path ) {
	$workflow_id =  slurp($token_path);
    }
    else { return undef; }
    my $path;
    if( $workflow_id ) {
	$path = $workflow_id . '_workflow';
    }
    else { return undef; }

    my @persisters = (
		      { name           => 'CMS',
			class          => 'Workflow::Persister::File',
			path           => $persist_dir, }
		      );
    my $site_wf = CMS::Workflow::CreateCmsSite->new({config_file_path=>$self->cms_site_config,
                                                     persistence_path => $persist_dir});
    my %init = (
                condition => $site_wf->conditions(),
                persister => $site_wf->persisters(),
                action    => $site_wf->actions(),
                workflow  => $site_wf->state_machine(),
            );
    my $factory = Workflow::Factory->instance;
    $factory->add_config( persister => \@persisters );
    $factory->add_config( %init);
    my $persister = $factory->get_persister( 'CMS' );
    my $wf = $factory->fetch_workflow( 'CreateCmsSite', $workflow_id);
    $self->{wf} = $wf;
}

startup destination_startup  => sub {
    my ($self) = @_;
    $self->last_run();
    $self->delete_test_data();
};

shutdown destination_shutdown  => sub {
    my ($self) = @_;
    $self->delete_test_data({site_id=>$self->{site_id}});
};

setup destination_setup => sub {
    my ($self) = @_;
    $self->{destroy_list} = [];
    my $path = $self->cms_site_config;
    my $persist_dir =  $self->storage_path;
    my $site_wf = CMS::Workflow::CreateCmsSite->new({config_file_path=>$path, persistence_path => $persist_dir});
    $self->{wf} = $site_wf;
};

test plan => 1, test_cms_create_destination_action => sub {
    my ($self) = @_;
    my $wf = $self->{wf};
    $wf->context($self->init_context());
    eval {  $wf->execute_action('bricolage configuration') };
    my $sa =  $wf->context->param('site_assets')->[0];
    my $token_path = catfile($self->storage_path, 'last_workflow_id');
    open my $fh , '>', $token_path or die "Cannot open $token_path  error message: $!\n";
    my $wf_id = $wf->id;
    print $fh $wf_id;
    close $fh;
    my $factory = FACTORY->instance();
    my $persister = $factory->get_persister('FileSystem');
    $persister->update_workflow($wf);
    $self->{wf} = $wf;
    if($sa) { 
	$self->{site_id} = $sa->get_id
    }
    if($@) {
	warn $@->message();
    }
    isa_ok ($wf, 'CMS::Workflow::CreateCmsSite');
    $self->delete_test_data();
};

sub init_context {
    my ($self) = @_;
    my $ctx = Workflow::Context->new();
    my $file_path = $self->cms_site_config;
    my $config = XMLin($file_path,
		       GroupTags => { categories => 'category', 
				      workflows => 'workflow', 
				      servers => 'server', 
				      templates => 'template', 
				      element_types => 'element_type', 
				      destinations => 'destination', 
				      users => 'user', 
				      groups => 'group', 
				      output_channels => 'output_channel', 
				      desks => 'desk', 
				      server_types => 'server_type' 
				      },
		       forcearray=>1);
    $ctx->{PARAMS} = $config;
    return $ctx;
}

sub execute {
    __PACKAGE__->run_tests();
}
execute unless caller;
1;

=pod

	
    $context->param('site_name'=>'dev_search123');
    $context->param('description'=>'the main site');
    $context->param('domain_name'=>'dev_search123.com');
    $context->param('bric_username'=>'admin');
    $context->param('bric_password'=>'hot12dog');
    $context->param('element_types_dir'=>q(/home/value/workspace/mburns/element_types));
    $context->param('bric_soap'=>q(/home/value/perl5/home/value/perl5/bricolage/bin/bric_soap));
    $context->param('bric_server'=>'cms101.sk.valueclick.com:8095');
    $context->param('media_publish_desk'=>'s123 media publish');
    my $groups = [ 
		   { name => 'dev_search123 operations',
		     description => '',
		     permanent => 1,
		     secret => 0},
		   { name => 'dev_search123 development',
		     description => '',
		     permanent => 1,
		     secret => 0},
		   ];
    $context->param( groups => $groups );
    my @users = ({
	'fname' => 'ham', 
	'mname' => 'sausage',
	'prefix' => 'Mr',
	'suffix' => 'Jr',
	'login' => 'hsausage',
	'lname' => 'lettuce', 
	'password' => 'password',

	});
    $context->param( users => \@users );
    my @categories = (
		      {
			  name => 's123root',
			  description => 'root',
			  directory => '/',
			  uri => '/',
		      },
		      {
			  name => 'about',
			  description => 'knowledge base',
			  directory => '/about',
			  uri => '/about',
		      },
		      {
			  name => 'partner',
			  description => 'partner resources',
			  directory => '/partner',
			  uri => '/partner',
		      },
		      {
			  name => 'advertiser',
			  description => 'advertiser resources',
			  directory => '/advertiser',
			  uri => '/advertiser',
		      },
		      {
			  name => 'media css',
			  description =>'user generated css',
			  directory => '/media/css',
			  uri => '/media/css',
		      },
		      {
			  name => 'site css',
			  description =>'developer generated css',
			  directory => '/site/css',
			  uri => '/site/css',
		      },
		      {
			  name => 'media javascript',
			  description =>'user generated javascript',
			  directory => '/media/js',
			  uri => '/media/js',
		      },
		      {
			  name => 'site javacript',
			  description =>'developer generated javascript',
			  directory => '/site/js',
			  uri => '/site/js',
		      },
		      {
			  name => 'site images',
			  description =>'site level images',
			  directory => '/site/images',
			  uri => '/site/images',
		      },
		      {
			  name => 'login',
			  description => 'login resources',
			  directory => '/login',
			  uri => '/login',
		      }
		      );
    $context->param( categories => \@categories );
    my @element_types = (
			 {
			     name  => 'test one', 
			     key_name => 'test_one',
			     description => 'test element type',
			     top_level => 1,
			     paginated => 1,
			     fixed_uri => 1,
			     related_story => 1,
			     related_media => 1,
			     media => 0, 
			     displayed  => 1,
			     },
			 {
			     name  => 'test two', 
			     key_name => 'test_two',
			     description => 'test element type',
			     top_level => 0,
			     paginated => 0,
			     fixed_uri => 0,
			     related_story => 0,
			     related_media => 0,
			     media => 0, 
			     displayed  => 0,
			     },
			 );
    $context->param( element_types => \@element_types );
    my @oc = ( 
	       {
		   name => 'Specified Web',
		   description => 'channel that allows slugname to be the filename',
		   active => '1',
		   filename => 'index',
		   file_ext => 'html',
		   include_channels => 'Index Web, Web',
		   fixed_uri_format =>  '/%{categories}/',
		   uri_format => '/%{categories}/%Y/%m/%d/%{slug}/',
		   use_slug => 1,
		   is_primary => 0,
	       },
	       {
		   name => 'Index Web',
		   description => 'channel that makes elements be named index',
		   active => '1',
		   filename => 'index',
		   file_ext => 'html',
		   include_channels => 'Web',
		   fixed_uri_format => '/%{categories}/',
		   uri_format => '/%{categories}/%Y/%m/%d/%{slug}/',
		   use_slug => 0,
		   is_primary => 1,
	       }
	       );
    $context->param( output_channels => \@oc );
    my @wf = ( 
	       {   
		   name => 'story workflow',
		   description => 'content workflow',
		   start_desk => 's123 content edit',
		   type => Bric::Biz::Workflow::STORY_WORKFLOW,
		   desks => [
			     {
				 name => 's123 content edit',
				 description => 'Edit Desk',
				 publish => 0,
			     },
			     {
				 name => 's123 content copy',
				 description => 'Copy Desk',
				 publish => 0,
			     },
			     {
				 name => 's123 content legal',
				 description => 'Legal Desk',
				 publish => 0,
			     },
			     {
				 name => 's123 content publish',
				 description => 'Publish Desk',
				 publish => 1,
			     }
			     ],
	       },
	       { 
		   name => 'media workflow ',
		   description => 'media workflow',
		   start_desk => 's123 media art',
		   type => Bric::Biz::Workflow::MEDIA_WORKFLOW,
		   desks => [
			     {
				 name => 's123 media art',
				 description => 'Art Desk',
				 publish => 0,
			     },
			     {
				 name => 's123 media legal',
				 description => 'Legal Desk',
				 publish => 0,
			     },
			     {
				 name => 's123 media publish',
				 description => 'Publish Desk',
				 publish => 1,
			     }
			     ],
	       },
	       { 
		   name => 'template workflow',
		   description => 'template workflow',
		   start_desk => 's123 template develop',
		   type => Bric::Biz::Workflow::TEMPLATE_WORKFLOW,
		   desks => [
			     {
				 name => 's123 template develop',
				 description => 'developer desk',
				 publish => 0
				 },
			     {
				 name => 's123 template deploy',
				 description => 'deployment desk',
				 publish => 1
				 }
			     ],
	       },
	       );
    $context->param(workflows => \@wf);
    $context->param(template_base_dir => '/home/value/workspace/mburns/templates/s123uk');
    my @templates = (
		     {
			 description => 'basic story element template',
			 deploy_date => stamp(),
			 expire_date => '20110610170055',
			 workflow_id => '',  # Assign programmatically
			 output_channel => 'Index Web',
			 output_channel__id => '',  # Assign programmatically
			 tplate_type => Bric::Biz::Asset::Template::ELEMENT_TEMPLATE,
			 name => undef,
			 element_type => 'Story',
			 element_type_id => '',
			 category => 's123root',
			 category__id => '', # Assign programmatically
#			 file_type => 'mason',
		     },
		     {
			 description => 'holds the main content of the page',
			 deploy_date => stamp(),
			 expire_date => '20110610170055',
			 workflow_id => '',  # Assign programmatically
			 output_channel => 'Index Web',
			 output_channel__id => '',  # Assign programmatically
			 tplate_type => Bric::Biz::Asset::Template::UTILITY_TEMPLATE,
			 name => 'content',
			 category => 's123root',
			 category__id => '',
		     },
		     {
			 description => 'the bottom layout framework of the site',
			 deploy_date => stamp(),
			 expire_date => '20110610170055',
			 workflow_id => '',  # Assign programmatically
			 output_channel => 'Index Web',
			 output_channel__id => '',  # Assign programmatically
			 tplate_type => Bric::Biz::Asset::Template::UTILITY_TEMPLATE,
			 name => 'footer',
			 category => 's123root',
			 category__id => '',
		     },
		     {
			 description => 'build the head node of the html dom',
			 deploy_date => stamp(),
			 expire_date => '20110610170055',
			 workflow_id => '',  # Assign programmatically
			 output_channel => 'Index Web',
			 output_channel__id => '',  # Assign programmatically
			 tplate_type => Bric::Biz::Asset::Template::UTILITY_TEMPLATE,
			 name => 'head',
			 category => 's123root',
			 category__id => '',
		     },
		     {
			 description => 'the top layout framework of the site',
			 deploy_date => stamp(),
			 expire_date => '20110610170055',
			 workflow_id => '',  # Assign programmatically
			 output_channel => 'Index Web',
			 output_channel__id => '',  # Assign programmatically
			 tplate_type => Bric::Biz::Asset::Template::UTILITY_TEMPLATE,
			 name => 'header',
			 category => 's123root',
			 category__id => '',
		     },
		     {
			 description => 'Javascript libraries to include',
			 deploy_date => stamp(),
			 expire_date => '20110610170055',
			 workflow_id => '',  # Assign programmatically
			 output_channel => 'Index Web',
			 output_channel__id => '',  # Assign programmatically
			 tplate_type => Bric::Biz::Asset::Template::UTILITY_TEMPLATE,
			 name => 'included_js_lib',
			 category => 's123root',
			 category__id => '',
		     },
		     {
			 description => 'Top DOM level framework',
			 deploy_date => stamp(),
			 expire_date => '20110610170055',
			 workflow_id => '',  # Assign programmatically
			 output_channel => 'Index Web',
			 output_channel__id => '',  # Assign programmatically
			 tplate_type => Bric::Biz::Asset::Template::UTILITY_TEMPLATE,
			 name => 'top',
			 category => 's123root',
			 category__id => '',
		     },
		     {
			 description => 'Framwork of the site top level content layout struture',
			 deploy_date => stamp(),
			 expire_date => '20110610170055',
			 workflow_id => '',  # Assign programmatically
			 output_channel => 'Index Web',
			 output_channel__id => '',  # Assign programmatically
			 tplate_type => Bric::Biz::Asset::Template::UTILITY_TEMPLATE,
			 name => 'wrap',
			 category => 's123root',
			 category__id => '',
		     },
		     {
			 description => 'stylesheet',
			 deploy_date => stamp(),
			 expire_date => '20110610170055',
			 workflow_id => '',  # Assign programmatically
			 output_channel => 'Index Web',
			 output_channel__id => '',  # Assign programmatically
			 tplate_type => Bric::Biz::Asset::Template::UTILITY_TEMPLATE,
			 name => 'style',
			 category => 's123root',
			 category__id => '',
		     },
		     {
			 description => 'site top autohandler',
			 deploy_date => stamp(),
			 expire_date =>  '20110610170055',
			 workflow_id => '',  # Assign programmatically
			 output_channel => 'Index Web',
			 output_channel__id => '',  # Assign programmatically
			 tplate_type => Bric::Biz::Asset::Template::CATEGORY_TEMPLATE,
			 name => 'autohandler',
			 element_type => 'test_one',
			 element_type_id => '',
			 category => 's123root',
			 category__id => '', # Assign programmatically
		     },

		     );
    $context->param( templates => \@templates );
    my $destinations = 
			{ 
			    server_type => { 
				name => 'Network share',
				description => 'NFS share connecting cms to web server',
				move_method => 'File System',
				output_channels => 'Index Web, Specified Web',
			    },
			    servers => [
					{
					    host_name => 'cms101.sk.valueclick.com',
					    os        => 'Unix',
					    doc_root  => q(/home/value/cms/sites/dev_search123.com),
					    login     => q(test),
					    password  => q(),
					    cookie    => q(),
					}
					],
			};
    $context->param( destinations => $destinations );
    my $media_config = {
	media_upload_app => q(/home/value/bin/bric_media_upload ),
	password => 'hot12dog',
	username => 'admin',
	tar => q(/home/value/workspace/mburns/s123resources.tar),
	bric_soap => q(/home/value/perl5/home/value/perl5/bricolage/bin/bric_soap),
	server => q(cms101.sk.valueclick.com:8095)
	};
    $context->param( media_resources_config => $media_config );

=cut




