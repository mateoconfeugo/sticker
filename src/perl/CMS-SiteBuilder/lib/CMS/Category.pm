package CMS::Category;
########################################################################
# ABSTRACT: CMS policy role for category related behavior & attributes.
########################################################################
use Moose::Role;
use Bric::Biz::Category;

has categories => (
    is=>'rw',
    isa=>'ArrayRef',
    lazy=>1,
    builder=>'create_categories'
    );

sub create_categories {
    my ($self, $args) = @_;
    my @bric_categories = ();
    my $parent;
    for my $record (@{$self->config->{'categories'}}) {
	my %init = map { $_ => $record->{$_}} qw(description uri directory);
	$init{site_id} = $self->site->{id};
        $init{name} = $record->{name};
	
	$init{parent_id} = 0 if $init{directory} ne '/'; 

	my $cat_obj = new Bric::Biz::Category(\%init);
	eval {  $cat_obj->save() };
	if($@) {
	    # TODO: Throw an Error.
	    my $bp = 'breakpoint';
	}
	push @bric_categories, $cat_obj;
    }
    return \@bric_categories;
}
no Moose;
1;
