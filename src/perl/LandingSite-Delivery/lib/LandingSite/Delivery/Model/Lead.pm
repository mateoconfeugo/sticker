package LandingSite::Delivery::Model::Lead;
use Moose;
extends 'Catalyst::Model::Adaptor';
use namespace::autoclean;
use LandingSite::Lead;
__PACKAGE__->config(class => 'LandingSite::Lead');
__PACKAGE__->meta->make_immutable;
no Moose;
1;
