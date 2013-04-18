/* 
   Component Name: lead_editor
   Description:  Gather leads
*/

define(
    ["jquery", "underscore", "backbone", "models/lead", "validate"],
    function($, _, Backbone, Lead) {
	var lead_editor =  Backbone.View.extend({
	    events: {
		"click .btn": "updateModel",
	    },
	    initialize: function(options) {
		_.bindAll(this, "render", "updateModel", "submit", "gather_data");
		this.template = options.template;
		this.router = options.router;
		this.model = new Lead();
		this.model.on("sync", this.render);
		this.render();
		$.validator.addMethod("phoneUS", function(phone_number, element) {
		    phone_number = phone_number.replace(/\s+/g, ""); 
		    return this.optional(element) || phone_number.length > 9 &&
			phone_number.match(/^(1-?)?(\([2-9]\d{2}\)|[2-9]\d{2})-?[2-9]\d{2}-?\d{4}$/);
		}, "Please specify a valid phone number");
		return this;
	    },
	    render: function(e) {
		this.$el.html(Jemplate.process(this.template));
		$("#lead_form").validate(this.model.validation);
		return this;
	    },
	    submit: function(e){
//		e.preventDefault();
	    },
	    gather_data: function() {
		var full_name = this.$('#lead_full_name').val();
		this.model.set('full_name', full_name);
		var email = this.$('#lead_email').val();
		this.model.set('email', email);
		var phone = this.$('#lead_phone').val();
		this.model.set('phone', phone);
	    },
	    updateModel: function(e) {
		e.preventDefault();
		var options = this.model.validation;
		$("#lead_form").validate(options)
		if(!$("#lead_form").valid()){
		    return this;
		}
		this.gather_data();
		this.model.save({}, {
		    success: function(model, response, options) {
			$('.modal-backdrop').remove();
			$('#lead_form_link').remove();
			$('#rootwizard').html(Jemplate.process("thank_you.tt"));			
		    },
		    error: function(e, response) {
			var json = JSON.parse(response.responseText);
			var html = Jemplate.process("server_validation_message.tt", {errors: JSON.parse(json.error)});
			this.$('.alert-error').show();
			this.$('.alert-error').append(html);
		    }
		});
		return this;
	    },
	    close: function(){
		this.$el.empty();
		this.unbind();
		return this;
	    }
	});
	return lead_editor;
    });

