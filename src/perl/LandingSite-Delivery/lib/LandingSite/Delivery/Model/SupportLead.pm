package LandingSite::Delivery::Model::SupportLead;
use Moose;
extends 'Catalyst::Model::Adaptor';
use namespace::autoclean;
use LandingSite::SupportLead;
__PACKAGE__->config(class => 'LandingSite::SupportLead');
__PACKAGE__->meta->make_immutable;
no Moose;
1;
