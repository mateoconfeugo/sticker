package CMS::Resource::Media;
use Moose::Role;
use IPC::Run3;   

sub import_media {
    my ( $self, $wf ) = @_;
    my %config = %{$wf->context()->param('media_resources_config')};
    my $site_name = $self->site->get_name();
    my $soap =  $self->config->{'bric_soap'};
    my $user = $self->config->{'bric_username'};
    my $pw = $self->config->{'bric_password'};
    my $server = $self->config->{'bric_server'};
    my ($app, $tar) = @config{qw(media_upload_app tar)};
    my $in; my $out; my $err;
    my $cmd = 
	"$app --target-username $user --target-password $pw --media-type generic_image --media-info --make-categories --bric_soap $soap --site $site_name $tar $server";
    run3 $cmd, \$in, \$out, \$err;	
    $self->config->{'import_media_assets_result'} = { out => $out, error => $err  } ;
    my $site_id = $self->site->get_id();    
    my $desk = $self->config->{'media_publish_desk'};
    my $move_cmd = 	"$soap media list_ids --search site_id=$site_id --server $server --user $user --password $pw --search publish_status=0 | $soap workflow move --desk='" . $desk ."' --server $server --user $user --password $pw -";
    run3 $move_cmd, \$in, \$out, \$err;	
    my $publish_cmd = 
	"$soap media list_ids --search site_id=$site_id --server $server --user $user --password $pw --search publish_status=0 | $soap workflow publish --server $server --user $user --password $pw -";
    run3 $publish_cmd, \$in, \$out, \$err;
    warn "$err\n" if $err;
}
 
no Moose;
1;
