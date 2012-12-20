use Test::Routine;
use Test::Routine::Util;
use Test::More;
use Test::Deep;
use Moose::Util qw( apply_all_roles );
use File::Spec::Functions qw(catfile catdir);
use Data::Dumper;

has test_adunit_id => (is=>'rw', lazy_build=>1);
has test_token => (is=>'rw', lazy_build=>1);
has test_adunit_name => (is=>'rw', lazy_build=>1);
has test_template_type => (is=>'rw', lazy_build=>1);
has test_sources => (is=>'rw', lazy_build=>1);
has test_publishers => (is=>'rw', lazy_build=>1);
has expected_destination_uri => (is=>'rw', lazy_build=>1);

test "get listing from flow "  => sub {
  my ($self, $args) = @_;
  my $keyword = 'shoes';
  my $ip = '';
  my $user_agent = '';
  my $referer = '';
  my $opts = {
	      extension_id => 1, 
	      keyword => 'shoes',
	      ip => '108.179.222.110',
	      user_agent => 'popunder_redirect_server/0.0.1',
	      refer => 'google.com',
	     };
  my $got_listings = $self->listings($opts);
  warn Dumper($got_listings->[0]) . "\n";
  like($got_listings->[0]->{url}, qr/http/, 'retrieved listings from flow success' );
};


=pod

test "redir uri to top xml listing destination url" => sub {
  my ($self, $args) = @_;
#  plan tests => 1;
#  my $got_listings = $self->list_catalog();
#  is($got_listings, $self->expected_destination_uri, 'redirection success');
};

test "top xml listing " => sub {
  my ($self, $args) = @_;
# plan tests => 1;
#  my $got_listing = $self->top_listing();
#  is($got_listing, $self->expected_listing,  'listing success');
};

=cut

sub BUILD {
  my  $self = shift;
  my @roles  = qw/FeedProxy/;
  apply_all_roles($self, @roles);
  return $self;
}

sub _build_config {
  my $self = shift;
  return {
	  template_storage_dir => undef,
	  tmp_storage_dir => undef,
	  };
}

run_me();
done_testing();
1;
