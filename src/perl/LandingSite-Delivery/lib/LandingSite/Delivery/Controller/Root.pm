package LandingSite::Delivery::Controller::Root;
use Moose;
use Try::Tiny;
use File::Spec::Functions qw(catfile);
use File::Slurp qw(read_file);
use namespace::autoclean;
use JSON::XS;
BEGIN { extends 'Catalyst::Controller' }

sub get_nav_header {
    my ($self, $args) = @_;
    my $index = $args->{index};
    my $site_data = $args->{site_data};
    my @pages = @{$site_data->{page}};
    my $page = $pages[$index - 1];
    my @insets = @{$page->{inset}};
    my $inset = $insets[0]->{copy};
    return $inset;
}

sub  pages : Regex('(\d)(\d)?\.html$')  {
    my ($self, $c) = @_;
    $DB::single=1;
    my $landing_site_id = $c->req->captures->[0] || 1;
    my $number = $c->req->captures->[1];
    my $token = $c->session->{token};
    my $mrkt_id =  $c->session->{market_vector_id} || 1;
    my $adgroup_id = $c->session->{adgroup_id} || 'default';
    my $path;
    if( $number && $landing_site_id ) {
	$path = catfile('site', "landing_site", "$landing_site_id",  "$landing_site_id". "$number.html") ;
    } else {
	$path = catfile('site', "landing_site", "$landing_site_id",   "$landing_site_id.html");
    }
    my @files = ();
    my @pages = ();
    my @headers = ();
    try {
	my $path = $c->path_to('root', 'src', 'site', "landing_site", "$landing_site_id");
	my $i = 1;
	opendir (my $dir, $path->stringify);
	while (my $file = readdir($dir)) {
	    next if $file =~ m/^\.+/xms;
	    my $site_data = decode_json($c->model('Redis')->redis->get($mrkt_id))->{'landing_site'}->[0];
	    my $header = $self->get_nav_header({index=>$i, site_data=>$site_data});
	    push @files, [$i, $file, $header];
	    $i += 1;
	}
	closedir($dir) 
    } catch {};
    my $path = $c->path_to('root', 'src', 'site', "landing_site", "$landing_site_id")->stringify();
    for my $pair (@files) {
	my $file = $pair->[1];
	my $content = File::Slurp::read_file(File::Spec::Functions::catfile($path,$file));
	push @pages, {order=>$pair->[0], content_data=>$content, header=>$pair->[2]};
    }

    my $selected_page = File::Slurp::read_file($c->path_to('root', 'src', 'site', "landing_site", "$landing_site_id", "$landing_site_id.html")->stringify());    
    $c->{stash}->{pages} = \@pages;
    $c->{stash}->{selected_page} = $selected_page;
    $c->{stash}->{template} = 'landing_page.tt';
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
