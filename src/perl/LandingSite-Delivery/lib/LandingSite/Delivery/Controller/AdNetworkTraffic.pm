package LandingSite::Delivery::Controller::AdNetworkTraffic;
use Moose;
use File::Spec::Functions qw(catfile);
use JSON::XS;
use namespace::autoclean;

BEGIN { extends 'Catalyst::Controller'; }

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
    use Data::Dumper;
    my $result = { listing_id => $c->session->{listing_id},
		   adnetwork_id => $c->session->{adnetwork_id},
		   campaign_id => $c->session->{campaign_id},
		   adgroup_id => $c->session->{adgroup_id}
    };
    $c->log->debug(Dumper($result));
    $c->session->{token} = $c->session->{market_vector_id};
#    $c->session->{token} = join '', ($c->session->{adnetwork_id},$c->session->{campaign_id},
#				     $c->session->{adgroup_id}) 

#	unless $c->session->{token};
    my $token = $c->session->{token};

    $c->session->{$token}->{market_vector_id} =  $c->model('Redis')->redis->get($token) 
	unless $c->session->{$token}->{market_vector};
    my $mrkt_vec = decode_json($c->model('Redis')->redis->get($token)) if $token;
    my $mrkt_vec_id = $c->session->{market_vector_id};
    $c->{stash}->{market_vector_id} = $mrkt_vec_id;
#    my $mrkt_id = $mrkt_vec->{id};
#    my $mrkt_id = 1;
    my $adgroup_id = $mrkt_vec->{ad_campaign}->{ad_campaign_adgroup}->[0]->{ad_grouping_id} 
    || $c->session->{adgroup_id} 
    || 'default';
    my $path = catfile("market_vector_$mrkt_vec_id", "adgroup_$adgroup_id", "index.html");
    $c->{stash}->{template} = $path || 'landing_page.tt';
    $c->forward('View::HTML');
}

__PACKAGE__->meta->make_immutable;
no Moose;
1;

__END__

 site
    ├── landing_site
    │   ├── 1
    │   │   ├── 1_one.html
    │   │   └── 1_two.html
    │   └── 2
    │       ├── 2_one.html
    │       ├── 2_three.html
    │       └── 2_two.html
    ├── market_matrix
    │   └── 1
    │       └── 1.html
    └── market_vector
        ├── 1
        │   └── 1.html
        ├── 2
        │   ├── 2.html
        │   └── 2.json
        └── 4
            ├── 4.html
            └── 4.json
