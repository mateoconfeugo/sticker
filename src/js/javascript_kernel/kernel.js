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
    },
    paths: {
	jquery: '/js/lib/jquery.min',
	underscore: '/js/lib/underscore-min',
	backbone: '/js/lib/backbone-min',
	text: '/js/lib/text'
    },
    text: {
	useXhr: function (url, protocol, hostname, port) {
	    // allow cross-domain requests
	    // remote server allows CORS
	    return true;
	}
    },
});

require(['backbone', 'kernel_router', 'models/config'], function(Backbone, Desktop, Config) {
    var config =  Config;
    this.router = new Desktop(config);	
});

