package LandingSite::MarketVector;
use Moose;
use File::Slurp;
use File::Spec::Functions qw(catfile catdir);
use JSON::XS;
use MooseX::Types::Path::Class;
our $VERSION = '0.01';

#has root_dir => (is=>'rw', isa=>'Path::Class::Dir', required=>1, coerce=>1);
has root_dir => (is=>'rw',  required=>1);

sub get {
  my ($self, $market_id) = @_;
  my $path = catfile($self->root_dir->stringify, "market_vector", "$market_id",  "$market_id" . '.json');
  my $data = decode_json(File::Slurp::read_file( $path));
  return $data;
}

sub run {
  my $obj = __PACKAGE__->new({root_dir=>''});
  my $market_vector = $obj->get(1);
}

run() unless caller;

__PACKAGE__->meta->make_immutable;
no Moose;
1;
