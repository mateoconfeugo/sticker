package CMS::App::SiteBuilder::Command::interactive;
########################################################################
# ABSTRACT:  CMS::App::SiteBuilder::Command::interactive -     
#            Programmatically manipulate the cms site object in a REPL
########################################################################
use Moose;
use Moose::Util qw(apply_all_roles);
use Devel::REPL;
use CMS::Shell;

extends qw(MooseX::App::Cmd::Command);

has repl_shell => (
		   is=>'rw',
		   lazy_build=>1
		  );

sub _build_repl_shell {
  return Devel::REPL->new();
}

has term_shell => (
		   is=>'rw',
		   lazy_build=>1
		  );

sub _build_term_shell {
  my ($self) = @_;
  my $shell =  CMS::Shell->new({repl=>$self->repl_shell});
  return $shell;
}

sub BUILD {
  my ($self) = @_;
  #  my @roles = (qw|CMS::Configuration|);
  #  apply_all_roles($self, @roles);

  $SIG{INT} = sub {
    my $err = shift;
    $self->repl_shell->exit_repl;
    warn "Exiting the REPL repl_shell and returning to term repl_shell\n";
    $self->term_shell->cmdloop();
    exit;
  };
}

# Bring into the cms object the various aggregate objects 
# that use data from resource files to initialize themselves.
sub execute {
  my ( $self, $opt, $args ) = @_;
  warn "Placeholder for the REPL repl_shell\n";
  $self->repl_shell->load_plugin($_) for qw(History LexEnv);
  $self->repl_shell->run();
}

no Moose;
1;

