package CMS::SiteAsData;
use Moose;
use Try::Tiny;
use HTML::StripTags qw(strip_tags);

has data =>(is=>'rw', isa=>'HashRef');

sub get_market_vectors {
    my ($self, $args) = @_;
    my $element = $args->{element};
    my @stories = ();
    for my $related_story_collection_host (@{$element->get_containers(qw(related_stories))}) {
	for my $story_host (@{$related_story_collection_host->get_containers(qw(related_story))}) {
	    push @stories, $story_host->get_related_story if $story_host->get_related_story;
	}
    }
    return @stories;
}

sub assemble_market_vectors {
    my ($self, $args) = @_;
    my $result = $args->{site_data};
    my %market_vectors = ();

    my %adnetworks = (
	'google' => {id=>1},
	'gorilla' => {id=>2},
	'yahoo' => {id=>3},
	'bing' => {id=>4},
	);

    for my $mrkt_vec (@{$result->{'market_vector'}}) {
	my $k = $mrkt_vec->{'market_vector_id'};

	next unless $k;
	$market_vectors{$k}->{market_vector_id} = $k;
	my $campaign =  @{$mrkt_vec->{ad_campaign}}[0];
	my $campaign_id = $campaign->{campaign_id};
	my $landing_site =  @{$mrkt_vec->{landing_site}}[0];
	my $landing_site_id = $landing_site->{landing_site_id};
	$market_vectors{$k}->{landing_site}->{$landing_site_id} = $landing_site;
	my @adgroups = @{$campaign->{ad_campaign_adgroup}};
	my @ad_networks = split '__OPT__', $campaign->{ad_network};

	next unless scalar @ad_networks > 0;
	for my $ag (@adgroups) {
	    my $ag_id = $ag->{ad_group_id};
	    $market_vectors{$k}->{campaign}->{$campaign_id}->{ad_groups}->{$ag_id} = $ag;
	    for my $an (@ad_networks) {	       
		my $adnetwork_id = $adnetworks{$an}->{id};
		$market_vectors{$k}->{campaign}->{$campaign_id}->{ad_networks}->{$adnetwork_id} = $an;
	    }
	}
    }
}

sub unroll_related {
    my ($self, $args) = @_;
    my $elements = $args->{elements};
    my $burner = $args->{burner};
    my $result = {};
    for my $element ( @$elements ) {
	try { 
	    if ($element->get_related_story) {
		$result->{$element->get_slug} = $self->unroll({elements =>element->get_related_story->get_elements, burner=> $burner});
		$self->data->{$element->get_slug} = $self->unroll({elements=>$element->get_related_story->get_elements, burner=>$burner});
	    }
	} catch {};
	my $token = $element->get_key_name;
	try {
	    if ($element->get_elements() ){
		my $children = $element->get_elements();
		my $branch  = $self->unroll({elements=>$children, burner=>$burner});
		push @{$result->{$token}}, $branch;
	    } 
	    else {
		my $val = strip_tags($element->get_value());
		$result->{$token} = $val;
	    }
	} catch {
	    $result->{$token} = strip_tags($element->get_value());
	};

    }
    return $result;
}


sub walk_site_heirarchy {
    my ($self, $args) = @_;
    my @stories =  @{$args->{stories}};
    for my $story (@stories) {
	try {
	    my $top = $story->get_elements();
	    my $related_story = $top->get_related_story if $top->get_related_story;
	    next unless $related_story;
	    my @elements = $top->get_elements;
	    next unless @elements;
	    my $result = $self->unroll({elements=>\@elements});
	    $self->data->{market_vector}->{$story->get_slug} = $result;
	    @elements = $related_story->get_elements;
	    $result = $self->unroll({elements=>\@elements});
	    push @{$self->data->{market_vector}->{$story->get_slug}->{landing_site}}, $result;
	} catch {};
    }

    my %market_vectors = ();

    my %adnetworks = (
	'google' => {id=>1},
	'gorilla' => {id=>2},
	'yahoo' => {id=>3},
	'bing' => {id=>4},
	);
    my $test = try {
	 $self->data->{'market_vector'}
    } catch {};
    if ($test) {
	for my $id (keys %{$self->data->{'market_vector'}}) {
	    my $vector = $self->data->{'market_vector'}->{$id};
	    my @campaigns = @{$vector->{ad_campaign}};
	    my $campaign =   $campaigns[0];
	    delete $vector->{ad_campaign};
	    my $campaign_id = $campaign->{"campaign_id"};

	    my $adgroups = $campaign->{ad_campaign_adgroup} if $campaign;

	    my @ad_networks = split '__OPT__', $campaign->{ad_network} if $campaign;
	    delete  $campaign->{ad_network};

	    for my $ag (@$adgroups) {
		my $grouping = $ag->{ab_grouping};
		my $ads = {};
		for my $ad (@$grouping) {
		    $campaign->{ad_groups}->{$ag->{ad_group_id}}->{'online_ads'}->{$ad->{'online_ad'}->[0]->{'ad_tag'}} = $ad->{'online_ad'}->[0];
		}
	    }
	    for my $an (@ad_networks) {	       
		my $adnetwork_id = $adnetworks{$an}->{id};
		$campaign->{ad_networks}->{$adnetwork_id} = $an;
	    }

	    delete $campaign->{ad_campaign_adgroup} if $campaign;
	    delete $campaign->{$campaign_id}->{ad_groups};
	    delete $campaign->{$campaign_id}->{ad_networks};
	    $market_vectors{$id}->{campaign}->{$campaign_id} = $campaign;
	}

	for my $index (keys %{$self->data->{market_vector}}) {
	    my $vector = $self->data->{market_vector}->{$index};
	    next unless $vector;
	    my $campaign = $market_vectors{$index}->{campaign};
	    next unless $campaign;
	    $vector->{campaign} = $campaign;
	}
    }
}

sub unroll {
    my ($self, $args) = @_;
    my $elements = $args->{elements};
    my $burner = $args->{burner};
    my $result = {};

    for my $element ( @$elements ) {
	my $token = $element->get_key_name;
	if ($element->get_key_name eq  'menu_item') {
	    my $rel_story = $element->get_related_story;
	    if (defined $rel_story) {
		my $uri = eval { $burner->best_uri($rel_story)->as_string };
		$uri =~ s|(.*)\/$|\1\.html|gms;
		my @field_types = $element->get_possible_field_types();
		my $selected_field_type;

		for my $f (@field_types) {
		    $selected_field_type = $f if $f->get_key_name eq 'menu_item_url';
		}
		eval { $element->add_field($selected_field_type, $uri) };
	    }
	}
	try {
	    if ($element->get_elements() ){
		my @children = $element->get_elements();
		my $branch  = $self->unroll({elements=>\@children, burner=>$burner});
		push @{$result->{$token}}, $branch;
	    }
	    else {
		push @{$result->{$token}}, $element->get_value();
	    }
	} catch {
	    $result->{$token} = $element->get_value();
	};
    }
    return $result;
}

=pod
    sub unroll {
	my $elements = shift;
	my $result = {};
	for my $element ( @$elements ) {
	    try{ 
		if ($element->get_related_story) {
		    $result->{$element->get_slug} = unroll($element->get_related_story->get_elements);
		}
	    } catch {};
	    my $token = $element->get_key_name;
	    try {
		if ($element->get_elements() ){
		    my $children = $element->get_elements();
		    my $branch  = unroll($children);
		    push @{$result->{$token}}, $branch;
		} 
		else {
		    my $val = strip_tags($element->get_value());
		    push @{$result->{$token}}, $val;
		}
	    } catch {
		$result->{$token} = strip_tags($element->get_value());
	    };
	}
	return $result;
}
=cut


    no Moose;
1;

