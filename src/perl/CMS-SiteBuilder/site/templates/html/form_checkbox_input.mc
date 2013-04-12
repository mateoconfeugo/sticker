% my $name = $element->get_value('name');
% my $value = $element->get_value('value');
% my $id = $element->get_key_name . '_id_' . $element->get_object_order;
% my $checked = $element->get_value('checked');
<label for="<% $id %>" title="<% $name %>" ><% $name %>:</label>
<input id="<% $id %>"  type="checkbox" name="<% $name %>" value="<% $value %>" checked="<% $checked %>" />