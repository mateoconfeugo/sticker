var HistoryExperience = _.extend({
    'nVer': navigator.appVersion,
    'nAgt': navigator.userAgent,
    'browserName': navigator.appName,
    'fullVersion': '' + parseFloat(navigator.appVersion),
    'majorVersion': parseInt(navigator.appVersion,10),
    'nameOffset': undefined,
    'verOffset': undefined,
    'debug': false,
    'ix': undefined,
    'iframeName': undefined,
    'iframeSrc':   'http://localhost:8980/delivery',
    'initialize': function(options) {
	_.bindAll(this); 
	this.token = options.token;
	if(options.debug) {
	    this.debug = true;
	}
	if(options.iframeSrc) {
	    this.iframeSrc = options.iframeSrc;
	}
	this.browserName = navigator.appName;
	this.setBrowserInfo();
	this.setup();

    },
    'setup' : function () {
	window.onbeforeunload = this.tryPop; 
	// attach events, 1st, the "standard" way
	if (window.addEventListener) {
	    window.addEventListener('load', this.onload, false); 
	    document.addEventListener('click', this.examineClick, false); 
	}
	// maybe the older, IE way
	else if (window.attachEvent) {
	    window.attachEvent('onload', this.onload);
	    document.attachEvent('onclick', this.examineClick);
	}
	// last resort
	else {
	    window.onload = this.onload;
	    document.onclick = this.examineClick;
	}
    }, 
    'setBrowserInfo': function() {
	this.browserName = jQuery.browser.name;
	this.fullVersion = jQuery.browser.version;
	this.majorVersion = parseInt('' + this.fullVersion, 10);
	if (isNaN(this.majorVersion)) {
	    fullVersion  = ''+parseFloat(navigator.appVersion); 
	    majorVersion = parseInt(navigator.appVersion,10);
	}
    },
    // look at the click event that just occurred and determine if it
    // should turn the exit pop off
    'examineClick': function(e) {
	if (!e) var e = window.event; // IE uses window.event rather than passing an arg
	if (this.debug) {
	    this.log("examining click, got event: " + e);
	}
	var targetEle;
	if (e.target) {
            targetEle = e.target;
	}
	else if (e.srcElement) {
            targetEle = e.srcElement;
	}
	if (targetEle.nodeType == 3) {
            targetEle = targetEle.parentNode;
	}
	if (this.debug) { 
	    this.log("click target's name: " + targetEle.nodeName);
	}
	// turn exit off for ANY clicked element that has one of the noExit classes attached
	if (this.matchClass(targetEle.className, this.noExitClasses)) {
            this.setPopState(false);         // matched, so turn off exit pop
	}
	else if (targetEle.nodeName == 'INPUT') {
            // click was on some form input, so no exit pop when posting/getting
            // new page (granted, could be somebody else's form, but we're ignoring that)
            this.setPopState(false);
	}
	else if (targetEle.nodeName == 'IMG') {
            this.setPopState(false);
	}
	else if (targetEle.nodeName == 'SPAN') {
            this.setPopState(false);
	}
	else if (targetEle.nodeName == 'A') {
            // click was on an anchor
            // look at the anchor's href, if it stays in our domain
            // or any of the "special" listed domains, then no exit pop here either
            var targetHost = this.hostFromUrl(targetEle.href);
            if (this.matchCurrentDomain(targetHost) ) {
		this.setPopState(false);
            }
            else if (this.matchListedDomain(targetHost, this.noExitDomains) ) {
		this.setPopState(false);
            }
            else if (this.debug) {
		this.log("anchor clicked, but no domain/class match");
		this.log("target's onclick is next");
		this.log("target's onclick is: " + targetEle.onclick);
            }
	}
	return true;
    },
    'matchCurrentDomain': function(hostname) {
	var currentDomain = this.getCurrentDomain();
	if(this.debug)
            this.log('matching domain: "' + hostname + '" against current: "' + currentDomain + '"');
	return hostname && currentDomain && this.isSubdomain(hostname, currentDomain );
    },
    // get the hostname of the current page, as high up as we can go
    'getCurrentDomain': function() {
	try {
            return window.top.location.hostname.replace(/^www\./,'');
	}
	catch(e) {
            return window.location.hostname.replace(/^www\./,'');
	}
    },
    'matchListedDomain': function(hostname, matchList) {
	if (this.debug) this.log('matching listed domains to: "' + hostname + '"');
	if (! hostname || ! matchList ) return false;
	if ( typeof(matchList) == 'string' ) {
            matchList = [ matchList ];      // if we got a single string rather than a list...
	}
	for (var i = 0; i < matchList.length; i += 1 ) {
            if ( this.isSubdomain( hostname, matchList[i] ) ) return true;
	}
	return false;
    },
    // is subdomain a subdomain of domain? returns true if so,
    // false otherwise. domain should NOT have a leading "."
    'isSubdomain' : function(subdomain, domain) {
	if(!domain || !subdomain || domain.length > subdomain.length) return false;
	if(domain == subdomain) return true;
	if(subdomain.substr(subdomain.length - domain.length, domain.length) == domain
	   && subdomain.charAt(subdomain.length - domain.length - 1) == '.') return true;
        return false;
    },
    // build a regex out of str and see if it matches host
    // returns true if so, false otherwise.  the regex should
    // match host literally, or if host is any subdomain of str
    'hostMatch': function(str, host) {
	var pat = str.replace(/\./g, '\\.'); // escape in 'dots' in str
	pat += '$';     // anchor pattern to end of str
	if (pat.substr(0, 2) != '\\.') {
            pat = '(^|\\.)' + pat;
	}
	var re = new RegExp( pat );
	if ( this.debug ) this.log('using pattern "' + re + '"');
	return re.test( host );
    },
    'hostFromUrl': function(url) {
	var parseUrl = /^(?:([A-Za-z]+):)?(\/{0,3})([0-9.\-A-Za-z]+)(?::(\d+))?(?:\/([^?#]*))?(?:\?([^#]*))?(?:#(.*))?$/;
	var results = parseUrl.exec(url);
	return results ? results[3] : '';
    },
    // test whether a class contained in eleClass matches one on matchList
    // returns true if so, false otherwise.
    'matchClass': function(eleClass, matchList) {
	if (this.debug){ 
	    this.log('matchClass() matching elements class "' + eleClass + '" with class(es) "' + matchList + '"');
	}
	if (! matchList || !eleClass ) {
	    return false;
	}
        if ( typeof(matchList) == 'string' ) {
            matchList = [ matchList ];      // matchList can be a list or a single class
	}
        // split element's class list into individual classes
	var eleClasses = eleClass.split(/\s+/);
        for (var i = 0; i <  matchList.length; i += 1 ) {
        var noExitClass = matchList[i];
            if (this.debug) {
		this.log('matchClass() matching noExitClass "' + noExitClass + '" against class(es) "' + eleClass + '"');
	    }
        for (var j = 0; j <  eleClasses.length; j += 1 ) {
	    var c = eleClasses[j];
	    if (c == noExitClass) {
                if (this.debug) this.log('matched ' + c + ' and ' + noExitClass);
                return true;
	    }
	    else {
                if (this.debug) this.log('did NOT match ' + c + ' and ' + noExitClass);
	    }
        }
    }   
    if (this.debug) {
        this.log('failed to match by class');
    }   
    return false;
    },
    'tryPop': function() {
	if (this.getPopState() || typeof(this.getPopState()) == 'undefined') {
            //alert("tryPop() popState is: " + this.getPopState() + " typeof is: " + typeof(this.getPopState()));
            if (this.debug) {
		this.log("tryPop() popState is: " + this.getPopState());
	    }
            this.setPopState(false);
            // onload should have added the adu div, but in case it was
            // hijacked, add it in here...
            if (! document.getElementById(this.aduDivId) ) {
		this.preinsert();
            }
            document.getElementById(this.outerDivId).style.display='none';
            document.getElementById(this.aduDivId).style.display='block';
	    var alertText = $('#revenue_recovery_alert_content').text();
	    if (this.browserName == "Chrome") {
		alert("Press \'STAY ON THIS PAGE\' or \'CANCEL\' to view more cash options.\n Or call 866-945-5809 to apply by phone. Are you sure you want to leave this page");
		scroll(0, 0);
		return "Press \'STAY ON THIS PAGE\' or \'CANCEL\' to view more cash options.\n Or call 866-945-5809 to apply by phone. Are you sure you want to leave this page";
	    }
	    else if (this.browserName == "Firefox")	{
		alert("Don\'t Leave! We have more options for you. \n Just click \'STAY ON THIS PAGE\' or \'CANCEL\' on the next window or call 866-945-5809 to apply by phone");
		scroll(0, 0);
		return "Don\'t Leave! We have more options for you. \n Just click \'STAY ON THIS PAGE\' or \'CANCEL\' on the next window or call 866-945-5809 to apply by phone";
	    }
	    else {
		alert("Don\'t Leave! We have more options for you. \n Just click \'STAY ON THIS PAGE\' or \'CANCEL\' on the next window or call 866-945-5809 to apply by phone."); // catch all
		scroll(0, 0);
		return "Don\'t Leave! We have more options for you. \n Just click \'STAY ON THIS PAGE\' or \'CANCEL\' on the next window or call 866-945-5809 to apply by phone.";  
	    }
	}
	else if (this.debug) {
            this.log('not popping, popState is false or ! undefined');
	}
    },
    // log a message, either to the console if available, or 
    // an alert otherwise
    'log': function() {
	var s = Array.prototype.slice.call(arguments).join(' ');
	if (console) {
            console.log(s);
	}
	else {
            alert(s);
	}
    },
    'setPopState': function(on_or_off) {
	if (this.debug) {
	    this.log("setPopState to: " + on_or_off);
	}
	this.popOnExit = on_or_off ? true : false;
	return this.popOnExit;
    },
    'getPopState': function() {
	return this.popOnExit;
    },
    'setPopStateOff': function() {
	if (this.debug) {
	    this.log("caught onmousover in button div");
	}
	this.setPopState(false);
    },
    'preinsert': function() {
	if (this.debug) { 
	    this.log("doing preinsert()");
	}
	this.setPopState(true);  // assume true to start
	// optionally use the old non-namespaced landingPage for backwards compatibility
	this.insertAduDiv(this.landingPage || window.landingPage);
	// always insert the "back-button-catching-div", unless explicitly asked not to:
	if (! this.getOpt('backButtonOn')) {
            this.insertButtonDiv();
	}
    },
    // Key part of adunit where an iframe is inserted prepopulated with the results of rendering
    // the template that are associated with the landing page.  Notice display set to none.
    'insertAduDiv': function(landingPage) {
	// TODO: Better names here: probably should be configurable
	this.outerDivId = 'oesBulwarkUnitNS_mainbody_div';
	this.aduDivId = 'this_adu_div';
	// new div element
	var nd = document.createElement('div');
	var iframeSrc = this.iframeSrc + '?token=' + this.token;
	nd.id=this.aduDivId;
	nd.style.width='100%';
	nd.style.display = 'none';
	nd.style.top='0pt';
	nd.style.zIndex='99999';
	nd.style.left='0pt';
	nd.style.height='100%';
	nd.style.position='absolute';
	nd.innerHTML = '<iframe scrolling="no" style="z-index:99999" height="2000" frameborder="0" width="100%" vspace="0" hspace="0" marginheight="0" marginwidth="0" src="' +
            iframeSrc + '">' + '</iframe>' +
            '<div id="' + this.outerDivId + '">';
	// this inserts the new div as first child after body
	document.body.insertBefore(nd,document.body.firstChild);
    },
    // create the "catch mouse nav to browser nav buttons" div
    'insertButtonDiv': function() {
	var nd = document.createElement('div');
	nd.style.top='0pt';
	nd.style.zIndex='999999';
	nd.style.left='0pt';
	nd.style.height='10px';
	nd.style.width='200px';
	nd.style.position='absolute';
	document.body.insertBefore(nd,document.body.firstChild);
//	nd.onmouseover = this.setPopStateOff;
//	nd.onmouseout = this.setPopStateOff;
    },
    // look for anchor tags in the page that have onclick handlers that
    // directly set window.location.   when found, replace them with
    // new functions that turn off the exit pop and then call the original
    // function
    'replaceWinLoc': function () {
	var tagsToCheck = ['a', 'img'];   // add dom element name that might have an onclick = "window.location = '...'" handler here

	for ( var j = 0; j < tagsToCheck.length; j++ ) {
            var tag = tagsToCheck[j];
	    
            var nodes = document.getElementsByTagName(tag);
            var size = nodes.length;
	    
            if (this.debug) this.log("starting search of " + size + ' "' + tag + '" tags');
            
            // loop through nodes and attach events
            var oldfuncs = [];
            for (var i = 0; i < size; i++) {
		if(nodes[i].onclick && typeof(nodes[i].onclick) == 'function') {
                    oldfuncs[i] = nodes[i].onclick;
                    var funStr = oldfuncs[i].toString().replace(/\n/g, ' ');
                    if (this.debug) {
			this.log('found a "' + tag + '" node w/onclick id is "' 
				 + nodes[i].id
				 + '"'
				);
			this.log("function is: " + funStr);
                    }
                    var locExp = /\{\s*window.location\s*=\s*([^;\}]+)\s*[;]?\s*\}\s*$/;
                    var matched = locExp.exec(funStr);
                    if(matched && matched.length > 1) {
			if (this.debug) this.log("matched window.location in func, loc is: '" + matched[1] + "'");
			nodes[i].onclick = new Function("event", "this.setPopState(false); window.location = " + matched[1] + ';');
			//var oldfunc = oldfuncs[i];
			//nodes[i].onclick = function (event) {
			//    this.setPopState(false);
			//    oldfunc(event);
			//};
                    }
		}
            }
	}
    },
    'setOpt': function(opt, value) {
	this.options = this.options || [];
	this.options[opt] = arguments.length > 1 ? value : true;
	return this.options[opt];
    },
    'getOpt': function(opt) {
	this.options = this.options || [];
	return this.options[opt];
    },
    // combine our two initialization functions into single func for convenience.
    'onload': function () {
	this.preinsert();
	if (this.getOpt('wrapWinLoc')) {
            this.replaceWinLoc();
	}
    }
});




