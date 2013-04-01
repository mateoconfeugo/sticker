package CMS::Template;
########################################################################
# ABSTRACT: CMS policy role for template related behavior & attributes.
########################################################################
use Moose::Role;
use IPC::Run3;   

use Bric::Biz::Asset::Template;
use Bric::Biz::ElementType;
use Bric::Biz::Workflow;
use Bric::Biz::Category;
use Bric::Biz::OutputChannel;
use Bric::Biz::Person::User;
use Bric::Util::Event;
use Bric::Util::Priv::Parts::Const qw(:all);
use Bric::Util::Time qw(:all);
use File::Slurp;
use File::Spec::Functions;
use Log::Log4perl qw(get_logger);

has type_map => (
		 is=>'ro',
		 isa=>'HashRef',
		 default => sub {
		   return { 
			   1 => 'element',
			   2 => 'category',
			   3 => 'utility'
			  };
                 }
		);

has user => (
	     is=>'rw'
	    );

has templates => (
		  is=>'rw', 
		  isa=>'ArrayRef',
		  lazy=>1,
		  builder=>'import_templates'
		 );

has cms_template_workflow => (
			      is=>'rw'
			     );

sub create_templates {
  my ( $self, $args ) = @_;
  my $templates = $self->config->{'templates'};
  my @bric_templates = ();

  my $user = Bric::Biz::Person::User->lookup({id => 0}); # super user admin
  $self->user($user);

  my @attrs = keys %{Bric::Biz::Asset::Template->my_meths};

  for my $t (@$templates) {    
    my %init = map { $_ => $t->{$_}} @attrs; # Start building the Template constructor args.
    my $et;
    # Fetch the element type for element templates - categories templates get filtered too.
    if ($init{tplate_type} ne Bric::Biz::Asset::Template::UTILITY_TEMPLATE) {
      ($et) = Bric::Biz::ElementType->list({'name' => $init{element_type}->[0]});
      next unless $et;
      $init{element_type_id} = $et->get_id();
      $init{element_type} = $et;
    }

    my ($wf) = Bric::Biz::Workflow->list({site_id => $site->get_id, type => Bric::Biz::Workflow::TEMPLATE_WORKFLOW});
    $self->cms_template_workflow($wf);
    $init{workflow_id} = $wf->get_id() if $wf;

    # Get the category the template will be under
    my $cat;
    if ( $t->{category} =~ /.*root.*$/ ) {
      $cat = Bric::Biz::Category->site_root_category($self->site->get_id);
    } else {
      ($cat) = Bric::Biz::Category->list({site_id => $self->site->get_id, name => $t->{category}});
    }
    $init{category} = $cat;

    # Setup the output channel the rendered content gets pushed into.
    my $cn = $t->{output_channel}->[0];
    $init{output_channel} = Bric::Biz::OutputChannel->lookup({site_id=>$self->site->get_id, name=>$cn});
    $init{site_id} = $self->site->get_id();
        
    # Build, activate, populate and deploy the template
    my $template = Bric::Biz::Asset::Template->new(\%init);
    $template->activate();
    # Populate the template with the mason code.
    my $content = $self->retrieve_template_content(\%init);
    $template->set_data($content);
    my $desk_id = $self->cms_template_workflow->get_head_desk_id();        
    my $desk = Bric::Biz::Workflow::Parts::Desk->lookup({id=>$desk_id});
    eval {
      $template->save();
      $template->set_current_desk($desk);
      $template->save();
    };
    if ($@) {
      my $bp = 'breakpoint';
    }
    push @bric_templates, $template;
    return \@bric_templates;
  }

  # Deploy the templates and also persist them in the workflow config.
    return \@bric_templates);
}
  $self->deploy_templates({templates=>$bric_templates});
 

sub deploy_templates {
  my ($self) = @_;
  my $user = $self->user;
  for my $template (@{$self->templates}) {
    # checkin the template
    $template->checkin;
    # remove from desk
    my $cur_desk = $template->get_current_desk;
    unless ($cur_desk && $cur_desk->can_publish) {
      # Find a desk to deploy from.
      my $wf = $self->cms_template_workflow();
      my $pub_desk;
      foreach my $d ($wf->allowed_desks) {
	$pub_desk = $d and last if $d->can_publish && $user->can_do($d, READ);
      }
      unless ($pub_desk) {
	warn "Cannot deploy ", $template->get_name, ": no deploy desk\n";
	return;
      }
      # Transfer the template to the publish desk.
      if ($cur_desk) {
	$cur_desk->transfer({ to    => $pub_desk, asset => $template });
	$cur_desk->save;
      } else {
	$pub_desk->accept({ asset => $template });
      }
      $cur_desk = $pub_desk;
      # Save the deploy desk and log it.
      $pub_desk->save;
      Bric::Util::Event->new({
			      key_name  => "template_moved",
			      obj       => $template,
			      user      => $user,
			      attr      => { Desk => $pub_desk->get_name },
			     });
    }
    # Make sure they have permission to deploy (publish).
    unless ($user->can_do($template, PUBLISH)) {
      warn "Cannot deploy ", $template->get_name, ": permission denied\n";
      return;
    }
    # Now remove it!
    $cur_desk->remove_asset($template);
    $cur_desk->save;
    # clear the workflow ID
    if ($template->get_workflow_id) {
      $template->set_workflow_id(undef);
      Bric::Util::Event->new({key_name=>"template_rem_workflow",obj=>$template,user=>$user});
    }
    $template->save;
    # get a new burner
    my $burner = Bric::Util::Burner->new;
    # deploy and save
    $burner->deploy($template);
    $template->set_deploy_date(strfdate());
    $template->set_deploy_status(1);
    $template->set_published_version($template->get_current_version);
    $template->save;
    # Be sure to undeploy it from the user's sandbox.
    my $sb = Bric::Util::Burner->new({user_id => $user->get_id });
    $sb->undeploy($template);
    # log the deploy
    Bric::Util::Event->new({
			    key_name => $template->get_deploy_status
			    ? 'template_redeploy'
			    : 'template_deploy',
			    obj => $template,
			    user => $user,
			   });
  }
}

sub retrieve_template_content {
  my ($self, $args) = @_;
  my $type = $args->{tplate_type};
  my $category_name = $args->{category}->{name};
  my $cat_path;
  if ($category_name =~ /^.*[R|r]oot.*$/xms ) {
    $cat_path = 'root' ;
  } else {
    $cat_path = $category_name;
  }
  my $base_dir = $self->config->{'template_base_dir'};
  my $filename;
  if ( $type == Bric::Biz::Asset::Template::UTILITY_TEMPLATE ) {
    $filename = $args->{name} . '.mc';
  } elsif (  $type == Bric::Biz::Asset::Template::ELEMENT_TEMPLATE ) {
    #        $filename = $args->{element_type}->{key_name} . '.mc';
    $filename = $args->{name} . '.mc';        
  } else {
    #        if ( -e catfile($base_dir, $cat_path, $self->type_map->{$type}, 'autohandler') ) {
    if ( -e catfile($base_dir,  'autohandler') ) {        
      $filename = 'autohandler';
    } else { 
      $filename = 'autohandler';
    }
  }
  #    return read_file(catfile($base_dir, $cat_path, $self->type_map->{$type}, $filename));
  return read_file(catfile($base_dir, $filename));    
}

no Moose;
1;
