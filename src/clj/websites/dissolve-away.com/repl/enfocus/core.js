goog.provide('enfocus.core');
goog.require('cljs.core');
goog.require('goog.dom.query');
goog.require('goog.async.Delay');
goog.require('goog.dom.classes');
goog.require('goog.dom.ViewportSizeMonitor');
goog.require('goog.events');
goog.require('enfocus.enlive.syntax');
goog.require('goog.dom');
goog.require('clojure.string');
goog.require('goog.Timer');
goog.require('goog.style');
goog.require('domina');
goog.require('goog.net.XhrIo');
goog.require('domina.xpath');
goog.require('domina.css');
enfocus.core.ISelector = {};
enfocus.core.select = (function() {
var select = null;
var select__1 = (function (this$){
if((function (){var and__3941__auto__ = this$;
if(and__3941__auto__)
{return this$.enfocus$core$ISelector$select$arity$1;
} else
{return and__3941__auto__;
}
})())
{return this$.enfocus$core$ISelector$select$arity$1(this$);
} else
{var x__3055__auto__ = (((this$ == null))?null:this$);
return (function (){var or__3943__auto__ = (enfocus.core.select[goog.typeOf(x__3055__auto__)]);
if(or__3943__auto__)
{return or__3943__auto__;
} else
{var or__3943__auto____$1 = (enfocus.core.select["_"]);
if(or__3943__auto____$1)
{return or__3943__auto____$1;
} else
{throw cljs.core.missing_protocol.call(null,"ISelector.select",this$);
}
}
})().call(null,this$);
}
});
var select__2 = (function (this$,root){
if((function (){var and__3941__auto__ = this$;
if(and__3941__auto__)
{return this$.enfocus$core$ISelector$select$arity$2;
} else
{return and__3941__auto__;
}
})())
{return this$.enfocus$core$ISelector$select$arity$2(this$,root);
} else
{var x__3055__auto__ = (((this$ == null))?null:this$);
return (function (){var or__3943__auto__ = (enfocus.core.select[goog.typeOf(x__3055__auto__)]);
if(or__3943__auto__)
{return or__3943__auto__;
} else
{var or__3943__auto____$1 = (enfocus.core.select["_"]);
if(or__3943__auto____$1)
{return or__3943__auto____$1;
} else
{throw cljs.core.missing_protocol.call(null,"ISelector.select",this$);
}
}
})().call(null,this$,root);
}
});
var select__3 = (function (this$,root,id_mask){
if((function (){var and__3941__auto__ = this$;
if(and__3941__auto__)
{return this$.enfocus$core$ISelector$select$arity$3;
} else
{return and__3941__auto__;
}
})())
{return this$.enfocus$core$ISelector$select$arity$3(this$,root,id_mask);
} else
{var x__3055__auto__ = (((this$ == null))?null:this$);
return (function (){var or__3943__auto__ = (enfocus.core.select[goog.typeOf(x__3055__auto__)]);
if(or__3943__auto__)
{return or__3943__auto__;
} else
{var or__3943__auto____$1 = (enfocus.core.select["_"]);
if(or__3943__auto____$1)
{return or__3943__auto____$1;
} else
{throw cljs.core.missing_protocol.call(null,"ISelector.select",this$);
}
}
})().call(null,this$,root,id_mask);
}
});
select = function(this$,root,id_mask){
switch(arguments.length){
case 1:
return select__1.call(this,this$);
case 2:
return select__2.call(this,this$,root);
case 3:
return select__3.call(this,this$,root,id_mask);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
select.cljs$core$IFn$_invoke$arity$1 = select__1;
select.cljs$core$IFn$_invoke$arity$2 = select__2;
select.cljs$core$IFn$_invoke$arity$3 = select__3;
return select;
})()
;
enfocus.core.ITransform = {};
enfocus.core.apply_transform = (function() {
var apply_transform = null;
var apply_transform__2 = (function (this$,nodes){
if((function (){var and__3941__auto__ = this$;
if(and__3941__auto__)
{return this$.enfocus$core$ITransform$apply_transform$arity$2;
} else
{return and__3941__auto__;
}
})())
{return this$.enfocus$core$ITransform$apply_transform$arity$2(this$,nodes);
} else
{var x__3055__auto__ = (((this$ == null))?null:this$);
return (function (){var or__3943__auto__ = (enfocus.core.apply_transform[goog.typeOf(x__3055__auto__)]);
if(or__3943__auto__)
{return or__3943__auto__;
} else
{var or__3943__auto____$1 = (enfocus.core.apply_transform["_"]);
if(or__3943__auto____$1)
{return or__3943__auto____$1;
} else
{throw cljs.core.missing_protocol.call(null,"ITransform.apply-transform",this$);
}
}
})().call(null,this$,nodes);
}
});
var apply_transform__3 = (function (this$,nodes,callback){
if((function (){var and__3941__auto__ = this$;
if(and__3941__auto__)
{return this$.enfocus$core$ITransform$apply_transform$arity$3;
} else
{return and__3941__auto__;
}
})())
{return this$.enfocus$core$ITransform$apply_transform$arity$3(this$,nodes,callback);
} else
{var x__3055__auto__ = (((this$ == null))?null:this$);
return (function (){var or__3943__auto__ = (enfocus.core.apply_transform[goog.typeOf(x__3055__auto__)]);
if(or__3943__auto__)
{return or__3943__auto__;
} else
{var or__3943__auto____$1 = (enfocus.core.apply_transform["_"]);
if(or__3943__auto____$1)
{return or__3943__auto____$1;
} else
{throw cljs.core.missing_protocol.call(null,"ITransform.apply-transform",this$);
}
}
})().call(null,this$,nodes,callback);
}
});
apply_transform = function(this$,nodes,callback){
switch(arguments.length){
case 2:
return apply_transform__2.call(this,this$,nodes);
case 3:
return apply_transform__3.call(this,this$,nodes,callback);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
apply_transform.cljs$core$IFn$_invoke$arity$2 = apply_transform__2;
apply_transform.cljs$core$IFn$_invoke$arity$3 = apply_transform__3;
return apply_transform;
})()
;
enfocus.core.debug = true;
enfocus.core.log_debug = (function log_debug(mesg){
if(cljs.core.truth_((function (){var and__3941__auto__ = enfocus.core.debug;
if(cljs.core.truth_(and__3941__auto__))
{return !(cljs.core._EQ_.call(null,window.console,undefined));
} else
{return and__3941__auto__;
}
})()))
{return console.log(mesg);
} else
{return null;
}
});
enfocus.core.setTimeout = (function setTimeout(func,ttime){
return goog.Timer.callOnce(func,ttime);
});
enfocus.core.node_QMARK_ = (function node_QMARK_(tst){
return goog.dom.isNodeLike(tst);
});
enfocus.core.nodelist_QMARK_ = (function nodelist_QMARK_(tst){
return (tst instanceof NodeList);
});
/**
* coverts a nodelist, node into a collection
*/
enfocus.core.nodes__GT_coll = (function nodes__GT_coll(nl){
if(cljs.core._EQ_.call(null,nl,window))
{return cljs.core.PersistentVector.fromArray([nl], true);
} else
{return domina.nodes.call(null,nl);
}
});
enfocus.core.flatten_nodes_coll = (function flatten_nodes_coll(values){
return cljs.core.mapcat.call(null,(function (p1__4191_SHARP_){
if(cljs.core.string_QMARK_.call(null,p1__4191_SHARP_))
{return cljs.core.PersistentVector.fromArray([goog.dom.createTextNode(p1__4191_SHARP_)], true);
} else
{if("\uFDD0:else")
{return enfocus.core.nodes__GT_coll.call(null,p1__4191_SHARP_);
} else
{return null;
}
}
}),values);
});
/**
* Sets property name to a value on a element and	Returns the original object
*/
enfocus.core.style_set = (function style_set(obj,values){
var seq__4198_4204 = cljs.core.seq.call(null,cljs.core.apply.call(null,cljs.core.hash_map,values));
var chunk__4199_4205 = null;
var count__4200_4206 = 0;
var i__4201_4207 = 0;
while(true){
if((i__4201_4207 < count__4200_4206))
{var vec__4202_4208 = cljs.core._nth.call(null,chunk__4199_4205,i__4201_4207);
var attr_4209 = cljs.core.nth.call(null,vec__4202_4208,0,null);
var value_4210 = cljs.core.nth.call(null,vec__4202_4208,1,null);
goog.style.setStyle(obj,cljs.core.name.call(null,attr_4209),value_4210);
{
var G__4211 = seq__4198_4204;
var G__4212 = chunk__4199_4205;
var G__4213 = count__4200_4206;
var G__4214 = (i__4201_4207 + 1);
seq__4198_4204 = G__4211;
chunk__4199_4205 = G__4212;
count__4200_4206 = G__4213;
i__4201_4207 = G__4214;
continue;
}
} else
{var temp__4092__auto___4215 = cljs.core.seq.call(null,seq__4198_4204);
if(temp__4092__auto___4215)
{var seq__4198_4216__$1 = temp__4092__auto___4215;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__4198_4216__$1))
{var c__3186__auto___4217 = cljs.core.chunk_first.call(null,seq__4198_4216__$1);
{
var G__4218 = cljs.core.chunk_rest.call(null,seq__4198_4216__$1);
var G__4219 = c__3186__auto___4217;
var G__4220 = cljs.core.count.call(null,c__3186__auto___4217);
var G__4221 = 0;
seq__4198_4204 = G__4218;
chunk__4199_4205 = G__4219;
count__4200_4206 = G__4220;
i__4201_4207 = G__4221;
continue;
}
} else
{var vec__4203_4222 = cljs.core.first.call(null,seq__4198_4216__$1);
var attr_4223 = cljs.core.nth.call(null,vec__4203_4222,0,null);
var value_4224 = cljs.core.nth.call(null,vec__4203_4222,1,null);
goog.style.setStyle(obj,cljs.core.name.call(null,attr_4223),value_4224);
{
var G__4225 = cljs.core.next.call(null,seq__4198_4216__$1);
var G__4226 = null;
var G__4227 = 0;
var G__4228 = 0;
seq__4198_4204 = G__4225;
chunk__4199_4205 = G__4226;
count__4200_4206 = G__4227;
i__4201_4207 = G__4228;
continue;
}
}
} else
{}
}
break;
}
return obj;
});
/**
* removes the property value from an elements style obj.
*/
enfocus.core.style_remove = (function style_remove(obj,values){
var seq__4233 = cljs.core.seq.call(null,values);
var chunk__4234 = null;
var count__4235 = 0;
var i__4236 = 0;
while(true){
if((i__4236 < count__4235))
{var attr = cljs.core._nth.call(null,chunk__4234,i__4236);
if(cljs.core.truth_(goog.userAgent.IE))
{goog.style.setStyle(obj,cljs.core.name.call(null,attr),"");
} else
{obj.style.removeProperty(cljs.core.name.call(null,attr));
}
{
var G__4237 = seq__4233;
var G__4238 = chunk__4234;
var G__4239 = count__4235;
var G__4240 = (i__4236 + 1);
seq__4233 = G__4237;
chunk__4234 = G__4238;
count__4235 = G__4239;
i__4236 = G__4240;
continue;
}
} else
{var temp__4092__auto__ = cljs.core.seq.call(null,seq__4233);
if(temp__4092__auto__)
{var seq__4233__$1 = temp__4092__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__4233__$1))
{var c__3186__auto__ = cljs.core.chunk_first.call(null,seq__4233__$1);
{
var G__4241 = cljs.core.chunk_rest.call(null,seq__4233__$1);
var G__4242 = c__3186__auto__;
var G__4243 = cljs.core.count.call(null,c__3186__auto__);
var G__4244 = 0;
seq__4233 = G__4241;
chunk__4234 = G__4242;
count__4235 = G__4243;
i__4236 = G__4244;
continue;
}
} else
{var attr = cljs.core.first.call(null,seq__4233__$1);
if(cljs.core.truth_(goog.userAgent.IE))
{goog.style.setStyle(obj,cljs.core.name.call(null,attr),"");
} else
{obj.style.removeProperty(cljs.core.name.call(null,attr));
}
{
var G__4245 = cljs.core.next.call(null,seq__4233__$1);
var G__4246 = null;
var G__4247 = 0;
var G__4248 = 0;
seq__4233 = G__4245;
chunk__4234 = G__4246;
count__4235 = G__4247;
i__4236 = G__4248;
continue;
}
}
} else
{return null;
}
}
break;
}
});
enfocus.core.get_eff_prop_name = (function get_eff_prop_name(etype){
return [cljs.core.str("__ef_effect_"),cljs.core.str(etype)].join('');
});
enfocus.core.get_mills = (function get_mills(){
return (new Date()).getMilliseconds();
});
enfocus.core.pix_round = (function pix_round(step){
if((step < 0))
{return Math.floor.call(null,step);
} else
{return Math.ceil.call(null,step);
}
});
enfocus.core.add_map_attrs = (function() {
var add_map_attrs = null;
var add_map_attrs__2 = (function (elem,ats){
if(cljs.core.truth_(elem))
{if(cljs.core.map_QMARK_.call(null,ats))
{var seq__4255_4261 = cljs.core.seq.call(null,ats);
var chunk__4256_4262 = null;
var count__4257_4263 = 0;
var i__4258_4264 = 0;
while(true){
if((i__4258_4264 < count__4257_4263))
{var vec__4259_4265 = cljs.core._nth.call(null,chunk__4256_4262,i__4258_4264);
var k_4266 = cljs.core.nth.call(null,vec__4259_4265,0,null);
var v_4267 = cljs.core.nth.call(null,vec__4259_4265,1,null);
add_map_attrs.call(null,elem,k_4266,v_4267);
{
var G__4268 = seq__4255_4261;
var G__4269 = chunk__4256_4262;
var G__4270 = count__4257_4263;
var G__4271 = (i__4258_4264 + 1);
seq__4255_4261 = G__4268;
chunk__4256_4262 = G__4269;
count__4257_4263 = G__4270;
i__4258_4264 = G__4271;
continue;
}
} else
{var temp__4092__auto___4272 = cljs.core.seq.call(null,seq__4255_4261);
if(temp__4092__auto___4272)
{var seq__4255_4273__$1 = temp__4092__auto___4272;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__4255_4273__$1))
{var c__3186__auto___4274 = cljs.core.chunk_first.call(null,seq__4255_4273__$1);
{
var G__4275 = cljs.core.chunk_rest.call(null,seq__4255_4273__$1);
var G__4276 = c__3186__auto___4274;
var G__4277 = cljs.core.count.call(null,c__3186__auto___4274);
var G__4278 = 0;
seq__4255_4261 = G__4275;
chunk__4256_4262 = G__4276;
count__4257_4263 = G__4277;
i__4258_4264 = G__4278;
continue;
}
} else
{var vec__4260_4279 = cljs.core.first.call(null,seq__4255_4273__$1);
var k_4280 = cljs.core.nth.call(null,vec__4260_4279,0,null);
var v_4281 = cljs.core.nth.call(null,vec__4260_4279,1,null);
add_map_attrs.call(null,elem,k_4280,v_4281);
{
var G__4282 = cljs.core.next.call(null,seq__4255_4273__$1);
var G__4283 = null;
var G__4284 = 0;
var G__4285 = 0;
seq__4255_4261 = G__4282;
chunk__4256_4262 = G__4283;
count__4257_4263 = G__4284;
i__4258_4264 = G__4285;
continue;
}
}
} else
{}
}
break;
}
return elem;
} else
{return null;
}
} else
{return null;
}
});
var add_map_attrs__3 = (function (elem,k,v){
elem.setAttribute(cljs.core.name.call(null,k),v);
return elem;
});
add_map_attrs = function(elem,k,v){
switch(arguments.length){
case 2:
return add_map_attrs__2.call(this,elem,k);
case 3:
return add_map_attrs__3.call(this,elem,k,v);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
add_map_attrs.cljs$core$IFn$_invoke$arity$2 = add_map_attrs__2;
add_map_attrs.cljs$core$IFn$_invoke$arity$3 = add_map_attrs__3;
return add_map_attrs;
})()
;
/**
* this is incremented everytime a remote template is loaded and decremented when
* it is added to the dom cache
*/
enfocus.core.tpl_load_cnt = cljs.core.atom.call(null,0);
/**
* cache for the remote templates
*/
enfocus.core.tpl_cache = cljs.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
enfocus.core.hide_style = cljs.core.PersistentArrayMap.fromArray(["style","display: none; width: 0px; height: 0px"], true).strobj;
/**
* Add a hidden div to hold the dom as we are transforming it this has to be done
* because css selectors do not work unless we have it in the main dom
*/
enfocus.core.create_hidden_dom = (function create_hidden_dom(child){
var div = goog.dom.createDom("div",enfocus.core.hide_style);
if(cljs.core._EQ_.call(null,child.nodeType,11))
{goog.dom.appendChild(div,child);
} else
{enfocus.core.log_debug.call(null,cljs.core.count.call(null,domina.nodes.call(null,child)));
var seq__4290_4294 = cljs.core.seq.call(null,domina.nodes.call(null,child));
var chunk__4291_4295 = null;
var count__4292_4296 = 0;
var i__4293_4297 = 0;
while(true){
if((i__4293_4297 < count__4292_4296))
{var node_4298 = cljs.core._nth.call(null,chunk__4291_4295,i__4293_4297);
goog.dom.appendChild(div,node_4298);
{
var G__4299 = seq__4290_4294;
var G__4300 = chunk__4291_4295;
var G__4301 = count__4292_4296;
var G__4302 = (i__4293_4297 + 1);
seq__4290_4294 = G__4299;
chunk__4291_4295 = G__4300;
count__4292_4296 = G__4301;
i__4293_4297 = G__4302;
continue;
}
} else
{var temp__4092__auto___4303 = cljs.core.seq.call(null,seq__4290_4294);
if(temp__4092__auto___4303)
{var seq__4290_4304__$1 = temp__4092__auto___4303;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__4290_4304__$1))
{var c__3186__auto___4305 = cljs.core.chunk_first.call(null,seq__4290_4304__$1);
{
var G__4306 = cljs.core.chunk_rest.call(null,seq__4290_4304__$1);
var G__4307 = c__3186__auto___4305;
var G__4308 = cljs.core.count.call(null,c__3186__auto___4305);
var G__4309 = 0;
seq__4290_4294 = G__4306;
chunk__4291_4295 = G__4307;
count__4292_4296 = G__4308;
i__4293_4297 = G__4309;
continue;
}
} else
{var node_4310 = cljs.core.first.call(null,seq__4290_4304__$1);
goog.dom.appendChild(div,node_4310);
{
var G__4311 = cljs.core.next.call(null,seq__4290_4304__$1);
var G__4312 = null;
var G__4313 = 0;
var G__4314 = 0;
seq__4290_4294 = G__4311;
chunk__4291_4295 = G__4312;
count__4292_4296 = G__4313;
i__4293_4297 = G__4314;
continue;
}
}
} else
{}
}
break;
}
}
goog.dom.appendChild(goog.dom.getDocument().documentElement,div);
return div;
});
/**
* removes the hidden div and returns the children
*/
enfocus.core.remove_node_return_child = (function remove_node_return_child(div){
var child = div.childNodes;
var frag = document.createDocumentFragment();
goog.dom.append(frag,child);
goog.dom.removeNode(div);
return frag;
});
/**
* replaces all the ids in a string html fragement/template with a generated
* symbol appended on to a existing id this is done to make sure we don't have
* id colisions during the transformation process
*/
enfocus.core.replace_ids = (function() {
var replace_ids = null;
var replace_ids__1 = (function (text){
return replace_ids.call(null,[cljs.core.str(cljs.core.name.call(null,cljs.core.gensym.call(null,"id"))),cljs.core.str("_")].join(''),text);
});
var replace_ids__2 = (function (id_mask,text){
var re = (new RegExp("(<.*?\\sid=['\"])(.*?)(['\"].*?>)","g"));
return cljs.core.PersistentVector.fromArray([id_mask,text.replace(re,(function (a,b,c,d){
return [cljs.core.str(b),cljs.core.str(id_mask),cljs.core.str(c),cljs.core.str(d)].join('');
}))], true);
});
replace_ids = function(id_mask,text){
switch(arguments.length){
case 1:
return replace_ids__1.call(this,id_mask);
case 2:
return replace_ids__2.call(this,id_mask,text);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
replace_ids.cljs$core$IFn$_invoke$arity$1 = replace_ids__1;
replace_ids.cljs$core$IFn$_invoke$arity$2 = replace_ids__2;
return replace_ids;
})()
;
/**
* before adding the dom back into the live dom we reset the masked ids to orig vals
*/
enfocus.core.reset_ids = (function reset_ids(sym,nod){
var id_nodes = enfocus.core.css_select.call(null,nod,"*[id]");
var nod_col = enfocus.core.nodes__GT_coll.call(null,id_nodes);
return cljs.core.doall.call(null,cljs.core.map.call(null,(function (p1__4315_SHARP_){
var id = p1__4315_SHARP_.getAttribute("id");
var rid = id.replace(sym,"");
return p1__4315_SHARP_.setAttribute("id",rid);
}),nod_col));
});
/**
* loads a remote file into the cache, and masks the ids to avoid collisions
*/
enfocus.core.load_remote_dom = (function load_remote_dom(uri,dom_key,id_mask){
if((cljs.core.deref.call(null,enfocus.core.tpl_cache).call(null,dom_key) == null))
{cljs.core.swap_BANG_.call(null,enfocus.core.tpl_load_cnt,cljs.core.inc);
var req = (new goog.net.XhrIo());
var callback = ((function (req){
return (function (req__$1){
var text = req__$1.getResponseText();
var vec__4317 = enfocus.core.replace_ids.call(null,id_mask,text);
var sym = cljs.core.nth.call(null,vec__4317,0,null);
var txt = cljs.core.nth.call(null,vec__4317,1,null);
return cljs.core.swap_BANG_.call(null,enfocus.core.tpl_cache,cljs.core.assoc,dom_key,cljs.core.PersistentVector.fromArray([sym,txt], true));
});})(req))
;
goog.events.listen(req,goog.net.EventType.COMPLETE,(function (){
callback.call(null,req);
return cljs.core.swap_BANG_.call(null,enfocus.core.tpl_load_cnt,cljs.core.dec);
}));
return req.send(uri,"GET");
} else
{return null;
}
});
enfocus.core.html_to_dom = (function html_to_dom(html){
var dfa = enfocus.core.nodes__GT_coll.call(null,domina.html_to_dom.call(null,html));
var frag = document.createDocumentFragment();
enfocus.core.log_debug.call(null,cljs.core.count.call(null,dfa));
var seq__4322_4326 = cljs.core.seq.call(null,dfa);
var chunk__4323_4327 = null;
var count__4324_4328 = 0;
var i__4325_4329 = 0;
while(true){
if((i__4325_4329 < count__4324_4328))
{var df_4330 = cljs.core._nth.call(null,chunk__4323_4327,i__4325_4329);
goog.dom.append(frag,df_4330);
{
var G__4331 = seq__4322_4326;
var G__4332 = chunk__4323_4327;
var G__4333 = count__4324_4328;
var G__4334 = (i__4325_4329 + 1);
seq__4322_4326 = G__4331;
chunk__4323_4327 = G__4332;
count__4324_4328 = G__4333;
i__4325_4329 = G__4334;
continue;
}
} else
{var temp__4092__auto___4335 = cljs.core.seq.call(null,seq__4322_4326);
if(temp__4092__auto___4335)
{var seq__4322_4336__$1 = temp__4092__auto___4335;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__4322_4336__$1))
{var c__3186__auto___4337 = cljs.core.chunk_first.call(null,seq__4322_4336__$1);
{
var G__4338 = cljs.core.chunk_rest.call(null,seq__4322_4336__$1);
var G__4339 = c__3186__auto___4337;
var G__4340 = cljs.core.count.call(null,c__3186__auto___4337);
var G__4341 = 0;
seq__4322_4326 = G__4338;
chunk__4323_4327 = G__4339;
count__4324_4328 = G__4340;
i__4325_4329 = G__4341;
continue;
}
} else
{var df_4342 = cljs.core.first.call(null,seq__4322_4336__$1);
goog.dom.append(frag,df_4342);
{
var G__4343 = cljs.core.next.call(null,seq__4322_4336__$1);
var G__4344 = null;
var G__4345 = 0;
var G__4346 = 0;
seq__4322_4326 = G__4343;
chunk__4323_4327 = G__4344;
count__4324_4328 = G__4345;
i__4325_4329 = G__4346;
continue;
}
}
} else
{}
}
break;
}
return frag;
});
/**
* returns and dom from the cache and symbol used to scope the ids
*/
enfocus.core.get_cached_dom = (function get_cached_dom(uri){
var nod = cljs.core.deref.call(null,enfocus.core.tpl_cache).call(null,uri);
if(cljs.core.truth_(nod))
{return cljs.core.PersistentVector.fromArray([cljs.core.first.call(null,nod),enfocus.core.html_to_dom.call(null,cljs.core.second.call(null,nod))], true);
} else
{return null;
}
});
/**
* returns the cached snippet or creates one and adds it to the cache if needed
*/
enfocus.core.get_cached_snippet = (function get_cached_snippet(uri,sel){
var sel_str = enfocus.core.create_sel_str.call(null,sel);
var cache = cljs.core.deref.call(null,enfocus.core.tpl_cache).call(null,[cljs.core.str(uri),cljs.core.str(sel_str)].join(''));
if(cljs.core.truth_(cache))
{return cljs.core.PersistentVector.fromArray([cljs.core.first.call(null,cache),enfocus.core.html_to_dom.call(null,cljs.core.second.call(null,cache))], true);
} else
{var vec__4349 = enfocus.core.get_cached_dom.call(null,uri);
var sym = cljs.core.nth.call(null,vec__4349,0,null);
var tdom = cljs.core.nth.call(null,vec__4349,1,null);
var dom = enfocus.core.create_hidden_dom.call(null,tdom);
var tsnip = domina.nodes.call(null,enfocus.core.css_select.call(null,sym,dom,sel));
var html_snip = cljs.core.apply.call(null,cljs.core.str,cljs.core.map.call(null,((function (vec__4349,sym,tdom,dom,tsnip){
return (function (p1__4347_SHARP_){
return p1__4347_SHARP_.outerHTML;
});})(vec__4349,sym,tdom,dom,tsnip))
,tsnip));
enfocus.core.remove_node_return_child.call(null,dom);
cljs.core.swap_BANG_.call(null,enfocus.core.tpl_cache,cljs.core.assoc,[cljs.core.str(uri),cljs.core.str(sel_str)].join(''),cljs.core.PersistentVector.fromArray([sym,html_snip], true));
return cljs.core.PersistentVector.fromArray([sym,enfocus.core.html_to_dom.call(null,html_snip)], true);
}
});
/**
* wrapper function for extractors that maps the extraction to all nodes returned by the selector
*/
enfocus.core.extr_multi_node = (function extr_multi_node(func){
var trans = (function trans(pnodes){
var pnod_col = enfocus.core.nodes__GT_coll.call(null,pnodes);
var result = cljs.core.map.call(null,func,pnod_col);
if((cljs.core.count.call(null,result) <= 1))
{return cljs.core.first.call(null,result);
} else
{return result;
}
});
if((void 0 === enfocus.core.t4353))
{goog.provide('enfocus.core.t4353');

/**
* @constructor
*/
enfocus.core.t4353 = (function (trans,func,extr_multi_node,meta4354){
this.trans = trans;
this.func = func;
this.extr_multi_node = extr_multi_node;
this.meta4354 = meta4354;
this.cljs$lang$protocol_mask$partition1$ = 0;
this.cljs$lang$protocol_mask$partition0$ = 393216;
})
enfocus.core.t4353.cljs$lang$type = true;
enfocus.core.t4353.cljs$lang$ctorStr = "enfocus.core/t4353";
enfocus.core.t4353.cljs$lang$ctorPrWriter = (function (this__2996__auto__,writer__2997__auto__,opt__2998__auto__){
return cljs.core._write.call(null,writer__2997__auto__,"enfocus.core/t4353");
});
enfocus.core.t4353.prototype.enfocus$core$ITransform$ = true;
enfocus.core.t4353.prototype.enfocus$core$ITransform$apply_transform$arity$2 = (function (_,nodes){
var self__ = this;
return self__.trans.call(null,nodes,null);
});
enfocus.core.t4353.prototype.enfocus$core$ITransform$apply_transform$arity$3 = (function (_,nodes,chain){
var self__ = this;
return self__.trans.call(null,nodes,chain);
});
enfocus.core.t4353.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_4355){
var self__ = this;
return self__.meta4354;
});
enfocus.core.t4353.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_4355,meta4354__$1){
var self__ = this;
return (new enfocus.core.t4353(self__.trans,self__.func,self__.extr_multi_node,meta4354__$1));
});
enfocus.core.__GT_t4353 = (function __GT_t4353(trans__$1,func__$1,extr_multi_node__$1,meta4354){
return (new enfocus.core.t4353(trans__$1,func__$1,extr_multi_node__$1,meta4354));
});
} else
{}
return (new enfocus.core.t4353(trans,func,extr_multi_node,null));
});
/**
* Allows standard domina functions to be chainable
*/
enfocus.core.multi_node_chain = (function() {
var multi_node_chain = null;
var multi_node_chain__1 = (function (func){
var trans = (function (nodes,chain){
var val = func.call(null,nodes);
if(cljs.core.truth_(chain))
{return enfocus.core.apply_transform.call(null,chain,nodes);
} else
{return val;
}
});
if((void 0 === enfocus.core.t4363))
{goog.provide('enfocus.core.t4363');

/**
* @constructor
*/
enfocus.core.t4363 = (function (trans,func,multi_node_chain,meta4364){
this.trans = trans;
this.func = func;
this.multi_node_chain = multi_node_chain;
this.meta4364 = meta4364;
this.cljs$lang$protocol_mask$partition1$ = 0;
this.cljs$lang$protocol_mask$partition0$ = 393216;
})
enfocus.core.t4363.cljs$lang$type = true;
enfocus.core.t4363.cljs$lang$ctorStr = "enfocus.core/t4363";
enfocus.core.t4363.cljs$lang$ctorPrWriter = (function (this__2996__auto__,writer__2997__auto__,opt__2998__auto__){
return cljs.core._write.call(null,writer__2997__auto__,"enfocus.core/t4363");
});
enfocus.core.t4363.prototype.enfocus$core$ITransform$ = true;
enfocus.core.t4363.prototype.enfocus$core$ITransform$apply_transform$arity$2 = (function (_,nodes){
var self__ = this;
return self__.trans.call(null,nodes,null);
});
enfocus.core.t4363.prototype.enfocus$core$ITransform$apply_transform$arity$3 = (function (_,nodes,chain){
var self__ = this;
return self__.trans.call(null,nodes,chain);
});
enfocus.core.t4363.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_4365){
var self__ = this;
return self__.meta4364;
});
enfocus.core.t4363.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_4365,meta4364__$1){
var self__ = this;
return (new enfocus.core.t4363(self__.trans,self__.func,self__.multi_node_chain,meta4364__$1));
});
enfocus.core.__GT_t4363 = (function __GT_t4363(trans__$1,func__$1,multi_node_chain__$1,meta4364){
return (new enfocus.core.t4363(trans__$1,func__$1,multi_node_chain__$1,meta4364));
});
} else
{}
return (new enfocus.core.t4363(trans,func,multi_node_chain,null));
});
var multi_node_chain__2 = (function (values,func){
var trans = (function (nodes,chain){
var vnodes = cljs.core.mapcat.call(null,(function (p1__4356_SHARP_){
return domina.nodes.call(null,p1__4356_SHARP_);
}),values);
var val = func.call(null,nodes,vnodes);
if(cljs.core.truth_(chain))
{return enfocus.core.apply_transform.call(null,chain,nodes);
} else
{return val;
}
});
if((void 0 === enfocus.core.t4366))
{goog.provide('enfocus.core.t4366');

/**
* @constructor
*/
enfocus.core.t4366 = (function (trans,func,values,multi_node_chain,meta4367){
this.trans = trans;
this.func = func;
this.values = values;
this.multi_node_chain = multi_node_chain;
this.meta4367 = meta4367;
this.cljs$lang$protocol_mask$partition1$ = 0;
this.cljs$lang$protocol_mask$partition0$ = 393216;
})
enfocus.core.t4366.cljs$lang$type = true;
enfocus.core.t4366.cljs$lang$ctorStr = "enfocus.core/t4366";
enfocus.core.t4366.cljs$lang$ctorPrWriter = (function (this__2996__auto__,writer__2997__auto__,opt__2998__auto__){
return cljs.core._write.call(null,writer__2997__auto__,"enfocus.core/t4366");
});
enfocus.core.t4366.prototype.enfocus$core$ITransform$ = true;
enfocus.core.t4366.prototype.enfocus$core$ITransform$apply_transform$arity$2 = (function (_,nodes){
var self__ = this;
return self__.trans.call(null,nodes,null);
});
enfocus.core.t4366.prototype.enfocus$core$ITransform$apply_transform$arity$3 = (function (_,nodes,chain){
var self__ = this;
return self__.trans.call(null,nodes,chain);
});
enfocus.core.t4366.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_4368){
var self__ = this;
return self__.meta4367;
});
enfocus.core.t4366.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_4368,meta4367__$1){
var self__ = this;
return (new enfocus.core.t4366(self__.trans,self__.func,self__.values,self__.multi_node_chain,meta4367__$1));
});
enfocus.core.__GT_t4366 = (function __GT_t4366(trans__$1,func__$1,values__$1,multi_node_chain__$1,meta4367){
return (new enfocus.core.t4366(trans__$1,func__$1,values__$1,multi_node_chain__$1,meta4367));
});
} else
{}
return (new enfocus.core.t4366(trans,func,values,multi_node_chain,null));
});
multi_node_chain = function(values,func){
switch(arguments.length){
case 1:
return multi_node_chain__1.call(this,values);
case 2:
return multi_node_chain__2.call(this,values,func);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
multi_node_chain.cljs$core$IFn$_invoke$arity$1 = multi_node_chain__1;
multi_node_chain.cljs$core$IFn$_invoke$arity$2 = multi_node_chain__2;
return multi_node_chain;
})()
;
/**
* Replaces the content of the element. Values can be nodes or collection of nodes.
* @param {...*} var_args
*/
enfocus.core.content = (function() { 
var content__delegate = function (values){
return enfocus.core.multi_node_chain.call(null,values,(function (p1__4369_SHARP_,p2__4370_SHARP_){
domina.destroy_children_BANG_.call(null,p1__4369_SHARP_);
return domina.append_BANG_.call(null,p1__4369_SHARP_,p2__4370_SHARP_);
}));
};
var content = function (var_args){
var values = null;
if (arguments.length > 0) {
  values = cljs.core.array_seq(Array.prototype.slice.call(arguments, 0),0);
} 
return content__delegate.call(this, values);
};
content.cljs$lang$maxFixedArity = 0;
content.cljs$lang$applyTo = (function (arglist__4371){
var values = cljs.core.seq(arglist__4371);
return content__delegate(values);
});
content.cljs$core$IFn$_invoke$arity$variadic = content__delegate;
return content;
})()
;
/**
* Replaces the content of the element with the dom structure represented by the html string passed
*/
enfocus.core.html_content = (function html_content(txt){
return enfocus.core.multi_node_chain.call(null,(function (p1__4372_SHARP_){
return domina.set_html_BANG_.call(null,p1__4372_SHARP_,txt);
}));
});
/**
* Assocs attributes and values on the selected element.
* @param {...*} var_args
*/
enfocus.core.set_attr = (function() { 
var set_attr__delegate = function (values){
var pairs = cljs.core.partition.call(null,2,values);
return enfocus.core.multi_node_chain.call(null,(function (p1__4373_SHARP_){
var seq__4380 = cljs.core.seq.call(null,pairs);
var chunk__4381 = null;
var count__4382 = 0;
var i__4383 = 0;
while(true){
if((i__4383 < count__4382))
{var vec__4384 = cljs.core._nth.call(null,chunk__4381,i__4383);
var name = cljs.core.nth.call(null,vec__4384,0,null);
var value = cljs.core.nth.call(null,vec__4384,1,null);
domina.set_attr_BANG_.call(null,p1__4373_SHARP_,name,value);
{
var G__4386 = seq__4380;
var G__4387 = chunk__4381;
var G__4388 = count__4382;
var G__4389 = (i__4383 + 1);
seq__4380 = G__4386;
chunk__4381 = G__4387;
count__4382 = G__4388;
i__4383 = G__4389;
continue;
}
} else
{var temp__4092__auto__ = cljs.core.seq.call(null,seq__4380);
if(temp__4092__auto__)
{var seq__4380__$1 = temp__4092__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__4380__$1))
{var c__3186__auto__ = cljs.core.chunk_first.call(null,seq__4380__$1);
{
var G__4390 = cljs.core.chunk_rest.call(null,seq__4380__$1);
var G__4391 = c__3186__auto__;
var G__4392 = cljs.core.count.call(null,c__3186__auto__);
var G__4393 = 0;
seq__4380 = G__4390;
chunk__4381 = G__4391;
count__4382 = G__4392;
i__4383 = G__4393;
continue;
}
} else
{var vec__4385 = cljs.core.first.call(null,seq__4380__$1);
var name = cljs.core.nth.call(null,vec__4385,0,null);
var value = cljs.core.nth.call(null,vec__4385,1,null);
domina.set_attr_BANG_.call(null,p1__4373_SHARP_,name,value);
{
var G__4394 = cljs.core.next.call(null,seq__4380__$1);
var G__4395 = null;
var G__4396 = 0;
var G__4397 = 0;
seq__4380 = G__4394;
chunk__4381 = G__4395;
count__4382 = G__4396;
i__4383 = G__4397;
continue;
}
}
} else
{return null;
}
}
break;
}
}));
};
var set_attr = function (var_args){
var values = null;
if (arguments.length > 0) {
  values = cljs.core.array_seq(Array.prototype.slice.call(arguments, 0),0);
} 
return set_attr__delegate.call(this, values);
};
set_attr.cljs$lang$maxFixedArity = 0;
set_attr.cljs$lang$applyTo = (function (arglist__4398){
var values = cljs.core.seq(arglist__4398);
return set_attr__delegate(values);
});
set_attr.cljs$core$IFn$_invoke$arity$variadic = set_attr__delegate;
return set_attr;
})()
;
/**
* Dissocs attributes on the selected element.
* @param {...*} var_args
*/
enfocus.core.remove_attr = (function() { 
var remove_attr__delegate = function (values){
return enfocus.core.multi_node_chain.call(null,(function (p1__4399_SHARP_){
var seq__4404 = cljs.core.seq.call(null,values);
var chunk__4405 = null;
var count__4406 = 0;
var i__4407 = 0;
while(true){
if((i__4407 < count__4406))
{var name = cljs.core._nth.call(null,chunk__4405,i__4407);
domina.remove_attr_BANG_.call(null,p1__4399_SHARP_,name);
{
var G__4408 = seq__4404;
var G__4409 = chunk__4405;
var G__4410 = count__4406;
var G__4411 = (i__4407 + 1);
seq__4404 = G__4408;
chunk__4405 = G__4409;
count__4406 = G__4410;
i__4407 = G__4411;
continue;
}
} else
{var temp__4092__auto__ = cljs.core.seq.call(null,seq__4404);
if(temp__4092__auto__)
{var seq__4404__$1 = temp__4092__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__4404__$1))
{var c__3186__auto__ = cljs.core.chunk_first.call(null,seq__4404__$1);
{
var G__4412 = cljs.core.chunk_rest.call(null,seq__4404__$1);
var G__4413 = c__3186__auto__;
var G__4414 = cljs.core.count.call(null,c__3186__auto__);
var G__4415 = 0;
seq__4404 = G__4412;
chunk__4405 = G__4413;
count__4406 = G__4414;
i__4407 = G__4415;
continue;
}
} else
{var name = cljs.core.first.call(null,seq__4404__$1);
domina.remove_attr_BANG_.call(null,p1__4399_SHARP_,name);
{
var G__4416 = cljs.core.next.call(null,seq__4404__$1);
var G__4417 = null;
var G__4418 = 0;
var G__4419 = 0;
seq__4404 = G__4416;
chunk__4405 = G__4417;
count__4406 = G__4418;
i__4407 = G__4419;
continue;
}
}
} else
{return null;
}
}
break;
}
}));
};
var remove_attr = function (var_args){
var values = null;
if (arguments.length > 0) {
  values = cljs.core.array_seq(Array.prototype.slice.call(arguments, 0),0);
} 
return remove_attr__delegate.call(this, values);
};
remove_attr.cljs$lang$maxFixedArity = 0;
remove_attr.cljs$lang$applyTo = (function (arglist__4420){
var values = cljs.core.seq(arglist__4420);
return remove_attr__delegate(values);
});
remove_attr.cljs$core$IFn$_invoke$arity$variadic = remove_attr__delegate;
return remove_attr;
})()
;
/**
* @param {...*} var_args
*/
enfocus.core.set_prop = (function() { 
var set_prop__delegate = function (forms){
return (function (node){
var h = cljs.core.mapcat.call(null,(function (p__4423){
var vec__4424 = p__4423;
var n = cljs.core.nth.call(null,vec__4424,0,null);
var v = cljs.core.nth.call(null,vec__4424,1,null);
return cljs.core.list.call(null,cljs.core.name.call(null,n),v);
}),cljs.core.partition.call(null,2,forms));
return goog.dom.setProperties(node,cljs.core.apply.call(null,cljs.core.js_obj,h));
});
};
var set_prop = function (var_args){
var forms = null;
if (arguments.length > 0) {
  forms = cljs.core.array_seq(Array.prototype.slice.call(arguments, 0),0);
} 
return set_prop__delegate.call(this, forms);
};
set_prop.cljs$lang$maxFixedArity = 0;
set_prop.cljs$lang$applyTo = (function (arglist__4425){
var forms = cljs.core.seq(arglist__4425);
return set_prop__delegate(forms);
});
set_prop.cljs$core$IFn$_invoke$arity$variadic = set_prop__delegate;
return set_prop;
})()
;
/**
* returns true if the element has a given class
*/
enfocus.core.has_class = (function has_class(el,cls){
return goog.dom.classes.hasClass(el,cls);
});
/**
* Adds the specified classes to the selected element.
* @param {...*} var_args
*/
enfocus.core.add_class = (function() { 
var add_class__delegate = function (values){
return enfocus.core.multi_node_chain.call(null,(function (p1__4426_SHARP_){
var seq__4431 = cljs.core.seq.call(null,values);
var chunk__4432 = null;
var count__4433 = 0;
var i__4434 = 0;
while(true){
if((i__4434 < count__4433))
{var val = cljs.core._nth.call(null,chunk__4432,i__4434);
domina.add_class_BANG_.call(null,p1__4426_SHARP_,val);
{
var G__4435 = seq__4431;
var G__4436 = chunk__4432;
var G__4437 = count__4433;
var G__4438 = (i__4434 + 1);
seq__4431 = G__4435;
chunk__4432 = G__4436;
count__4433 = G__4437;
i__4434 = G__4438;
continue;
}
} else
{var temp__4092__auto__ = cljs.core.seq.call(null,seq__4431);
if(temp__4092__auto__)
{var seq__4431__$1 = temp__4092__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__4431__$1))
{var c__3186__auto__ = cljs.core.chunk_first.call(null,seq__4431__$1);
{
var G__4439 = cljs.core.chunk_rest.call(null,seq__4431__$1);
var G__4440 = c__3186__auto__;
var G__4441 = cljs.core.count.call(null,c__3186__auto__);
var G__4442 = 0;
seq__4431 = G__4439;
chunk__4432 = G__4440;
count__4433 = G__4441;
i__4434 = G__4442;
continue;
}
} else
{var val = cljs.core.first.call(null,seq__4431__$1);
domina.add_class_BANG_.call(null,p1__4426_SHARP_,val);
{
var G__4443 = cljs.core.next.call(null,seq__4431__$1);
var G__4444 = null;
var G__4445 = 0;
var G__4446 = 0;
seq__4431 = G__4443;
chunk__4432 = G__4444;
count__4433 = G__4445;
i__4434 = G__4446;
continue;
}
}
} else
{return null;
}
}
break;
}
}));
};
var add_class = function (var_args){
var values = null;
if (arguments.length > 0) {
  values = cljs.core.array_seq(Array.prototype.slice.call(arguments, 0),0);
} 
return add_class__delegate.call(this, values);
};
add_class.cljs$lang$maxFixedArity = 0;
add_class.cljs$lang$applyTo = (function (arglist__4447){
var values = cljs.core.seq(arglist__4447);
return add_class__delegate(values);
});
add_class.cljs$core$IFn$_invoke$arity$variadic = add_class__delegate;
return add_class;
})()
;
/**
* Removes the specified classes from the selected element.
* @param {...*} var_args
*/
enfocus.core.remove_class = (function() { 
var remove_class__delegate = function (values){
return enfocus.core.multi_node_chain.call(null,(function (p1__4448_SHARP_){
var seq__4453 = cljs.core.seq.call(null,values);
var chunk__4454 = null;
var count__4455 = 0;
var i__4456 = 0;
while(true){
if((i__4456 < count__4455))
{var val = cljs.core._nth.call(null,chunk__4454,i__4456);
domina.remove_class_BANG_.call(null,p1__4448_SHARP_,val);
{
var G__4457 = seq__4453;
var G__4458 = chunk__4454;
var G__4459 = count__4455;
var G__4460 = (i__4456 + 1);
seq__4453 = G__4457;
chunk__4454 = G__4458;
count__4455 = G__4459;
i__4456 = G__4460;
continue;
}
} else
{var temp__4092__auto__ = cljs.core.seq.call(null,seq__4453);
if(temp__4092__auto__)
{var seq__4453__$1 = temp__4092__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__4453__$1))
{var c__3186__auto__ = cljs.core.chunk_first.call(null,seq__4453__$1);
{
var G__4461 = cljs.core.chunk_rest.call(null,seq__4453__$1);
var G__4462 = c__3186__auto__;
var G__4463 = cljs.core.count.call(null,c__3186__auto__);
var G__4464 = 0;
seq__4453 = G__4461;
chunk__4454 = G__4462;
count__4455 = G__4463;
i__4456 = G__4464;
continue;
}
} else
{var val = cljs.core.first.call(null,seq__4453__$1);
domina.remove_class_BANG_.call(null,p1__4448_SHARP_,val);
{
var G__4465 = cljs.core.next.call(null,seq__4453__$1);
var G__4466 = null;
var G__4467 = 0;
var G__4468 = 0;
seq__4453 = G__4465;
chunk__4454 = G__4466;
count__4455 = G__4467;
i__4456 = G__4468;
continue;
}
}
} else
{return null;
}
}
break;
}
}));
};
var remove_class = function (var_args){
var values = null;
if (arguments.length > 0) {
  values = cljs.core.array_seq(Array.prototype.slice.call(arguments, 0),0);
} 
return remove_class__delegate.call(this, values);
};
remove_class.cljs$lang$maxFixedArity = 0;
remove_class.cljs$lang$applyTo = (function (arglist__4469){
var values = cljs.core.seq(arglist__4469);
return remove_class__delegate(values);
});
remove_class.cljs$core$IFn$_invoke$arity$variadic = remove_class__delegate;
return remove_class;
})()
;
/**
* Sets the specified classes on the selected element
* @param {...*} var_args
*/
enfocus.core.set_class = (function() { 
var set_class__delegate = function (values){
return enfocus.core.multi_node_chain.call(null,(function (p1__4470_SHARP_){
return domina.set_classes_BANG_.call(null,p1__4470_SHARP_,values);
}));
};
var set_class = function (var_args){
var values = null;
if (arguments.length > 0) {
  values = cljs.core.array_seq(Array.prototype.slice.call(arguments, 0),0);
} 
return set_class__delegate.call(this, values);
};
set_class.cljs$lang$maxFixedArity = 0;
set_class.cljs$lang$applyTo = (function (arglist__4471){
var values = cljs.core.seq(arglist__4471);
return set_class__delegate(values);
});
set_class.cljs$core$IFn$_invoke$arity$variadic = set_class__delegate;
return set_class;
})()
;
/**
* @param {...*} var_args
*/
enfocus.core.do__GT_ = (function() { 
var do__GT___delegate = function (forms){
return (function (pnod){
var seq__4476 = cljs.core.seq.call(null,forms);
var chunk__4477 = null;
var count__4478 = 0;
var i__4479 = 0;
while(true){
if((i__4479 < count__4478))
{var fun = cljs.core._nth.call(null,chunk__4477,i__4479);
enfocus.core.apply_transform.call(null,fun,pnod);
{
var G__4480 = seq__4476;
var G__4481 = chunk__4477;
var G__4482 = count__4478;
var G__4483 = (i__4479 + 1);
seq__4476 = G__4480;
chunk__4477 = G__4481;
count__4478 = G__4482;
i__4479 = G__4483;
continue;
}
} else
{var temp__4092__auto__ = cljs.core.seq.call(null,seq__4476);
if(temp__4092__auto__)
{var seq__4476__$1 = temp__4092__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__4476__$1))
{var c__3186__auto__ = cljs.core.chunk_first.call(null,seq__4476__$1);
{
var G__4484 = cljs.core.chunk_rest.call(null,seq__4476__$1);
var G__4485 = c__3186__auto__;
var G__4486 = cljs.core.count.call(null,c__3186__auto__);
var G__4487 = 0;
seq__4476 = G__4484;
chunk__4477 = G__4485;
count__4478 = G__4486;
i__4479 = G__4487;
continue;
}
} else
{var fun = cljs.core.first.call(null,seq__4476__$1);
enfocus.core.apply_transform.call(null,fun,pnod);
{
var G__4488 = cljs.core.next.call(null,seq__4476__$1);
var G__4489 = null;
var G__4490 = 0;
var G__4491 = 0;
seq__4476 = G__4488;
chunk__4477 = G__4489;
count__4478 = G__4490;
i__4479 = G__4491;
continue;
}
}
} else
{return null;
}
}
break;
}
});
};
var do__GT_ = function (var_args){
var forms = null;
if (arguments.length > 0) {
  forms = cljs.core.array_seq(Array.prototype.slice.call(arguments, 0),0);
} 
return do__GT___delegate.call(this, forms);
};
do__GT_.cljs$lang$maxFixedArity = 0;
do__GT_.cljs$lang$applyTo = (function (arglist__4492){
var forms = cljs.core.seq(arglist__4492);
return do__GT___delegate(forms);
});
do__GT_.cljs$core$IFn$_invoke$arity$variadic = do__GT___delegate;
return do__GT_;
})()
;
/**
* Appends the content of the element. Values can be nodes or collection of nodes.
* @param {...*} var_args
*/
enfocus.core.append = (function() { 
var append__delegate = function (values){
return enfocus.core.multi_node_chain.call(null,values,(function (p1__4493_SHARP_,p2__4494_SHARP_){
return domina.append_BANG_.call(null,p1__4493_SHARP_,p2__4494_SHARP_);
}));
};
var append = function (var_args){
var values = null;
if (arguments.length > 0) {
  values = cljs.core.array_seq(Array.prototype.slice.call(arguments, 0),0);
} 
return append__delegate.call(this, values);
};
append.cljs$lang$maxFixedArity = 0;
append.cljs$lang$applyTo = (function (arglist__4495){
var values = cljs.core.seq(arglist__4495);
return append__delegate(values);
});
append.cljs$core$IFn$_invoke$arity$variadic = append__delegate;
return append;
})()
;
/**
* Prepends the content of the element. Values can be nodes or collection of nodes.
* @param {...*} var_args
*/
enfocus.core.prepend = (function() { 
var prepend__delegate = function (values){
return enfocus.core.multi_node_chain.call(null,values,(function (p1__4496_SHARP_,p2__4497_SHARP_){
return domina.prepend_BANG_.call(null,p1__4496_SHARP_,p2__4497_SHARP_);
}));
};
var prepend = function (var_args){
var values = null;
if (arguments.length > 0) {
  values = cljs.core.array_seq(Array.prototype.slice.call(arguments, 0),0);
} 
return prepend__delegate.call(this, values);
};
prepend.cljs$lang$maxFixedArity = 0;
prepend.cljs$lang$applyTo = (function (arglist__4498){
var values = cljs.core.seq(arglist__4498);
return prepend__delegate(values);
});
prepend.cljs$core$IFn$_invoke$arity$variadic = prepend__delegate;
return prepend;
})()
;
/**
* inserts the content before the selected node. Values can be nodes or collection of nodes
* @param {...*} var_args
*/
enfocus.core.before = (function() { 
var before__delegate = function (values){
return enfocus.core.multi_node_chain.call(null,values,(function (p1__4499_SHARP_,p2__4500_SHARP_){
return domina.insert_before_BANG_.call(null,p1__4499_SHARP_,p2__4500_SHARP_);
}));
};
var before = function (var_args){
var values = null;
if (arguments.length > 0) {
  values = cljs.core.array_seq(Array.prototype.slice.call(arguments, 0),0);
} 
return before__delegate.call(this, values);
};
before.cljs$lang$maxFixedArity = 0;
before.cljs$lang$applyTo = (function (arglist__4501){
var values = cljs.core.seq(arglist__4501);
return before__delegate(values);
});
before.cljs$core$IFn$_invoke$arity$variadic = before__delegate;
return before;
})()
;
/**
* inserts the content after the selected node. Values can be nodes or collection of nodes
* @param {...*} var_args
*/
enfocus.core.after = (function() { 
var after__delegate = function (values){
return enfocus.core.multi_node_chain.call(null,values,(function (p1__4502_SHARP_,p2__4503_SHARP_){
return domina.insert_after_BANG_.call(null,p1__4502_SHARP_,p2__4503_SHARP_);
}));
};
var after = function (var_args){
var values = null;
if (arguments.length > 0) {
  values = cljs.core.array_seq(Array.prototype.slice.call(arguments, 0),0);
} 
return after__delegate.call(this, values);
};
after.cljs$lang$maxFixedArity = 0;
after.cljs$lang$applyTo = (function (arglist__4504){
var values = cljs.core.seq(arglist__4504);
return after__delegate(values);
});
after.cljs$core$IFn$_invoke$arity$variadic = after__delegate;
return after;
})()
;
/**
* substitutes the content for the selected node. Values can be nodes or collection of nodes
* @param {...*} var_args
*/
enfocus.core.substitute = (function() { 
var substitute__delegate = function (values){
return enfocus.core.multi_node_chain.call(null,values,(function (p1__4505_SHARP_,p2__4506_SHARP_){
return domina.swap_content_BANG_.call(null,p1__4505_SHARP_,p2__4506_SHARP_);
}));
};
var substitute = function (var_args){
var values = null;
if (arguments.length > 0) {
  values = cljs.core.array_seq(Array.prototype.slice.call(arguments, 0),0);
} 
return substitute__delegate.call(this, values);
};
substitute.cljs$lang$maxFixedArity = 0;
substitute.cljs$lang$applyTo = (function (arglist__4507){
var values = cljs.core.seq(arglist__4507);
return substitute__delegate(values);
});
substitute.cljs$core$IFn$_invoke$arity$variadic = substitute__delegate;
return substitute;
})()
;
/**
* removes the selected nodes from the dom
*/
enfocus.core.remove_node = (function remove_node(){
return enfocus.core.multi_node_chain.call(null,(function (p1__4508_SHARP_){
return domina.detach_BANG_.call(null,p1__4508_SHARP_);
}));
});
/**
* wrap and element in a new element defined as :div {:class 'temp'}
*/
enfocus.core.wrap = (function wrap(elm,mattr){
return (function (pnod){
var elem = goog.dom.createElement(cljs.core.name.call(null,elm));
enfocus.core.add_map_attrs.call(null,elem,mattr);
enfocus.core.at.call(null,elem,enfocus.core.content.call(null,pnod.cloneNode(true)));
return enfocus.core.at.call(null,pnod,enfocus.core.do__GT_.call(null,enfocus.core.after.call(null,elem),enfocus.core.remove_node.call(null)));
});
});
/**
* replaces a node with all its children
*/
enfocus.core.unwrap = (function unwrap(){
return (function (pnod){
var frag = document.createDocumentFragment();
goog.dom.append(frag,pnod.childNodes);
return goog.dom.replaceNode(frag,pnod);
});
});
/**
* set a list of style elements from the selected nodes
* @param {...*} var_args
*/
enfocus.core.set_style = (function() { 
var set_style__delegate = function (values){
var pairs = cljs.core.partition.call(null,2,values);
return enfocus.core.multi_node_chain.call(null,(function (p1__4509_SHARP_){
var seq__4516 = cljs.core.seq.call(null,pairs);
var chunk__4517 = null;
var count__4518 = 0;
var i__4519 = 0;
while(true){
if((i__4519 < count__4518))
{var vec__4520 = cljs.core._nth.call(null,chunk__4517,i__4519);
var name = cljs.core.nth.call(null,vec__4520,0,null);
var value = cljs.core.nth.call(null,vec__4520,1,null);
domina.set_style_BANG_.call(null,p1__4509_SHARP_,name,value);
{
var G__4522 = seq__4516;
var G__4523 = chunk__4517;
var G__4524 = count__4518;
var G__4525 = (i__4519 + 1);
seq__4516 = G__4522;
chunk__4517 = G__4523;
count__4518 = G__4524;
i__4519 = G__4525;
continue;
}
} else
{var temp__4092__auto__ = cljs.core.seq.call(null,seq__4516);
if(temp__4092__auto__)
{var seq__4516__$1 = temp__4092__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__4516__$1))
{var c__3186__auto__ = cljs.core.chunk_first.call(null,seq__4516__$1);
{
var G__4526 = cljs.core.chunk_rest.call(null,seq__4516__$1);
var G__4527 = c__3186__auto__;
var G__4528 = cljs.core.count.call(null,c__3186__auto__);
var G__4529 = 0;
seq__4516 = G__4526;
chunk__4517 = G__4527;
count__4518 = G__4528;
i__4519 = G__4529;
continue;
}
} else
{var vec__4521 = cljs.core.first.call(null,seq__4516__$1);
var name = cljs.core.nth.call(null,vec__4521,0,null);
var value = cljs.core.nth.call(null,vec__4521,1,null);
domina.set_style_BANG_.call(null,p1__4509_SHARP_,name,value);
{
var G__4530 = cljs.core.next.call(null,seq__4516__$1);
var G__4531 = null;
var G__4532 = 0;
var G__4533 = 0;
seq__4516 = G__4530;
chunk__4517 = G__4531;
count__4518 = G__4532;
i__4519 = G__4533;
continue;
}
}
} else
{return null;
}
}
break;
}
}));
};
var set_style = function (var_args){
var values = null;
if (arguments.length > 0) {
  values = cljs.core.array_seq(Array.prototype.slice.call(arguments, 0),0);
} 
return set_style__delegate.call(this, values);
};
set_style.cljs$lang$maxFixedArity = 0;
set_style.cljs$lang$applyTo = (function (arglist__4534){
var values = cljs.core.seq(arglist__4534);
return set_style__delegate(values);
});
set_style.cljs$core$IFn$_invoke$arity$variadic = set_style__delegate;
return set_style;
})()
;
/**
* remove a list style elements from the selected nodes. note: you can only remove styles that are inline
* @param {...*} var_args
*/
enfocus.core.remove_style = (function() { 
var remove_style__delegate = function (values){
return (function (pnod){
return enfocus.core.style_remove.call(null,pnod,values);
});
};
var remove_style = function (var_args){
var values = null;
if (arguments.length > 0) {
  values = cljs.core.array_seq(Array.prototype.slice.call(arguments, 0),0);
} 
return remove_style__delegate.call(this, values);
};
remove_style.cljs$lang$maxFixedArity = 0;
remove_style.cljs$lang$applyTo = (function (arglist__4535){
var values = cljs.core.seq(arglist__4535);
return remove_style__delegate(values);
});
remove_style.cljs$core$IFn$_invoke$arity$variadic = remove_style__delegate;
return remove_style;
})()
;
/**
* calls the focus function on the selected node
*/
enfocus.core.focus = (function focus(){
return (function (node){
return node.focus();
});
});
/**
* calls the blur function on the selected node
*/
enfocus.core.blur = (function blur(){
return (function (node){
return node.blur();
});
});
/**
* addes key value pair of data to the selected nodes. Only use clojure data structures when setting
*/
enfocus.core.set_data = (function set_data(ky,val){
return enfocus.core.multi_node_chain.call(null,(function (p1__4536_SHARP_){
return domina.set_data_BANG_.call(null,p1__4536_SHARP_,ky,val);
}));
});
/**
* delays and action by a set timeout, note this is an async operations
* @param {...*} var_args
*/
enfocus.core.delay = (function() { 
var delay__delegate = function (ttime,funcs){
return (function (pnod){
return enfocus.core.setTimeout.call(null,(function (){
return cljs.core.apply.call(null,enfocus.core.at,pnod,funcs);
}),ttime);
});
};
var delay = function (ttime,var_args){
var funcs = null;
if (arguments.length > 1) {
  funcs = cljs.core.array_seq(Array.prototype.slice.call(arguments, 1),0);
} 
return delay__delegate.call(this, ttime, funcs);
};
delay.cljs$lang$maxFixedArity = 1;
delay.cljs$lang$applyTo = (function (arglist__4537){
var ttime = cljs.core.first(arglist__4537);
var funcs = cljs.core.rest(arglist__4537);
return delay__delegate(ttime, funcs);
});
delay.cljs$core$IFn$_invoke$arity$variadic = delay__delegate;
return delay;
})()
;
/**
* replaces entries like the following ${var1} in attributes and text
*/
enfocus.core.replace_vars = (function replace_vars(vars){
var rep_str = (function rep_str(text){
return clojure.string.replace.call(null,text,/\$\{\s*(\S+)\s*}/,(function (p1__4539_SHARP_,p2__4538_SHARP_){
return vars.call(null,cljs.core.keyword.call(null,p2__4538_SHARP_));
}));
});
return (function rep_node(pnod){
if(cljs.core.truth_(pnod.attributes))
{var seq__4564_4572 = cljs.core.seq.call(null,cljs.core.range.call(null,pnod.attributes.length));
var chunk__4565_4573 = null;
var count__4566_4574 = 0;
var i__4567_4575 = 0;
while(true){
if((i__4567_4575 < count__4566_4574))
{var idx_4576 = cljs.core._nth.call(null,chunk__4565_4573,i__4567_4575);
var attr_4577 = pnod.attributes.item(idx_4576);
if(cljs.core.truth_(attr_4577.specified))
{attr_4577.value = rep_str.call(null,attr_4577.value);
} else
{}
{
var G__4578 = seq__4564_4572;
var G__4579 = chunk__4565_4573;
var G__4580 = count__4566_4574;
var G__4581 = (i__4567_4575 + 1);
seq__4564_4572 = G__4578;
chunk__4565_4573 = G__4579;
count__4566_4574 = G__4580;
i__4567_4575 = G__4581;
continue;
}
} else
{var temp__4092__auto___4582 = cljs.core.seq.call(null,seq__4564_4572);
if(temp__4092__auto___4582)
{var seq__4564_4583__$1 = temp__4092__auto___4582;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__4564_4583__$1))
{var c__3186__auto___4584 = cljs.core.chunk_first.call(null,seq__4564_4583__$1);
{
var G__4585 = cljs.core.chunk_rest.call(null,seq__4564_4583__$1);
var G__4586 = c__3186__auto___4584;
var G__4587 = cljs.core.count.call(null,c__3186__auto___4584);
var G__4588 = 0;
seq__4564_4572 = G__4585;
chunk__4565_4573 = G__4586;
count__4566_4574 = G__4587;
i__4567_4575 = G__4588;
continue;
}
} else
{var idx_4589 = cljs.core.first.call(null,seq__4564_4583__$1);
var attr_4590 = pnod.attributes.item(idx_4589);
if(cljs.core.truth_(attr_4590.specified))
{attr_4590.value = rep_str.call(null,attr_4590.value);
} else
{}
{
var G__4591 = cljs.core.next.call(null,seq__4564_4583__$1);
var G__4592 = null;
var G__4593 = 0;
var G__4594 = 0;
seq__4564_4572 = G__4591;
chunk__4565_4573 = G__4592;
count__4566_4574 = G__4593;
i__4567_4575 = G__4594;
continue;
}
}
} else
{}
}
break;
}
} else
{}
if(cljs.core._EQ_.call(null,pnod.nodeType,3))
{return pnod.nodeValue = rep_str.call(null,pnod.nodeValue);
} else
{var seq__4568 = cljs.core.seq.call(null,enfocus.core.nodes__GT_coll.call(null,pnod.childNodes));
var chunk__4569 = null;
var count__4570 = 0;
var i__4571 = 0;
while(true){
if((i__4571 < count__4570))
{var cnode = cljs.core._nth.call(null,chunk__4569,i__4571);
rep_node.call(null,cnode);
{
var G__4595 = seq__4568;
var G__4596 = chunk__4569;
var G__4597 = count__4570;
var G__4598 = (i__4571 + 1);
seq__4568 = G__4595;
chunk__4569 = G__4596;
count__4570 = G__4597;
i__4571 = G__4598;
continue;
}
} else
{var temp__4092__auto__ = cljs.core.seq.call(null,seq__4568);
if(temp__4092__auto__)
{var seq__4568__$1 = temp__4092__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__4568__$1))
{var c__3186__auto__ = cljs.core.chunk_first.call(null,seq__4568__$1);
{
var G__4599 = cljs.core.chunk_rest.call(null,seq__4568__$1);
var G__4600 = c__3186__auto__;
var G__4601 = cljs.core.count.call(null,c__3186__auto__);
var G__4602 = 0;
seq__4568 = G__4599;
chunk__4569 = G__4600;
count__4570 = G__4601;
i__4571 = G__4602;
continue;
}
} else
{var cnode = cljs.core.first.call(null,seq__4568__$1);
rep_node.call(null,cnode);
{
var G__4603 = cljs.core.next.call(null,seq__4568__$1);
var G__4604 = null;
var G__4605 = 0;
var G__4606 = 0;
seq__4568 = G__4603;
chunk__4569 = G__4604;
count__4570 = G__4605;
i__4571 = G__4606;
continue;
}
}
} else
{return null;
}
}
break;
}
}
});
});
/**
* takes clojure data structure and emits a document element
*/
enfocus.core.html = (function html(node_spec){
if(cljs.core.string_QMARK_.call(null,node_spec))
{return document.createTextNode(node_spec);
} else
{if(cljs.core.vector_QMARK_.call(null,node_spec))
{var vec__4616 = node_spec;
var tag = cljs.core.nth.call(null,vec__4616,0,null);
var vec__4617 = cljs.core.nthnext.call(null,vec__4616,1);
var m = cljs.core.nth.call(null,vec__4617,0,null);
var ms = cljs.core.nthnext.call(null,vec__4617,1);
var more = vec__4617;
var _ = enfocus.core.log_debug.call(null,cljs.core.pr_str.call(null,tag,m,ms));
var vec__4618 = cljs.core.name.call(null,tag).split(/(?=[#.])/);
var tag_name = cljs.core.nth.call(null,vec__4618,0,null);
var segments = cljs.core.nthnext.call(null,vec__4618,1);
var id = cljs.core.some.call(null,((function (vec__4616,tag,vec__4617,m,ms,more,_,vec__4618,tag_name,segments){
return (function (seg){
if(cljs.core._EQ_.call(null,"#",seg.charAt(0)))
{return cljs.core.subs.call(null,seg,1);
} else
{return null;
}
});})(vec__4616,tag,vec__4617,m,ms,more,_,vec__4618,tag_name,segments))
,segments);
var classes = cljs.core.keep.call(null,((function (vec__4616,tag,vec__4617,m,ms,more,_,vec__4618,tag_name,segments,id){
return (function (seg){
if(cljs.core._EQ_.call(null,".",seg.charAt(0)))
{return cljs.core.subs.call(null,seg,1);
} else
{return null;
}
});})(vec__4616,tag,vec__4617,m,ms,more,_,vec__4618,tag_name,segments,id))
,segments);
var attrs = ((cljs.core.map_QMARK_.call(null,m))?m:cljs.core.PersistentArrayMap.EMPTY);
var attrs__$1 = (cljs.core.truth_(id)?cljs.core.assoc.call(null,attrs,"\uFDD0:id",id):attrs);
var attrs__$2 = ((!(cljs.core.empty_QMARK_.call(null,classes)))?cljs.core.assoc.call(null,attrs__$1,"\uFDD0:class",cljs.core.apply.call(null,cljs.core.str,cljs.core.interpose.call(null," ",classes))):attrs__$1);
var content = cljs.core.flatten.call(null,cljs.core.map.call(null,html,((cljs.core.map_QMARK_.call(null,m))?ms:more)));
var node = document.createElement(tag_name);
var seq__4619_4625 = cljs.core.seq.call(null,attrs__$2);
var chunk__4620_4626 = null;
var count__4621_4627 = 0;
var i__4622_4628 = 0;
while(true){
if((i__4622_4628 < count__4621_4627))
{var vec__4623_4629 = cljs.core._nth.call(null,chunk__4620_4626,i__4622_4628);
var key_4630 = cljs.core.nth.call(null,vec__4623_4629,0,null);
var val_4631 = cljs.core.nth.call(null,vec__4623_4629,1,null);
node.setAttribute(cljs.core.name.call(null,key_4630),val_4631);
{
var G__4632 = seq__4619_4625;
var G__4633 = chunk__4620_4626;
var G__4634 = count__4621_4627;
var G__4635 = (i__4622_4628 + 1);
seq__4619_4625 = G__4632;
chunk__4620_4626 = G__4633;
count__4621_4627 = G__4634;
i__4622_4628 = G__4635;
continue;
}
} else
{var temp__4092__auto___4636 = cljs.core.seq.call(null,seq__4619_4625);
if(temp__4092__auto___4636)
{var seq__4619_4637__$1 = temp__4092__auto___4636;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__4619_4637__$1))
{var c__3186__auto___4638 = cljs.core.chunk_first.call(null,seq__4619_4637__$1);
{
var G__4639 = cljs.core.chunk_rest.call(null,seq__4619_4637__$1);
var G__4640 = c__3186__auto___4638;
var G__4641 = cljs.core.count.call(null,c__3186__auto___4638);
var G__4642 = 0;
seq__4619_4625 = G__4639;
chunk__4620_4626 = G__4640;
count__4621_4627 = G__4641;
i__4622_4628 = G__4642;
continue;
}
} else
{var vec__4624_4643 = cljs.core.first.call(null,seq__4619_4637__$1);
var key_4644 = cljs.core.nth.call(null,vec__4624_4643,0,null);
var val_4645 = cljs.core.nth.call(null,vec__4624_4643,1,null);
node.setAttribute(cljs.core.name.call(null,key_4644),val_4645);
{
var G__4646 = cljs.core.next.call(null,seq__4619_4637__$1);
var G__4647 = null;
var G__4648 = 0;
var G__4649 = 0;
seq__4619_4625 = G__4646;
chunk__4620_4626 = G__4647;
count__4621_4627 = G__4648;
i__4622_4628 = G__4649;
continue;
}
}
} else
{}
}
break;
}
if(cljs.core.truth_(content))
{return domina.append_BANG_.call(null,node,content);
} else
{return null;
}
} else
{if(cljs.core.sequential_QMARK_.call(null,node_spec))
{return cljs.core.flatten.call(null,cljs.core.map.call(null,html,node_spec));
} else
{if("\uFDD0:else")
{return document.createTextNode([cljs.core.str(node_spec)].join(''));
} else
{return null;
}
}
}
}
});
/**
* returns the attribute on the selected element or elements.
* in cases where more than one element is selected you will
* receive a list of values
*/
enfocus.core.get_attr = (function get_attr(attr){
return enfocus.core.extr_multi_node.call(null,(function (pnod){
return pnod.getAttribute(cljs.core.name.call(null,attr));
}));
});
/**
* returns the text value of the selected element or elements.
* in cases where more than one element is selected you will
* receive a list of values
*/
enfocus.core.get_text = (function get_text(){
return enfocus.core.extr_multi_node.call(null,(function (pnod){
return goog.dom.getTextContent(pnod);
}));
});
/**
* returns the data on a selected node for a given key. If bubble is set will look at parent
*/
enfocus.core.get_data = (function() {
var get_data = null;
var get_data__1 = (function (ky){
return get_data.call(null,ky,false);
});
var get_data__2 = (function (ky,bubble){
return enfocus.core.extr_multi_node.call(null,(function (node){
return domina.get_data.call(null,node,ky,bubble);
}));
});
get_data = function(ky,bubble){
switch(arguments.length){
case 1:
return get_data__1.call(this,ky);
case 2:
return get_data__2.call(this,ky,bubble);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
get_data.cljs$core$IFn$_invoke$arity$1 = get_data__1;
get_data.cljs$core$IFn$_invoke$arity$2 = get_data__2;
return get_data;
})()
;
/**
* returns the property on the selected element or elements.
* in cases where more than one element is selected you will
* receive a list of values
*/
enfocus.core.get_prop = (function get_prop(prop){
return enfocus.core.extr_multi_node.call(null,(function (pnod){
return (pnod[cljs.core.name.call(null,prop)]);
}));
});
/**
* this function takes a map, key and value.  It will check if
* the value exists and create a seq of values if one exits.
*/
enfocus.core.merge_form_val = (function merge_form_val(form_map,ky,val){
var mval = form_map.call(null,ky);
if(cljs.core.coll_QMARK_.call(null,mval))
{return cljs.core.assoc.call(null,form_map,ky,cljs.core.conj.call(null,mval,val));
} else
{if(cljs.core.truth_(mval))
{return cljs.core.assoc.call(null,form_map,ky,cljs.core.list.call(null,val,mval));
} else
{if("\uFDD0:else")
{return cljs.core.assoc.call(null,form_map,ky,val);
} else
{return null;
}
}
}
});
enfocus.core.read_simple_input = (function read_simple_input(el,col){
return enfocus.core.merge_form_val.call(null,col,cljs.core.keyword.call(null,el.name),el.value);
});
enfocus.core.read_checked_input = (function read_checked_input(el,col){
if(cljs.core.truth_(el.checked))
{return enfocus.core.merge_form_val.call(null,col,cljs.core.keyword.call(null,el.name),el.value);
} else
{return col;
}
});
enfocus.core.read_select_input = (function read_select_input(el,col){
var nm = cljs.core.keyword.call(null,el.name);
var onodes = domina.nodes.call(null,el.options);
var opts = cljs.core.filter.call(null,((function (nm,onodes){
return (function (p1__4650_SHARP_){
return p1__4650_SHARP_.selected;
});})(nm,onodes))
,onodes);
return enfocus.core.merge_form_val.call(null,col,nm,cljs.core.map.call(null,(function (p1__4651_SHARP_){
return p1__4651_SHARP_.value;
}),opts));
});
/**
* returns a map of the form values tied to name of input fields.
* {:name1 'value1' name2 ('select1' 'select2')}
*/
enfocus.core.read_form = (function read_form(){
return enfocus.core.extr_multi_node.call(null,(function (node){
var inputs = node.elements;
return cljs.core.reduce.call(null,(function (p1__4653_SHARP_,p2__4652_SHARP_){
var G__4656 = p2__4652_SHARP_.nodeName;
if(cljs.core._EQ_.call(null,"BUTTON",G__4656))
{return enfocus.core.read_simple_input.call(null,p2__4652_SHARP_,p1__4653_SHARP_);
} else
{if(cljs.core._EQ_.call(null,"SELECT",G__4656))
{return enfocus.core.read_select_input.call(null,p2__4652_SHARP_,p1__4653_SHARP_);
} else
{if(cljs.core._EQ_.call(null,"TEXTAREA",G__4656))
{return enfocus.core.read_simple_input.call(null,p2__4652_SHARP_,p1__4653_SHARP_);
} else
{if(cljs.core._EQ_.call(null,"INPUT",G__4656))
{var G__4657 = p2__4652_SHARP_.type;
if(cljs.core._EQ_.call(null,"radio",G__4657))
{return enfocus.core.read_checked_input.call(null,p2__4652_SHARP_,p1__4653_SHARP_);
} else
{if(cljs.core._EQ_.call(null,"checkbox",G__4657))
{return enfocus.core.read_checked_input.call(null,p2__4652_SHARP_,p1__4653_SHARP_);
} else
{if(cljs.core._EQ_.call(null,"submit",G__4657))
{return enfocus.core.read_simple_input.call(null,p2__4652_SHARP_,p1__4653_SHARP_);
} else
{if(cljs.core._EQ_.call(null,"reset",G__4657))
{return enfocus.core.read_simple_input.call(null,p2__4652_SHARP_,p1__4653_SHARP_);
} else
{if(cljs.core._EQ_.call(null,"button",G__4657))
{return enfocus.core.read_simple_input.call(null,p2__4652_SHARP_,p1__4653_SHARP_);
} else
{if(cljs.core._EQ_.call(null,"password",G__4657))
{return enfocus.core.read_simple_input.call(null,p2__4652_SHARP_,p1__4653_SHARP_);
} else
{if(cljs.core._EQ_.call(null,"hidden",G__4657))
{return enfocus.core.read_simple_input.call(null,p2__4652_SHARP_,p1__4653_SHARP_);
} else
{if(cljs.core._EQ_.call(null,"text",G__4657))
{return enfocus.core.read_simple_input.call(null,p2__4652_SHARP_,p1__4653_SHARP_);
} else
{if("\uFDD0:else")
{return p1__4653_SHARP_;
} else
{return null;
}
}
}
}
}
}
}
}
}
} else
{if("\uFDD0:else")
{return p1__4653_SHARP_;
} else
{return null;
}
}
}
}
}
}),cljs.core.PersistentArrayMap.EMPTY,inputs);
}));
});
enfocus.core.reg_filt = cljs.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
/**
* filter allows you to apply function to futhur scope down what is returned by a selector
*/
enfocus.core.filter = (function filter(tst,trans){
return enfocus.core.multi_node_chain.call(null,(function() {
var filt = null;
var filt__1 = (function (pnodes){
return filt.call(null,pnodes,null);
});
var filt__2 = (function (pnodes,chain){
var pnod_col = enfocus.core.nodes__GT_coll.call(null,pnodes);
var ttest = ((cljs.core.keyword_QMARK_.call(null,tst))?cljs.core.deref.call(null,enfocus.core.reg_filt).call(null,tst):tst);
var res = cljs.core.filter.call(null,ttest,pnod_col);
if((chain == null))
{return enfocus.core.apply_transform.call(null,trans,res);
} else
{return enfocus.core.apply_transform.call(null,trans,res,chain);
}
});
filt = function(pnodes,chain){
switch(arguments.length){
case 1:
return filt__1.call(this,pnodes);
case 2:
return filt__2.call(this,pnodes,chain);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
filt.cljs$core$IFn$_invoke$arity$1 = filt__1;
filt.cljs$core$IFn$_invoke$arity$2 = filt__2;
return filt;
})()
);
});
/**
* registers a filter for a given keyword
*/
enfocus.core.register_filter = (function register_filter(ky,func){
return cljs.core.swap_BANG_.call(null,enfocus.core.reg_filt,cljs.core.assoc,ky,func);
});
/**
* takes a list of options and returns the selected ones.
*/
enfocus.core.selected_options = (function selected_options(pnod){
return pnod.selected;
});
/**
* takes a list of radio or checkboxes and returns the checked ones
*/
enfocus.core.checked_radio_checkbox = (function checked_radio_checkbox(pnod){
return pnod.checked;
});
enfocus.core.register_filter.call(null,"\uFDD0:selected",enfocus.core.selected_options);
enfocus.core.register_filter.call(null,"\uFDD0:checked",enfocus.core.checked_radio_checkbox);
enfocus.core.match_QMARK_ = (function match_QMARK_(selector){
return (function (node){
if(cljs.core.truth_((node["matches"])))
{return node.matches(selector);
} else
{if(cljs.core.truth_((node["matchesSelector"])))
{return node.matchesSelector(selector);
} else
{if(cljs.core.truth_((node["msMatchesSelector"])))
{return node.msMatchesSelector(selector);
} else
{if(cljs.core.truth_((node["mozMatchesSelector"])))
{return node.mozMatchesSelector(selector);
} else
{if(cljs.core.truth_((node["webkitMatchesSelector"])))
{return node.webkitMatchesSelector(selector);
} else
{if("\uFDD0:else")
{return cljs.core.some.call(null,cljs.core.PersistentHashSet.fromArray([node,null], true),enfocus.core.nodes__GT_coll.call(null,enfocus.core.select.call(null,node)));
} else
{return null;
}
}
}
}
}
}
});
});
/**
* converts keywords, symbols and strings used in the enlive selector
* syntax to a string representing a standard css selector.  It also
* applys id masking if mask provided
*/
enfocus.core.create_sel_str = (function() {
var create_sel_str = null;
var create_sel_str__1 = (function (css_sel){
return create_sel_str.call(null,"",css_sel);
});
var create_sel_str__2 = (function (id_mask_sym,css_sel){
return cljs.core.apply.call(null,cljs.core.str,cljs.core.map.call(null,(function (p1__4658_SHARP_){
if((p1__4658_SHARP_ instanceof cljs.core.Symbol))
{return enfocus.core.css_syms.call(null,p1__4658_SHARP_);
} else
{if(cljs.core.keyword_QMARK_.call(null,p1__4658_SHARP_))
{return [cljs.core.str(" "),cljs.core.str(cljs.core.name.call(null,p1__4658_SHARP_).replace("#",[cljs.core.str("#"),cljs.core.str(id_mask_sym)].join('')))].join('');
} else
{if(cljs.core.vector_QMARK_.call(null,p1__4658_SHARP_))
{return create_sel_str.call(null,p1__4658_SHARP_);
} else
{if(cljs.core.string_QMARK_.call(null,p1__4658_SHARP_))
{return p1__4658_SHARP_.replace("#",[cljs.core.str("#"),cljs.core.str(id_mask_sym)].join(''));
} else
{return null;
}
}
}
}
}),css_sel));
});
create_sel_str = function(id_mask_sym,css_sel){
switch(arguments.length){
case 1:
return create_sel_str__1.call(this,id_mask_sym);
case 2:
return create_sel_str__2.call(this,id_mask_sym,css_sel);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
create_sel_str.cljs$core$IFn$_invoke$arity$1 = create_sel_str__1;
create_sel_str.cljs$core$IFn$_invoke$arity$2 = create_sel_str__2;
return create_sel_str;
})()
;
/**
* takes either an enlive selector or a css3 selector and returns a set of nodes that match the selector
*/
enfocus.core.css_select = (function() {
var css_select = null;
var css_select__1 = (function (css_sel){
return css_select.call(null,"",document,css_sel);
});
var css_select__2 = (function (dom_node,css_sel){
return css_select.call(null,"",dom_node,css_sel);
});
var css_select__3 = (function (id_mask_sym,dom_node,css_sel){
var sel = clojure.string.trim.call(null,enfocus.enlive.syntax.convert.call(null,enfocus.core.create_sel_str.call(null,id_mask_sym,css_sel)));
var ret = domina.css.sel.call(null,dom_node,sel);
return ret;
});
css_select = function(id_mask_sym,dom_node,css_sel){
switch(arguments.length){
case 1:
return css_select__1.call(this,id_mask_sym);
case 2:
return css_select__2.call(this,id_mask_sym,dom_node);
case 3:
return css_select__3.call(this,id_mask_sym,dom_node,css_sel);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
css_select.cljs$core$IFn$_invoke$arity$1 = css_select__1;
css_select.cljs$core$IFn$_invoke$arity$2 = css_select__2;
css_select.cljs$core$IFn$_invoke$arity$3 = css_select__3;
return css_select;
})()
;
enfocus.core.nil_t = (function nil_t(func){
var or__3943__auto__ = func;
if(cljs.core.truth_(or__3943__auto__))
{return or__3943__auto__;
} else
{return enfocus.core.remove_node;
}
});
/**
* @param {...*} var_args
*/
enfocus.core.i_at = (function() { 
var i_at__delegate = function (id_mask,node,trans){
var cnt = cljs.core.count.call(null,trans);
var sel_QMARK_ = (function (){var G__4667 = node;
if(G__4667)
{if(cljs.core.truth_((function (){var or__3943__auto__ = null;
if(cljs.core.truth_(or__3943__auto__))
{return or__3943__auto__;
} else
{return G__4667.enfocus$core$ISelector$;
}
})()))
{return true;
} else
{if((!G__4667.cljs$lang$protocol_mask$partition$))
{return cljs.core.type_satisfies_.call(null,enfocus.core.ISelector,G__4667);
} else
{return false;
}
}
} else
{return cljs.core.type_satisfies_.call(null,enfocus.core.ISelector,G__4667);
}
})();
if((function (){var and__3941__auto__ = cljs.core.not.call(null,sel_QMARK_);
if(and__3941__auto__)
{return cljs.core._EQ_.call(null,1,cnt);
} else
{return and__3941__auto__;
}
})())
{return enfocus.core.apply_transform.call(null,cljs.core.first.call(null,trans),node);
} else
{var vec__4668 = (cljs.core.truth_(sel_QMARK_)?cljs.core.list.call(null,document,cljs.core.conj.call(null,trans,node)):cljs.core.list.call(null,node,trans));
var node__$1 = cljs.core.nth.call(null,vec__4668,0,null);
var trans__$1 = cljs.core.nth.call(null,vec__4668,1,null);
var seq__4669 = cljs.core.seq.call(null,cljs.core.partition.call(null,2,trans__$1));
var chunk__4670 = null;
var count__4671 = 0;
var i__4672 = 0;
while(true){
if((i__4672 < count__4671))
{var vec__4673 = cljs.core._nth.call(null,chunk__4670,i__4672);
var sel = cljs.core.nth.call(null,vec__4673,0,null);
var t = cljs.core.nth.call(null,vec__4673,1,null);
enfocus.core.apply_transform.call(null,enfocus.core.nil_t.call(null,t),enfocus.core.select.call(null,sel,node__$1,id_mask));
{
var G__4675 = seq__4669;
var G__4676 = chunk__4670;
var G__4677 = count__4671;
var G__4678 = (i__4672 + 1);
seq__4669 = G__4675;
chunk__4670 = G__4676;
count__4671 = G__4677;
i__4672 = G__4678;
continue;
}
} else
{var temp__4092__auto__ = cljs.core.seq.call(null,seq__4669);
if(temp__4092__auto__)
{var seq__4669__$1 = temp__4092__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__4669__$1))
{var c__3186__auto__ = cljs.core.chunk_first.call(null,seq__4669__$1);
{
var G__4679 = cljs.core.chunk_rest.call(null,seq__4669__$1);
var G__4680 = c__3186__auto__;
var G__4681 = cljs.core.count.call(null,c__3186__auto__);
var G__4682 = 0;
seq__4669 = G__4679;
chunk__4670 = G__4680;
count__4671 = G__4681;
i__4672 = G__4682;
continue;
}
} else
{var vec__4674 = cljs.core.first.call(null,seq__4669__$1);
var sel = cljs.core.nth.call(null,vec__4674,0,null);
var t = cljs.core.nth.call(null,vec__4674,1,null);
enfocus.core.apply_transform.call(null,enfocus.core.nil_t.call(null,t),enfocus.core.select.call(null,sel,node__$1,id_mask));
{
var G__4683 = cljs.core.next.call(null,seq__4669__$1);
var G__4684 = null;
var G__4685 = 0;
var G__4686 = 0;
seq__4669 = G__4683;
chunk__4670 = G__4684;
count__4671 = G__4685;
i__4672 = G__4686;
continue;
}
}
} else
{return null;
}
}
break;
}
}
};
var i_at = function (id_mask,node,var_args){
var trans = null;
if (arguments.length > 2) {
  trans = cljs.core.array_seq(Array.prototype.slice.call(arguments, 2),0);
} 
return i_at__delegate.call(this, id_mask, node, trans);
};
i_at.cljs$lang$maxFixedArity = 2;
i_at.cljs$lang$applyTo = (function (arglist__4687){
var id_mask = cljs.core.first(arglist__4687);
arglist__4687 = cljs.core.next(arglist__4687);
var node = cljs.core.first(arglist__4687);
var trans = cljs.core.rest(arglist__4687);
return i_at__delegate(id_mask, node, trans);
});
i_at.cljs$core$IFn$_invoke$arity$variadic = i_at__delegate;
return i_at;
})()
;
/**
* @param {...*} var_args
*/
enfocus.core.at = (function() { 
var at__delegate = function (node,trans){
return cljs.core.apply.call(null,enfocus.core.i_at,"",node,trans);
};
var at = function (node,var_args){
var trans = null;
if (arguments.length > 1) {
  trans = cljs.core.array_seq(Array.prototype.slice.call(arguments, 1),0);
} 
return at__delegate.call(this, node, trans);
};
at.cljs$lang$maxFixedArity = 1;
at.cljs$lang$applyTo = (function (arglist__4688){
var node = cljs.core.first(arglist__4688);
var trans = cljs.core.rest(arglist__4688);
return at__delegate(node, trans);
});
at.cljs$core$IFn$_invoke$arity$variadic = at__delegate;
return at;
})()
;
/**
* @param {...*} var_args
*/
enfocus.core.from = (function() { 
var from__delegate = function (node,trans){
var cnt = cljs.core.count.call(null,trans);
var sel_QMARK_ = (function (){var G__4693 = node;
if(G__4693)
{if(cljs.core.truth_((function (){var or__3943__auto__ = null;
if(cljs.core.truth_(or__3943__auto__))
{return or__3943__auto__;
} else
{return G__4693.enfocus$core$ISelector$;
}
})()))
{return true;
} else
{if((!G__4693.cljs$lang$protocol_mask$partition$))
{return cljs.core.type_satisfies_.call(null,enfocus.core.ISelector,G__4693);
} else
{return false;
}
}
} else
{return cljs.core.type_satisfies_.call(null,enfocus.core.ISelector,G__4693);
}
})();
if(cljs.core.truth_((function (){var and__3941__auto__ = sel_QMARK_;
if(cljs.core.truth_(and__3941__auto__))
{return cljs.core._EQ_.call(null,1,cnt);
} else
{return and__3941__auto__;
}
})()))
{return enfocus.core.apply_transform.call(null,cljs.core.first.call(null,trans),enfocus.core.select.call(null,node));
} else
{if(cljs.core._EQ_.call(null,1,cnt))
{return enfocus.core.apply_transform.call(null,cljs.core.first.call(null,trans),node);
} else
{if("\uFDD0:else")
{var vec__4694 = (cljs.core.truth_(sel_QMARK_)?cljs.core.list.call(null,document,cljs.core.conj.call(null,trans,node)):cljs.core.list.call(null,node,trans));
var node__$1 = cljs.core.nth.call(null,vec__4694,0,null);
var trans__$1 = cljs.core.nth.call(null,vec__4694,1,null);
return cljs.core.apply.call(null,cljs.core.hash_map,cljs.core.mapcat.call(null,(function (p__4695){
var vec__4696 = p__4695;
var ky = cljs.core.nth.call(null,vec__4696,0,null);
var sel = cljs.core.nth.call(null,vec__4696,1,null);
var ext = cljs.core.nth.call(null,vec__4696,2,null);
return cljs.core.PersistentVector.fromArray([ky,enfocus.core.apply_transform.call(null,ext,enfocus.core.select.call(null,sel,node__$1,""))], true);
}),cljs.core.partition.call(null,3,trans__$1)));
} else
{return null;
}
}
}
};
var from = function (node,var_args){
var trans = null;
if (arguments.length > 1) {
  trans = cljs.core.array_seq(Array.prototype.slice.call(arguments, 1),0);
} 
return from__delegate.call(this, node, trans);
};
from.cljs$lang$maxFixedArity = 1;
from.cljs$lang$applyTo = (function (arglist__4697){
var node = cljs.core.first(arglist__4697);
var trans = cljs.core.rest(arglist__4697);
return from__delegate(node, trans);
});
from.cljs$core$IFn$_invoke$arity$variadic = from__delegate;
return from;
})()
;
enfocus.core.xpath = (function xpath(path){
return (function (root,id_mask){
if(cljs.core.empty_QMARK_.call(null,id_mask))
{return domina.xpath.xpath.call(null,root,path);
} else
{var tmp = path.replace("@ID='",[cljs.core.str("@ID='"),cljs.core.str(id_mask)].join(''));
var mpath = path.replace("@id='",[cljs.core.str("@id='"),cljs.core.str(id_mask)].join(''));
return domina.xpath.xpath.call(null,root,mpath);
}
});
});
enfocus.core.this_node = (function this_node(root,id_mask){
return root;
});
if(cljs.core.truth_((typeof Text != 'undefined')))
{Text.prototype.domina$DomContent$ = true;
Text.prototype.domina$DomContent$nodes$arity$1 = (function (content){
return cljs.core.PersistentVector.fromArray([content], true);
});
Text.prototype.domina$DomContent$single_node$arity$1 = (function (content){
return content;
});
} else
{}
String.prototype.enfocus$core$ISelector$ = true;
String.prototype.enfocus$core$ISelector$select$arity$1 = (function (this$){
return enfocus.core.select.call(null,this$,document,"");
});
String.prototype.enfocus$core$ISelector$select$arity$2 = (function (this$,root){
return enfocus.core.select.call(null,this$,root,"");
});
String.prototype.enfocus$core$ISelector$select$arity$3 = (function (this$,root,id_mask){
return enfocus.core.css_select.call(null,id_mask,root,cljs.core.PersistentVector.fromArray([this$], true));
});
cljs.core.PersistentVector.prototype.enfocus$core$ISelector$ = true;
cljs.core.PersistentVector.prototype.enfocus$core$ISelector$select$arity$1 = (function (this$){
return enfocus.core.select.call(null,this$,document,"");
});
cljs.core.PersistentVector.prototype.enfocus$core$ISelector$select$arity$2 = (function (this$,root){
return enfocus.core.select.call(null,this$,root,"");
});
cljs.core.PersistentVector.prototype.enfocus$core$ISelector$select$arity$3 = (function (this$,root,id_mask){
return enfocus.core.css_select.call(null,id_mask,root,this$);
});
(enfocus.core.ISelector["function"] = true);
(enfocus.core.select["function"] = (function (this$){
return enfocus.core.select.call(null,this$,document,"");
}));
(enfocus.core.select["function"] = (function (this$,root){
return enfocus.core.select.call(null,this$,root,"");
}));
(enfocus.core.select["function"] = (function (this$,root,id_mask){
return this$.call(null,root,id_mask);
}));
(enfocus.core.ITransform["function"] = true);
(enfocus.core.apply_transform["function"] = (function (trans,nodes){
return cljs.core.doall.call(null,cljs.core.map.call(null,trans,enfocus.core.nodes__GT_coll.call(null,nodes)));
}));
(enfocus.core.apply_transform["function"] = (function (trans,nodes,chain){
var pnod_col = enfocus.core.nodes__GT_coll.call(null,nodes);
var val = cljs.core.doall.call(null,cljs.core.map.call(null,trans,pnod_col));
if(cljs.core.truth_(chain))
{return enfocus.core.apply_transform.call(null,chain,nodes);
} else
{return val;
}
}));
