package CMS::Shell;
use Moose;
use MooseX::NonMoose;

extends qw(Term::Shell);

has repl => (
    is=>'rw',
    required=>1,
    );

sub run_repl  { 
    my ($self) = @_;
    print "command 1!\n"; 
    $self->repl->run();
    
}

sub smry_repl { "what does command1 do?" }

sub help_repl{
    <<'END';
    Help on 'repl', the interactive cms repl of the cms site
END
}


sub run_load_site  { 
    my ($self) = @_;
    print "command 1!\n"; 
    $self->load_site->run();
    
}

sub smry_load_site { "what does load site do?" }

sub help_load_site{
    <<'END';
    Help on 'load_site', the interactive cms repl of the cms site
END
}


no Moose;
1;
