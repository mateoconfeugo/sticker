% my @elements = $element->get_elements();
% $burner->display_element($_) for (@elements);


<&| /utils/javascript.mc &>
    var connection_name = 'rest_user_advertiser';
    var cp = $.com.search123.connections[connection_name];
    cp.response_cb = function(json_response) {
      if (json_response.redirect_uri) {
         window.location.href = data.redirect_uri;
      }
      else {
        alert(json_response);
        return false;
      }
      $("#<% $css_id %>").submit(function() { connection_point.send(); }
    }
</&>

<%init>
my $channel = { type => 'POST', req_uri => '/rest/user/advertiser' };
my ($bric_form_elem) = $element->get_elements(('input_form'));
my $key_name = $element->get_key_name();
my $order = $element->get_object_order();
my $css_id = "$key_name$order";
</%init>