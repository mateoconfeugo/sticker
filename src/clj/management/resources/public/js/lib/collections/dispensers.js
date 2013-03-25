define(['jquery', 'underscore', 'backbone', 'models/dispenser'],
       function($, _, Backbone, Dispenser) {
	   var Dispensers = Backbone.Collection.extend({
	       urlRoot: 'dispenser',
               model: Dispenser,
	       parse: function (response) {
		   return response;
	       }
	   });
	   return Dispensers;
       });
