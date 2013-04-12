#!perl -T

use Test::More tests => 16;

BEGIN {
    use_ok( 'CMS::App' ) || print "Bail out!
";
    use_ok( 'CMS::App::Biz' ) || print "Bail out!
";
    use_ok( 'CMS::App::Config' ) || print "Bail out!
";
    use_ok( 'CMS::App::Context' ) || print "Bail out!
";
    use_ok( 'CMS::App::Biz::Action::CreateSite' ) || print "Bail out!
";
    use_ok( 'CMS::App::Biz::Action::CreateGroups' ) || print "Bail out!
";
    use_ok( 'CMS::App::Biz::Action::CreateUsers' ) || print "Bail out!
";
    use_ok( 'CMS::App::Biz::Action::CreateOutputChannels' ) || print "Bail out!
";
    use_ok( 'CMS::App::Biz::Action::CreateUsers' ) || print "Bail out!
";
    use_ok( 'CMS::App::Biz::Action::CreateElements' ) || print "Bail out!
";
    use_ok( 'CMS::App::Biz::Action::CreateTemplates' ) || print "Bail out!
";
    use_ok( 'CMS::App::Biz::Action::CreateCategories' ) || print "Bail out!
";
    use_ok( 'CMS::App::Biz::Action::CreateDestinations' ) || print "Bail out!
";
    use_ok( 'CMS::App::Biz::Action::CreateAlerts' ) || print "Bail out!
";
    use_ok( 'CMS::App::Biz::Action::CreateMediaTypes' ) || print "Bail out!
";
    use_ok( 'CMS::App::Biz::Action::CreateWorkflows' ) || print "Bail out!
";
}

diag( "Testing CMS::App $CMS::App::VERSION, Perl $], $^X" );
