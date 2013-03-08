package LandingSite::Delivery::Controller::Root;
use Moose;
use Try::Tiny;
use File::Spec::Functions qw(catfile);
use namespace::autoclean;
use JSON::XS;
BEGIN { extends 'Catalyst::Controller' }

sub  pages : Regex('^(\d*)_(.*)\.html$')  {
    my ($self, $c) = @_;
    $DB::single=1;
    my $landing_site_id = $c->req->captures->[0];
    my $number = $c->req->captures->[1];
    my $token = $c->session->{token};
    if($token) {     # Inter-Site?
	my $mrkt_id =  $c->session->{market_vector_id};
	my $adgroup_id = $c->session->{adgroup_id} || 'default';
	my $path = catfile("landing_site", "$landing_site_id_$number.html");
	    $c->{stash}->{template} = $c->uri_for($path);
    } else {   # Organic
	$c->{stash}->{template} = "sample_page.tt";
    }
    $c->forward('View::HTML');
}

sub jemplate : Path('/js/jemplate') {
    my($self, $c) = @_;
    $c->forward('View::Jemplate');
}

sub runtime : Path('/js/Jemplate.js') {
    my ( $self, $c ) = @_;
    $c->stash->{jemplate} = {
        runtime => 1,
        files   => [],
    };
    $c->forward('View::Jemplate');
}

sub default :Path {
    my ( $self, $c ) = @_;
    $c->response->body( 'Page not found' );
    $c->response->status(404);
}

#sub end : ActionClass('RenderView') {}

sub finalize_error {
    my $c = shift;
    $c->res->header('X-Error' => $c->error->[0]);
    $c->NEXT::finalize_error;
}

__PACKAGE__->meta->make_immutable;
no Moose;
1;
