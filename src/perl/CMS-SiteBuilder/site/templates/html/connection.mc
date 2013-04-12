% if($channel_data) {
<&| /js/javascript.mc &>
  <& '/html/channel.mc', channel => $channel_data &>
</&>
% }
% else {
%  $burner->display_element($_) for $element->get_elements('channel');
% }

<%init>
use File::Slurp;
use XML::Simple;
my $channel_data = XMLin(read_file($element->get_related_media()->get_file()));
</%init>