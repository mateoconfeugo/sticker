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
    my $page = $args->{page};
    my @insets = @{$page->{inset}};
    my $inset = $insets[0]->{copy};
    return $inset;
}

sub assemble_site_files {
    my ($self, $c,  $args) = @_;
    my $landing_site_id = $args->{landing_site_id};
    my $site_data = $args->{site_data};
    my @files = ();
    try {
	my $landing_path_root = $c->path_to('root', 'src', 'site', "landing_site", $landing_site_id);
	my $pages  = $site_data->{'landing_site'}->{$landing_site_id}->{page};
	for ( my $i = 0; $i <= scalar @$pages; $i++ ) {
	    my $page = $pages->[$i];
	    my $header = $self->get_nav_header({index=>$i, page=>$page});
	    my $file;
	    if($i == 0) {
	      $file = $landing_site_id . '.html' ;
	    } else {
	      $file = $landing_site_id . $i . '.html' ;
	    }
	    my $file_path = catfile($landing_path_root, $file);
	    push @files, [$i, $file_path, $header] if -e $file_path;
	}
    } catch {};
    return \@files;
}

sub populate_content {
    my ($self, $c, $args) = @_;
    my $paths = $args->{paths};
    my $landing_site_id = $args->{landing_site_id};    
    my @pages = ();
    # Grab the html version of the pages and put in an array
    my $path = $c->path_to('root', 'src', 'site', "landing_site", "$landing_site_id")->stringify();
    for my $pair (@$paths) {
	my $file_path = $pair->[1];
	my $content = File::Slurp::read_file($file_path);
	push @pages, {order=>$pair->[0], content_data=>$content, header=>$pair->[2]};
    }
    return \@pages;
}

########################################################################
# PURPOSE: Assemble the pages of the site data structure along with 
#          the selected page to be displayed. The site data structure 
#          is used to gather the pages as well as the order they are 
#          to appear in if one was to iterate through the site. This
#          data structure is consumed by the template.
########################################################################
sub  pages : Regex('(\d)(\d)?\.html$')  {
    my ($self, $c) = @_;
    $DB::single=1;
    my $landing_site_id = $c->req->captures->[0] || 1;
    my $number = $c->req->captures->[1];
    my $token = $c->session->{token};
    my $mrkt_id =  $c->session->{market_vector_id} || 1;
    my $adgroup_id = $c->session->{adgroup_id} || 'default';

    # Landing site from the market vector
    my $site_data = $c->model('MarketVector')->get($mrkt_id);
    my $file_paths = $self->assemble_site_files($c, {landing_site_id=>$landing_site_id, site_data=>$site_data});
    my $pages = $self->populate_content($c, {landing_site_id=>$landing_site_id, paths=>$file_paths});
    my $path;
    # Determine if the home page or a member page called for
    if( $number && $landing_site_id ) {
	$path = catfile('root', 'src', 'site', "landing_site", "$landing_site_id",  "$landing_site_id". "$number.html") ;
    } else {
	$path = catfile('root', 'src', 'site', "landing_site", "$landing_site_id",   "$landing_site_id.html");
    }
    my $selected_page = File::Slurp::read_file($c->path_to($path));

    # push response through template
    $c->{stash}->{pages} = $pages;
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

sub index :Path('/') :Args(0) {
    my ( $self, $c ) = @_;
    $c->response->redirect($c->uri_for('/', '1.html'));
}

#sub end : ActionClass('RenderView') {}

sub finalize_error {
    my $c = shift;
    $c->res->header('X-Error' => $c->error->[0]);
    $c->NEXT::finalize_error;
}

=pod

sub end : Private {
    my ( $self, $c ) = @_;
    
    if ( scalar @{ $c->error } ) {
        $c->stash->{errors}   = $c->error;
        for my $error ( @{ $c->error } ) {
            $c->log->error($error);
        }
        $c->stash->{template} = 'error.tt2';
        $c->forward('LandingSite::Delivery::View::HTML');
        $c->clear_errors;
    }
 
    return 1 if $c->response->status =~ /^3\d\d$/;
    return 1 if $c->response->body;
 
    unless ( $c->response->content_type ) {
        $c->response->content_type('text/html; charset=utf-8');
    }
 
    $c->forward('LandingSite::Delivery::View::HTML');
}

=cut

__PACKAGE__->meta->make_immutable;
no Moose;
1;
