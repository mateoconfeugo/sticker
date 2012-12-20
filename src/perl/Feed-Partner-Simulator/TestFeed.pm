########################################################################
# ABSTRACT: development delivery server to simulate feed partners 
########################################################################
package TestFeed;
use Moose;
use Moose::Util qw( apply_all_roles );
use Plack::Request;
use JSON::XS;
use File::Slurp qw(read_file);

has config => (is=>'rw', isa=>'HashRef', lazy_build=>1);

sub BUILD {
  my $self = shift;
  my @roles = ('RenderingTemplateEngine');
  apply_all_roles($self, @roles);
  return $self;
}

sub handle_request {
  my ($self, $args) = @_;
  my $req = $args->{request};
  my $token = $req->parameters->{token};
  $args->{token} = $token;
  my $html = $self->render($args);
  return $html;
}

sub render {
  my ($self, $args) = @_;
  $args->{config} = $self->config;
  $args->{adunit_name} = 'revenue_recovery';
  $args->{adunit_type_name} = 'revenue_recovery';
  $args->{template_type} = 'rendering';
  my $output = $self->render_template($args);
  return $output;
}

use File::Spec::Functions qw(catfile catdir);

sub _build_config {
  my $self = shift;
  my $path = catfile($ENV{TEST_FEED_ROOT_DIR}, 'conf', 'test-feed-server.conf');
  return decode_json(read_file($path));
}

sub run {
  my $obj = __PACKAGE__->new();
  my $test_token = 1212;
  my $output = $obj->render({token=>$test_token});
  warn "$output\n";
}

run() unless caller;

no Moose;
1;
