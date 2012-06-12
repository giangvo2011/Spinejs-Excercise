var Api = Spine.Model.sub();

Api.configure("Api", "api_name","description");

Api.extend(Spine.Model.Local);

Api.extend({
	listApi : function() {
		return this.select(function(item) {
			return item;
		});
	},

	destroyDone: function(){
		var items = this.done();
		for(var i=0; i < items.length; i++)
			items[i].destroy();
	},
	
	render: function(){
		$("#apiTemplate").tmpl(this.item).appendTo(".api_items");
		return this;
	}
});

var Apis = Spine.Controller.sub({ 
	events : {
		"click .api .delApi" : "delApi"
	},
	elements : {
		".api_items" : "items",
		".api"		: "api"
	},
	
	template: function(items){
		return($("#apiTemplate").tmpl(items));
	},
	item : null,
	init: function(){
		//this.api.bind("update_api", this.proxy(this.render));
		//this.api.bind("clearApiItem", this.proxy(this.remove));
		//this.edit_form.hide();
		this.proxy(this.render());
	},

	render: function(){
		this.el.append($("#apiTemplate").tmpl(this.item));
		return this;
	},
	
	remove: function(event){
		this.el.remove();
		this.release();
	},
	delApi: function() {
		this.el.remove();
		this.release();
		this.item.destroy();
	}
});
