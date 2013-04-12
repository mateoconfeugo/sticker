package CMS::App::SiteBuilder::Test;
use strict;
use warnings;




use Carp;
use Test::Able;

use Test::More;
use Test::MockModule;
use Test::Exception;

use CMS::Workflow::CreateCmsSite;
use CMS::TestUtils qw(:all);

startup sitebuilder_startup  => sub {
    my ($self) = @_;
};

shutdown sitebuilder_shutdown  => sub {
    my ($self) = @_;
};

setup sitebuilder_setup => sub {
    my ($self) = @_;
    $self->{destroy_list} = [];
    my $app = CMS::App::SiteBilder->new({config_file_path=>undef});
    $self->{app} = $app;
};

teardown sitebuilder_destruction => sub {
    my ($self) = @_;
};

test plan => 1, test_run_sitebuilder => sub {
    my ($self) = @_;
    my $app = $self->{app};
    isa_ok($app, 'CMS::Workflow::CreateCmsSite');
    my $opts = {config_file => q(/home/value/workspace/mburns/search123.com)};
    $app->execute($opts);
};

sub execute {
    __PACKAGE__->run_tests();
}
execute unless caller;
1;
