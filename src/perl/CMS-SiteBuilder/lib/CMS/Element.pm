#### TODO: Remove this file think #####

package CMS::Action::CreateElements;
use Moose;
use MooseX::NonMoose;


use Bric::Biz::Category;
use Bric::Biz::ElementType;

extends 'Workflow::Action';

sub execute {
    my ( $self, $wf ) = @_;
    use Log::Log4perl qw(get_logger);
    my $logger = get_logger("");
    my $categories = $wf->context()->param('categories');
    my $bric_et = [];
    my $site = $wf->context->param('site_asset');
    for my $et (@$element_types) {
	my @attrs = (qw|name key_name description top_level paginated fixed_uri 
		     related_story related_media media displayed biz_class_id|);
	$init{site_id} = $site->{id};
	my %init = map { $_ => $et->{$_}} @attrs;
	my $obj = Bric::Biz::ElementType->new($init)
	eval {
	    $obj->save();
	};
	if($@) {
	    my $bp = 'breakpoint';
	}
	push @$bric_et, $obj;
    }
    $wf->context->param('element_type_assets' => $bric_et);
}


=head1 NAME

CMS::Action::CreateElements - The great new CMS::Action::CreateElements!

=head1 VERSION

Version 0.01

=cut

our $VERSION = '0.01';


=head1 SYNOPSIS

Quick summary of what the module does.

Perhaps a little code snippet.

    use CMS::Action::CreateElements;

    my $foo = CMS::Action::CreateElements->new();
    ...

=head1 EXPORT

A list of functions that can be exported.  You can delete this section
if you don't export anything, such as for a purely object-oriented module.

=head1 SUBROUTINES/METHODS

=head2 function1

=cut

sub function1 {
}

=head2 function2

=cut

sub function2 {
}

=head1 AUTHOR

Matt Burns, C<< <mburns at valueclick.com> >>

=head1 BUGS

Please report any bugs or feature requests to C<bug-cms-app at rt.cpan.org>, or through
the web interface at L<http://rt.cpan.org/NoAuth/ReportBug.html?Queue=CMS-App>.  I will be notified, and then you'll
automatically be notified of progress on your bug as I make changes.




=head1 SUPPORT

You can find documentation for this module with the perldoc command.

    perldoc CMS::Action::CreateElements


You can also look for information at:

=over 4

=item * RT: CPAN's request tracker

L<http://rt.cpan.org/NoAuth/Bugs.html?Dist=CMS-App>

=item * AnnoCPAN: Annotated CPAN documentation

L<http://annocpan.org/dist/CMS-App>

=item * CPAN Ratings

L<http://cpanratings.perl.org/d/CMS-App>

=item * Search CPAN

L<http://search.cpan.org/dist/CMS-App/>

=back


=head1 ACKNOWLEDGEMENTS


=head1 LICENSE AND COPYRIGHT

Copyright 2010 Matt Burns.

This program is free software; you can redistribute it and/or modify it
under the terms of either: the GNU General Public License as published
by the Free Software Foundation; or the Artistic License.

See http://dev.perl.org/licenses/ for more information.


=cut

1; # End of CMS::Action::CreateElements
