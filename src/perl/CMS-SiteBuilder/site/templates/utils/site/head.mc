<%args>
$title => ()
$stylesheets
</%args>
<head>
<title><% $title %></title>
<meta http-equiv="content-type" content="text/html; charset=iso-8859-1">
% for my $sheet (@$stylesheets) {
  <link  rel="stylesheet" href="<% '/css/' . $sheet %>" type="text/css">
% }
</head>
