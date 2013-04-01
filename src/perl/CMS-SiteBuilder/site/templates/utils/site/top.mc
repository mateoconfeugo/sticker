<% # HTML Template %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
  <& /utils/site/head.mc, title => $title, stylesheets => $stylesheets  &>
  <body>
    <& /utils/site/wrap.mc &>
    <& /utils/site/included_js_lib.mc, js_libs => $libraries &>
    <script type="text/javascript" src="/js/<% $site_name . ".js" %>"</script>
  </body>
</html>

<% # Component arguments %>
<%args>
  $site_name => undef
  $title => undef
  $stylesheets => undef
  $libraries => undef
</%args>

<%doc>

=head1 Name

top

=head1 Description

This is the main template the autohandler uses to create the web pages, add in the stylesheets, included the javascript libraries.  It creates the shell of the website that the content and resource are contained.

=cut
</%doc>
