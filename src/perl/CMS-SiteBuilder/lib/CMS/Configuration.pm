package CMS::Configuration;
########################################################################
# ABSTRACT: CMS policy role for config related behavior & attributes.
########################################################################
use Moose::Role;

use Data::Validate::XSD;
use Data::Validate::XSD::ParseXML;
use File::Temp qw(tempfile tempdir);
use XML::Simple;

#requires 'configfile, context';

has config => (
	       is=>'rw',
	       isa=>'HashRef',
	       lazy_build=>1
	      );

has configfile => (
	       is=>'rw',
	       isa=>'HashRef',
	      );

has cms_schema_name => (
			is=>'rw', 
			isa=>'Str',
			default=>'cms_site_config.xsd'
		       );

# TODO remove the default build programmatic
has tmp_dir => (
		is => 'rw', 
		default =>'/tmp'
	       );

has validator => (
		  is=>'rw', 
		  lazy_build=>1
		 );  

has xsd_path => (
		 is=>'rw'
		);

has xsd_schema => (
		   is=>'rw', 
		   lazy_build=>1
		  );


sub _build_config {
  my ($self) = @_;
  return $self->get_config_from_file($self->configfile);
}


sub get_config_from_file {
  my ($self, $file) = @_;
  $XML::Simple::PREFERRED_PARSER = 'XML::Parser';    
  return XMLin($file,
	       KeyAttr => '',
	       ForceArray => [ 'workflow', 'server', 'element_type', 'destination', 'user', 
			       'group', 'output_channel','desk', 'server', 'template'],                                        
	       GroupTags => {workflows => 'workflow',
			     servers => 'server',
			     templates => 'template',
			     element_types => 'element_type',
			     destinations => 'destination',
			     users => 'user',
			     groups => 'group',
			     output_channels => 'output_channel',
			     desks => 'desk',
			     categories => 'category',                                     
			     servers => 'server'},
	      );
}

sub initialize_config {
    my ( $self, $wf ) = @_;
    my $ctx = $self->context();
    # TODO: validate and throw exceptions.
#    my $errors = $self->validator->validate($self->config);
    $self->context($self->config);
    return $self;
}

sub _build_validator {
    my $self = shift;
    my $obj = Data::Validate::XSD->newFromFile($self->xsd_path);
    $obj->setStrict(0); # Turn off.
    $self->validator($obj);
}

sub default_template {
   my ($self) = @_;
  my $site_name = 'ham'; 
  my $bric_admin_password = 'test123',
  my $domain_ip = undef; 
  my $base_bric_dir = undef; 
  my $bric_admin_user = undef; 
  my $domain_name = undef; 
  my $description = undef; 
  my $base_site_dir = undef; 
   my $deploy_date = undef;
   my $expire_date = undef;
      my $domain_port = undef;
      my $doc_root = undef;
      my $host_name  = undef;

   my $xsd =  <<XML;
<cms_site_configuration>
  <name>{$site_name}</name>
  <bric_password>{$bric_admin_password}</bric_password>
  <bric_server>{$domain_ip}:{$domain_port}</bric_server>
  <bric_soap>{$base_bric_dir}/bin/bric_soap</bric_soap>
  <bric_username>{$bric_admin_user}</bric_username>
  <domain_name>{$domain_name}</domain_name>
  <description>{$description}</description>
  <element_types_dir>{$base_site_dir}/element_types</element_types_dir>
  <media_publish_desk>{$site_name} media publish</media_publish_desk>
  <template_publish_desk>{$site_name} template deploy</template_publish_desk>
  <template_base_dir>{$base_site_dir}/templates</template_base_dir>
  <story_publish_desk>{$site_name} story publish</story_publish_desk>
  <story_base_dir>{$base_site_dir}/content</story_base_dir>
  
  <media_resources_config>
    <bric_soap>{$base_bric_dir}/bric_soap</bric_soap>
    <media_upload_app>{$base_bric_dir}/bin/bric_media_upload </media_upload_app>
    <password>{$bric_admin_password}</password>
    <server>{$domain_ip}:{$domain_port}</server>
    <tar>{$base_site_dir}/resources/{$site_name}resources.tar</tar>
    <username>{$bric_admin_user}</username>
  </media_resources_config>
  
  <categories>
    <category>
      <name>{$site_name}root</name>
      <description>root</description>
      <directory>/</directory>
      <uri>/</uri>
    </category>
    <category>
      <name>html</name>
      <description>html documents</description>
      <directory>/html/</directory>
      <uri>/html/</uri>
    </category>
    <category>
      <name>about</name>
      <description>knowledge base</description>
      <directory>/html/about/</directory>
      <uri>/html/about/</uri>
    </category>
    <category>
      <name>publisher</name>
      <description>publisher resources</description>
      <directory>/html/publisher/</directory>
      <uri>/html/publisher/</uri>
    </category>
    <category>
      <name>advertiser</name>
      <description>advertiser resources</description>
      <directory>/html/advertiser/</directory>
      <uri>/html/advertiser/</uri>
    </category>
    <category>
      <name>press release</name>
      <description>press releases</description>
      <directory>/html/press/</directory>
      <uri>/html/press/</uri>
    </category>
    <category>
      <name>css</name>
      <description>user generated css</description>
      <directory>/css/</directory>
      <uri>/css/</uri>
    </category>
    <category>
      <name>javascript</name>
      <description>javascript libraries</description>
      <directory>/js/</directory>
      <uri>/js/</uri>
    </category>
    <category>
      <name>site images</name>
      <description>site level images</description>
      <directory>/images/</directory>
      <uri>/images/</uri>
    </category>
    <category>
      <name>login</name>
      <description>login resources</description>
      <directory>/html/login</directory>
      <uri>/html/login/</uri>
    </category>
    <category>
      <name>utils</name>
      <description>Root for utility templates</description>
      <directory>/utils/</directory>
      <uri>/utils/</uri>
    </category>
    <category>
      <name>utils_site</name>
      <description>Utility templates for building the framework of the site</description>
      <directory>/utils/site/</directory>
      <uri>/utils/site/</uri>
    </category>
    <category>
      <name>utils_tools</name>
      <description>Utility templates for common needed tasks</description>
      <directory>/utils/tools/</directory>
      <uri>/utils/tools/</uri>
    </category>        
  </categories>

  <destinations>
    <destination>
      <server_type>
	<name>Dev Network share</name>
	<description>NFS share connecting cms to web server</description>
	<move_method>File System</move_method>
	<output_channels>html, Javascript, css</output_channels>
      </server_type>
      <servers>
	<server>
	  <cookie></cookie>
	  <doc_root>{$doc_root}</doc_root>
	  <host_name>{$host_name}</host_name>
	  <login>test</login>
	  <os>Unix</os>
	  <password></password>
	</server>
      </servers>
    </destination>
  </destinations>

  <groups>
    <group>
      <name>{$site_name} operations</name>
      <description></description>
      <permanent>1</permanent>
      <secret>0</secret>
    </group>
    <group>
      <name>{$site_name} development</name>
      <description></description>
      <permanent>1</permanent>
      <secret>0</secret>
    </group>
  </groups>

  <output_channels>
    <output_channel>
      <name>html</name>
      <active>1</active>
      <description>channel that allows slugname to be the filename</description>
      <file_ext>html</file_ext>
      <filename>/%{slug}/</filename>
      <fixed_uri_format>/%{categories}/</fixed_uri_format>
      <include_channels>Javascript</include_channels>
      <is_primary>0</is_primary>
      <uri_format>/%{categories}/%Y/%m/%d/%{slug}/</uri_format>
      <use_slug>1</use_slug>
    </output_channel>
    <output_channel>
      <name>css</name>
      <active>1</active>
      <description>channel that allows slugname to be the filename</description>
      <file_ext>css</file_ext>
      <filename>/%{slug}/</filename>
      <fixed_uri_format>/%{categories}/</fixed_uri_format>
      <is_primary>0</is_primary>
      <uri_format>/%{categories}/%Y/%m/%d/%{slug}/</uri_format>
      <use_slug>1</use_slug>
    </output_channel>
    <output_channel>
      <name>Javascript</name>
      <active>1</active>
      <description>channel that allows slugname to be the filename</description>
      <file_ext>js</file_ext>
      <filename>/%{slug}/</filename>
      <fixed_uri_format>/%{categories}/</fixed_uri_format>
      <is_primary>0</is_primary>
      <uri_format>/%{categories}/%Y/%m/%d/%{slug}/</uri_format>
      <use_slug>1</use_slug>
    </output_channel>
  </output_channels>

  <templates>
<!-- Category Templates -->
    <template>
      <name>autohandler</name>
      <category>/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>site top autohandler does almost nothing</description>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>2</tplate_type>
      <workflow_id></workflow_id>
    </template>
    <template>
      <name>autohandler</name>
      <category>/html/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>autohandler responsible for createing dom framework</description>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>2</tplate_type>
      <workflow_id></workflow_id>
    </template>
  
<!-- Element Templates -->    
    <template>
      <name>generic_story</name>
      <category>/html/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>basic story element template</description>
      <element_type>generic_story</element_type>
      <element_type_id></element_type_id>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>1</tplate_type>
      <workflow_id></workflow_id>
    </template>
    <template>
      <name>network_advertiser_signup</name>
      <category>/html/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>basic story element template</description>
      <element_type>network_advertiser_signup</element_type>
      <element_type_id></element_type_id>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>1</tplate_type>
      <workflow_id></workflow_id>
    </template>    
    <template>
      <name>form</name>
      <category>/html/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>form element template</description>
      <element_type>form</element_type>
      <element_type_id></element_type_id>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>1</tplate_type>
      <workflow_id></workflow_id>
    </template>
    <template>
      <name>form checkbox input</name>
      <category>/html/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>form checkbox element template</description>
      <element_type>form checkbox input</element_type>
      <element_type_id></element_type_id>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>1</tplate_type>
      <workflow_id></workflow_id>
    </template>
    <template>
      <name>form hidden input</name>
      <category>/html/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>form hiddenelement template</description>
      <element_type>form_hidden_input</element_type>
      <element_type_id></element_type_id>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>1</tplate_type>
      <workflow_id></workflow_id>
    </template>
        <template>
      <name>form password input</name>
      <category>/html/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>form passwordelement template</description>
      <element_type>form_password_input</element_type>
      <element_type_id></element_type_id>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>1</tplate_type>
      <workflow_id></workflow_id>
    </template>
        <template>
      <name>form radio input</name>
      <category>/html/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>form radio  element template</description>
      <element_type>form_radio_input</element_type>
      <element_type_id></element_type_id>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>1</tplate_type>
      <workflow_id></workflow_id>
    </template>
    <template>
      <name>form reset</name>
      <category>/html/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>form reset button element template</description>
      <element_type>form_rest_input</element_type>
      <element_type_id></element_type_id>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>1</tplate_type>
      <workflow_id></workflow_id>
    </template>
    <template>
      <name>form text input</name>
      <category>/html/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>form text element template</description>
      <element_type>form_text_input</element_type>
      <element_type_id></element_type_id>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>1</tplate_type>
      <workflow_id></workflow_id>
    </template>
    <template>
      <name>form submit input</name>
      <category>/html/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>form submit inputelement template</description>
      <element_type>form_submit_input</element_type>
      <element_type_id></element_type_id>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>1</tplate_type>
      <workflow_id></workflow_id>
    </template>        
    <template>
      <name>page</name>
      <category>/html/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>page element template</description>
      <element_type>page</element_type>
      <element_type_id></element_type_id>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>1</tplate_type>
      <workflow_id></workflow_id>
    </template>
    <template>
      <name>channel</name>
      <category>/html/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>channel element template</description>
      <element_type>channel</element_type>
      <element_type_id></element_type_id>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>1</tplate_type>
      <workflow_id></workflow_id>
    </template>
    <template>
      <name>connection</name>
      <category>/html/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>connection element template</description>
      <element_type>connection</element_type>
      <element_type_id></element_type_id>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>1</tplate_type>
      <workflow_id></workflow_id>
    </template>
    <template>
      <name>faqs</name>
      <category>/html/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>faqs element template</description>
      <element_type>faqs</element_type>
      <element_type_id></element_type_id>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>1</tplate_type>
      <workflow_id></workflow_id>
    </template>
    <template>
      <name>faq</name>
      <category>/html/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>faq element template</description>
      <element_type>faq</element_type>
      <element_type_id></element_type_id>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>1</tplate_type>
      <workflow_id></workflow_id>
    </template>

    <template>
      <name>related stories</name>
      <category>/html/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>related stories element template</description>
      <element_type>related_stories</element_type>
      <element_type_id></element_type_id>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>1</tplate_type>
      <workflow_id></workflow_id>
    </template>
    <template>
      <name>related story</name>
      <category>/html/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>related story element template</description>
      <element_type>related_story</element_type>
      <element_type_id></element_type_id>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>1</tplate_type>
      <workflow_id></workflow_id>
    </template>
    <template>
      <name>related media</name>
      <category>/html/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>related media element template</description>
      <element_type>related_media</element_type>
      <element_type_id></element_type_id>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>1</tplate_type>
      <workflow_id></workflow_id>
    </template>

    <template>
      <name>story</name>
      <category>/html/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>basic story element template</description>
      <element_type>story</element_type>
      <element_type_id></element_type_id>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>1</tplate_type>
      <workflow_id></workflow_id>
    </template>
    <template>
      <name>cover</name>
      <category>/html/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>basic coverstory element template</description>
      <element_type>cover</element_type>
      <element_type_id></element_type_id>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>1</tplate_type>
      <workflow_id></workflow_id>
    </template>

    <template>
      <name>css</name>
      <category>/css/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>cascaded stylesheet</description>
      <element_type>css</element_type>
      <element_type_id></element_type_id>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>css</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>1</tplate_type>
      <workflow_id></workflow_id>
    </template>

    <template>
      <name>javascript snippet</name>
      <category>/js/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>creates javascript elements into the site specific javascript library</description>
      <expire_date>{$expire_date}</expire_date>
      <element_type>javascript_snippet</element_type>
      <element_type_id></element_type_id>      
      <output_channel>Javascript</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>1</tplate_type>
      <workflow_id></workflow_id>
    </template>
    
    <template>
      <name>javascript library</name>
      <category>/js/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>Javascript libraries to include and work on the html document</description>
      <element_type>javascript_library</element_type>
      <element_type_id></element_type_id>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>Javascript</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>1</tplate_type>
      <workflow_id></workflow_id>
    </template>

    <template>
      <name>javascript</name>
      <category>/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>Javascript to include in to the site javascript library</description>
      <element_type>javascript</element_type>
      <element_type_id></element_type_id>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>Javascript</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>3</tplate_type>
      <workflow_id></workflow_id>
    </template>

    <template>
      <name>content</name>
      <category>/utils_site/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>holds the main content of the page</description>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>3</tplate_type>
      <workflow_id></workflow_id>
    </template>

    <template>
      <name>footer</name>
      <category>/utils_site/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>the bottom layout framework of the site</description>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>3</tplate_type>
      <workflow_id></workflow_id>
    </template>
    <template>
      <name>nav_bar</name>
      <category>/utils_site/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>side menu</description>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>3</tplate_type>
      <workflow_id></workflow_id>
    </template>
    <template>
      <name>site_menu</name>
      <category>/utils_site/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>main menu</description>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>3</tplate_type>
      <workflow_id></workflow_id>
    </template>        
    <template>
      <name>head</name>
      <category>/utils_site/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>build the head node of the html dom</description>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>3</tplate_type>
      <workflow_id></workflow_id>
    </template>
    <template>
      <name>header</name>
      <category>/utils_site/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>the top layout framework of the site</description>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>3</tplate_type>
      <workflow_id></workflow_id>
    </template>
    <template>
      <name>top</name>
      <category>/utils_site/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>Top DOM level framework</description>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>3</tplate_type>
      <workflow_id></workflow_id>
    </template>
    <template>
      <name>wrap</name>
      <category>/utils_site/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>Framwork of the site top level content layout struture</description>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>3</tplate_type>
      <workflow_id></workflow_id>
    </template>
    <template>
      <name>storylist</name>
      <category>/utils_tools/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>Reteive a list of story ids</description>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>3</tplate_type>
      <workflow_id></workflow_id>      
    </template>
    <template>
      <name>search_box</name>
      <category>/utils_site/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>Reteive a list of story ids</description>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>3</tplate_type>
      <workflow_id></workflow_id>      
    </template>
    <template>
      <name>copy_write</name>
      <category>/utils_site/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>copywrite</description>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>3</tplate_type>
      <workflow_id></workflow_id>      
    </template>
    <template>
      <name>included_js_lib</name>
      <category>/utils_site/</category>
      <category__id></category__id>
      <deploy_date>{$deploy_date}</deploy_date>
      <description>copywrite</description>
      <expire_date>{$expire_date}</expire_date>
      <output_channel>html</output_channel>
      <output_channel__id></output_channel__id>
      <tplate_type>3</tplate_type>
      <workflow_id></workflow_id>      
    </template>            
  </templates>
  <users>
    <user>
      <fname>ham</fname>
      <lname>lettuce</lname>
      <login>hsausage</login>
      <mname>sausage</mname>
      <password>password</password>
      <prefix>Mr</prefix>
      <suffix>Jr</suffix>
    </user>
  </users>
  <workflows>
    <workflow>
      <name>story workflow</name>
      <description>content workflow</description>
      <start_desk>{$site_name} content edit</start_desk>
      <type>2</type>
      <desks>
	<desk>
	  <name>{$site_name} content edit</name>
	  <description>Edit Desk</description>
	  <publish>0</publish>
	</desk>
	<desk>
	  <name>{$site_name} content copy</name>
	  <description>Copy Desk</description>
	  <publish>0</publish>
	</desk>
	<desk>
	  <name>{$site_name} content legal</name>
	  <description>Legal Desk</description>
	  <publish>0</publish>
	</desk>
	<desk>
	  <name>{$site_name} content publish</name>
	  <description>Publish Desk</description>
	  <publish>1</publish>
	</desk>
      </desks>
    </workflow>
    <workflow>
      <name>media workflow </name>
      <description>media workflow</description>
      <start_desk>{$site_name} media art</start_desk>
      <type>3</type>      
      <desks>
	<desk>
	  <name>{$site_name} media art</name>
	  <description>Art Desk</description>
	  <publish>0</publish>
	</desk>
	<desk>
	  <name>{$site_name} media legal</name>
	  <description>Legal Desk</description>
	  <publish>0</publish>
	</desk>
	<desk>
	  <name>{$site_name} media publish</name>
	  <description>Publish Desk</description>
	  <publish>1</publish>
	</desk>
      </desks>
    </workflow>
    <workflow>
      <name>template workflow</name>
      <description>template workflow</description>
      <start_desk>{$site_name} template develop</start_desk>
      <type>1</type>
      <desks>
	<desk>
	  <name>{$site_name} template develop</name>
	  <description>developer desk</description>
	  <publish>0</publish>
	</desk>
	<desk>
	  <name>{$site_name} template deploy</name>
	  <description>deployment desk</description>
	  <publish>1</publish>
	</desk>
      </desks>
    </workflow>
  </workflows>
</cms_site_configuration>
XML
}


sub _build_xsd_schema {
    my ($self) = @_;
    my $xsd =  <<XSD;
<?xml version="1.0"?>
<!DOCTYPE xs:schema PUBLIC "-//W3C//DTD XMLSCHEMA 200102//EN" "XMLSchema.dtd">
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema">
  <xs:element ref="cms_site_configuration" name="cms_site_configuration">
    <xs:complexType name="cms_site_configuration">
      <xs:sequence>
        <xs:element ref="name"/>
        <xs:element ref="bric_password"/>
        <xs:element ref="bric_server"/>
        <xs:element ref="bric_soap"/>
        <xs:element ref="bric_username"/>
        <xs:element ref="domain_name"/>
        <xs:element ref="description"/>
        <xs:element ref="element_types_dir"/>
        <xs:element ref="media_publish_desk"/>
        <xs:element ref="template_base_dir"/>
        <xs:element ref="media_resources_config"/>
        <xs:element ref="categories"/>
        <xs:element ref="destinations"/>
        <xs:element ref="groups"/>
        <xs:element ref="output_channels"/>
        <xs:element ref="templates"/>
        <xs:element ref="users"/>
        <xs:element ref="workflows"/>
      </xs:sequence>
    </xs:complexType>
  </xs:element>
  <xs:element name="bric_password" type="xs:token"/>
  <xs:element name="bric_server" type="xs:token"/>
  <xs:element name="bric_username" type="xs:token"/>
  <xs:element name="domain_name" type="xs:token"/>
  <xs:element name="element_types_dir" type="xs:token"/>
  <xs:element name="media_publish_desk" type="xs:token"/>
  <xs:element name="template_base_dir" type="xs:token"/>
  <xs:element name="media_resources_config">
    <xs:complexType>
      <xs:sequence>
        <xs:element ref="bric_soap"/>
        <xs:element ref="media_upload_app"/>
        <xs:element ref="password"/>
        <xs:element ref="server"/>
        <xs:element ref="tar"/>
        <xs:element ref="username"/>
      </xs:sequence>
    </xs:complexType>
  </xs:element>
  <xs:element name="media_upload_app" type="xs:token"/>
  <xs:element name="tar" type="xs:token"/>
  <xs:element name="username" type="xs:token"/>

    <xs:complexType name="categories">
      <xs:sequence>
        <xs:element maxOccurs="unbounded" ref="category"/>
      </xs:sequence>
    </xs:complexType>

    <xs:complexType name="destinations">
      <xs:sequence>
        <xs:element ref="destination"/>
      </xs:sequence>
    </xs:complexType>

    <xs:complexType name="destination">
      <xs:sequence>
        <xs:element ref="server_types"/>
      </xs:sequence>
    </xs:complexType>


    <xs:complexType name="server_types">
      <xs:sequence>
        <xs:element ref="server_type"/>
        <xs:element ref="servers"/>
      </xs:sequence>
    </xs:complexType>

    <xs:complexType name="server_type">
      <xs:sequence>
        <xs:element ref="name"/>
        <xs:element ref="description"/>
        <xs:element ref="move_method"/>
        <xs:element ref="output_channels"/>
      </xs:sequence>
    </xs:complexType>

  <xs:element name="move_method" type="xs:token"/>

    <xs:complexType name="servers">
      <xs:sequence>
        <xs:element ref="server"/>
      </xs:sequence>
    </xs:complexType>

    <xs:complexType name="groups">
      <xs:sequence>
        <xs:element maxOccurs="unbounded" ref="group"/>
      </xs:sequence>
    </xs:complexType>


    <xs:complexType name="group">
      <xs:sequence>
        <xs:element ref="name"/>
        <xs:element ref="description"/>
        <xs:element ref="permanent"/>
        <xs:element ref="secret"/>
      </xs:sequence>
    </xs:complexType>

  <xs:element name="permanent" type="xs:integer"/>
  <xs:element name="secret" type="xs:integer"/>

    <xs:complexType name="templates">
      <xs:sequence>
        <xs:element maxOccurs="unbounded" ref="template"/>
      </xs:sequence>
    </xs:complexType>

    <xs:complexType name="template">
      <xs:sequence>
        <xs:element ref="name"/>
        <xs:element ref="category"/>
        <xs:element ref="category__id"/>
        <xs:element ref="deploy_date"/>
        <xs:element ref="description"/>
        <xs:sequence minOccurs="0">
          <xs:element ref="element_type"/>
          <xs:element ref="element_type_id"/>
        </xs:sequence>
        <xs:element ref="expire_date"/>
        <xs:element ref="output_channel"/>
        <xs:element ref="output_channel__id"/>
        <xs:element ref="tplate_type"/>
        <xs:element ref="workflow_id"/>
      </xs:sequence>
    </xs:complexType>

    <xs:complexType name="category__id"/>

  <xs:element name="deploy_date" type="xs:integer"/>
  <xs:element name="element_type" type="xs:token"/>

    <xs:complexType name="element_type_id"/>

  <xs:element name="expire_date" type="xs:integer"/>

    <xs:complexType name="output_channel__id"/>

  <xs:element name="tplate_type" type="xs:integer"/>
    <xs:complexType  name="workflow_id"/>


    <xs:complexType name="users">
      <xs:sequence>
        <xs:element ref="user"/>
      </xs:sequence>
    </xs:complexType>

    <xs:complexType name="user">
      <xs:sequence>
        <xs:element ref="fname"/>
        <xs:element ref="lname"/>
        <xs:element ref="login"/>
        <xs:element ref="mname"/>
        <xs:element ref="password"/>
        <xs:element ref="prefix"/>
        <xs:element ref="suffix"/>
      </xs:sequence>
    </xs:complexType>

  <xs:element name="fname" type="xs:token"/>
  <xs:element name="lname" type="xs:token"/>
  <xs:element name="mname" type="xs:token"/>
  <xs:element name="prefix" type="xs:token"/>
  <xs:element name="suffix" type="xs:token"/>

    <xs:complexType name="workflows">
      <xs:sequence>
        <xs:element maxOccurs="unbounded" ref="workflow"/>
      </xs:sequence>
    </xs:complexType>

    <xs:complexType name="workflow">
      <xs:sequence>
        <xs:element ref="name"/>
        <xs:element ref="description"/>
        <xs:choice maxOccurs="unbounded">
          <xs:element ref="desks"/>
          <xs:element ref="start_desk"/>
          <xs:element ref="type"/>
        </xs:choice>
      </xs:sequence>
    </xs:complexType>


    <xs:complexType name="desks">
      <xs:sequence>
        <xs:element maxOccurs="unbounded" ref="desk"/>
      </xs:sequence>
    </xs:complexType>

    <xs:complexType name="desk">
      <xs:sequence>
        <xs:element ref="name"/>
        <xs:element ref="description"/>
        <xs:element ref="publish"/>
      </xs:sequence>
    </xs:complexType>

  <xs:element name="publish" type="xs:integer"/>
  <xs:element name="start_desk" type="xs:token"/>
  <xs:element name="type" type="xs:integer"/>
  <xs:element name="name" type="xs:token"/>
  <xs:element name="bric_soap" type="xs:token"/>
  <xs:element name="description" type="xs:token"/>
  <xs:element name="password" type="xs:token"/>

    <xs:complexType mixed="true" name="server">
      <xs:choice minOccurs="0" maxOccurs="unbounded">
        <xs:element ref="login"/>
        <xs:element ref="password"/>
        <xs:element ref="cookie"/>
        <xs:element ref="doc_root"/>
        <xs:element ref="host_name"/>
        <xs:element ref="os"/>
      </xs:choice>
    </xs:complexType>

    <xs:complexType name="cookie"/>
  <xs:element name="doc_root" type="xs:token"/>
  <xs:element name="host_name" type="xs:token"/>
  <xs:element name="os" type="xs:token"/>

    <xs:complexType mixed="true" name="category">
      <xs:choice minOccurs="0" maxOccurs="unbounded">
        <xs:element ref="description"/>
        <xs:element ref="name"/>
        <xs:element ref="directory"/>
        <xs:element ref="uri"/>
      </xs:choice>
    </xs:complexType>

  <xs:element name="directory" type="xs:token"/>
  <xs:element name="uri" type="xs:token"/>

  <xs:complexType mixed="true" name="output_channels">
      <xs:sequence>
        <xs:element minOccurs="0" maxOccurs="unbounded" ref="output_channel"/>
      </xs:sequence>
    </xs:complexType>

    <xs:complexType mixed="true" name="output_channel">
      <xs:choice minOccurs="0" maxOccurs="unbounded">
        <xs:element ref="description"/>
        <xs:element ref="name"/>
        <xs:element ref="active"/>
        <xs:element ref="file_ext"/>
        <xs:element ref="filename"/>
        <xs:element ref="fixed_uri_format"/>
        <xs:element ref="include_channels"/>
        <xs:element ref="is_primary"/>
        <xs:element ref="uri_format"/>
        <xs:element ref="use_slug"/>
      </xs:choice>
    </xs:complexType>

  <xs:element name="active" type="xs:integer"/>
  <xs:element name="file_ext" type="xs:token"/>
  <xs:element name="filename" type="xs:token"/>
  <xs:element name="fixed_uri_format" type="xs:token"/>
  <xs:element name="include_channels" type="xs:token"/>
  <xs:element name="is_primary" type="xs:integer"/>
  <xs:element name="uri_format" type="xs:token"/>
  <xs:element name="use_slug" type="xs:integer"/>
  <xs:element name="login" type="xs:token"/>
</xs:schema>
XSD

    my ($xsd_fh, $xsd_path) = tempfile(TEMPLATE => $self->cms_schema_name, DIR => $self->tmp_dir);
    $self->xsd_path($xsd_path);
    print $xsd_fh $xsd;
    close $xsd_fh;
    $self->xsd_schema($xsd);
    return $xsd;
}
no Moose;
1;
