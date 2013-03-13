package LandingSite::Delivery::Controller::AdNetworkTraffic;
use Moose;
use File::Spec::Functions qw(catfile);
use JSON::XS;
use Data::Dumper;
use File::Slurp;

use namespace::autoclean;

BEGIN { extends 'Catalyst::Controller'; }

sub redirect_to_action {
    my ($c, $controller, $action, @params) =@_;

    $c->detach;
}

sub base : Chained('/') PathPart('adnetwork') CaptureArgs(1) {
    my ($self, $c, $adnetwork_id) = @_;
    $c->session->{adnetwork_id} = $adnetwork_id;
}

sub campaign : Chained('base') PathPart('campaign') CaptureArgs(1) {
    my ($self, $c, $campaign_id) = @_;
    $c->session->{campaign_id} = $campaign_id;
}

sub adgroup : Chained('campaign') PathPart('adgroup') CaptureArgs(1) {
    my ($self, $c, $adgroup_id) = @_;
    $c->session->{adgroup_id} =  $adgroup_id;
}

sub listing : Chained('adgroup') PathPart('listing') CaptureArgs(1) {
    my ($self, $c, $listing_id) = @_;
    $c->session->{listing_id} =  $listing_id;
}

sub market_vector : Chained('listing') PathPart('market_vector') CaptureArgs(1) {
    my ($self, $c, $mrkt_vec_id) = @_;
    $c->session->{market_vector_id} =  $mrkt_vec_id;
}

sub view : Chained('market_vector') PathPart('view') Args(0) {
    my ($self, $c) = @_;
    $DB::single=1;
    my $item = $c->stash->{item};
    $c->log->debug(Dumper($c->session));
#    my $mrkt_vec_id = 1;
    my $mrkt_vec_id = $c->session->{market_vector_id};
    my $path = $c->path_to('root', 'src', 'site', 'market_vector', $mrkt_vec_id, "$mrkt_vec_id.json");
    my $contents = File::Slurp::read_file($path->stringify);
    my $data = decode_json $contents;
    my $landing_site_id = shift [keys %{$data->{landing_site}}];
    my $page_name = "$landing_site_id" . $c->session->{adgroup_id} . ".html";
    my $landing_path = catfile('site', 'landing_site', $landing_site_id,  $page_name);
    $c->response->redirect($c->uri_for('1.html'));
}

__PACKAGE__->meta->make_immutable;
no Moose;
1;

__END__
