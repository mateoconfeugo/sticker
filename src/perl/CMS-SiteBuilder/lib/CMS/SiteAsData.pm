package CMS::SiteAsData
use Moose;
use Try::Tiny;
use Bric::Biz::Asset::Business::Story

sub unroll_json {
  my $elements = shift;
  my $result = {};
  my $name = $element->get_key_name;
  for my $element ( @$elements ) {
    my $token = $element->get_key_name;
    try {
      if ($element->get_elements() ){
        my $children = $element->get_elements();
        my $branch  = unroll($children);
        push @{$result->{$token}}, $branch;
      }
      else {
        push @{$result->{$token}}, $element->get_value();
      }
    } catch {
      $result->{$token} = $element->get_value();
    };
  }
  my $coder = JSON::XS->new->ascii->pretty->allow_nonref->allow_blessed;
  return $result;
}
no Moose;
1;

