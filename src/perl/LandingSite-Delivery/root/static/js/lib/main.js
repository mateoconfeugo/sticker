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
	"validate": {
	    deps: ["jquery"],
	    exports: "jQuery.fn.validate"
	},
	'log4javascript': {
	    exports: 'getDefaultLogger log'
	},
	"shim": {
	    "jquery-gentleSelect" : {
		deps: ["jquery"],
		exports: "jQuery.fn.gentleSelect"
	    }
	}
    },
    paths: {
	jquery: '/js/lib/jquery.min',
	underscore: '/js/lib/underscore-min',
	backbone: '/js/lib/backbone-min',
	bootstrapWizard: '/js/lib/jquery.bootstrap.wizard.min',
	socketio: '/js/lib/socket.io',
	bootstrap: '/js/lib/bootstrap',
	log4javascript:'/js/lib/log4javascript',
	validate:'/js/lib/jquery.validate.min'
    },
    text: {
	useXhr: function (url, protocol, hostname, port) {
	    // allow cross-domain requests
	    // remote server allows CORS
	    return true;
	}
    },

});

require(['jquery', 'underscore', 'backbone', 'routers/desktop_router', 'views/lead_editor', 'bootstrapWizard'], 
	function($, _, Backbone, Desktop, LeadEditor) {
	    // Main entry point that runs after getting the configuration data
	    var fetchSuccess = function(cfg) {
		var router = new Desktop({config: cfg});
		cfg.router = router;
		Backbone.history.start();
		$('#rootwizard').bootstrapWizard({
		    'class': 'nav nav-tabs',
		    onNext: function(event) { 
//			$('#lead_form_link').trigger('click');
			router.navigate('lead_form', {trigger: true});
			var index = $('#rootwizard').bootstrapWizard('currentIndex');
			var size = $('#rootwizard').bootstrapWizard('navigationLength');
			if(index >= size) {
			    $('#lead-form-link').trigger('click');
//			    return false;
			}
			return true;
		    }
		});
		var pager_cntls = $(".pager wizard");
		$('.hero-unit').append($('#nav-controls'));
//		var lead_editor = new LeadEditor({el: '#side-lead-form', router: router, template: "lead_form.tt"});
//		lead_editor.render();
	    };
	    
	    // Get configuration and tie to application
	    var config = new Backbone.Model();
	    config.url = "/clientconfig/clientconfig";
	    config.fetch({success: fetchSuccess});
	});
