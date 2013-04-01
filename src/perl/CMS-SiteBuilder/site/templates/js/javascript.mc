<%init>
my @snippets = $element->get_elements(qw(javascript_snippet));

my $library = '$(document).ready(function() {';
for my $snip (@snippets) {
 my $code = $snip->get_value('code');
 $library .= "$code\n";
}

$library .= '})';
$m->print($library);
</%init>