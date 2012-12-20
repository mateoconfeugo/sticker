define(['jquery', 'underscore', 'backbone', 'models/listings','views/listing', 'models/config'],
       function($, _, Backbone, Listings, ListingView, Config) {
	   return Backbone.View.extend({
	       tabName: '#listingContainer'
	       events: {
		   "click a": "clicked"
	       },
	       clicked: function(e){
		   e.preventDefault();
		   var options = {adName:  e.currentTarget.innerHTML};
		   var html = Jemplate.process('default_ad_type.tt', options);
		   $(this.tabName).append(html);
		   return this;
	       },
	       initialize: function() {
		   _.bindAll(this, 'render', 'close', 'clicked', 'updateModel');
		   this.collection = new Listings();
		   this.collection.on("add", this.render);
	       },
	       render: function() {
		   if (this.collection.length > 0 ) {
		       self = this;
		       var models = this.collection.models;
		       $(this.el).empty();
		       _.each(models, function(Listings) {
			   var listingView = new ListingView({model: Listings});
			   var html = listingView.html;
			   $(self.el).append(html);
		       }, this);
		   }
		   return this;
	       },
	       updateModel: function(){
		   this.model.set({adType:$('#ad-type').text()});
		   $('#ad-type').remove();
	       },
	       close: function(){
		   $('#main_content').empty();
		   this.unbind();
	       }
	   });
       });
	