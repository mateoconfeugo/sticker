package CMS::TestUtils;
use Moose::Role;
use Exporter;
use DBI;

sub cms_db {
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


1;
