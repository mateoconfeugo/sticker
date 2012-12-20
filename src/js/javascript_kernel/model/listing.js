define(['backbone'], function(Backbone) {
	return Backbone.Model.extend({
		// Intended attributes:
		// bid
		// displayURI
		// URI
	        // discription
	        // type: iab
	        // title
		defaults: {
			created: 'today'
		},

	})
});

// 	    urlRoot: '/display'