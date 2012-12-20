# ABSTRACT:  Request AdConnect Listings take the top listings display uri as redirect 
package FeedProxy;

# DEPENDENCIES
use Moose::Role;
use XML::Simple;
use LWP::Simple;

# ATTRIBUTES
has extension_id_map => (is=>'rw', isa=>'HashRef', lazy_build=>1);
has query_server_base_uri => (is=>'rw', isa=>'Str', default=>'http://interestingsoftwarestuff.com/delivery');
has auth_code => (is=>'rw', isa=>'Str', default=>'56LB');
has state => (is=>'rw', isa=>'Str', default=>'CA');
has city => (is=>'rw', isa=>'Str', default=>'Los Angeles');
has count => (is=>'rw', isa=>'Int', default=>10);

# METHODS
sub listings {
  my ($self, $args) = @_;
  my $uri = $self->assemble_query_uri($args);
  my $data =  XMLin(get $uri);
  return $data->{results}->{sponsored}->{listing};

=pod

  my $test_listings = 
       [
	  {
	      url=>'www.Yellow.com',
	      title=>'Airlines',
	      description => [
		  'cures ugliness',
		  'cures sobriety',
	      ]o,
	      imageurl => 'img/foo.png',
	  },
	  {
	      url=>'www.Blue.com',
	      title=>'Cars',
	      description => [
		  'four rotating wheels',
		  'tires included',
	      ],
	      imageurl => 'img/bar.png',
	  }
       ];



  return $test_listings;

=cut

}

# Build up the query uri for ad connect query servers
sub assemble_query_uri {
  my ($self, $args) = @_;
  my ($keyword, $extension_id, $ip, $user_agent, $refer, $city, $state) = @$args{('keyword', 'extension_id', 'ip', 'user_agent', 'refer', 'city', 'state')};
#  return undef unless $keyword && $extension_id &&  $ip && $user_agent && $refer;


  my $base = $self->query_server_base_uri;
  my ($source_id, $pub_id) = values %{$self->extension_id_map->{$extension_id}};
  my $uri = "http://$source_id.$pub_id.$base";
#  my $uri = $base;
  my %params= (sid=>$source_id, count=>$self->count, ip=> $ip, ua=>$user_agent, 
	       city => $city || $self->city, state => $state || $self->state,
	       auth=>$self->auth_code, q => $keyword, ref=>$refer);
  my @pairs = q();
  push @pairs, "$_=$params{$_}" for (keys %params);
  my $params_str = join '&', @pairs;
  $uri = qq($uri?$params_str);
  return $uri;
}


sub redir_uri_to_listing {
  my ($self, $args) = @_;
  my $uri = $self->assemble_query_uri($args);
  # request listing from query servers and extract top listing
  my $listing = $self->top_listing({proxy_uri=>$uri});
  my $redir_uri = $listing->{displayurl};
  $redir_uri = 'http://' . $redir_uri if ($redir_uri !~ /^http.*/);
  return $redir_uri;
}

sub top_listing {
  my ($self, $args) = @_;
  my $uri = $args->{proxy_uri};
  my $data =  XMLin(get $uri);

=pod

  my $data = { results => {
			   sponsored => {
					 listing => {
						       displayurl => 'http://www.google.com'
						       }
					}
			  }
	     };
  my $redir_uri = $data->{results}->{sponsored}->{listing}->{displayurl};
  $redir_uri = 'http://' . $redir_uri if ($redir_uri !~ /^http.*/);
  return $redir_uri;

=cut
  return $data;

}

# ATTRIBUTE BUILDER METHODS
# TODO: get correct source and pub ids further make this a GUID and get a real source id and pull from a database.
sub _build_extension_id_map {
  my $self = shift;
  return {
	  1 => {source_id => 27305, publisher_id => 13925}
	 };
}

no Moose;
1;
