define(['underscore', 'backbone', 'models/listing'], 
       function(_, Backbone, Listing) {
	   return Backbone.Collection.extend({
	       model: Listing,
	       url: '/delivery',
	       initialize: function() {
		   _.bindAll(this, 'onSuccess');
		   this.fetch({success: this.onSuccess});
	       },
	       onSuccess: function(listings) {
		   if(listingss) {
		       var self = this;
		       models = listings.models;
		       _.each(models, function(listing) {
			   self.add(listing);
		       });
		       self.trigger('add');  // This sucks! should not have to do this
		   }
	       }
	   });
       });