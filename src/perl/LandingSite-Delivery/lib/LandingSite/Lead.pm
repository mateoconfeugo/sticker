package LandingSite::Lead;
use Moose;
use FormValidator::Simple;
use Number::Phone;
our $VERSION = '0.01';

with 'LandingSite::DB';

my @fields = (qw|event_time email full_name offer phone adnetwork adgroup listing profile campaign market_vector landing_site user_agent|);

sub create {
  my ($self, $args) = @_;
  my $lead = $args->{lead};
  my $sql = q|INSERT INTO lead_log (event_time, email, full_name, offer, phone, adnetwork, adgroup, listing, profile, campaign, market_vector, landing_site, user_agent) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)|;
  my @vals = map { $lead->{$_} } @fields;
  my $sth = $self->dbh->prepare($sql);   
  $sth->execute(@vals);
  return $self;
}

sub validate {
    my ($self, $args) = @_;
    my $query = $args->{query};
    my $locale = $args->{locale} || 'US';
    my $phone_number = Number::Phone->new($locale, $query->{phone});
    if($phone_number) {
	$query->{phone} = ($phone_number->areacode . $phone_number->subscriber) * 1;
    }
    my $result = FormValidator::Simple->check($query => [
						   full_name => ['NOT_BLANK', ['LENGTH', 2, 50]],
						   email  => ['NOT_BLANK', 'EMAIL_LOOSE'],
						   phone  => ['NOT_BLANK', 'INT', ['LENGTH', 10]],
					       ]);
    return $result;
}

=pod

sub create_update {
  my ($self, $args) = @_;
  my $recipe = $args->{recipe};
  my $description = $recipe->{description};
  my $new_record = undef;
  my $data = $self->get_recipe({id=>$recipe->{id}});
  if ($data->{description} ne $description) {
    $self->insert_recipe({id=>$recipe->{id}, description=>$description});
    $data->{description}=$description;
  }
  if (scalar(@{$data->{ingredients}})) {
    $new_record = 0;
  } else {
    $new_record = 1;
    $self->insert_recipe({recipe_id=>$recipe->{id}, description=>$description ||'None'});
  }

  my $delete_sql = qq|delete from recipe_ingredient_amount where recipe_id=| . $recipe->{id};
  $self->dbh->do($delete_sql);

  my $sql = q|INSERT INTO recipe_ingredient_amount (ingredient_id, recipe_id, amount, units) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE  amount=?|;

  my @ingredients = @{$recipe->{ingredients}};
  for my $ingredient (@ingredients) {
    my ($amount, $ingredient_id, $units) =  @$ingredient{qw(amount id  units)};
    my $sth = $self->dbh->prepare($sql);
    $sth->execute( $ingredient_id, $recipe->{id}, $amount, $units, $amount);
  }
  $data = $self->get_recipe({id=>$recipe->{id}});
  return $data;
}

sub insert_lead {
  my ($self, $args) = @_;
  my $lead_id = $args->{id};
  my $description = $args->{description};
  my $sql = q|INSERT INTO lead (id, description) VALUES(?, ?) ON DUPLICATE KEY UPDATE  description=VALUES(description)|;
  my $sth = $self->dbh->prepare($sql);
  $sth->execute($lead_id, $description);
  #    my $lead = $self->dbh->selectall_arrayref($sql, { Slice => {} }, ($lead_id));
  my $lead = $self->get_lead({id=>$lead_id});
  return {id=>$lead_id, ingredients=>$lead};
}



sub list {
  my ($self, $args) = @_;
  my $sql = q(select * from lead);
  my $leads = $self->dbh->selectall_arrayref($sql, { Slice => {} }, ());
  return $leads;
}

sub create_update {
  my ($self, $args) = @_;
  my $lead = $args->{lead};
  my $description = $lead->{description};
  my $new_record = undef;
  my $data = $self->get_lead({id=>$lead->{id}});
  if ($data->{description} ne $description) {
    $self->insert_lead({id=>$lead->{id}, description=>$description});
    $data->{description}=$description;
  }
  if (scalar(@{$data->{ingredients}})) {
    $new_record = 0;
  } else {
    $new_record = 1;
    $self->insert_lead({lead_id=>$lead->{id}, description=>$description ||'None'});
  }

  my $delete_sql = qq|delete from lead_ingredient_amount where lead_id=| . $lead->{id};
  $self->dbh->do($delete_sql);

  my $sql = q|INSERT INTO lead_ingredient_amount (ingredient_id, lead_id, amount, units) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE  amount=?|;

  my @ingredients = @{$lead->{ingredients}};
  for my $ingredient (@ingredients) {
    my ($amount, $ingredient_id, $units) =  @$ingredient{qw(amount id  units)};
    my $sth = $self->dbh->prepare($sql);
    $sth->execute( $ingredient_id, $lead->{id}, $amount, $units, $amount);
  }
  $data = $self->get_lead({id=>$lead->{id}});
  return $data;
}

sub get_lead {
  my ($self, $args) = @_;
  my $lead_id = $args->{id};
  my $sql = q(select i.id as id, ria.amount, i.common_name, ria.units from lead r LEFT JOIN lead_ingredient_amount ria ON r.id =  ria.lead_id LEFT JOIN ingredient i ON ria.ingredient_id=i.id where r.id=?);
  my $lead = $self->dbh->selectall_arrayref($sql, { Slice => {} }, ($lead_id));
  $sql = q(SELECT description FROM lead WHERE id=?);
  my $result = $self->dbh->selectall_arrayref($sql, {Slice=>{}},($lead_id)); 
  return {id=>$lead_id, ingredients=>$lead, description=>$result->[0]->{description}};
}

sub insert_lead {
  my ($self, $args) = @_;
  my $lead_id = $args->{id};
  my $description = $args->{description};
  my $sql = q|INSERT INTO lead (id, description) VALUES(?, ?) ON DUPLICATE KEY UPDATE  description=VALUES(description)|;
  my $sth = $self->dbh->prepare($sql);
  $sth->execute($lead_id, $description);
  #    my $lead = $self->dbh->selectall_arrayref($sql, { Slice => {} }, ($lead_id));
  my $lead = $self->get_lead({id=>$lead_id});
  return {id=>$lead_id, ingredients=>$lead};
}

sub delete_lead {
  my ($self, $args) = @_;
  my $lead_id = $args->{id};
  my $sql = qq(DELETE FROM lead where id=$lead_id);
  my $ret_val = $self->dbh->do($sql);
  $sql = qq(DELETE FROM lead_ingredient_amount WHERE lead_id=$lead_id);
  $ret_val = $self->dbh->do($sql);
  return $self;
}

sub _build_ingredients {
  my $self = shift;
  my $result = $self->get_lead({id=>$self->id});
  return $result->{ingredients};
}

sub _build_order {
  my $self = shift;
}

=cut

sub run {
  my $obj = __PACKAGE__->new({id=>11});
  my $leads = $obj->list();
}

run() unless caller;

__PACKAGE__->meta->make_immutable;
no Moose;
1;
