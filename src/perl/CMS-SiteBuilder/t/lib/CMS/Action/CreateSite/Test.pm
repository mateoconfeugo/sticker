package CMS::Action::CreateSite::Test;
use strict;
use warnings;



use Carp;
use Test::Able;
#use Test::More qw(no_plan);
use Test::More;
use Test::MockModule;
use Test::Exception;
use Moose::Meta::Class;

use CMS::Action::CreateSite;
use CMS::Workflow::CreateCmsSite;

use Bric::Biz::Site;
use Workflow::Factory qw( FACTORY );

startup site_startup  => sub {
    my ($self) = @_;
#    my $file = $ENV{CMS_App_Biz_Action_CreateSite_Test};
#    unless ($p = do $file ) {
#        warn "couldn't parse $file: $@" if $@;
#        warn "couldn't do $file: $!"    unless defined $p;
#        warn "couldn't run $file"       unless $p;
#    }
    
};

setup site_setup => sub {
    my ($self) = @_;
    $self->{action} = CMS::Action::CreateSite->new();
    my $obj  = CMS::Workflow::CreateCmsSite->new();
    $self->{wf} = $obj->workflow();
};

test plan => 1, test_site_creation => sub {
    my ($self) = @_;
    my $action = $self->{action};
    isa_ok( $action, 'CMS::Action::CreateSite');
    $action->execute($self->{wf});
    my $site = $action->site();
    isa_ok( $site, 'Bric::Biz::Site');    
};

sub execute {
    __PACKAGE__->run_tests();
}
execute unless caller;

__PACKAGE__->meta->make_immutable;
no Moose;

1;
