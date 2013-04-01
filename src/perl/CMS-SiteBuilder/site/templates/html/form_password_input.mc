<%perl>
  my $name = $element->get_value('name');
  my $size  = $element->get_value('size');
  my $html = '<input type="password"' . '  value="$name"' . ' size="$size" </input>';
  $m->print($html);
</%perl>