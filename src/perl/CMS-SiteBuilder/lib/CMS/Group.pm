package CMS::Group;
########################################################################
# ABSTRACT: CMS policy role for group related behavior & attributes.
########################################################################
use Moose::Role;
use Bric::Util::Grp;

has groups => (
    is=>'rw',
    isa=>'ArrayRef',
    lazy=>1,
    builder=>'create_groups'
    );

sub create_groups {
    my ( $self, $wf ) = @_;
    my @group_assets = ();
    for my $record (@{$self->config->{'groups'}}) {
	my %init = map { $_ => $record->{$_}} qw(description permanent secret);
        $init{name} = $record->{name};
	my $grp = Bric::Util::Grp->new(\%init);
	$grp->save();
	push @group_assets, $grp;
    }
    return \@group_assets;
}

no Moose;
1;
