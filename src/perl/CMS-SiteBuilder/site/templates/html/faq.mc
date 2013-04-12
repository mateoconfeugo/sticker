<li>
  <a name="<% 'faq' . $order %>" ></a>
  <% $question %>
  <p><% $answer %></p>
</li>

<%init>
my $question = $element->get_value('question');
my $answer = $element->get_value('answer');
my $order = $element->get_object_order();
</%init>