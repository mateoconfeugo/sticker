define(['jquery', 'underscore', 'backbone'],
       function($, _, Backbone) {
	   var model = Backbone.Model.extend({
	       url: "/testDelivery/delivery",
	       initialize: function() {
		   _.bindAll(this, 'onSuccess');                  
		   this.fetch({success: 'onSuccess'});
	       },
	       onSuccess: function(json) {
		   this.json = json;
		   return json;
	       }
	   });
	   return new model();
       });
