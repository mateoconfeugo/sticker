package AdUnit;
use Moose::Role;
use namespace::autoclean;

has adunit_type => (is=>'rw', isa=>'Str'); 
has author => (is=>'rw', isa=>'Str'); 
has created => (is=>'rw', isa=>'Str');
has version => (is=>'rw', isa=>'Str');

no Moose;
1;
