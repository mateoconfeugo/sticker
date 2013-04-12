% my $id = $element->get_key_name . '_id_' . $element->get_object_order;
% my $name = $element->get_value('name');
% my $value = $element->get_value('value');
<label for="<% $id %>" title="<% $name %>" ><% $name %>:</label> 
<input id="<% $id %>" name="<% $name %>" value="<% $value %>" />
