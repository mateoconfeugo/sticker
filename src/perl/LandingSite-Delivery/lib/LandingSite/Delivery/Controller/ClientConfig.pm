package LandingSite::Delivery::Controller::ClientConfig;
use Moose;
use namespace::autoclean;
BEGIN { extends 'Catalyst::Controller::REST' }

sub clientconfig: Local : ActionClass('REST') { }

sub index_GET {
    my ( $self, $c ) = @_;
   my $cfg = $c->config->{clientConfig};
    $self->status_ok($c, entity => => $cfg);
}

sub clientconfig_GET {
   my ( $self, $c) = @_;
   my $cfg = $c->config->{clientConfig};
   $self->status_ok($c, entity => $cfg );
}

__PACKAGE__->meta->make_immutable;
1;
