package LandingSite::DB;
use Moose::Role;
use DBIx::Connector;
use DBI;

# Create a connection.
has dbh => (is=>'rw', lazy_build=>1);
has dsn => (is=>'rw', lazy_build=>1);
has username => (is=>'rw', isa=>'Str', default=>'root');
has password => (is=>'rw', isa=>'Str', default=>'sa'); 

sub _build_dbh {
    my $self = shift;
#    my $conn = DBIx::Connector->new($self->dsn, $self->username, $self->password, { RaiseError => 1,   AutoCommit => 1,});
    my $dbh = DBI->connect($self->dsn, 'root', '', {
	AutoCommit => 1,
	RaiseError => 1,
	PrintError => 1 });

#    return $conn->dbh;
    return $dbh;
}

sub _build_dsn {
    my $self = shift;
    return "dbi:mysql:pcs:127.0.0.1:3306";
}


no Moose;
1;
