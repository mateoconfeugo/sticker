package CMS::Action::CreateWorkflows::Test;
use strict;
use warnings;




use Carp;
use Test::Able;
use Test::More;

use CMS::Workflow::CreateCmsSite;
use CMS::Action::CreateWorkflows;
use CMS::TestUtils qw(:all);
use Bric::Biz::Workflow;

startup workflow_startup  => sub {
    my ($self) = @_;
    my $dbh = cms_db();
    $dbh->do('delete from site where name = "s123uktest"');
    $dbh->do('delete from grp where name like "%test%"');
    $dbh->do('delete from person where fname = "ham"');
    $dbh->do('delete from usr where login = "hsausage"');
    $dbh->do('delete from category where description like"%test%"');
    $dbh->do('delete from element_type where key_name like"%test%"');
    $dbh->do('delete from output_channel where name like"%test%"');
    $dbh->do('delete from workflow where name like"%test%"');
    $dbh->do('delete from desk where name like"%test%"');
    $dbh->disconnect();
};

shutdown workflow_shutdown  => sub {
    my ($self) = @_;
    my $dbh = cms_db();
    $dbh->do('delete from site where name = "s123uktest"');
    $dbh->do('delete from grp where name like "%test%"');
    $dbh->do('delete from person where fname = "ham"');
    $dbh->do('delete from usr where login = "hsausage"');
    $dbh->do('delete from category where description like"%test%"');
    $dbh->do('delete from element_type where key_name like"%test%"');
    $dbh->do('delete from output_channel where name like"%test%"');
    $dbh->do('delete from workflow where name like"%test%"');
    $dbh->do('delete from desk where name like"%test%"');
    $dbh->disconnect();
};

setup workflow_setup => sub {
    my ($self) = @_;
    $self->{destroy_list} = [];
    my $site_wf = CMS::Workflow::CreateCmsSite->new();
    $self->{wf} = $site_wf;
};

test plan => 1, test_cms_create_workflow_action => sub {
    my ($self) = @_;
    my $wf = $self->{wf}->workflow;
    $wf->context($self->init_context());
    eval {  $wf->execute_action('bricolage configuration') };
    if($@) {
	warn $@->message();
    }
    isa_ok ($wf, 'Workflow');
    my $dbh = cms_db();
    $dbh->do('delete from site where name = "s123uktest"');
    $dbh->do('delete from grp where name like "%test%"');
    $dbh->do('delete from person where fname = "ham"');
    $dbh->do('delete from usr where login = "hsausage"');
    $dbh->do('delete from category where description like"%test%"');
    $dbh->do('delete from element_type where key_name like"%test%"');
    $dbh->do('delete from output_channel where name like"%test%"');
    $dbh->do('delete from workflow where name like"%test%"');
    $dbh->do('delete from desk where name like"%test%"');
    $dbh->disconnect();
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
			  name => 'documentation test',
			  description => 'API Documentation test category ',
			  directory => '/doc',
			  uri => '/doc',
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
		   name => 'test category two',
		   description => 'test category two',
		   active => '1'
	       },
	       {
		   name => 'test category one',
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
		   name => 'content test workflow two',
		   description => 'content test workflow two',
		   start_desk => 'test four',
		   type => Bric::Biz::Workflow::STORY_WORKFLOW,
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
	       );
    $context->param( workflows => \@wf );
    return $context;
}

sub execute {
    __PACKAGE__->run_tests();
}
execute unless caller;
1;
