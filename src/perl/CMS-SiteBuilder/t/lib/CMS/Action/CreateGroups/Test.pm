package CMS::Action::CreateGroups::Test;
use strict;
use warnings;




use Carp;
use Test::Able;
#use Test::More qw(no_plan);
use Test::More;
use Test::MockModule;
use Test::Exception;
use Moose::Meta::Class;

use CMS::Action::CreateGroups;
use CMS::Action::CreateSite;
use CMS::Workflow::CreateCmsSite;


use Bric::Biz::Site;
use Bric::Util::Grp;
use Workflow::Factory qw( FACTORY );

use CMS::TestUtils qw(:all);

startup site_startup  => sub {
    my ($self) = @_;
#    my $file = $ENV{CMS_App_Biz_Action_CreateSite_Test};
#    unless ($p = do $file ) {
#        warn "couldn't parse $file: $@" if $@;
#        warn "couldn't do $file: $!"    unless defined $p;
#        warn "couldn't run $file"       unless $p;
#    }
    
};

setup groups_setup => sub {
    my ($self) = @_;
#    $self->{action} = CMS::Action::CreateGroups->new();
#    my $obj  = CMS::Workflow::CreateCmsSite->new();
#   $self->{wf} = $obj->workflow();
    $self->{destroy_list} = [];
};

teardown groups_destruction => sub {
    my ($self) = @_;
    for my $name (@{$self->{destroy_list}}) {
	my $sql = "delete from grp where name = ?";
	my $dbh = cms_db();
	$dbh->do($sql, {}, ($name));
    }
};

test plan => 1, test_bric_group_obj => sub {
    my ($self) = @_;
    my $init = { name => 's123testuk operations',
	      description => '',
	      permanent => 1,
	      secret => 0};
    my $grp = Bric::Util::Grp->new($init);
    $grp->save();
    push @{$self->{destroy_list}}, $grp->{name}; 
    isa_ok($grp, 'Bric::Util::Grp');
};

=pod

test plan => 1, test_site_creation => sub {
    my ($self) = @_;
    my $action = $self->{action};
    isa_ok( $action, 'CMS::Action::CreateGroups');
#    $action->execute($self->{wf});
#    my $site = $action->site();
#    isa_ok( $site, 'Bric::Biz::Site');    
};

=cut

sub execute {
    __PACKAGE__->run_tests();
}
execute unless caller;

__PACKAGE__->meta->make_immutable;
no Moose;

1;
