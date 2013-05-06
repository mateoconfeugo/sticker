define(['jquery', 'underscore', 'backbone'],
       function($, _, Backbone) {
	   var model = Backbone.Model.extend({
	       url: "/clientconfig",
	   });
	   return model;
       });
