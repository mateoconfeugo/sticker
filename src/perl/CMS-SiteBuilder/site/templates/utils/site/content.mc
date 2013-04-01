<div id="content">
  <div id="side_1"></div>
  <div id="side_2">
% my @default = ('Overview', 'Company History', 'Press Release', 'Newsletter', 'Jobs');
    <& /utils/site/site_menu.mc, menu_items => \@default &>
  </div>
  <div id="main_content">
% $burner->chain_next(@_);
  </div>

</div>