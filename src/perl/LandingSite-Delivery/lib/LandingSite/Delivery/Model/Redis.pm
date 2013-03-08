package LandingSite::Delivery::Model::Redis;
use strict;
use warnings;
use base 'Catalyst::Model::Redis';

__PACKAGE__->config(
#    host => "192.168.1.5",
#    host => "localhost",
    host => "76.173.9.205",
    port => "6379",
    utf8 => "1",
);

1;

=head1 NAME

LandingSite::Delivery::Model::Redis - Redis Catalyst model

=head1 DESCRIPTION

Redis Catalyst model component. See L<Catalyst::Model::Redis>

