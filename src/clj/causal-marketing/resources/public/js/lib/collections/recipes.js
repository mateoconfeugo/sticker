define(['jquery', 'underscore', 'backbone', 'models/recipe'],
       function($, _, Backbone, Recipe) {
	   var Recipes = Backbone.Collection.extend({
	       urlRoot: 'recipe',
               model: Recipe,
	       parse: function (response) {
		   return response;
	       }
	   });
	   return Recipes;
       });
