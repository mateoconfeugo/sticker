package CMS::Resource::Story;
use Moose::Role;
use Bric::Biz::Asset::Business::Story;
use Bric::Util::Priv::Parts::Const qw(:all);
use File::Spec::Functions;
use IPC::Run3;
use Bric::Util::Time qw(:all);
use Bric::Util::Burner;
use Bric::Util::Job::Pub;

has stories => (is=>'rw'
	      isa=>'ArrayRef',
	      lazy=>1,
	      builder=>'import_story_resources'
);

has cms_story_workflow => (is=>'rw');

has user => (is=>'rw');

sub import_story_resources {
    my ( $self, $args ) = @_;
    return undef unless $self->site;  # TODO: throw error.
    my %config = %{$self->config{'media_resources_config'};

    # bric_soap options
    my $site_name = $self->site->get_name();
    my $soap =  $self->config->{'bric_soap'};
#    my $user = $self->config->{'bric_username'};
    my $pw = $self->config->{'bric_password'};
    my $server = $self->config->{'bric_server'};
    my $in; my $out; my $err;
    my $site_id = $site->get_id();    
    my $desk = $self->config->{'story_publish_desk'};
    my $path = $self->config->{'story_base_dir'};
    my @stories = ();
    my $dir;

    # Create the stories
    if(-d $path) {
	my $file;
	unless (opendir $dir, $path) { 
	    warn "Could not open directory $path: Error $!\n";
	    return;
	}
	my $user = $self->user;
	my $in; my $out; my $err;
	while($file = readdir $dir) {
            my $cmd = 	qq($soap story create --server $server --user $user->{login} --password $pw );
	    next if $file eq '.' || $file eq '..';
	    $cmd .= ' ' . catfile($path,$file);
	    eval {
		run3 $cmd, \$in, \$out, \$err;		    
	    };
	    if($err) {
		my $bp = 'breakpoint';
	    }
	    my $id;
	    ($id = $out) =~ s/story_(\d+)/$1/g if $out;
	    my $obj = Bric::Biz::Asset::Business::Story->lookup({id=>$id});
	    push @stories, $obj if $obj;
	}
    }
    $self->context->{'story_assets'} = \@stories;
    $self->stories(\@stories);
		   return \@stories;
	}

sub publish_stories {
    my ($self, $args) = @_;
    # Move stories to publish desk.
    my ($bric_wf) = Bric::Biz::Workflow->list({site_id => $site->get_id, type => Bric::Biz::Workflow::STORY_WORKFLOW});
    $self->cms_story_workflow($bric_wf);
    $self->deploy_stories;
    
    # Publish stories
    my $publish_cmd = 
	"$soap story list_ids --search site_id=$site_id --server $server --username $user->{login} --password $pw --search publish_status=0 | $soap workflow publish --server $server --username $user->{login} --password $pw -";
    run3 $publish_cmd, \$in, \$out, \$err;
    warn "$err\n" if $err;
}


sub deploy_stories {
    my ($self) = @_;
    my $user = $self->user;
    for my $story (@{$self->stories}) {
        # checkin the story
        $story->checkin if $story->get_checked_out;
        # remove from desk
        my $cur_desk = $story->get_current_desk;
        unless ($cur_desk && $cur_desk->can_publish) {
            # Find a desk to deploy from.
            my $wf = $self->cms_story_workflow();
            my $pub_desk;
            foreach my $d ($wf->allowed_desks) {
                $pub_desk = $d and last if $d->can_publish && $user->can_do($d, READ);
            }
            unless ($pub_desk) {
                warn "Cannot deploy ", $story->get_name, ": no deploy desk\n";
                return;
            }
            # Transfer the story to the publish desk.
            if ($cur_desk) {
                $cur_desk->transfer({ to    => $pub_desk, asset => $story });
                $cur_desk->save;
            } else {
                $pub_desk->accept({ asset => $story });
            }
            $cur_desk = $pub_desk;
            # Save the deploy desk and log it.
            $pub_desk->save;
            Bric::Util::Event->new({
                                    key_name  => "story_moved",
                                    obj       => $story,
                                    user      => $user,
                                    attr      => { Desk => $pub_desk->get_name },
                                });
        }

=pod        
        # Make sure they have permission to deploy (publish).
        unless ($user->can_do($story, PUBLISH)) {
            warn "Cannot deploy ", $story->get_name, ": permission denied\n";
            return;
        }
        # Push the story through the correct template via a burner
        my $burner = Bric::Util::Burner->new;
        my (@cats) = $story->get_categories;
        my @oc = $story->get_output_channels;
        for my $channel (@oc) {
            for my $category (@cats) {
                $burner->burn_one($story, $channel, $category);
            }
        }
        # Now remove it!
        $cur_desk->remove_asset($story);
        $cur_desk->save;
        # clear the workflow ID
        if ($story->get_workflow_id) {
            $story->set_workflow_id(undef);
            Bric::Util::Event->new({key_name=>"story_rem_workflow",obj=>$story,user=>$user});
        }
        $story->save;
        $story->set_published_version($story->get_current_version);
        $story->save;
        # log the deploy
=cut
    
    }
}
        
no Moose;
1;
