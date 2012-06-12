var Category = Spine.Model.sub();

Category.configure("Category", "name","api_list");

Category.extend(Spine.Model.Local);

Category.extend({
	listCategory : function() {
		return this.select(function(item) {
			return item;
		});
	},

	destroyDone: function(){
		var items = this.done();
		for(var i=0; i < items.length; i++)
			items[i].destroy();
	}
});


var Categories = Spine.Controller.sub({ 
	el : $('#categories'),
	events : {
		"click .destroy"	: "clearItem",
		"click .edit"		: "renderEditor",
		"click .savebtn"	: "edit",
		"click .addApi"		: "renderApiAddForm",
		"click .addbtn"		: "addNewApi",
		"click .category_name"		: "renderListApi",
		"click .delApi"     : "delApi"
	},
	elements : {
		".editContent" : "editContent",
		".edit_form": "edit_form",
		".edit_form .input_edit": "input_edit",
		".api_add_form": "api_add_form",
		".api_add_form .input_name" : "input_api_name",
		".api_add_form .input_desc" : "input_api_desc",
		".item": "wrapper",
		".api_items" : "api_items"
	},

	init: function(){
		this.item.bind("update", this.proxy(this.render));
		this.item.bind("clearItem", this.proxy(this.remove));

		this.edit_form.hide();
	},

	render: function(){
		this.replace($("#categoryTemplate").tmpl(this.item));
		//Apis.init({el:this.api_items, apiArray: this.item.api_list});
		for(var i=0; i < this.item.api_list.length; i++)
			//this.api_items.append($("#apiTemplate").tmpl(this.item.api_list[i]));
			Apis.init({el : this.api_items, item: this.item.api_list[i]});
		return this;
	},
	
	renderEditor: function(){
		this.edit_form.slideDown();
		return this;
	},
	renderApiAddForm: function(){
		this.api_add_form.slideDown();
		return this;
	},
	//clear item from UI & Local Storage
	clearItem: function(event){
		this.item.trigger("clearItem");
		this.item.destroy();
	},
	
	remove: function(event){
		this.el.remove();
		this.release();
	},
	
	edit: function() {
		this.wrapper.removeClass("edit_form");
		this.item.updateAttributes({name: this.input_edit.val()});
		this.item.trigger("update");
	},
	
	addNewApi: function() {
		this.wrapper.removeClass("api_add_form");
		var list = this.item.api_list;
		list.push(new Api({api_name: this.input_api_name.val(), description: this.input_api_desc.val()}));
		this.item.updateAttributes({api_list: list});
		this.renderListApi();
	},
	
	renderListApi: function() {
		//for(var i=0; i < this.item.api_list.length; i++)
		//	this.api_items.append($("#apiTemplate").tmpl(this.item.api_list[i]));
		if (this.item.api_list.length > 0)
			this.api_items.slideToggle();
	}
});

var CategoryController = Spine.Controller.sub({
	events: {
		"submit form": "create",
		"click  .clear": "clear"
	},

	elements : {
		".items" : "items",
		"form input": "input"
	},

	init: function() {
		Category.bind("create",  this.proxy(this.addOne));
		Category.bind("refresh", this.proxy(this.addAll));
		Category.fetch();
	},

	addOne : function(category) {
		var view = new Categories({item: category});
		this.items.append(view.render().el);
	},

	addAll: function(){
		Category.each(this.proxy(this.addOne));
	},

	create: function(e) {
		e.preventDefault();
		Category.create({name: this.input.val(), api_list: new Array()});
		this.input.val("");
	},

	clear: function(){
		Category.destroyDone();
	}
});

jQuery(function($) {
	return new CategoryController({
		el: $("#categories")
	});
});





