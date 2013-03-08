define(
    ['jquery', 'underscore', 'backbone', 'common/region_manager', 'log4javascript'], 

    function($, _, Backbone, RegionManager) {
	var log = log4javascript.getDefaultLogger();
	var router = Backbone.Router.extend({
	    initialize: function(options) {
//		_.bindAll(this, 'routes');
		this.config = options.config;
//		log.info("Initializing Bailout Client Application Router");
		this.manager = new RegionManager({config: options.config});
//		var view = new ServerMonitor({config: this.config});
//		return this;
//		$('#server_message_channel').html(view.render());
//		this.manager.show({view: ServerMonitor, el: '#server_message_channel',  config: this.config});		   	       
	    },
            'routes': {
		'mixer': 'nutrient_mixer'
            },
	    'nutrient_mixer':  function() {
		this.manager.show({view: Mixer, el: '#stage',  config: this.config});
	    }
	});
	return router;
    });
