package TemplateStore;
use Moose::Role;
use File::Slurp qw(read_file write_file);
use File::Spec::Functions qw(catfile catdir);
use File::Copy;
use namespace::autoclean;

# requires 'config';

has tmp_storage_dir => (is=>'rw', lazy_build=>1);
has configuration => (is=>'rw');
has template_storage_dir => (is=>'rw', lazy_build=>1);

sub list_catalog {
  my ($self, $args) = @_;
  my $config = $args->{config};
  $self->configuration($config);
  my $dir;
  my $file;
  my @results = ();
  if( -d $self->template_storage_dir ) {
    unless (opendir $dir, $self->configuration->{template_storage_dir} ) {
      warn "Could not open directory";
    }
    while($file = readdir  $dir) {
      next if $file eq '.' || $file eq '..' || $file eq '.svn';
      my $path =  catfile($self->template_storage_dir, $file);
      push @results, $file if -d $path;
    }
  }
  @results = map { {name => $_} } @results;
  return \@results;
}

sub retrieve_template_path {
  my ($self, $args) = @_;
  my $id = $args->{id};
  my $config = $args->{config};
  $self->configuration($config);
  my $template_type = $args->{template_type};
  my $adunit_name = $args->{adunit_name};
  my $path = catfile($self->configuration->{'template_storage_dir'}, $adunit_name, $template_type);
  # TODO: Throw an exception if the path is invalid
  return $path;
}

sub retrieve_template {
  my ($self, $args) = @_;
  my $path = $self->retrieve_template_path($args);
  # TODO: Throw an exception if there is no path
  return read_file($path);
}

sub persist_adunit_type_templates {
  my ($self, $args) = @_;
  my $tmp_dir = $args->{temporary_directory} || $self->tmp_storage_dir;
  my $adunit_type_name = $args->{adunit_type_name};
  my $adunit_dir = catdir($self->template_storage_dir, $adunit_type_name);
  # TODO: Throw an exception if move does not work.
  move($tmp_dir, $adunit_dir);  
}

sub store_template {
  my ($self, $args) = @_;
  my $fh = $args->{fh};
  my $dir = $args->{directory};
  my $adunit_name = $args->{adunit_name};  
  my $config = $args->{config};
  $self->configuration($config);
  my $template_type = $args->{template_type};
  # TODO: Throw an exception if missing an argument instead of returning undef.
  return undef unless $template_type && $adunit_name && $dir;
  $DB::single=1;
  mkdir catdir($self->template_storage_dir, $adunit_name) unless -e  catdir($self->template_storage_dir, $adunit_name);
  my $target = catfile($dir, $adunit_name, $template_type);
  eval {
    write_file($target, File::Slurp::read_file($fh));
  };
  if($@) {
    warn "Unable to persist template: $@";
  }
  return $target;
}

sub validate_template {}

sub _build_tmp_storage_dir {
  my $self = shift;
  my $dir = $self->configuration->{tmp_template_storage_dir} ||  catdir($self->template_storage_dir, 'tmp');
  return $dir;
}

sub _build_template_storage_dir {
  my $self = shift;
  my $dir = $self->configuration->{template_storage_dir};
  return $dir;
}

no Moose;
1;
