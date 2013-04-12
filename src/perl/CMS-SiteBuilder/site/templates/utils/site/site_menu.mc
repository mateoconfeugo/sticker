<%args>
 $menu_items
</%args>


<div id="button">
  <ul>
% for my $item (@$menu_items) {
    <li><a href="<% $item %>" ><% $item %></a></li>
% }
  </ul>
</div>