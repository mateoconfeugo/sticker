package CMS::App::SiteBuilder::Command::configure;
########################################################################
# ABSTRACT:  CMS::App::SiteBuilder::Command::build_config -
#            SiteBuilder command to prompt user for info to build a
#            site config that actually is used consturct the site.
########################################################################
use Moose;
use Moose::Util qw(apply_all_roles);
use XML::Simple;
use Term::UI;
use Term::ReadLine;
use Proc::InvokeEditor;
use Text::Template;

extends qw(MooseX::App::Cmd::Command);

has questions => (
    is         => 'ro',
    isa        => 'HashRef',
    lazy_build => 1
);

has user_responses => (
    is      => 'rw',
    isa     => 'HashRef',
    lazy    => 1,
    builder => 'prompt_user'
);

has template_engine => (
    is         => 'rw',
    lazy_build => 1,
);

has ui => (
    is         => 'rw',
    lazy_build => 1,
);

has ui_type => (
    is      => 'rw',
    default => 'terminal'
);

sub _build_ui {
    my $term = Term::ReadLine->new('cms');
    return $term;
}

sub BUILD {
    my ($self) = @_;
    my @roles = (qw|CMS::Configuration|);
    apply_all_roles( $self, @roles );
}

sub execute {
    my ( $self, $opt, $args ) = @_;
    $self->create_config();
}

sub create_config {
    my ($self) = @_;
    my $text = $self->template_engine->fill_in( HASH => $self->user_responses );
    my $edited_text = Proc::InvokeEditor->edit($text)
      if ( $self->user_responses->{edit_config} );
}

sub prompt_user {
    my ($self) = @_;
    my %options = ();
    my %questions = %{ $self->questions };
    for ( keys %questions ) {
        my ( $function, $prompt, $default ) =
          @questions{qw[function prompt default]};
        $function ||= 'get_reply';
        $options{$_} =
          $self->ui()->$function( prompt => $prompt, default => $default );
    }
    return \%options;
}

sub _build_questions {
    my $self = shift;
    return {
        bric_admin_user =>
          { prompt => 'What is bric admin user name?', default => 'admin', },
        bric_admin_password =>
          { prompt => 'What is bric admin user name?', default => 'changeme' },
        domain_ip =>
          { prompt => 'What is bric server ip?', default => '192.168.1.116' },
        domain_port =>
          { propt => 'What is bric server port?', default => 7000 },
        base_bric_dir => {
            prompt  => 'What is bric base dir?',
            default => '/usr/local/bricolage'
        },
        base_site_dir => { prompt => 'What is bric site resource dir?' },
        domain        => { prompt => 'What is bric server domain name?' },
        host_name     => { prompt => 'What is bric server host name?' },
        site_name     => { prompt => 'What is site name?' },
        deploy_date =>
          { prompt => 'What is sites deploy date?', default => time },
        expire_date =>
          { prompt => 'What is sites expire date?', default => time },
        config_file_path => prompt => 'What is config file name?',
#        default => "$options{site_name}.conf.xml"
      },
      edit_config => {
        function => $self->ui->ask_yn,
        prompt  => 'Would you like to the resulting config file in your editor',
        default => 'y'
      };
}

sub _build_template_engine {
    my ($self) = @_;
    my $template = Text::template->new(
        TYPE   => 'STRING',
        SOURCE => $self->config->default_template
    );
    return $template;
}

no Moose;
1;
