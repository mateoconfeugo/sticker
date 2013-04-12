package CMS::Action::CreateDestinations::Test;
use strict;
use warnings;




use Carp;
use Test::Able;
use Test::More;

use CMS::Workflow::CreateCmsSite;
use CMS::Action::CreateDestinations;
use CMS::TestUtils qw(:all);
use Bric::Biz::Asset::Template;
use File::Slurp qw(slurp);
use Timestamp::Simple qw(stamp);
use Workflow::Factory qw( FACTORY );
use Workflow::Persister::File;

sub delete_test_data {
    my ($self, $args) = @_;
    my $dbh = cms_db();
    my $site_id = $self->{site_id};
    my $wf = $self->{wf};
    my @tables = (qw{site grp person usr category element_type output_channel 
			 workflow desk template server_type server});
    if($wf) {
    for my $t (@tables) {
#	my $config = eval { $wf->context->param($t) };
	my $assets = eval { $wf->context->param("$t" . "_assets") };
	if( $assets ) {
	    for my $asset (@$assets) {
		my $sql = "delete from $t where id = " . $asset->get_id();
		$dbh->do($sql);
	    }
	}
    }
}
    $dbh->do('delete from usr where login = "hsausage"');

=pod

    $dbh->do('delete from usr where login = "hsausage"');
    $dbh->do('delete from category site__id = ' . $site_id );
    $dbh->do('delete from element_type where key_name like"%test%"');
    $dbh->do('delete from output_channel where name like"%test%"');
    $dbh->do('delete from workflow where name like"%test%"');
    $dbh->do('delete from desk where name like"%test%"');
    $dbh->do('delete from template where description like"%test%"');
    $dbh->do('delete from server_type where name like"%test%"');
    $dbh->do('delete from server where login like"%test%"');

=cut

    $dbh->disconnect();
}

sub last_run {
    my ($self) = @_;
    my $token_path = qw(/home/value/workspace/mburns/persistence/last_workflow_id);
    my $persist_dir = qw(/home/value/workspace/mburns/persistence);
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
    my $site_wf = CMS::Workflow::CreateCmsSite->new();
    my $init = $site_wf->create_workflow_config();
    my $factory = Workflow::Factory->instance;
    $factory->add_config( persister => \@persisters );
    $factory->add_config( %$init);
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
    $self->delete_test_data();
    $self->{destroy_list} = [];
    my $site_wf = CMS::Workflow::CreateCmsSite->new();
    $self->{wf} = $site_wf;
};

test plan => 1, test_cms_create_destination_action => sub {
    my ($self) = @_;
    my $wf = $self->{wf}->workflow;
    $wf->context($self->init_context());
    eval {  $wf->execute_action('bricolage configuration') };
    my $sa =  $wf->context->param('site_asset');
    my $token_path = qw(/home/value/workspace/mburns/persistence/last_workflow_id);
    open my $fh , '>', $token_path or die "Cannot open $token_path  error message: $!\n";
    my $wf_id = $wf->{id};
    print $fh $wf_id;
    close $fh;
    my $factory = FACTORY->instance();
    my $persister = $factory->get_persister('CMS');
    $persister->update_workflow($wf);
    $self->{wf} = $wf;
    $self->{site_id} = $sa->get_id() if $sa;
    if($@) {
	warn $@->message();
    }
    isa_ok ($wf, 'Workflow');
    $self->delete_test_data();
};

sub init_context {
    my ($self) = @_;
    my $context = Workflow::Context->new();
    $context->param( 'name' => 's123uktest' );
    $context->param( 'description'=> 'test run for building the main site' );
    $context->param( 'domain_name'=> 'search123.com' );
    my $groups = [ 
		   { name => 's123testuk operations',
		     description => '',
		     permanent => 1,
		     secret => 0},
		   { name => 's123testuk development',
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
			  name => 'info',
			  description => 'knowledge base',
			  directory => '/info',
			  uri => '/info',
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
		   name => 'test output channel two',
		   description => 'test category two',
		   active => '1'
	       },
	       {
		   name => 'test output channel one',
		   description => 'test category one',
		   active => '1'
	       }
	       );
    $context->param( output_channels => \@oc );
    my @wf = ( 
	       {   
		   name => 'content test workflow one',
		   description => 'content test workflow one',
		   start_desk => 'test two',
		   type => Bric::Biz::Workflow::STORY_WORKFLOW,
		   desks => [
			     {
				 name => 'test one',
				 description => 'test workflow two desk two',
				 publish => 0,
			     },
			     {
				 name => 'test two',
				 description => 'test workflow two desk two',
				 publish => 1,
				 }
			     ],
	       },
	       { 
		   name => 'media test workflow two',
		   description => 'content test workflow two',
		   start_desk => 'test four',
		   type => Bric::Biz::Workflow::MEDIA_WORKFLOW,
		   desks => [
			     {
				 name => 'test three',
				 description => 'test workflow one desk two',
				 publish => 0
				 },
			     {
				 name => 'test four',
				 description => 'test workflow one desk two',
				 publish => 1
				 }
			     ],
	       },
	       { 
		   name => 'template test workflow three',
		   description => 'content test workflow two',
		   start_desk => 'test five',
		   type => Bric::Biz::Workflow::TEMPLATE_WORKFLOW,
		   desks => [
			     {
				 name => 'test five',
				 description => 'test workflow three desk two',
				 publish => 0
				 },
			     {
				 name => 'test six',
				 description => 'test workflow three desk two',
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
			 output_channel => 'test output channel one', 
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
			 output_channel => 'test output channel one',
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
			 output_channel => 'test output channel one',
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
			 output_channel => 'test output channel one',
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
			 output_channel => 'test output channel one',
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
			 output_channel => 'test output channel one',
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
			 output_channel => 'test output channel one',
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
			 output_channel => 'test output channel one',
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
			 output_channel => 'test output channel one',
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
			 output_channel => 'test output channel one',
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
				name => 'test server type',
				description => 'test server type',
				move_method => 'File System',
				output_channels => 'test output channel one',
			    },
			    servers => [
					{
					    host_name => 'cms101.sk.valueclick.com',
					    os        => 'Unix',
					    doc_root  => q(/home/value/cms/sites),
					    login     => q(test),
					    password  => q(),
					    cookie    => q(),
					}
					],
			};
    $context->param( destinations => $destinations );
    return $context;
}

sub execute {
    __PACKAGE__->run_tests();
}
execute unless caller;
1;




