package LandingSite::Delivery;
use Moose;
use namespace::autoclean;
use Catalyst::Runtime 5.80;
use File::Spec::Functions qw(catfile catdir);
use Log::Log4perl::Catalyst;
use Catalyst qw/-Debug 
		ConfigLoader
		Static::Simple
		Session
		Session::Store::FastMmap 
		Session::State::Cookie 
                Compress::Gzip
	       /;

extends 'Catalyst';
our $VERSION = '0.01';

my $base_dir = $ENV{PCS_BASE_DIR} || '/home/pcs';
my $path = catfile($base_dir, 'github', 'florish-online', 'src', 'perl', 'LandingSite-Delivery', 'delivery_config.json');

__PACKAGE__->config('Plugin::ConfigLoader' => {file => $path});
__PACKAGE__->log(Log::Log4perl::Catalyst->new());

__PACKAGE__->config("enable_catalyst_header" => 1);
__PACKAGE__->config('View::Jemplate' => {
    jemplate_dir => __PACKAGE__->path_to('root', 'jemplate'),
    jemplate_ext =>  "tt"
		    });
__PACKAGE__->config('Model::MarketVector' => { args => {root_dir => __PACKAGE__->path_to('root', 'src', 'site')}});
__PACKAGE__->config("Plugin::Static::Simple" => {
    "include_path" => [
	"root/static",
	"root/src",
	"root/static/img",
	"root/static/images",
	"root/static/css",
	"root/static/js"
	],

		    });
__PACKAGE__->config(
    "clientConfig" => {
      "models" => {
         "dispenser" => {
            "url" => "http=>//localhost:3000/dispenser/volume"
         },
      },
      "views" => {
         "RecipeBuilder" => {
            "host_element" => "#hero-unit",
            "template" => "recipe_builder.tt",
            "url" => "http=>//localhost:3000/api/rest/recipe"
         },
      },
      "host_element" => "stage"
   });


__PACKAGE__->setup();

1;
