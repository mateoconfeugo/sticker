package LandingSite::Delivery::Controller::Lead;
use Moose;
use namespace::autoclean;
use JSON::XS;
use DateTime;
use DateTime::Format::MySQL;

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
   my $model = $c->model('Lead');
   my $errors = {};
   my $result = $model->validate({query=>$lead});
   if($result->has_error) {
       for my $k (@{$result->error}) {
	   for my $type (@{$result->error($k)}) {
	       push @{$errors->{$k}}, $type;
	   }
       }
       my $msg = encode_json $errors;
       $self->status_bad_request($c, message => $msg);
   } else {
       $lead->{market_vector} = $c->session->{market_vector_id};
       $lead->{adnetwork} =  $c->session->{adnetwork_id};
       $lead->{campaign} =  $c->session->{campaign_id};
       $lead->{adgroup} =  $c->session->{adgroup_id};
       $lead->{listing} =  $c->session->{listing_id};
       $lead->{event_time} = DateTime::Format::MySQL->format_datetime(DateTime->now);
       $lead->{offer} = $c->session->{offer_id};
       $lead->{landing_site} = $c->session->{landing_site};
       $lead->{user_agent} = $c->req->user_agent;
       my $data = $model->create({lead=>$lead});
       $self->status_created($c, location => $c->req->uri, entity => {lead => $lead});
   }
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
