use strict;
use warnings;
use Test::More;


use Catalyst::Test 'LandingSite::Delivery';
use LandingSite::Delivery::Controller::ThankYou;

ok( request('/thankyou')->is_success, 'Request should succeed' );
done_testing();
