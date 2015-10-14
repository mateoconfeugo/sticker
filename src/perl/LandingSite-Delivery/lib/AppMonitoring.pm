package AppMonitoring;
use Moose::Role;
use Try::Tiny;
use Riemann::Client;

has monitor => (is=>'rw', lazy_build=>1, predicate=>'has_monitor');

sub _build_monitor {
    my $self = shift;
    my $host = 'localhost' || $self->monitor_host;
    my $port = 5555 || $self->monitor_port;
    my $client;
    try {
	$client = Riemann::Client->new(host=>$host, port=>$port);
	$client->send({desciption=>'testing connection',
		       service=>'connection diagnostic'});
    }
    catch {
	package mock;
	use Moose;
	use Data::Dumper;

	sub send {
	    my ($self, $args) = @_;
	    print Dumper $args;
	}
	no Moose;
	$client = mock->new();
    };
    return $client;
}
no Moose;
1;
