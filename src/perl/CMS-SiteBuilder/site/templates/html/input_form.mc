<form <% $name %>  id="<% $css_id %>" class="<% $css_class %>" action="<% $action %>" method="<% $method %>" enctype="<% $enctype %>" >
  <ul>
% foreach my $sub_element ($element->get_elements(@element_list)) {
  <li>
%  $burner->display_element($sub_element);
  </li>
% }
  </ul>
</form>

<&| /utils/javascript.mc &>
    var connection_point = <% $connection %>;
    $.com.search123.connections[<% $connection->get_value('name') %>] = connection_point;
</&>

<%init>
my  $method = 'post';
my  $enctype = '';

my $key_name = $element->get_key_name();
my $order = $element->get_object_order();
my $css_id = "$key_name$order";

my  $name = $element->get_value('name');
my $action = $element->get_value('uri');
my $css_class = $element->get_value('css_class');


my @element_names = qw|form_checkbox_input form_hidden_input form_submit_input
                       form_radio_input form_reset_input form_password_input
                       form_text_input country_selector political_subdivision_selector|;
 my @element_list = map { $element->get_elements($_) } $element->get_elements(@element_names);
 
 my ($connection) = $element->get_elements('connection');
</%init>

