/* 
   Component Name: lead_editor
   Description:  Gather leads
*/

define(
    ["jquery", "underscore", "backbone", "models/lead", "validate"],
    function($, _, Backbone, Lead) {
	var lead_editor =  Backbone.View.extend({
	    events: {
		"click .lead-btn": "updateModel",
	    },
	    initialize: function(options) {
		_.bindAll(this, "render", "updateModel", "submit", "gather_data", "on_save_success", "on_save_error");
		this.template = options.template;
		this.router = options.router;
		this.model = new Lead();
		this.model.on("sync", this.render);

		$.validator.addMethod("phoneUS", function(phone_number, element) {
		    phone_number = phone_number.replace(/\s+/g, ""); 
		    return this.optional(element) || phone_number.length > 9 &&
			phone_number.match(/^(1-?)?(\([2-9]\d{2}\)|[2-9]\d{2})-?[2-9]\d{2}-?\d{4}$/);
		}, "Please specify a valid phone number");
	    },
	    render: function(e) {
		return this;
	    },
	    submit: function(e){
//		e.preventDefault();
	    },
	    gather_data: function(model) {
		var full_name = this.$('#lead_full_name').val();
		model.set('full_name', full_name);
		var email = this.$('#lead_email').val();
		model.set('email', email);
		var phone = this.$('#lead_phone').val();
		model.set('phone', phone);
		try {
		    var postal_code = this.$('#lead_postal_code').val();
		    model.set('postal_code', postal_code);
		} catch(err) {}
		return model;
	    },
	    updateModel: function(e) {
		e.preventDefault();
		var options = this.model.validation;
		$("#lead_form").validate(options)
		if(!$("#lead_form").valid()){
		    return this;
		}
		this.gather_data(this.model);
		this.model.save({}, {
		    success: this.on_save_success,
		    error: this.on_save_error
		});
		return this;
	    },
	    on_save_success: function(model, response, options) {
		$('.modal-backdrop').remove();
		$('#lead_form_link').remove();
		$('#rootwizard').html(Jemplate.process("thank_you.tt"));			
		$('aside#side-lead-form').remove();
	    },
	    on_save_error: function(e, response) {
		var json = JSON.parse(response.responseText);
		var html = Jemplate.process("server_validation_message.tt", {errors: JSON.parse(json.error)});
		this.$('.alert-error').show();
		this.$('.alert-error').append(html);
	    },
	    close: function(){
		this.$el.empty();
		this.unbind();
		return this;
	    }
	});
	return lead_editor;
    });

