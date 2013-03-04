use strict;
use warnings;
use Plack::Builder;
use PocketIO;
use LandingSite::Delivery;
#use LandingSite::Profiler;

my $app = LandingSite::Delivery->apply_default_middlewares(LandingSite::Delivery->psgi_app);
#my $profiler = LandingSite::Profiler->new();

builder {
  mount '/' => $app,
  mount '/socket.io' => PocketIO->new(
				      handler => sub {
					my $socket = shift;
					$socket->on('profile' => sub {
						      my ($self, $args) = @_;
#						      my $user_profile = $profiler->categorize($args);
						      $socket->emit("profile", "exciting message");
#						      $socket->emit("profile", encode_json($user_profile));
						    });
				      });
};

