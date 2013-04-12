<!-- Start "Story" -->

%# Only show this if we are on the first page
% my @elems = $element->get_elements('page');
% if(! scalar @elems ) {
% $burner->display_element($_) for $element->get_elements('faqs', 'page', 'pull_quote')
% }
% else {
% unless ($burner->get_page     ) {
<h1><% $story->get_title %></h1>
<% $element->get_value('deck') %>
<hr />
% }

%# Display all the pages of this story
% $burner->display_pages('page');

<br>
Page <% $burner->get_page + 1 %>
<!-- End "Story" -->
% }