package CMS::Workflows;
use Moose::Role;
use Bric::Biz::Workflow;
use Bric::Biz::Workflow::Parts::Desk;

has workflows => (
    is=>'rw',
    isa=>'ArrayRef',
    lazy=>1,
    builder=>'create_workflows'
    );

sub create_workflows {
    my ( $self, $args) = @_;
    my $bric_workflows = $self->config->{'workflows'};
    my @bric_wf = ();
    my $biz_type = 'Workflow'; 
    my $class = "Bric::Biz::$biz_type";
    eval "require $class; 1"    or  die $@;
    my @bric_desks = ();
    for my $record (@$bric_workflows) {
	my @desks = ();
	for my $desk (@{$record->{desks}}) {
#            my ($desk_name, $desk_record) = each %$desk;
	    my %init = map { $_ => $desk->{$_}} ('description', 'publish');
            $init{name} = $desk->{name};
            next unless $init{name};
	    my $obj = Bric::Biz::Workflow::Parts::Desk->new(\%init);
	    eval {
		$obj->activate();
		$obj->save();
	    };
	    if($@) {
		# TODO: throw error
		my $bp = 'breakpoint';
	    }
	    push @desks, $obj;
	}
	push @bric_desks, @desks;
	my %init = map { $_ => $record->{$_}} (qw|name description start_desk|);
	my $start_desk = $init{start_desk};
	my $workflow = Bric::Biz::Workflow->new();
	$workflow->set_name($record->{name});
	$workflow->set_description($record->{description});
	$workflow->set_type($record->{type});
	$workflow->set_site_id($self->site->{id});
	$workflow->activate();

	for my $desk (@desks) {
	    my $did = $desk->get_id;
	    $workflow->add_desk({allowed=>[$did]}) if $did;
	    if($start_desk eq $desk->{name} && $did) {
		$workflow->set_start_desk($did);
	    }
	}

	eval {
	    $workflow->save();
	};
	if($@) {
	    # TODO: throw error
	    my $bp = 'breakpoint';
	}
	push @$bric_wf, $workflow;
    }
    $self->config->{'desk_assets'} = \@bric_desks;
    $self->desks(\@bric_desks);
    return \@bric_wf;
    
}

no Moose;
1;
