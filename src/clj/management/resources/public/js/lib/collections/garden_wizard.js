define(['jquery', 'underscore', 'backbone', 
	'views/garden_editor', 'views/reservoir_editor', 'views/mixer_editor',
	'views/station_editor', 'views/grow_browser', 'views/recipe_builder',
	'views/actuator_editor', 'views/task_scheduler', 'views/constraint_task_editor'
	'views/plant_editor', 'log4javascript', 'arboreal'], 
       function($, _, Backbone
		GardenEditor, ReservoirEditor, MixerEditor,
		StationEditor, GrowBrowser, RecipeBuilder,
		ActuatorEditor, TaskScheduler, ConstraintEditor,
		PlantEditor
	       ){
	   var garden = Backbone.Model.extend();
	   var wiz_manager = Backbone.View.extend({
	       initialize: function(options) {
		   var garden_setup = {
		       category: "garden", "title": "Setting up the Garden",
		       subcategories: [
			   {category: "create_garden", "title": "Name the Garden", "type": "GardenEditor"},
			   {category: "create_reservoir", "title": "Configure Reservoir", "type": "ReservoirEditor"},
			   {category: "create_nutrient_mixer", "title": "Configure Nutrient Mixer", "type": "MixerEditor"},
			   {category: "grow", "title": "Assembling a Grow"
			    subcategories: [
				{category: "station", "title": "Adding Stations", "type": "StationEditor"},	 
				{category: "create_grow", "title": "Naming the Grow", "type": "GrowBrowser"},
				{category: "feed_plan", "title": "Building a Feed Plan"
				 subcategories:[{
				     category: "recipe", "title": "Select or create Recipe", "type": "RecipeBuilder"
				 }]},
				{category: "actuator", "title": "Connect Actuators", "type": "ActuatorEditor"},
				{category: "scheduled_task", "title": "Schedule Tasks", "type": "TaskScheduler"},
				{category: "constraint", "title": "Setup System Triggered Tasks": "ConstraintTaskEditor"}
				{category: "plant", "title": "Add Plants": "PlantEditor"}
			    ]}
		       ]};
		   this.wizard_tree = Arboreal.parse(garden_setup, 'subcategories');
		   this.current_postion = 0;
		   this.current_node = this.wizard_tree.find("0");
		   this.render();
		   this.setup_wizard_event_handlers();
		   return this;
	       },
	       tree_iter: function(node) {
		   var location = this.current_postion;
		   var n = node || this.current_node || this.wizard_tree.find(location).data.category;
		   this.current_node = node;
		   return node;
	       },
	       render: function () {
		   var node = this.current_node;
		   var i = 0; views = [];
		   // Build up Model
		   _.each(node.children, function(view) {
		       var token = view.id;
		       view[token] = token;
		       view[parent_id] = node.id
		   });
		   // Create the DOM
		   this.$el.html(Jemplate.process("nested_wizard.tt", views));
		   // Insert views into the DOM
		   _.each(node.children, function(v) {
		       var view = new view.type();
		       $(v.token).html(view.render().el);
		   });
	       },
	       setup_wizard_event_handlers: function() {
		   this.$el.bootstrap({
		       onLast: function(tab, navigation, index) {
			   log.info("Showing the last view");
			   // set the parent wizard active
			   this.wizard_tree.transverseUp(this.tree_iter);
			   self.trigger("depth_changed");
		       },
		       onNext: function(tab, navigation, index) {
			   log.info("Moving to next step in wizard: ")
			   if (self.current_node.children) {
			       self.wizard_tree.transverseDown(this.tree_iter);
			       self.trigger("depth_changed");
			   } else {
			       this.next(); 
			   }
		       },
		       onPrevious: function(tab, navigation, index) {
			   log.info("Moving to previous step in wizard");
			   if(this.currentIndex == 0 ) {
			       this.wizard_tree.transverseUp(this.tree_iter);
			       self.trigger("depth_changed");
			   }
			   this.previous()
		       },
		       onInit: function(tab, navigation, index) {
			   log.info("Building new wizard in nested wizard manager");
		       },
		       onShow: function(tab, navigation, index) {
			   log.info("Showing the view");
			   // Check to see if there are any children if so 
			   // Make that level active
		       },
		       onFirst: function(tab, navigation, index) {
			   log.info("Showing the first view");
		       },
		       onTabClick: function(tab, navigation, index) {
			   log.info("Tab clicking the view");
		       },
		       onTabShow: function(tab, navigation, index) {
			   log.info("Tab Showing the view");
		       }
		   });
	       }

	   });
	   return wiz_manager;
       });

