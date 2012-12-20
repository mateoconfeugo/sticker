define(['jquery', 'backbone','delivery'], 
       function($, Backbone, Delivery) {
	   return Backbone.Router.extend({
               'initialize': function(config){
		   self = this;
		   self.config = config;
		   _.bindAll(this);
               },
               'routes': {
		   "delivery" : "get_listings",
               },
	       "get_listings" : function() {
		   Delivery({"query": "shoes" ,"token": 1212 );
	       }
	   });
       });
