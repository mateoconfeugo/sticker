<ol>
% for my $question (@questions) {
 <li><a href="<%  '#faq' . $question->{order} %>" ><% $question->{text} %></a></li>
%}
</ol>
<ol>
% $burner->display_element($_) for @faqs;
</ol>

<%init>
my @faqs =$element->get_elements(('faq'));
my @questions;
for my $faq (@faqs) {
  my $record = { order => $faq->get_object_order, text => $faq->get_value('question')};
  push @questions, $record;
}
</%init>