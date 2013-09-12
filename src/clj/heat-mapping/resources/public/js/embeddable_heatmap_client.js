require.config({
    shim: {
	'underscore': {
	    exports: '_'
	},
	"backbone": {
            deps: ["underscore", "jquery"],
            exports: "Backbone" 
	},
    },
    paths: {
	jquery: '/js/lib/jquery.min',
	underscore: '/js/lib/underscore-min',
	backbone: '/js/lib/backbone-min',
	socketio: '/js/lib/socket.io',
    }
});

require(['jquery', 'underscore', 'backbone'], 
	function($, _, Backbone) {
	    var mouse_position = new Backbone.Model.extend({urlRoot: 'heatmap'});
	    var setup_heatmapping = function(cfg) {
		$("body").mousemove(function(event) {
		    var coord = mouse_position;
		    coord.set("x", event.pageX);
		    coord.set("y", event.pageY);
		    coord.save()});
	    };
	    var config = new Backbone.Model({"url": "/clientconfig"});
	    config.fetch({success: function(cfg) {
		setup_heatmapping(cfg);
	    }});
	});
