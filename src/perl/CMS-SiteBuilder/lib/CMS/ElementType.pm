package CMS::ElementType;
########################################################################
# ABSTRACT: CMS policy role for element type related behavior & attributes.
########################################################################
use Moose::Role;
use Bric::Biz::Category;
use Bric::Biz::ElementType;
use File::Spec::Functions;
use IPC::Run3;

has element_types => (
    is=>'rw',
    isa=>'ArrayRef',
    lazy=>1,
    builder=>'create_element_types'
    );

sub create_element_types {
    my ( $self, $args) = @_;

    my $site_id = $$self->site->get_id();
    my $username = $self->config->{'bric_username'};
    my $password = $self->config->{'bric_password'};
    my $server = $self->config->{'bric_server'};
    my $path = $self->config->{'element_types_dir'};
    my $soap =  $self->config->{'bric_soap'};

    my @bric_et = ();
    my $dir;
    if(-d $path) {
	my $file;
	unless (opendir $dir, $path) { 
	    warn "Could not open directory $path: Error $!\n";
	    return;
	}
	my $in; my $out; my $err;
	while($file = readdir $dir) {
	    my $cmd = qq($soap element_type create --server $server --user $username --password $password);
	    next if $file eq '.' || $file eq '..';
	    $cmd .= ' ' . catfile($path,$file);
	    eval {
		run3 $cmd, \$in, \$out, \$err;		    

	    };
	    if($err) {
		# TODO: Throw an error.
		my $bp = 'breakpoint';
	    }
	    my $id;
	    ($id = $out) =~ s/element_type_(\d+)/$1/g if $out;
	    my $obj = Bric::Biz::ElementType->lookup({id=>$id});
	    push @bric_et, $obj if $obj;
	}
    }
    return \@bric_et;
}

no Moose;
1;
