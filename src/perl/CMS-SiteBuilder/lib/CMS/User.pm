package CMS::Users;
########################################################################
# ABSTRACT: CMS policy role for user related behavior & attributes.
########################################################################
use Moose::Role;
use Bric::Biz::Person;
use Bric::Biz::Person::User;

has users => (
	      is=>'rw', 
	      isa=>'ArrayRef',
	      lazy=>1,
	      builder=>'create_users'
	     );

sub create_users {
    my ( $self, $args ) = @_;
    my @bric_users = ();
    for my $user (@{$self->config->{'users'}}) {
	my %init = map { $_ => $user->{$_}} qw(lname fname mname prefix suffix login password);
	my $bu = Bric::Biz::Person::User->new(\%init);
	eval {
	    $bu->save();
	};
	if($@) {
	    my $bp = 'breakpoint';
	}
	push @bric_users, $bu;
    }
    return \@bric_users;
}
1;
