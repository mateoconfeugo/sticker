define(['jquery', 'underscore', 'backbone'],
       function($, _, Backbone) {
	   var dispenser = Backbone.Model.extend({
               urlRoot: 'dispenser'
	   });
	   return dispenser;
       });
