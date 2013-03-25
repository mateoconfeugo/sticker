define(['jquery', 'underscore', 'backbone', 'models/grow'],
       function($, _, Backbone, Grow) {
	   var Grows = Backbone.Collection.extend({
	       urlRoot: 'grow',
               model: Grow,
	       parse: function (response) {
		   return response;
	       }
	   });
	   return Grows;
       });
