package CMS::Destination;
########################################################################
# ABSTRACT: CMS policy role for destination related behavior & attributes.
########################################################################
use Moose::Role;
use Bric::Biz::OutputChannel;
use Bric::Dist::Server;
use Bric::Dist::ServerType;
use Bric::Dist::Action;
use Bric::Biz::Site;

has destinations => (
		  is=>'rw', 
		  isa=>'ArrayRef',
		  lazy=>1,
		  builder=>'create_destinations'
		 );

has servers => (
		  is=>'rw', 
		  isa=>'ArrayRef',
		  lazy=>1,
		  builder=>'create_servers'
		 );

sub create_destinations {
    my ( $self, $wf ) = @_;
    return undef unless $self->config->{'destinations'} && $self->site;  # TODO: throw error.
    my @pairs = ();
    for my $dest (@{$self->config->{'destinations'}}) {
        my $st = $self->create_server_type({config=>$dest->{server_type}});
        my $s = $self->create_servers({servers => $dest->{servers}, server_type => $st});
        my $dest_obj = { server_type => $st, servers => $s};
        push @pairs, $dest_obj;
    }
    return \@pairs;
}

sub create_servers {
    my ($self, $args) = @_;
    my $server_type = $args->{server_type};
    my @servers = @{$args->{servers}};
    my @server_accum = ();

    for my $s (@servers) {
	my @attrs = keys %{Bric::Dist::Server->my_meths};
	my %init = map { $_ => $s->{$_}} keys %{Bric::Dist::Server->my_meths};
	$init{site_id} = $self->site()->get_id();
	my $server = Bric::Dist::Server->new(\%init);
	$server->set_server_type_id($server_type->get_id);
	eval { 
	    $server->save();
	};
	if($@) { 
	    return undef;   # TODO: Throw an error.
	}
	push @server_accum, $server;
    }
    return \@server_accum;  # TODO: add want array logic
}

sub create_server_type {
    my ($self, $args) = @_;
    my $config = $args->{config};
    my %init = map { $_ => $config->{$_}}  keys %{Bric::Dist::ServerType->my_meths};
    $init{site_id} = $self->site()->get_id();
    my $st;
    eval {
	$st = Bric::Dist::ServerType->new(\%init);
	my @channels = ();
	my @channel_names = split ',', $config->{output_channels};
	for my $channel_name (@channel_names) {
	    $channel_name =~ s/^\s+(.*)$/$1/g;
	    my $oc = Bric::Biz::OutputChannel->lookup({site_id => $self->site->get_id, name => $channel_name});
	    push @channels, $oc;
	}
	$st->save();
	$st->add_output_channels(@channels) if @channels;
	$st->save();
	my $action = Bric::Dist::Action->new({type=>'Move', server_type_id=>$st->get_id});
	$action->save();
    };
    if($@) {
	return undef;
    }
    return $st;
}
no Moose;
1;
