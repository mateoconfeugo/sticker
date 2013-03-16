package LandingSite::Delivery::Model::MarketVector;
use Moose;
extends 'Catalyst::Model::Adaptor';
use namespace::autoclean;
use LandingSite::Lead;
__PACKAGE__->config(class => 'LandingSite::MarketVector');
__PACKAGE__->meta->make_immutable;
no Moose;
1;
