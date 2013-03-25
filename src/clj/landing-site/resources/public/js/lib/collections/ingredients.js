define(['jquery', 'underscore', 'backbone', 'models/ingredient'],
       function($, _, Backbone, Ingredient) {
	   var Ingredients = Backbone.Collection.extend({
	       urlRoot: 'ingredient',
               model: Ingredient,
	       parse: function (response) {
		   return response;
	       }
	   });
	   return Ingredients;
       });
