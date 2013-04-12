package CMS::Workflow::CreateCmsSite::Test;
use strict;
use warnings;
no warnings 'redefine';


use Carp;
use Test::Able;
#use Test::More qw(no_plan);
use Test::More;
use Test::MockModule;
use Test::Exception;
use Moose::Meta::Class;

use CMS::Workflow::CreateCmsSite;

use CMS::TestUtils qw(:all);

use Bric::Biz::Site;
use Workflow::Factory qw( FACTORY );

{
    package TestCreateCmsSite;
    use strict;
    use warnings;
    use Moose;
    use XML::Simple;
    
    extends 'CMS::Workflow::CreateCmsSite';



override 'actions_declaration' => sub  {
	my ($self, $args) = @_;
	my $actions = <<ACTIONS;
<actions>
  <action name="INITIAL" class="Workflow::Action::Null" />
  <action name="bricolage configuration" class="Workflow::Action::Null" />
  <action name="create site" class="CMS::Action::CreateSite" />
  <action name="create groups" class="CMS::Action::CreateGroups" />
  <action name="create users" class="CMS::Action::CreateUsers" />
  <action name="create alerts" class="Workflow::Action::Null" />
  <action name="create destinations" class="Workflow::Action::Null" />
  <action name="create workflows" class="Workflow::Action::Null" />
  <action name="create categories" class="Workflow::Action::Null" />
  <action name="create elements" class="Workflow::Action::Null" />
  <action name="create media type" class="Workflow::Action::Null" />
  <action name="create templates" class="Workflow::Action::Null" />
  <action name="create media content" class="Workflow::Action::Null" />
  <action name="create story content" class="Workflow::Action::Null" />
</actions>
ACTIONS
   return  $self->translate_xml({config=>$actions, type=>'action'}) ;

    }
}

startup create_cms_site_wf_startup  => sub {
    my ($self) = @_;
};

setup site_setup => sub {
    my ($self) = @_;
    my $obj  = TestCreateCmsSite->new();
    $self->{site_wf} = $obj;
    $self->{wf} = $obj->workflow();
    $self->{wf}->context( $self->init_context() );
};



teardown site_destruction => sub {
    my $dbh = cms_db();
    $dbh->do('delete from site where name = "s123uktest"');
    $dbh->do('delete from grp where name like "%test%"');
};

test plan => 1, test_bric_site_obj => sub {
    my ($self) = @_;
    ok( my $site = Bric::Biz::Site->new, "Construct new site" );
};

test plan => 2, test_site_creation => sub {
    my ($self) = @_;
    isa_ok( $self->{wf}, 'Workflow');
    isa_ok( $self->{site_wf}, 'CMS::Workflow::CreateCmsSite');
    $self->{site_wf}->execute_action('bricolage configuration');
    my $ph = 'ham';
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
#    $context->parm( 'asset_priority'=> '' );
#    $context->parm( 'asset_element_type_id'=> '' );
#    $context->parm( 'cms_source'=> '' );
#    $context->parm( 'cms_user'=> '' );
#    $context->parm( 'site_id'=> '' );
#    $context->parm( 'asset_active'=> '' );
#    $context->parm( 'asset_title'=> '' );
#    $context->parm( 'asset_description'=> '' );
#    $context->parm( 'asset_slug'=> '' );
#    $context->parm( 'cover_date'=> '' );
#    $context->parm( 'asset_name' => '' );
    return $context;
}


sub execute {
    __PACKAGE__->run_tests();
}
execute unless caller;

__PACKAGE__->meta->make_immutable;
no Moose;

1;
