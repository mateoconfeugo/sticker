package LandingSite::Delivery::Controller::ThankYou;
use Moose;
use namespace::autoclean;

BEGIN { extends 'Catalyst::Controller'; }

sub index :Path :Args(0) {
    my ( $self, $c ) = @_;
        $c->response->body('Matched LandingSite::Delivery::Controller::ThankYou in ThankYou.');
    $c->{stash}->{template} = 'thank_you.tt';
    $c->forward('View::HTML');
}

__PACKAGE__->meta->make_immutable;
no Moose;
1;
