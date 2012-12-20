package AdUnitStore;
use Moose::Role;
use File::Slurp;
use File::Spec::Functions;
use namespace::autoclean;
use JSON::XS;

requires 'config';
# TODO: Fix this so it comes in via config file, env variable or constructor.

sub list_adunits {
  my ($self, $args) = @_;
  # TODO: Replace with query to database
  return [ { name => 'popunder'}, {name => 'exit_page'} ];
}

sub retrieve_adunit_settings {
  my ($self, $args) = @_;
  my $token = $args->{token};
  # TODO Replace with query to database or filesystem.
  my $path = catfile($ENV{TEST_FEED_ROOT_DIR}, 'templates', 'default_passthru', 'sample_data.json');
  my $settings = decode_json(read_file($path));
  return $settings;
}

sub store_adunit_instance {
  my ($self, $args) = @_;
  my $adunit_settings = $args->{settings};
  my $adunit_type = $adunit_settings->{adUnitType} || 'default';
  # TODO: Replace with storing in database
  my $target = catfile($self->config->{temp_storage_dir}, $adunit_type);
  use YAML;
  eval {
    write_file($target, YAML::Dump($adunit_settings));
  };
  if($@) {
    warn "Unable to persist test feed settings: $@";
  }
  return $self;
}

sub validate_adunit_settings {}

no Moose;
1;
