package RenderingTemplateEngine;
use Moose::Role;
use Template;
use namespace::autoclean;
use JSON::XS;

requires qw(config );

with 'FeedProxy';
with 'AdUnitStore';
with 'TemplateStore';
with 'AdUnit';

has template_vars => (is=>'rw', isa=>'HashRef', lazy_build=>1);
has template_basedir =>(is=>'rw', isa=>'Str');
has template_engine =>(is=>'rw', lazy_build=>1);
has templates => (is=>'rw', isa=>'HashRef', lazy_build=>1);

sub render_template {
  my ($self, $args) = @_;

  my $opts = {
	      'config' => $args->{config},
	      'aduint_id' => $args->{adunit_id},
	      'token' => $args->{token},
	      'adunit_name' => $args->{adunit_type_name},
	      'template_type' => $args->{template_type} || 'rendering'};

  my $path = $self->retrieve_template_path($args);
  return unless -e $path; # TODO: throw an error
  my $settings = $self->retrieve_adunit_settings($opts);
  my $listings =  $settings->{listings};
  return $listings;
}

sub prepare {
  my ($self, $args) = @_;
  return %$args;
}

sub process_template {
    my ($self, $args) = @_;
    my $template = $args->{template};
    my $vars = $args->{vars};
    my $stream = $args->{stream} || q();
    $self->template_engine->process($template, $vars, \$stream);
    return $stream unless  $self->template_engine->error();
    return undef; # TODO: Throw exception
}

sub _build_templates {
    my $self = shift;
    return {
	browser => catfile($self->config->{templateDir}, 'admin', 'asp', 'notAuthorized.html'),
	editor => catfile($self->config->{templateDir}),
	rendering => catfile($self->config->{templateDir}, 'admin', 'asp', 'message.html'),
    };
}

sub _build_template_engine {
    my $self = shift;
    my $engine = Template->new(
	INCLUDE_PATH => $self->config->{template_storage_dir},
	ABSOLUTE => 1,
    );
    return $engine;
}

no Moose;
1;
