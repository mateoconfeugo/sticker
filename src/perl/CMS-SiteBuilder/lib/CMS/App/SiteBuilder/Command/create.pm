package CMS::App::SiteBuilder::Command::create;
########################################################################
# ABSTRACT:  CMS::App::SiteBuilder::Command::create -     
#            SiteBuilder command to create a cms driven web site from 
#            a configuration file.
#            ex.  sitebuilder create -c /path/to/config.file
########################################################################
use Moose;
use Moose::Util qw(apply_all_roles);
use XML::Simple;

extends qw(MooseX::App::Cmd::Command);

has configfile => (
		   is=>"rw",
		   required=>1,
		  );

# Setup the almighty config object.
# build all the cms asset objects initialized solely 
# on information in the config object.

sub BUILD {
  my ($self) = @_;
  my @roles = (qw| 
		   CMS::Utils CMS::Configuration CMS::Site CMS::Group CMS::Category CMS::ElementType 
		   CMS::Workflow CMS::Template CMS::Destination CMS::Media CMS::Resource::Story
		   CMS::OutputChannel
		 |);
  apply_all_roles($self, @roles);
  $self->initialize_config;
  $self->create_site;
  $self->create_groups;
  $self->create_users;
  $self->create_categories;
  $self->create_output_channels;
  $self->create_workflows;
  $self->create_destinations;
}
  
# Bring into the cms object the various aggregate objects 
# that use data from resource files to initialize themselves.
sub execute {
  my ( $self, $opt, $args ) = @_;
  $self->import_element_types();
  $self->import_templates();
  $self->deploy_templates();
  $self->import_story_resources();
  $self->import_media_resources();
  $self->publish_stories();
}

no Moose;
1;

