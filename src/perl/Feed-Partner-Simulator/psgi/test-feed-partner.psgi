# The test feed partner server for simulating feed partners
use Plack::Builder;
use Plack::App::File;
use Plack::Response;
use Plack::Request;
use File::Spec::Functions;
use Feed::Partner::Simulator;
use XML::Simple;

# Environment Setup
my $base_dir = $ENV{TEST_FEED_ROOT_DIR};
my $test_base_dir = catdir($base_dir, 'src', 'delivery-engine', 't');
warn "$test_base_dir\n";
exit unless $test_base_dir;

# Server Object
my $server = Feed::Partner::Simulator->new();
my $keyword = "shoes";
my $token = 1212;

# Request handler
my $handler = sub {
    my $env = shift;
    my $req = Plack::Request->new($env);
    my $args = {
		request=>$req,
		keyword=>$keyword, 
		token=>$token,
		ip=>$req->address,
		ua=>$req->headers->user_agent,
		ref=>$req->headers->referer};
    my $data = $server->handle_request($args);
    my $xml =  XMLout( {listing=>$data} , RootName=>"listings");
    my $res = $req->new_response();
    $res->body($xml);
    $res->headers([ 'Content-Type' => 'application/xml' ]);
    $res->status(200);
    return $res->finalize();
};

# http app server configuration
my $root = catfile($test_base_dir, 'tools');
my $tt_path = catfile($test_base_dir, 'tools', 'template');
my $js_path = catfile($test_base_dir, 'tools', 'js');
my $html_path = catfile($test_base_dir, 'tools', 'html');
my $css_path = catfile($test_base_dir, 'tools', 'css');

builder {
      mount "/" => Plack::App::File->new(root=>$html_path);
      mount "/js" => Plack::App::File->new(root=>$js_path);
      mount "/css" => Plack::App::File->new(root=>$css_path);
      mount "/delivery" => $handler;
    };

