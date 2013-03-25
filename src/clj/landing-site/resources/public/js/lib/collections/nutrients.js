define(['jquery', 'underscore', 'backbone', 'models/nutrient'],
       function($, _, Backbone, Nutrient) {
	   var nutrients = Backbone.Collection.extend({
	       urlRoot: 'nutrient',
               model: Nutrient,
	       parse: function (response) {
		   return response;
	       }
	   });
	   return nutrients;
       });
