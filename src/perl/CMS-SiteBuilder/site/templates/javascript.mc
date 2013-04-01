<%init>
  my $self = $m->request_comp;
  my $script = $m->content();
  $self->call_method('add_js', code=>$script);
</%init>
