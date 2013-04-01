package CMS::Action::CreateOutputChannels;
########################################################################
# ABSTRACT: CMS policy role for output channel related behavior & attributes.
########################################################################
use Moose::Role
use Bric::Biz::OutputChannel;

has output_channels => (
	      is=>'rw', 
	      isa=>'ArrayRef',
	      lazy=>1,
	      builder=>'create_output_channels'
	     );

sub create_output_channels {
    my ( $self, $args ) = @_;
    my $bric_ocs = ();
    my $site_id = $self->site->get_id();
    my @pairs = ();
    for my $record (@{$self->config->{'output_channels'}}) {
	my @attrs = keys %{Bric::Biz::OutputChannel->my_meths};
	my %init = map { $_ => $record->{$_}} @attrs;
        $init{name} = $record->{name};
	$init{site_id} = $site->{id};
	my $obj = Bric::Biz::OutputChannel->new(\%init);
	eval {
	    $obj->save();
	};
	if($@) {
	    # TODO: Throw error.
	    my $bp = 'breakpoint';
	}
	push @bric_ocs, $obj;
	push @pairs, [$obj, $record];
    }
    for my  $pair (@pairs) {
	my $bric_oc = $pair->[0];
	my $oc_cfg = $pair->[1];
	my @included_channels = ();
	my @included_oc_name = split ',', $oc_cfg->{include_channels};
	for my $name (@included_oc_name) {
#	    my $c = Bric::Biz::OutputChannel->lookup({name=>$name, site_id=>$site_id}) if $site_id;
	    my @c = Bric::Biz::OutputChannel->list({name=>$name});
	    my $c = $c[0];
	    push @included_channels, $c if $c && $c->get_id() != $bric_oc->get_id();
	}
	$bric_oc->set_includes(@included_channels);
	$bric_oc->set_uri_format($oc_cfg->{uri_format});
	$bric_oc->set_fixed_uri_format($oc_cfg->{fixed_uri_format});
	eval {
	    $bric_oc->save();
	};
	if($@) {
	    my $bp = 'breakpoint';
	}
    }

    return \@bric_ocs;
}

no Moose;
1;
