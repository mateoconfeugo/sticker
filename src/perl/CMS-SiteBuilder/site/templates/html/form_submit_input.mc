<%perl>
  my $value = $element->get_value('value');
  my $name  = $element->get_value('name');
  my $html = '<input type="text"' . '  value="$value"' . ' name="$name" </input>';
  $m->print($html);
</%perl>
