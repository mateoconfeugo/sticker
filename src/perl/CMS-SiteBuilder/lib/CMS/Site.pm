package CMS::Site;
########################################################################
# ABSTRACT: CMS policy role for site related behavior & attributes.
########################################################################
use Moose::Role;
use Bric::Biz::Site;

has site => (
    is=>'rw',
    lazy=>1,
    builder=>'create_site'
    );

sub create_site {
    my ($self) = @_;
    my %init = map { $_ => $self->config->{$_} } qw(name description domain_name);
    my $site = Bric::Biz::Site->new(\%init);
    $site->save();
    return $site;
}

__PACKAGE__->meta->make_immutable;
no Moose;
1; 
