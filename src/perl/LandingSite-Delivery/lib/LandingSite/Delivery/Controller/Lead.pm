package LandingSite::Delivery::Controller::Lead;
use Moose;
use namespace::autoclean;

with "LandingSite::DB";

BEGIN { extends 'Catalyst::Controller::REST' }

sub lead : Local : ActionClass('REST') {}

#sub index : Path : Args(0) {
#    my ( $self, $c ) = @_;
#    $self->status_ok($c, entity => $leads);
#}

sub  lead_POST : Path : Args(0) {
   my ($self, $c) = @_;
   $DB::single=1;
   my $lead = $c->req->data;
   my $data = $c->model('Lead')->create({lead=>$lead});
   $self->status_created($c, location => $c->req->uri, entity => {lead => 'blah'});
}

sub lead_GET : Path : Args(1) {
   my ($self, $c, $lead_id) = @_;
   $DB::single=1;
   my $data = $c->model('Lead')->get_lead({id=>$lead_id});
   $self->status_ok($c, entity => {lead => 'BINGO'});   
}


=pod






sub lead_PUT {
  my ( $self, $c, $lead_id) = @_;
  my $lead = $c->req->data->{lead};
  my $response = $c->model('Lead')->create_update({lead=>$lead});
  $self->status_ok($c, entity=>$response);
}


sub lead_DELETE {
   my ($self, $c, $lead_id) = @_;
   $DB::single=1;
   my $data = $c->model('Lead')->delete_lead({id=>$lead_id});
   $self->status_ok($c, entity => {lead => $data});   
}

=cut

__PACKAGE__->meta->make_immutable;
no Moose;
1;
