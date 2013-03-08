define(['jquery', 'underscore', 'backbone', 'models/reservoir'],
       function($, _, Backbone, Reservoir) {
	   var Reservoirs = Backbone.Collection.extend({
	       urlRoot: 'reservoir',
               model: Reservoir,
	       parse: function (response) {
		   return response;
	       }
	   });
	   return Reservoirs;
       });
