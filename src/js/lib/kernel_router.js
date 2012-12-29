define(['jquery', 'backbone','views/listings'], 
       function($, Backbone, Listings) {
	   return Backbone.Router.extend({
               'initialize': function(config){
		   self = this;
		   self.config = config;
		   _.bindAll(this);
               },
               'routes': {
		   "delivery" : "get_listings"
               },
	       "get_listings" : function() {
		   Listings({"query": "shoes" ,"token": 1212 });
	       }})});
	       

