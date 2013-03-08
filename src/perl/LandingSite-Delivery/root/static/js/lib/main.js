// Filename: main.js
require.config({
    shim: {
	'underscore': {
	    exports: '_'
	},
	"backbone": {
            deps: ["underscore", "jquery"],
            exports: "Backbone" //attaches "Backbone" to the window object
	},
	"socketio": {
	    deps: ["jquery"],
	    exports: "io"
	},
	"bootstrap": {
	    deps: ["jquery"]
	},
	"bootstrapWizard": {
	    deps: ["jquery", "bootstrap"],
	    exports: "jQuery.bootstrapWizard"
	},
	'log4javascript': {
	    exports: 'getDefaultLogger log'
	},
	"shim": {
	    "jquery-gentleSelect" : {
		deps: ["jquery"],
		exports: "jQuery.fn.gentleSelect"
	    },
	}
    },
    paths: {
	jquery: '/js/lib/jquery.min',
	underscore: '/js/lib/underscore-min',
	backbone: '/js/lib/backbone-min',
	bootstrapWizard: '/js/lib/jquery.bootstrap.wizard',
	socketio: '/js/lib/socket.io',
	bootstrap: '/js/lib/bootstrap',
	log4javascript:'/js/lib/log4javascript',
    },
    text: {
	useXhr: function (url, protocol, hostname, port) {
	    // allow cross-domain requests
	    // remote server allows CORS
	    return true;
	}
    },

});


require(['jquery', 'underscore', 'backbone', 'routers/desktop_router'], 
	function($, _, Backbone, Desktop) {

	    // Main entry point that runs after getting the configuration data
	    var fetchSuccess = function(cfg) {
		var router = new Desktop({config: cfg});
		cfg.router = router;
		Backbone.history.start();
	    };
	    
	    // Get configuration and tie to application
	    var config = new Backbone.Model();
	    config.url = "/clientconfig/clientconfig";
	    config.fetch({success: fetchSuccess});
	});
