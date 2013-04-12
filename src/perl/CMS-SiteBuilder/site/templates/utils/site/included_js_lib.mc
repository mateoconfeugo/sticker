<%args>
  $js_libs => []
</%args>

% for my $lib (@$js_libs) {
<script type="text/javascript" src="<% '/js/' . $lib %>" ></script>
% }