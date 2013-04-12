package CMS::Action::CreateUsers::Test;
use strict;
use warnings;




use Carp;
use Test::Able;

use Test::More;
use Test::MockModule;
use Test::Exception;

use CMS::Workflow::CreateCmsSite;
use CMS::Action::CreateUsers;
use CMS::TestUtils qw(:all);


startup user_startup  => sub {
    my ($self) = @_;
    my $dbh = cms_db();
    $dbh->do('delete from site where name = "s123uktest"');
    $dbh->do('delete from grp where name like "%test%"');
    $dbh->do('delete from person where fname = "ham"');
    $dbh->do('delete from usr where login = "hsausage"');
    $dbh->disconnect();
};

shutdown user_shutdown  => sub {
    my ($self) = @_;
    my $dbh = cms_db();
    $dbh->do('delete from site where name = "s123uktest"');
    $dbh->do('delete from grp where name like "%test%"');
    $dbh->do('delete from person where fname = "ham"');
    $dbh->do('delete from usr where login = "hsausage"');
    $dbh->disconnect();
};

setup user_setup => sub {
    my ($self) = @_;
    $self->{destroy_list} = [];
    my $site_wf = CMS::Workflow::CreateCmsSite->new();
    $self->{wf} = $site_wf;
};


teardown user_destruction => sub {
    my ($self) = @_;
    my $dbh = cms_db();
    $dbh->do('delete from site where name = "s123uktest"');
    $dbh->do('delete from grp where name like "%test%"');
    $dbh->do('delete from person where fname = "ham"');
    $dbh->do('delete from usr where login = "hsausage"');
    $dbh->disconnect();
};

test plan => 1, test_cms_create_user_action => sub {
    my ($self) = @_;
    my $wf = $self->{wf}->workflow;
    $wf->context($self->init_context());
    $wf->execute_action('bricolage configuration');
    isa_ok ($wf, 'Workflow');
    my $dbh = cms_db();
    $dbh->do('delete from site where name = "s123uktest"');
    $dbh->do('delete from grp where name like "%test%"');
    $dbh->do('delete from person where fname = "ham"');
    $dbh->do('delete from usr where login = "hsausage"');
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
	'password' => 'password',
	'lname' => 'bacon', 
	});
    $context->param( users => \@users );
    return $context;
}

sub execute {
    __PACKAGE__->run_tests();
}
execute unless caller;
1;
