goog.provide('domina');
goog.require('cljs.core');
goog.require('domina.support');
goog.require('goog.dom.classes');
goog.require('goog.events');
goog.require('goog.dom.xml');
goog.require('goog.dom.forms');
goog.require('goog.dom');
goog.require('goog.string');
goog.require('clojure.string');
goog.require('goog.style');
goog.require('cljs.core');
domina.re_html = /<|&#?\w+;/;
domina.re_leading_whitespace = /^\s+/;
domina.re_xhtml_tag = /<(?!area|br|col|embed|hr|img|input|link|meta|param)(([\w:]+)[^>]*)\/>/i;
domina.re_tag_name = /<([\w:]+)/;
domina.re_no_inner_html = /<(?:script|style)/i;
domina.re_tbody = /<tbody/i;
var opt_wrapper_4706 = cljs.core.PersistentVector.fromArray([1,"<select multiple='multiple'>","</select>"], true);
var table_section_wrapper_4707 = cljs.core.PersistentVector.fromArray([1,"<table>","</table>"], true);
var cell_wrapper_4708 = cljs.core.PersistentVector.fromArray([3,"<table><tbody><tr>","</tr></tbody></table>"], true);
domina.wrap_map = cljs.core.PersistentHashMap.fromArrays(["col","\uFDD0:default","tfoot","caption","optgroup","legend","area","td","thead","th","option","tbody","tr","colgroup"],[cljs.core.PersistentVector.fromArray([2,"<table><tbody></tbody><colgroup>","</colgroup></table>"], true),cljs.core.PersistentVector.fromArray([0,"",""], true),table_section_wrapper_4707,table_section_wrapper_4707,opt_wrapper_4706,cljs.core.PersistentVector.fromArray([1,"<fieldset>","</fieldset>"], true),cljs.core.PersistentVector.fromArray([1,"<map>","</map>"], true),cell_wrapper_4708,table_section_wrapper_4707,cell_wrapper_4708,opt_wrapper_4706,table_section_wrapper_4707,cljs.core.PersistentVector.fromArray([2,"<table><tbody>","</tbody></table>"], true),table_section_wrapper_4707]);
domina.remove_extraneous_tbody_BANG_ = (function remove_extraneous_tbody_BANG_(div,html,tag_name,start_wrap){
var no_tbody_QMARK_ = cljs.core.not.call(null,cljs.core.re_find.call(null,domina.re_tbody,html));
var tbody = (((function (){var and__3941__auto__ = cljs.core._EQ_.call(null,tag_name,"table");
if(and__3941__auto__)
{return no_tbody_QMARK_;
} else
{return and__3941__auto__;
}
})())?(function (){var and__3941__auto__ = div.firstChild;
if(cljs.core.truth_(and__3941__auto__))
{return div.firstChild.childNodes;
} else
{return and__3941__auto__;
}
})():(((function (){var and__3941__auto__ = cljs.core._EQ_.call(null,start_wrap,"<table>");
if(and__3941__auto__)
{return no_tbody_QMARK_;
} else
{return and__3941__auto__;
}
})())?divchildNodes:cljs.core.PersistentVector.EMPTY));
var seq__4713 = cljs.core.seq.call(null,tbody);
var chunk__4714 = null;
var count__4715 = 0;
var i__4716 = 0;
while(true){
if((i__4716 < count__4715))
{var child = cljs.core._nth.call(null,chunk__4714,i__4716);
if((function (){var and__3941__auto__ = cljs.core._EQ_.call(null,child.nodeName,"tbody");
if(and__3941__auto__)
{return cljs.core._EQ_.call(null,child.childNodes.length,0);
} else
{return and__3941__auto__;
}
})())
{child.parentNode.removeChild(child);
} else
{}
{
var G__4717 = seq__4713;
var G__4718 = chunk__4714;
var G__4719 = count__4715;
var G__4720 = (i__4716 + 1);
seq__4713 = G__4717;
chunk__4714 = G__4718;
count__4715 = G__4719;
i__4716 = G__4720;
continue;
}
} else
{var temp__4092__auto__ = cljs.core.seq.call(null,seq__4713);
if(temp__4092__auto__)
{var seq__4713__$1 = temp__4092__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__4713__$1))
{var c__3186__auto__ = cljs.core.chunk_first.call(null,seq__4713__$1);
{
var G__4721 = cljs.core.chunk_rest.call(null,seq__4713__$1);
var G__4722 = c__3186__auto__;
var G__4723 = cljs.core.count.call(null,c__3186__auto__);
var G__4724 = 0;
seq__4713 = G__4721;
chunk__4714 = G__4722;
count__4715 = G__4723;
i__4716 = G__4724;
continue;
}
} else
{var child = cljs.core.first.call(null,seq__4713__$1);
if((function (){var and__3941__auto__ = cljs.core._EQ_.call(null,child.nodeName,"tbody");
if(and__3941__auto__)
{return cljs.core._EQ_.call(null,child.childNodes.length,0);
} else
{return and__3941__auto__;
}
})())
{child.parentNode.removeChild(child);
} else
{}
{
var G__4725 = cljs.core.next.call(null,seq__4713__$1);
var G__4726 = null;
var G__4727 = 0;
var G__4728 = 0;
seq__4713 = G__4725;
chunk__4714 = G__4726;
count__4715 = G__4727;
i__4716 = G__4728;
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
domina.restore_leading_whitespace_BANG_ = (function restore_leading_whitespace_BANG_(div,html){
return div.insertBefore(document.createTextNode(cljs.core.first.call(null,cljs.core.re_find.call(null,domina.re_leading_whitespace,html))),div.firstChild);
});
/**
* takes an string of html and returns a NodeList of dom fragments
*/
domina.html_to_dom = (function html_to_dom(html){
var html__$1 = clojure.string.replace.call(null,html,domina.re_xhtml_tag,"<$1></$2>");
var tag_name = [cljs.core.str(cljs.core.second.call(null,cljs.core.re_find.call(null,domina.re_tag_name,html__$1)))].join('').toLowerCase();
var vec__4730 = cljs.core.get.call(null,domina.wrap_map,tag_name,(new cljs.core.Keyword("\uFDD0:default")).call(null,domina.wrap_map));
var depth = cljs.core.nth.call(null,vec__4730,0,null);
var start_wrap = cljs.core.nth.call(null,vec__4730,1,null);
var end_wrap = cljs.core.nth.call(null,vec__4730,2,null);
var div = (function (){var wrapper = (function (){var div = document.createElement("div");
div.innerHTML = [cljs.core.str(start_wrap),cljs.core.str(html__$1),cljs.core.str(end_wrap)].join('');
return div;
})();
var level = depth;
while(true){
if((level > 0))
{{
var G__4731 = wrapper.lastChild;
var G__4732 = (level - 1);
wrapper = G__4731;
level = G__4732;
continue;
}
} else
{return wrapper;
}
break;
}
})();
if(cljs.core.truth_(domina.support.extraneous_tbody_QMARK_))
{domina.remove_extraneous_tbody_BANG_.call(null,div,html__$1,tag_name,start_wrap);
} else
{}
if(cljs.core.truth_((function (){var and__3941__auto__ = cljs.core.not.call(null,domina.support.leading_whitespace_QMARK_);
if(and__3941__auto__)
{return cljs.core.re_find.call(null,domina.re_leading_whitespace,html__$1);
} else
{return and__3941__auto__;
}
})()))
{domina.restore_leading_whitespace_BANG_.call(null,div,html__$1);
} else
{}
return div.childNodes;
});
domina.string_to_dom = (function string_to_dom(s){
if(cljs.core.truth_(cljs.core.re_find.call(null,domina.re_html,s)))
{return domina.html_to_dom.call(null,s);
} else
{return document.createTextNode(s);
}
});
domina.DomContent = {};
domina.nodes = (function nodes(content){
if((function (){var and__3941__auto__ = content;
if(and__3941__auto__)
{return content.domina$DomContent$nodes$arity$1;
} else
{return and__3941__auto__;
}
})())
{return content.domina$DomContent$nodes$arity$1(content);
} else
{var x__3055__auto__ = (((content == null))?null:content);
return (function (){var or__3943__auto__ = (domina.nodes[goog.typeOf(x__3055__auto__)]);
if(or__3943__auto__)
{return or__3943__auto__;
} else
{var or__3943__auto____$1 = (domina.nodes["_"]);
if(or__3943__auto____$1)
{return or__3943__auto____$1;
} else
{throw cljs.core.missing_protocol.call(null,"DomContent.nodes",content);
}
}
})().call(null,content);
}
});
domina.single_node = (function single_node(nodeseq){
if((function (){var and__3941__auto__ = nodeseq;
if(and__3941__auto__)
{return nodeseq.domina$DomContent$single_node$arity$1;
} else
{return and__3941__auto__;
}
})())
{return nodeseq.domina$DomContent$single_node$arity$1(nodeseq);
} else
{var x__3055__auto__ = (((nodeseq == null))?null:nodeseq);
return (function (){var or__3943__auto__ = (domina.single_node[goog.typeOf(x__3055__auto__)]);
if(or__3943__auto__)
{return or__3943__auto__;
} else
{var or__3943__auto____$1 = (domina.single_node["_"]);
if(or__3943__auto____$1)
{return or__3943__auto____$1;
} else
{throw cljs.core.missing_protocol.call(null,"DomContent.single-node",nodeseq);
}
}
})().call(null,nodeseq);
}
});
domina._STAR_debug_STAR_ = true;
/**
* @param {...*} var_args
*/
domina.log_debug = (function() { 
var log_debug__delegate = function (mesg){
if(cljs.core.truth_((function (){var and__3941__auto__ = domina._STAR_debug_STAR_;
if(cljs.core.truth_(and__3941__auto__))
{return !(cljs.core._EQ_.call(null,window.console,undefined));
} else
{return and__3941__auto__;
}
})()))
{return console.log(cljs.core.apply.call(null,cljs.core.str,mesg));
} else
{return null;
}
};
var log_debug = function (var_args){
var mesg = null;
if (arguments.length > 0) {
  mesg = cljs.core.array_seq(Array.prototype.slice.call(arguments, 0),0);
} 
return log_debug__delegate.call(this, mesg);
};
log_debug.cljs$lang$maxFixedArity = 0;
log_debug.cljs$lang$applyTo = (function (arglist__4733){
var mesg = cljs.core.seq(arglist__4733);
return log_debug__delegate(mesg);
});
log_debug.cljs$core$IFn$_invoke$arity$variadic = log_debug__delegate;
return log_debug;
})()
;
/**
* @param {...*} var_args
*/
domina.log = (function() { 
var log__delegate = function (mesg){
if(cljs.core.truth_(window.console))
{return console.log(cljs.core.apply.call(null,cljs.core.str,mesg));
} else
{return null;
}
};
var log = function (var_args){
var mesg = null;
if (arguments.length > 0) {
  mesg = cljs.core.array_seq(Array.prototype.slice.call(arguments, 0),0);
} 
return log__delegate.call(this, mesg);
};
log.cljs$lang$maxFixedArity = 0;
log.cljs$lang$applyTo = (function (arglist__4734){
var mesg = cljs.core.seq(arglist__4734);
return log__delegate(mesg);
});
log.cljs$core$IFn$_invoke$arity$variadic = log__delegate;
return log;
})()
;
/**
* Returns content containing a single node by looking up the given ID
*/
domina.by_id = (function by_id(id){
return goog.dom.getElement(cljs.core.name.call(null,id));
});
/**
* Returns content containing nodes which have the specified CSS class.
*/
domina.by_class = (function by_class(class_name){
if((void 0 === domina.t4738))
{goog.provide('domina.t4738');

/**
* @constructor
*/
domina.t4738 = (function (class_name,by_class,meta4739){
this.class_name = class_name;
this.by_class = by_class;
this.meta4739 = meta4739;
this.cljs$lang$protocol_mask$partition1$ = 0;
this.cljs$lang$protocol_mask$partition0$ = 393216;
})
domina.t4738.cljs$lang$type = true;
domina.t4738.cljs$lang$ctorStr = "domina/t4738";
domina.t4738.cljs$lang$ctorPrWriter = (function (this__2996__auto__,writer__2997__auto__,opt__2998__auto__){
return cljs.core._write.call(null,writer__2997__auto__,"domina/t4738");
});
domina.t4738.prototype.domina$DomContent$ = true;
domina.t4738.prototype.domina$DomContent$nodes$arity$1 = (function (_){
var self__ = this;
return domina.normalize_seq.call(null,goog.dom.getElementsByClass(cljs.core.name.call(null,self__.class_name)));
});
domina.t4738.prototype.domina$DomContent$single_node$arity$1 = (function (_){
var self__ = this;
return domina.normalize_seq.call(null,goog.dom.getElementByClass(cljs.core.name.call(null,self__.class_name)));
});
domina.t4738.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_4740){
var self__ = this;
return self__.meta4739;
});
domina.t4738.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_4740,meta4739__$1){
var self__ = this;
return (new domina.t4738(self__.class_name,self__.by_class,meta4739__$1));
});
domina.__GT_t4738 = (function __GT_t4738(class_name__$1,by_class__$1,meta4739){
return (new domina.t4738(class_name__$1,by_class__$1,meta4739));
});
} else
{}
return (new domina.t4738(class_name,by_class,null));
});
/**
* Gets all the child nodes of the elements in a content. Same as (xpath content '*') but more efficient.
*/
domina.children = (function children(content){
return cljs.core.doall.call(null,cljs.core.mapcat.call(null,goog.dom.getChildren,domina.nodes.call(null,content)));
});
/**
* Returns the deepest common ancestor of the argument contents (which are presumed to be single nodes), or nil if they are from different documents.
* @param {...*} var_args
*/
domina.common_ancestor = (function() { 
var common_ancestor__delegate = function (contents){
return cljs.core.apply.call(null,goog.dom.findCommonAncestor,cljs.core.map.call(null,domina.single_node,contents));
};
var common_ancestor = function (var_args){
var contents = null;
if (arguments.length > 0) {
  contents = cljs.core.array_seq(Array.prototype.slice.call(arguments, 0),0);
} 
return common_ancestor__delegate.call(this, contents);
};
common_ancestor.cljs$lang$maxFixedArity = 0;
common_ancestor.cljs$lang$applyTo = (function (arglist__4741){
var contents = cljs.core.seq(arglist__4741);
return common_ancestor__delegate(contents);
});
common_ancestor.cljs$core$IFn$_invoke$arity$variadic = common_ancestor__delegate;
return common_ancestor;
})()
;
/**
* Returns true if the first argument is an ancestor of the second argument. Presumes both arguments are single-node contents.
*/
domina.ancestor_QMARK_ = (function ancestor_QMARK_(ancestor_content,descendant_content){
return cljs.core._EQ_.call(null,domina.common_ancestor.call(null,ancestor_content,descendant_content),domina.single_node.call(null,ancestor_content));
});
/**
* Returns a deep clone of content.
*/
domina.clone = (function clone(content){
return cljs.core.map.call(null,(function (p1__4742_SHARP_){
return p1__4742_SHARP_.cloneNode(true);
}),domina.nodes.call(null,content));
});
/**
* Given a parent and child contents, appends each of the children to all of the parents. If there is more than one node in the parent content, clones the children for the additional parents. Returns the parent content.
*/
domina.append_BANG_ = (function append_BANG_(parent_content,child_content){
domina.apply_with_cloning.call(null,goog.dom.appendChild,parent_content,child_content);
return parent_content;
});
/**
* Given a parent and child contents, appends each of the children to all of the parents at the specified index. If there is more than one node in the parent content, clones the children for the additional parents. Returns the parent content.
*/
domina.insert_BANG_ = (function insert_BANG_(parent_content,child_content,idx){
domina.apply_with_cloning.call(null,(function (p1__4743_SHARP_,p2__4744_SHARP_){
return goog.dom.insertChildAt(p1__4743_SHARP_,p2__4744_SHARP_,idx);
}),parent_content,child_content);
return parent_content;
});
/**
* Given a parent and child contents, prepends each of the children to all of the parents. If there is more than one node in the parent content, clones the children for the additional parents. Returns the parent content.
*/
domina.prepend_BANG_ = (function prepend_BANG_(parent_content,child_content){
domina.insert_BANG_.call(null,parent_content,child_content,0);
return parent_content;
});
/**
* Given a content and some new content, inserts the new content immediately before the reference content. If there is more than one node in the reference content, clones the new content for each one.
*/
domina.insert_before_BANG_ = (function insert_before_BANG_(content,new_content){
domina.apply_with_cloning.call(null,(function (p1__4746_SHARP_,p2__4745_SHARP_){
return goog.dom.insertSiblingBefore(p2__4745_SHARP_,p1__4746_SHARP_);
}),content,new_content);
return content;
});
/**
* Given a content and some new content, inserts the new content immediately after the reference content. If there is more than one node in the reference content, clones the new content for each one.
*/
domina.insert_after_BANG_ = (function insert_after_BANG_(content,new_content){
domina.apply_with_cloning.call(null,(function (p1__4748_SHARP_,p2__4747_SHARP_){
return goog.dom.insertSiblingAfter(p2__4747_SHARP_,p1__4748_SHARP_);
}),content,new_content);
return content;
});
/**
* Given some old content and some new content, replaces the old content with new content. If there are multiple nodes in the old content, replaces each of them and clones the new content as necessary.
*/
domina.swap_content_BANG_ = (function swap_content_BANG_(old_content,new_content){
domina.apply_with_cloning.call(null,(function (p1__4750_SHARP_,p2__4749_SHARP_){
return goog.dom.replaceNode(p2__4749_SHARP_,p1__4750_SHARP_);
}),old_content,new_content);
return old_content;
});
/**
* Removes all the nodes in a content from the DOM and returns them.
*/
domina.detach_BANG_ = (function detach_BANG_(content){
return cljs.core.doall.call(null,cljs.core.map.call(null,goog.dom.removeNode,domina.nodes.call(null,content)));
});
/**
* Removes all the nodes in a content from the DOM. Returns nil.
*/
domina.destroy_BANG_ = (function destroy_BANG_(content){
return cljs.core.dorun.call(null,cljs.core.map.call(null,goog.dom.removeNode,domina.nodes.call(null,content)));
});
/**
* Removes all the child nodes in a content from the DOM. Returns the original content.
*/
domina.destroy_children_BANG_ = (function destroy_children_BANG_(content){
cljs.core.dorun.call(null,cljs.core.map.call(null,goog.dom.removeChildren,domina.nodes.call(null,content)));
return content;
});
/**
* Gets the value of a CSS property. Assumes content will be a single node. Name may be a string or keyword. Returns nil if there is no value set for the style.
*/
domina.style = (function style(content,name){
var s = goog.style.getStyle(domina.single_node.call(null,content),cljs.core.name.call(null,name));
if(cljs.core.truth_(clojure.string.blank_QMARK_.call(null,s)))
{return null;
} else
{return s;
}
});
/**
* Gets the value of an HTML attribute. Assumes content will be a single node. Name may be a stirng or keyword. Returns nil if there is no value set for the style.
*/
domina.attr = (function attr(content,name){
return domina.single_node.call(null,content).getAttribute(cljs.core.name.call(null,name));
});
/**
* Sets the value of a CSS property for each node in the content. Name may be a string or keyword. Value will be cast to a string, multiple values wil be concatenated.
* @param {...*} var_args
*/
domina.set_style_BANG_ = (function() { 
var set_style_BANG___delegate = function (content,name,value){
var seq__4755_4759 = cljs.core.seq.call(null,domina.nodes.call(null,content));
var chunk__4756_4760 = null;
var count__4757_4761 = 0;
var i__4758_4762 = 0;
while(true){
if((i__4758_4762 < count__4757_4761))
{var n_4763 = cljs.core._nth.call(null,chunk__4756_4760,i__4758_4762);
goog.style.setStyle(n_4763,cljs.core.name.call(null,name),cljs.core.apply.call(null,cljs.core.str,value));
{
var G__4764 = seq__4755_4759;
var G__4765 = chunk__4756_4760;
var G__4766 = count__4757_4761;
var G__4767 = (i__4758_4762 + 1);
seq__4755_4759 = G__4764;
chunk__4756_4760 = G__4765;
count__4757_4761 = G__4766;
i__4758_4762 = G__4767;
continue;
}
} else
{var temp__4092__auto___4768 = cljs.core.seq.call(null,seq__4755_4759);
if(temp__4092__auto___4768)
{var seq__4755_4769__$1 = temp__4092__auto___4768;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__4755_4769__$1))
{var c__3186__auto___4770 = cljs.core.chunk_first.call(null,seq__4755_4769__$1);
{
var G__4771 = cljs.core.chunk_rest.call(null,seq__4755_4769__$1);
var G__4772 = c__3186__auto___4770;
var G__4773 = cljs.core.count.call(null,c__3186__auto___4770);
var G__4774 = 0;
seq__4755_4759 = G__4771;
chunk__4756_4760 = G__4772;
count__4757_4761 = G__4773;
i__4758_4762 = G__4774;
continue;
}
} else
{var n_4775 = cljs.core.first.call(null,seq__4755_4769__$1);
goog.style.setStyle(n_4775,cljs.core.name.call(null,name),cljs.core.apply.call(null,cljs.core.str,value));
{
var G__4776 = cljs.core.next.call(null,seq__4755_4769__$1);
var G__4777 = null;
var G__4778 = 0;
var G__4779 = 0;
seq__4755_4759 = G__4776;
chunk__4756_4760 = G__4777;
count__4757_4761 = G__4778;
i__4758_4762 = G__4779;
continue;
}
}
} else
{}
}
break;
}
return content;
};
var set_style_BANG_ = function (content,name,var_args){
var value = null;
if (arguments.length > 2) {
  value = cljs.core.array_seq(Array.prototype.slice.call(arguments, 2),0);
} 
return set_style_BANG___delegate.call(this, content, name, value);
};
set_style_BANG_.cljs$lang$maxFixedArity = 2;
set_style_BANG_.cljs$lang$applyTo = (function (arglist__4780){
var content = cljs.core.first(arglist__4780);
arglist__4780 = cljs.core.next(arglist__4780);
var name = cljs.core.first(arglist__4780);
var value = cljs.core.rest(arglist__4780);
return set_style_BANG___delegate(content, name, value);
});
set_style_BANG_.cljs$core$IFn$_invoke$arity$variadic = set_style_BANG___delegate;
return set_style_BANG_;
})()
;
/**
* Sets the value of an HTML property for each node in the content. Name may be a string or keyword. Value will be cast to a string, multiple values wil be concatenated.
* @param {...*} var_args
*/
domina.set_attr_BANG_ = (function() { 
var set_attr_BANG___delegate = function (content,name,value){
var seq__4785_4789 = cljs.core.seq.call(null,domina.nodes.call(null,content));
var chunk__4786_4790 = null;
var count__4787_4791 = 0;
var i__4788_4792 = 0;
while(true){
if((i__4788_4792 < count__4787_4791))
{var n_4793 = cljs.core._nth.call(null,chunk__4786_4790,i__4788_4792);
n_4793.setAttribute(cljs.core.name.call(null,name),cljs.core.apply.call(null,cljs.core.str,value));
{
var G__4794 = seq__4785_4789;
var G__4795 = chunk__4786_4790;
var G__4796 = count__4787_4791;
var G__4797 = (i__4788_4792 + 1);
seq__4785_4789 = G__4794;
chunk__4786_4790 = G__4795;
count__4787_4791 = G__4796;
i__4788_4792 = G__4797;
continue;
}
} else
{var temp__4092__auto___4798 = cljs.core.seq.call(null,seq__4785_4789);
if(temp__4092__auto___4798)
{var seq__4785_4799__$1 = temp__4092__auto___4798;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__4785_4799__$1))
{var c__3186__auto___4800 = cljs.core.chunk_first.call(null,seq__4785_4799__$1);
{
var G__4801 = cljs.core.chunk_rest.call(null,seq__4785_4799__$1);
var G__4802 = c__3186__auto___4800;
var G__4803 = cljs.core.count.call(null,c__3186__auto___4800);
var G__4804 = 0;
seq__4785_4789 = G__4801;
chunk__4786_4790 = G__4802;
count__4787_4791 = G__4803;
i__4788_4792 = G__4804;
continue;
}
} else
{var n_4805 = cljs.core.first.call(null,seq__4785_4799__$1);
n_4805.setAttribute(cljs.core.name.call(null,name),cljs.core.apply.call(null,cljs.core.str,value));
{
var G__4806 = cljs.core.next.call(null,seq__4785_4799__$1);
var G__4807 = null;
var G__4808 = 0;
var G__4809 = 0;
seq__4785_4789 = G__4806;
chunk__4786_4790 = G__4807;
count__4787_4791 = G__4808;
i__4788_4792 = G__4809;
continue;
}
}
} else
{}
}
break;
}
return content;
};
var set_attr_BANG_ = function (content,name,var_args){
var value = null;
if (arguments.length > 2) {
  value = cljs.core.array_seq(Array.prototype.slice.call(arguments, 2),0);
} 
return set_attr_BANG___delegate.call(this, content, name, value);
};
set_attr_BANG_.cljs$lang$maxFixedArity = 2;
set_attr_BANG_.cljs$lang$applyTo = (function (arglist__4810){
var content = cljs.core.first(arglist__4810);
arglist__4810 = cljs.core.next(arglist__4810);
var name = cljs.core.first(arglist__4810);
var value = cljs.core.rest(arglist__4810);
return set_attr_BANG___delegate(content, name, value);
});
set_attr_BANG_.cljs$core$IFn$_invoke$arity$variadic = set_attr_BANG___delegate;
return set_attr_BANG_;
})()
;
/**
* Removes the specified HTML property for each node in the content. Name may be a string or keyword.
*/
domina.remove_attr_BANG_ = (function remove_attr_BANG_(content,name){
var seq__4815_4819 = cljs.core.seq.call(null,domina.nodes.call(null,content));
var chunk__4816_4820 = null;
var count__4817_4821 = 0;
var i__4818_4822 = 0;
while(true){
if((i__4818_4822 < count__4817_4821))
{var n_4823 = cljs.core._nth.call(null,chunk__4816_4820,i__4818_4822);
n_4823.removeAttribute(cljs.core.name.call(null,name));
{
var G__4824 = seq__4815_4819;
var G__4825 = chunk__4816_4820;
var G__4826 = count__4817_4821;
var G__4827 = (i__4818_4822 + 1);
seq__4815_4819 = G__4824;
chunk__4816_4820 = G__4825;
count__4817_4821 = G__4826;
i__4818_4822 = G__4827;
continue;
}
} else
{var temp__4092__auto___4828 = cljs.core.seq.call(null,seq__4815_4819);
if(temp__4092__auto___4828)
{var seq__4815_4829__$1 = temp__4092__auto___4828;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__4815_4829__$1))
{var c__3186__auto___4830 = cljs.core.chunk_first.call(null,seq__4815_4829__$1);
{
var G__4831 = cljs.core.chunk_rest.call(null,seq__4815_4829__$1);
var G__4832 = c__3186__auto___4830;
var G__4833 = cljs.core.count.call(null,c__3186__auto___4830);
var G__4834 = 0;
seq__4815_4819 = G__4831;
chunk__4816_4820 = G__4832;
count__4817_4821 = G__4833;
i__4818_4822 = G__4834;
continue;
}
} else
{var n_4835 = cljs.core.first.call(null,seq__4815_4829__$1);
n_4835.removeAttribute(cljs.core.name.call(null,name));
{
var G__4836 = cljs.core.next.call(null,seq__4815_4829__$1);
var G__4837 = null;
var G__4838 = 0;
var G__4839 = 0;
seq__4815_4819 = G__4836;
chunk__4816_4820 = G__4837;
count__4817_4821 = G__4838;
i__4818_4822 = G__4839;
continue;
}
}
} else
{}
}
break;
}
return content;
});
/**
* Parses a CSS style string and returns the properties as a map.
*/
domina.parse_style_attributes = (function parse_style_attributes(style){
return cljs.core.reduce.call(null,(function (acc,pair){
var vec__4841 = pair.split(/\s*:\s*/);
var k = cljs.core.nth.call(null,vec__4841,0,null);
var v = cljs.core.nth.call(null,vec__4841,1,null);
if(cljs.core.truth_((function (){var and__3941__auto__ = k;
if(cljs.core.truth_(and__3941__auto__))
{return v;
} else
{return and__3941__auto__;
}
})()))
{return cljs.core.assoc.call(null,acc,cljs.core.keyword.call(null,k.toLowerCase()),v);
} else
{return acc;
}
}),cljs.core.PersistentArrayMap.EMPTY,style.split(/\s*;\s*/));
});
/**
* Returns a map of the CSS styles/values. Assumes content will be a single node. Style names are returned as keywords.
*/
domina.styles = (function styles(content){
var style = domina.attr.call(null,content,"style");
if(cljs.core.string_QMARK_.call(null,style))
{return domina.parse_style_attributes.call(null,style);
} else
{if(cljs.core.truth_(style.cssText))
{return domina.parse_style_attributes.call(null,style.cssText);
} else
{return null;
}
}
});
/**
* Returns a map of the HTML attributes/values. Assumes content will be a single node. Attribute names are returned as keywords.
*/
domina.attrs = (function attrs(content){
var node = domina.single_node.call(null,content);
var attrs__$1 = node.attributes;
return cljs.core.reduce.call(null,cljs.core.conj,cljs.core.filter.call(null,cljs.core.complement.call(null,cljs.core.nil_QMARK_),cljs.core.map.call(null,(function (p1__4842_SHARP_){
var attr = attrs__$1.item(p1__4842_SHARP_);
var value = attr.nodeValue;
if((function (){var and__3941__auto__ = cljs.core.not_EQ_.call(null,null,value);
if(and__3941__auto__)
{return cljs.core.not_EQ_.call(null,"",value);
} else
{return and__3941__auto__;
}
})())
{return cljs.core.PersistentArrayMap.fromArray([cljs.core.keyword.call(null,attr.nodeName.toLowerCase()),attr.nodeValue], true);
} else
{return null;
}
}),cljs.core.range.call(null,attrs__$1.length))));
});
/**
* Sets the specified CSS styles for each node in the content, given a map of names and values. Style names may be keywords or strings.
*/
domina.set_styles_BANG_ = (function set_styles_BANG_(content,styles){
var seq__4849_4855 = cljs.core.seq.call(null,styles);
var chunk__4850_4856 = null;
var count__4851_4857 = 0;
var i__4852_4858 = 0;
while(true){
if((i__4852_4858 < count__4851_4857))
{var vec__4853_4859 = cljs.core._nth.call(null,chunk__4850_4856,i__4852_4858);
var name_4860 = cljs.core.nth.call(null,vec__4853_4859,0,null);
var value_4861 = cljs.core.nth.call(null,vec__4853_4859,1,null);
domina.set_style_BANG_.call(null,content,name_4860,value_4861);
{
var G__4862 = seq__4849_4855;
var G__4863 = chunk__4850_4856;
var G__4864 = count__4851_4857;
var G__4865 = (i__4852_4858 + 1);
seq__4849_4855 = G__4862;
chunk__4850_4856 = G__4863;
count__4851_4857 = G__4864;
i__4852_4858 = G__4865;
continue;
}
} else
{var temp__4092__auto___4866 = cljs.core.seq.call(null,seq__4849_4855);
if(temp__4092__auto___4866)
{var seq__4849_4867__$1 = temp__4092__auto___4866;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__4849_4867__$1))
{var c__3186__auto___4868 = cljs.core.chunk_first.call(null,seq__4849_4867__$1);
{
var G__4869 = cljs.core.chunk_rest.call(null,seq__4849_4867__$1);
var G__4870 = c__3186__auto___4868;
var G__4871 = cljs.core.count.call(null,c__3186__auto___4868);
var G__4872 = 0;
seq__4849_4855 = G__4869;
chunk__4850_4856 = G__4870;
count__4851_4857 = G__4871;
i__4852_4858 = G__4872;
continue;
}
} else
{var vec__4854_4873 = cljs.core.first.call(null,seq__4849_4867__$1);
var name_4874 = cljs.core.nth.call(null,vec__4854_4873,0,null);
var value_4875 = cljs.core.nth.call(null,vec__4854_4873,1,null);
domina.set_style_BANG_.call(null,content,name_4874,value_4875);
{
var G__4876 = cljs.core.next.call(null,seq__4849_4867__$1);
var G__4877 = null;
var G__4878 = 0;
var G__4879 = 0;
seq__4849_4855 = G__4876;
chunk__4850_4856 = G__4877;
count__4851_4857 = G__4878;
i__4852_4858 = G__4879;
continue;
}
}
} else
{}
}
break;
}
return content;
});
/**
* Sets the specified CSS styles fpr each node in the content, given a map of names and values. Style names may be keywords or strings.
*/
domina.set_attrs_BANG_ = (function set_attrs_BANG_(content,attrs){
var seq__4886_4892 = cljs.core.seq.call(null,attrs);
var chunk__4887_4893 = null;
var count__4888_4894 = 0;
var i__4889_4895 = 0;
while(true){
if((i__4889_4895 < count__4888_4894))
{var vec__4890_4896 = cljs.core._nth.call(null,chunk__4887_4893,i__4889_4895);
var name_4897 = cljs.core.nth.call(null,vec__4890_4896,0,null);
var value_4898 = cljs.core.nth.call(null,vec__4890_4896,1,null);
domina.set_attr_BANG_.call(null,content,name_4897,value_4898);
{
var G__4899 = seq__4886_4892;
var G__4900 = chunk__4887_4893;
var G__4901 = count__4888_4894;
var G__4902 = (i__4889_4895 + 1);
seq__4886_4892 = G__4899;
chunk__4887_4893 = G__4900;
count__4888_4894 = G__4901;
i__4889_4895 = G__4902;
continue;
}
} else
{var temp__4092__auto___4903 = cljs.core.seq.call(null,seq__4886_4892);
if(temp__4092__auto___4903)
{var seq__4886_4904__$1 = temp__4092__auto___4903;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__4886_4904__$1))
{var c__3186__auto___4905 = cljs.core.chunk_first.call(null,seq__4886_4904__$1);
{
var G__4906 = cljs.core.chunk_rest.call(null,seq__4886_4904__$1);
var G__4907 = c__3186__auto___4905;
var G__4908 = cljs.core.count.call(null,c__3186__auto___4905);
var G__4909 = 0;
seq__4886_4892 = G__4906;
chunk__4887_4893 = G__4907;
count__4888_4894 = G__4908;
i__4889_4895 = G__4909;
continue;
}
} else
{var vec__4891_4910 = cljs.core.first.call(null,seq__4886_4904__$1);
var name_4911 = cljs.core.nth.call(null,vec__4891_4910,0,null);
var value_4912 = cljs.core.nth.call(null,vec__4891_4910,1,null);
domina.set_attr_BANG_.call(null,content,name_4911,value_4912);
{
var G__4913 = cljs.core.next.call(null,seq__4886_4904__$1);
var G__4914 = null;
var G__4915 = 0;
var G__4916 = 0;
seq__4886_4892 = G__4913;
chunk__4887_4893 = G__4914;
count__4888_4894 = G__4915;
i__4889_4895 = G__4916;
continue;
}
}
} else
{}
}
break;
}
return content;
});
/**
* Returns true if the node has the specified CSS class. Assumes content is a single node.
*/
domina.has_class_QMARK_ = (function has_class_QMARK_(content,class$){
return goog.dom.classes.has(domina.single_node.call(null,content),class$);
});
/**
* Adds the specified CSS class to each node in the content.
*/
domina.add_class_BANG_ = (function add_class_BANG_(content,class$){
var seq__4921_4925 = cljs.core.seq.call(null,domina.nodes.call(null,content));
var chunk__4922_4926 = null;
var count__4923_4927 = 0;
var i__4924_4928 = 0;
while(true){
if((i__4924_4928 < count__4923_4927))
{var node_4929 = cljs.core._nth.call(null,chunk__4922_4926,i__4924_4928);
goog.dom.classes.add(node_4929,class$);
{
var G__4930 = seq__4921_4925;
var G__4931 = chunk__4922_4926;
var G__4932 = count__4923_4927;
var G__4933 = (i__4924_4928 + 1);
seq__4921_4925 = G__4930;
chunk__4922_4926 = G__4931;
count__4923_4927 = G__4932;
i__4924_4928 = G__4933;
continue;
}
} else
{var temp__4092__auto___4934 = cljs.core.seq.call(null,seq__4921_4925);
if(temp__4092__auto___4934)
{var seq__4921_4935__$1 = temp__4092__auto___4934;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__4921_4935__$1))
{var c__3186__auto___4936 = cljs.core.chunk_first.call(null,seq__4921_4935__$1);
{
var G__4937 = cljs.core.chunk_rest.call(null,seq__4921_4935__$1);
var G__4938 = c__3186__auto___4936;
var G__4939 = cljs.core.count.call(null,c__3186__auto___4936);
var G__4940 = 0;
seq__4921_4925 = G__4937;
chunk__4922_4926 = G__4938;
count__4923_4927 = G__4939;
i__4924_4928 = G__4940;
continue;
}
} else
{var node_4941 = cljs.core.first.call(null,seq__4921_4935__$1);
goog.dom.classes.add(node_4941,class$);
{
var G__4942 = cljs.core.next.call(null,seq__4921_4935__$1);
var G__4943 = null;
var G__4944 = 0;
var G__4945 = 0;
seq__4921_4925 = G__4942;
chunk__4922_4926 = G__4943;
count__4923_4927 = G__4944;
i__4924_4928 = G__4945;
continue;
}
}
} else
{}
}
break;
}
return content;
});
/**
* Removes the specified CSS class from each node in the content.
*/
domina.remove_class_BANG_ = (function remove_class_BANG_(content,class$){
var seq__4950_4954 = cljs.core.seq.call(null,domina.nodes.call(null,content));
var chunk__4951_4955 = null;
var count__4952_4956 = 0;
var i__4953_4957 = 0;
while(true){
if((i__4953_4957 < count__4952_4956))
{var node_4958 = cljs.core._nth.call(null,chunk__4951_4955,i__4953_4957);
goog.dom.classes.remove(node_4958,class$);
{
var G__4959 = seq__4950_4954;
var G__4960 = chunk__4951_4955;
var G__4961 = count__4952_4956;
var G__4962 = (i__4953_4957 + 1);
seq__4950_4954 = G__4959;
chunk__4951_4955 = G__4960;
count__4952_4956 = G__4961;
i__4953_4957 = G__4962;
continue;
}
} else
{var temp__4092__auto___4963 = cljs.core.seq.call(null,seq__4950_4954);
if(temp__4092__auto___4963)
{var seq__4950_4964__$1 = temp__4092__auto___4963;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__4950_4964__$1))
{var c__3186__auto___4965 = cljs.core.chunk_first.call(null,seq__4950_4964__$1);
{
var G__4966 = cljs.core.chunk_rest.call(null,seq__4950_4964__$1);
var G__4967 = c__3186__auto___4965;
var G__4968 = cljs.core.count.call(null,c__3186__auto___4965);
var G__4969 = 0;
seq__4950_4954 = G__4966;
chunk__4951_4955 = G__4967;
count__4952_4956 = G__4968;
i__4953_4957 = G__4969;
continue;
}
} else
{var node_4970 = cljs.core.first.call(null,seq__4950_4964__$1);
goog.dom.classes.remove(node_4970,class$);
{
var G__4971 = cljs.core.next.call(null,seq__4950_4964__$1);
var G__4972 = null;
var G__4973 = 0;
var G__4974 = 0;
seq__4950_4954 = G__4971;
chunk__4951_4955 = G__4972;
count__4952_4956 = G__4973;
i__4953_4957 = G__4974;
continue;
}
}
} else
{}
}
break;
}
return content;
});
/**
* Returns a seq of all the CSS classes currently applied to a node. Assumes content is a single node.
*/
domina.classes = (function classes(content){
return cljs.core.seq.call(null,goog.dom.classes.get(domina.single_node.call(null,content)));
});
/**
* Sets the class attribute of the content nodes to classes, which can
* be either a class attribute string or a seq of classname strings.
*/
domina.set_classes_BANG_ = (function set_classes_BANG_(content,classes){
var classes_4983__$1 = ((cljs.core.coll_QMARK_.call(null,classes))?clojure.string.join.call(null," ",classes):classes);
var seq__4979_4984 = cljs.core.seq.call(null,domina.nodes.call(null,content));
var chunk__4980_4985 = null;
var count__4981_4986 = 0;
var i__4982_4987 = 0;
while(true){
if((i__4982_4987 < count__4981_4986))
{var node_4988 = cljs.core._nth.call(null,chunk__4980_4985,i__4982_4987);
goog.dom.classes.set(node_4988,classes_4983__$1);
{
var G__4989 = seq__4979_4984;
var G__4990 = chunk__4980_4985;
var G__4991 = count__4981_4986;
var G__4992 = (i__4982_4987 + 1);
seq__4979_4984 = G__4989;
chunk__4980_4985 = G__4990;
count__4981_4986 = G__4991;
i__4982_4987 = G__4992;
continue;
}
} else
{var temp__4092__auto___4993 = cljs.core.seq.call(null,seq__4979_4984);
if(temp__4092__auto___4993)
{var seq__4979_4994__$1 = temp__4092__auto___4993;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__4979_4994__$1))
{var c__3186__auto___4995 = cljs.core.chunk_first.call(null,seq__4979_4994__$1);
{
var G__4996 = cljs.core.chunk_rest.call(null,seq__4979_4994__$1);
var G__4997 = c__3186__auto___4995;
var G__4998 = cljs.core.count.call(null,c__3186__auto___4995);
var G__4999 = 0;
seq__4979_4984 = G__4996;
chunk__4980_4985 = G__4997;
count__4981_4986 = G__4998;
i__4982_4987 = G__4999;
continue;
}
} else
{var node_5000 = cljs.core.first.call(null,seq__4979_4994__$1);
goog.dom.classes.set(node_5000,classes_4983__$1);
{
var G__5001 = cljs.core.next.call(null,seq__4979_4994__$1);
var G__5002 = null;
var G__5003 = 0;
var G__5004 = 0;
seq__4979_4984 = G__5001;
chunk__4980_4985 = G__5002;
count__4981_4986 = G__5003;
i__4982_4987 = G__5004;
continue;
}
}
} else
{}
}
break;
}
return content;
});
/**
* Returns the text of a node. Assumes content is a single node. For consistency across browsers, will always trim whitespace of the beginning and end of the returned text.
*/
domina.text = (function text(content){
return goog.string.trim(goog.dom.getTextContent(domina.single_node.call(null,content)));
});
/**
* Sets the text value of all the nodes in the given content.
*/
domina.set_text_BANG_ = (function set_text_BANG_(content,value){
var seq__5009_5013 = cljs.core.seq.call(null,domina.nodes.call(null,content));
var chunk__5010_5014 = null;
var count__5011_5015 = 0;
var i__5012_5016 = 0;
while(true){
if((i__5012_5016 < count__5011_5015))
{var node_5017 = cljs.core._nth.call(null,chunk__5010_5014,i__5012_5016);
goog.dom.setTextContent(node_5017,value);
{
var G__5018 = seq__5009_5013;
var G__5019 = chunk__5010_5014;
var G__5020 = count__5011_5015;
var G__5021 = (i__5012_5016 + 1);
seq__5009_5013 = G__5018;
chunk__5010_5014 = G__5019;
count__5011_5015 = G__5020;
i__5012_5016 = G__5021;
continue;
}
} else
{var temp__4092__auto___5022 = cljs.core.seq.call(null,seq__5009_5013);
if(temp__4092__auto___5022)
{var seq__5009_5023__$1 = temp__4092__auto___5022;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__5009_5023__$1))
{var c__3186__auto___5024 = cljs.core.chunk_first.call(null,seq__5009_5023__$1);
{
var G__5025 = cljs.core.chunk_rest.call(null,seq__5009_5023__$1);
var G__5026 = c__3186__auto___5024;
var G__5027 = cljs.core.count.call(null,c__3186__auto___5024);
var G__5028 = 0;
seq__5009_5013 = G__5025;
chunk__5010_5014 = G__5026;
count__5011_5015 = G__5027;
i__5012_5016 = G__5028;
continue;
}
} else
{var node_5029 = cljs.core.first.call(null,seq__5009_5023__$1);
goog.dom.setTextContent(node_5029,value);
{
var G__5030 = cljs.core.next.call(null,seq__5009_5023__$1);
var G__5031 = null;
var G__5032 = 0;
var G__5033 = 0;
seq__5009_5013 = G__5030;
chunk__5010_5014 = G__5031;
count__5011_5015 = G__5032;
i__5012_5016 = G__5033;
continue;
}
}
} else
{}
}
break;
}
return content;
});
/**
* Returns the value of a node (presumably a form field). Assumes content is a single node.
*/
domina.value = (function value(content){
return goog.dom.forms.getValue(domina.single_node.call(null,content));
});
/**
* Sets the value of all the nodes (presumably form fields) in the given content.
*/
domina.set_value_BANG_ = (function set_value_BANG_(content,value){
var seq__5038_5042 = cljs.core.seq.call(null,domina.nodes.call(null,content));
var chunk__5039_5043 = null;
var count__5040_5044 = 0;
var i__5041_5045 = 0;
while(true){
if((i__5041_5045 < count__5040_5044))
{var node_5046 = cljs.core._nth.call(null,chunk__5039_5043,i__5041_5045);
goog.dom.forms.setValue(node_5046,value);
{
var G__5047 = seq__5038_5042;
var G__5048 = chunk__5039_5043;
var G__5049 = count__5040_5044;
var G__5050 = (i__5041_5045 + 1);
seq__5038_5042 = G__5047;
chunk__5039_5043 = G__5048;
count__5040_5044 = G__5049;
i__5041_5045 = G__5050;
continue;
}
} else
{var temp__4092__auto___5051 = cljs.core.seq.call(null,seq__5038_5042);
if(temp__4092__auto___5051)
{var seq__5038_5052__$1 = temp__4092__auto___5051;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__5038_5052__$1))
{var c__3186__auto___5053 = cljs.core.chunk_first.call(null,seq__5038_5052__$1);
{
var G__5054 = cljs.core.chunk_rest.call(null,seq__5038_5052__$1);
var G__5055 = c__3186__auto___5053;
var G__5056 = cljs.core.count.call(null,c__3186__auto___5053);
var G__5057 = 0;
seq__5038_5042 = G__5054;
chunk__5039_5043 = G__5055;
count__5040_5044 = G__5056;
i__5041_5045 = G__5057;
continue;
}
} else
{var node_5058 = cljs.core.first.call(null,seq__5038_5052__$1);
goog.dom.forms.setValue(node_5058,value);
{
var G__5059 = cljs.core.next.call(null,seq__5038_5052__$1);
var G__5060 = null;
var G__5061 = 0;
var G__5062 = 0;
seq__5038_5042 = G__5059;
chunk__5039_5043 = G__5060;
count__5040_5044 = G__5061;
i__5041_5045 = G__5062;
continue;
}
}
} else
{}
}
break;
}
return content;
});
/**
* Returns the innerHTML of a node. Assumes content is a single node.
*/
domina.html = (function html(content){
return domina.single_node.call(null,content).innerHTML;
});
domina.replace_children_BANG_ = (function replace_children_BANG_(content,inner_content){
return domina.append_BANG_.call(null,domina.destroy_children_BANG_.call(null,content),inner_content);
});
domina.set_inner_html_BANG_ = (function set_inner_html_BANG_(content,html_string){
var allows_inner_html_QMARK_ = cljs.core.not.call(null,cljs.core.re_find.call(null,domina.re_no_inner_html,html_string));
var leading_whitespace_QMARK_ = cljs.core.re_find.call(null,domina.re_leading_whitespace,html_string);
var tag_name = [cljs.core.str(cljs.core.second.call(null,cljs.core.re_find.call(null,domina.re_tag_name,html_string)))].join('').toLowerCase();
var special_tag_QMARK_ = cljs.core.contains_QMARK_.call(null,domina.wrap_map,tag_name);
if(cljs.core.truth_((function (){var and__3941__auto__ = allows_inner_html_QMARK_;
if(and__3941__auto__)
{var and__3941__auto____$1 = (function (){var or__3943__auto__ = domina.support.leading_whitespace_QMARK_;
if(cljs.core.truth_(or__3943__auto__))
{return or__3943__auto__;
} else
{return cljs.core.not.call(null,leading_whitespace_QMARK_);
}
})();
if(cljs.core.truth_(and__3941__auto____$1))
{return !(special_tag_QMARK_);
} else
{return and__3941__auto____$1;
}
} else
{return and__3941__auto__;
}
})()))
{var value_5073 = clojure.string.replace.call(null,html_string,domina.re_xhtml_tag,"<$1></$2>");
try{var seq__5069_5074 = cljs.core.seq.call(null,domina.nodes.call(null,content));
var chunk__5070_5075 = null;
var count__5071_5076 = 0;
var i__5072_5077 = 0;
while(true){
if((i__5072_5077 < count__5071_5076))
{var node_5078 = cljs.core._nth.call(null,chunk__5070_5075,i__5072_5077);
goog.events.removeAll(node_5078);
node_5078.innerHTML = value_5073;
{
var G__5079 = seq__5069_5074;
var G__5080 = chunk__5070_5075;
var G__5081 = count__5071_5076;
var G__5082 = (i__5072_5077 + 1);
seq__5069_5074 = G__5079;
chunk__5070_5075 = G__5080;
count__5071_5076 = G__5081;
i__5072_5077 = G__5082;
continue;
}
} else
{var temp__4092__auto___5083 = cljs.core.seq.call(null,seq__5069_5074);
if(temp__4092__auto___5083)
{var seq__5069_5084__$1 = temp__4092__auto___5083;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__5069_5084__$1))
{var c__3186__auto___5085 = cljs.core.chunk_first.call(null,seq__5069_5084__$1);
{
var G__5086 = cljs.core.chunk_rest.call(null,seq__5069_5084__$1);
var G__5087 = c__3186__auto___5085;
var G__5088 = cljs.core.count.call(null,c__3186__auto___5085);
var G__5089 = 0;
seq__5069_5074 = G__5086;
chunk__5070_5075 = G__5087;
count__5071_5076 = G__5088;
i__5072_5077 = G__5089;
continue;
}
} else
{var node_5090 = cljs.core.first.call(null,seq__5069_5084__$1);
goog.events.removeAll(node_5090);
node_5090.innerHTML = value_5073;
{
var G__5091 = cljs.core.next.call(null,seq__5069_5084__$1);
var G__5092 = null;
var G__5093 = 0;
var G__5094 = 0;
seq__5069_5074 = G__5091;
chunk__5070_5075 = G__5092;
count__5071_5076 = G__5093;
i__5072_5077 = G__5094;
continue;
}
}
} else
{}
}
break;
}
}catch (e5068){if((e5068 instanceof Error))
{var e_5095 = e5068;
domina.replace_children_BANG_.call(null,content,value_5073);
} else
{if("\uFDD0:else")
{throw e5068;
} else
{}
}
}} else
{domina.replace_children_BANG_.call(null,content,html_string);
}
return content;
});
/**
* Sets the innerHTML value for all the nodes in the given content.
*/
domina.set_html_BANG_ = (function set_html_BANG_(content,inner_content){
if(cljs.core.string_QMARK_.call(null,inner_content))
{return domina.set_inner_html_BANG_.call(null,content,inner_content);
} else
{return domina.replace_children_BANG_.call(null,content,inner_content);
}
});
/**
* Returns data associated with a node for a given key. Assumes
* content is a single node. If the bubble parameter is set to true,
* will search parent nodes if the key is not found.
*/
domina.get_data = (function() {
var get_data = null;
var get_data__2 = (function (node,key){
return get_data.call(null,node,key,false);
});
var get_data__3 = (function (node,key,bubble){
var m = domina.single_node.call(null,node).__domina_data;
var value = (cljs.core.truth_(m)?cljs.core.get.call(null,m,key):null);
if(cljs.core.truth_((function (){var and__3941__auto__ = bubble;
if(cljs.core.truth_(and__3941__auto__))
{return (value == null);
} else
{return and__3941__auto__;
}
})()))
{var temp__4092__auto__ = domina.single_node.call(null,node).parentNode;
if(cljs.core.truth_(temp__4092__auto__))
{var parent = temp__4092__auto__;
return get_data.call(null,parent,key,true);
} else
{return null;
}
} else
{return value;
}
});
get_data = function(node,key,bubble){
switch(arguments.length){
case 2:
return get_data__2.call(this,node,key);
case 3:
return get_data__3.call(this,node,key,bubble);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
get_data.cljs$core$IFn$_invoke$arity$2 = get_data__2;
get_data.cljs$core$IFn$_invoke$arity$3 = get_data__3;
return get_data;
})()
;
/**
* Sets a data on the node for a given key. Assumes content is a
* single node. Data should be ClojureScript values and data structures
* only; using other objects as data may result in memory leaks on some
* browsers.
*/
domina.set_data_BANG_ = (function set_data_BANG_(node,key,value){
var m = (function (){var or__3943__auto__ = domina.single_node.call(null,node).__domina_data;
if(cljs.core.truth_(or__3943__auto__))
{return or__3943__auto__;
} else
{return cljs.core.PersistentArrayMap.EMPTY;
}
})();
return domina.single_node.call(null,node).__domina_data = cljs.core.assoc.call(null,m,key,value);
});
/**
* Takes a two-arg function, a reference DomContent and new
* DomContent. Applies the function for each reference / content
* combination. Uses clones of the new content for each additional
* parent after the first.
*/
domina.apply_with_cloning = (function apply_with_cloning(f,parent_content,child_content){
var parents = domina.nodes.call(null,parent_content);
var children = domina.nodes.call(null,child_content);
var first_child = (function (){var frag = document.createDocumentFragment();
var seq__5102_5106 = cljs.core.seq.call(null,children);
var chunk__5103_5107 = null;
var count__5104_5108 = 0;
var i__5105_5109 = 0;
while(true){
if((i__5105_5109 < count__5104_5108))
{var child_5110 = cljs.core._nth.call(null,chunk__5103_5107,i__5105_5109);
frag.appendChild(child_5110);
{
var G__5111 = seq__5102_5106;
var G__5112 = chunk__5103_5107;
var G__5113 = count__5104_5108;
var G__5114 = (i__5105_5109 + 1);
seq__5102_5106 = G__5111;
chunk__5103_5107 = G__5112;
count__5104_5108 = G__5113;
i__5105_5109 = G__5114;
continue;
}
} else
{var temp__4092__auto___5115 = cljs.core.seq.call(null,seq__5102_5106);
if(temp__4092__auto___5115)
{var seq__5102_5116__$1 = temp__4092__auto___5115;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__5102_5116__$1))
{var c__3186__auto___5117 = cljs.core.chunk_first.call(null,seq__5102_5116__$1);
{
var G__5118 = cljs.core.chunk_rest.call(null,seq__5102_5116__$1);
var G__5119 = c__3186__auto___5117;
var G__5120 = cljs.core.count.call(null,c__3186__auto___5117);
var G__5121 = 0;
seq__5102_5106 = G__5118;
chunk__5103_5107 = G__5119;
count__5104_5108 = G__5120;
i__5105_5109 = G__5121;
continue;
}
} else
{var child_5122 = cljs.core.first.call(null,seq__5102_5116__$1);
frag.appendChild(child_5122);
{
var G__5123 = cljs.core.next.call(null,seq__5102_5116__$1);
var G__5124 = null;
var G__5125 = 0;
var G__5126 = 0;
seq__5102_5106 = G__5123;
chunk__5103_5107 = G__5124;
count__5104_5108 = G__5125;
i__5105_5109 = G__5126;
continue;
}
}
} else
{}
}
break;
}
return frag;
})();
var other_children = cljs.core.doall.call(null,cljs.core.repeatedly.call(null,(cljs.core.count.call(null,parents) - 1),((function (parents,children,first_child){
return (function (){
return first_child.cloneNode(true);
});})(parents,children,first_child))
));
if(cljs.core.seq.call(null,parents))
{f.call(null,cljs.core.first.call(null,parents),first_child);
return cljs.core.doall.call(null,cljs.core.map.call(null,(function (p1__5096_SHARP_,p2__5097_SHARP_){
return f.call(null,p1__5096_SHARP_,p2__5097_SHARP_);
}),cljs.core.rest.call(null,parents),other_children));
} else
{return null;
}
});
domina.lazy_nl_via_item = (function() {
var lazy_nl_via_item = null;
var lazy_nl_via_item__1 = (function (nl){
return lazy_nl_via_item.call(null,nl,0);
});
var lazy_nl_via_item__2 = (function (nl,n){
if((n < nl.length))
{return (new cljs.core.LazySeq(null,false,(function (){
return cljs.core.cons.call(null,nl.item(n),lazy_nl_via_item.call(null,nl,(n + 1)));
}),null));
} else
{return null;
}
});
lazy_nl_via_item = function(nl,n){
switch(arguments.length){
case 1:
return lazy_nl_via_item__1.call(this,nl);
case 2:
return lazy_nl_via_item__2.call(this,nl,n);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
lazy_nl_via_item.cljs$core$IFn$_invoke$arity$1 = lazy_nl_via_item__1;
lazy_nl_via_item.cljs$core$IFn$_invoke$arity$2 = lazy_nl_via_item__2;
return lazy_nl_via_item;
})()
;
domina.lazy_nl_via_array_ref = (function() {
var lazy_nl_via_array_ref = null;
var lazy_nl_via_array_ref__1 = (function (nl){
return lazy_nl_via_array_ref.call(null,nl,0);
});
var lazy_nl_via_array_ref__2 = (function (nl,n){
if((n < nl.length))
{return (new cljs.core.LazySeq(null,false,(function (){
return cljs.core.cons.call(null,(nl[n]),lazy_nl_via_array_ref.call(null,nl,(n + 1)));
}),null));
} else
{return null;
}
});
lazy_nl_via_array_ref = function(nl,n){
switch(arguments.length){
case 1:
return lazy_nl_via_array_ref__1.call(this,nl);
case 2:
return lazy_nl_via_array_ref__2.call(this,nl,n);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
lazy_nl_via_array_ref.cljs$core$IFn$_invoke$arity$1 = lazy_nl_via_array_ref__1;
lazy_nl_via_array_ref.cljs$core$IFn$_invoke$arity$2 = lazy_nl_via_array_ref__2;
return lazy_nl_via_array_ref;
})()
;
/**
* A lazy seq view of a js/NodeList, or other array-like javascript things
*/
domina.lazy_nodelist = (function lazy_nodelist(nl){
if(cljs.core.truth_(nl.item))
{return domina.lazy_nl_via_item.call(null,nl);
} else
{return domina.lazy_nl_via_array_ref.call(null,nl);
}
});
domina.array_like_QMARK_ = (function array_like_QMARK_(obj){
var and__3941__auto__ = obj;
if(cljs.core.truth_(and__3941__auto__))
{return obj.length;
} else
{return and__3941__auto__;
}
});
/**
* Some versions of IE have things that are like arrays in that they
* respond to .length, but are not arrays nor NodeSets. This returns a
* real sequence view of such objects. If passed an object that is not
* a logical sequence at all, returns a single-item seq containing the
* object.
*/
domina.normalize_seq = (function normalize_seq(list_thing){
if((list_thing == null))
{return cljs.core.List.EMPTY;
} else
{if((function (){var G__5128 = list_thing;
if(G__5128)
{if((function (){var or__3943__auto__ = (G__5128.cljs$lang$protocol_mask$partition0$ & 8388608);
if(or__3943__auto__)
{return or__3943__auto__;
} else
{return G__5128.cljs$core$ISeqable$;
}
})())
{return true;
} else
{if((!G__5128.cljs$lang$protocol_mask$partition0$))
{return cljs.core.type_satisfies_.call(null,cljs.core.ISeqable,G__5128);
} else
{return false;
}
}
} else
{return cljs.core.type_satisfies_.call(null,cljs.core.ISeqable,G__5128);
}
})())
{return cljs.core.seq.call(null,list_thing);
} else
{if(cljs.core.truth_(domina.array_like_QMARK_.call(null,list_thing)))
{return domina.lazy_nodelist.call(null,list_thing);
} else
{if("\uFDD0:default")
{return cljs.core.seq.call(null,cljs.core.PersistentVector.fromArray([list_thing], true));
} else
{return null;
}
}
}
}
});
(domina.DomContent["_"] = true);
(domina.nodes["_"] = (function (content){
if((content == null))
{return cljs.core.List.EMPTY;
} else
{if((function (){var G__5129 = content;
if(G__5129)
{if((function (){var or__3943__auto__ = (G__5129.cljs$lang$protocol_mask$partition0$ & 8388608);
if(or__3943__auto__)
{return or__3943__auto__;
} else
{return G__5129.cljs$core$ISeqable$;
}
})())
{return true;
} else
{if((!G__5129.cljs$lang$protocol_mask$partition0$))
{return cljs.core.type_satisfies_.call(null,cljs.core.ISeqable,G__5129);
} else
{return false;
}
}
} else
{return cljs.core.type_satisfies_.call(null,cljs.core.ISeqable,G__5129);
}
})())
{return cljs.core.seq.call(null,content);
} else
{if(cljs.core.truth_(domina.array_like_QMARK_.call(null,content)))
{return domina.lazy_nodelist.call(null,content);
} else
{if("\uFDD0:default")
{return cljs.core.seq.call(null,cljs.core.PersistentVector.fromArray([content], true));
} else
{return null;
}
}
}
}
}));
(domina.single_node["_"] = (function (content){
if((content == null))
{return null;
} else
{if((function (){var G__5130 = content;
if(G__5130)
{if((function (){var or__3943__auto__ = (G__5130.cljs$lang$protocol_mask$partition0$ & 8388608);
if(or__3943__auto__)
{return or__3943__auto__;
} else
{return G__5130.cljs$core$ISeqable$;
}
})())
{return true;
} else
{if((!G__5130.cljs$lang$protocol_mask$partition0$))
{return cljs.core.type_satisfies_.call(null,cljs.core.ISeqable,G__5130);
} else
{return false;
}
}
} else
{return cljs.core.type_satisfies_.call(null,cljs.core.ISeqable,G__5130);
}
})())
{return cljs.core.first.call(null,content);
} else
{if(cljs.core.truth_(domina.array_like_QMARK_.call(null,content)))
{return content.item(0);
} else
{if("\uFDD0:default")
{return content;
} else
{return null;
}
}
}
}
}));
(domina.DomContent["string"] = true);
(domina.nodes["string"] = (function (s){
return cljs.core.doall.call(null,domina.nodes.call(null,domina.string_to_dom.call(null,s)));
}));
(domina.single_node["string"] = (function (s){
return domina.single_node.call(null,domina.string_to_dom.call(null,s));
}));
if(cljs.core.truth_((typeof NodeList != 'undefined')))
{NodeList.prototype.cljs$core$ISeqable$ = true;
NodeList.prototype.cljs$core$ISeqable$_seq$arity$1 = (function (nodelist){
return domina.lazy_nodelist.call(null,nodelist);
});
NodeList.prototype.cljs$core$IIndexed$ = true;
NodeList.prototype.cljs$core$IIndexed$_nth$arity$2 = (function (nodelist,n){
return nodelist.item(n);
});
NodeList.prototype.cljs$core$IIndexed$_nth$arity$3 = (function (nodelist,n,not_found){
if((nodelist.length <= n))
{return not_found;
} else
{return cljs.core.nth.call(null,nodelist,n);
}
});
NodeList.prototype.cljs$core$ICounted$ = true;
NodeList.prototype.cljs$core$ICounted$_count$arity$1 = (function (nodelist){
return nodelist.length;
});
} else
{}
if(cljs.core.truth_((typeof StaticNodeList != 'undefined')))
{StaticNodeList.prototype.cljs$core$ISeqable$ = true;
StaticNodeList.prototype.cljs$core$ISeqable$_seq$arity$1 = (function (nodelist){
return domina.lazy_nodelist.call(null,nodelist);
});
StaticNodeList.prototype.cljs$core$IIndexed$ = true;
StaticNodeList.prototype.cljs$core$IIndexed$_nth$arity$2 = (function (nodelist,n){
return nodelist.item(n);
});
StaticNodeList.prototype.cljs$core$IIndexed$_nth$arity$3 = (function (nodelist,n,not_found){
if((nodelist.length <= n))
{return not_found;
} else
{return cljs.core.nth.call(null,nodelist,n);
}
});
StaticNodeList.prototype.cljs$core$ICounted$ = true;
StaticNodeList.prototype.cljs$core$ICounted$_count$arity$1 = (function (nodelist){
return nodelist.length;
});
} else
{}
if(cljs.core.truth_((typeof HTMLCollection != 'undefined')))
{HTMLCollection.prototype.cljs$core$ISeqable$ = true;
HTMLCollection.prototype.cljs$core$ISeqable$_seq$arity$1 = (function (coll){
return domina.lazy_nodelist.call(null,coll);
});
HTMLCollection.prototype.cljs$core$IIndexed$ = true;
HTMLCollection.prototype.cljs$core$IIndexed$_nth$arity$2 = (function (coll,n){
return coll.item(n);
});
HTMLCollection.prototype.cljs$core$IIndexed$_nth$arity$3 = (function (coll,n,not_found){
if((coll.length <= n))
{return not_found;
} else
{return cljs.core.nth.call(null,coll,n);
}
});
HTMLCollection.prototype.cljs$core$ICounted$ = true;
HTMLCollection.prototype.cljs$core$ICounted$_count$arity$1 = (function (coll){
return coll.length;
});
} else
{}
