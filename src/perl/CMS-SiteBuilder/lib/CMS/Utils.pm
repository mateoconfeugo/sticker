package CMS::Utils;
########################################################################
# ABSTRACT: CMS policy role for utility related behavior & attributes.
########################################################################
use Moose::Role;
use DBI;

has  cms_db => (is=>'rw',lazy_build=>1);

sub _build_cms_db {
    my ($self, $arg) = @_;
    my $platform = "mysql";
    my $database = "bric";
    my $host = "localhost";
    my $port = "3306";
    my $user = "bric",
    my $pw = "test123";
    my $dsn = "dbi:mysql:$database:localhost:3306";
    my $dbh = DBI->connect($dsn, $user, $pw);
    return $dbh;
}

no Moose;
1;
