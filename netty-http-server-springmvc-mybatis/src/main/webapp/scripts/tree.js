
DOMhelp={
	debugWindowId:'DOMhelpdebug',
	init:function(){
		if(!document.getElementById || !document.createTextNode){return;}
	},
	lastSibling:function(node){
		var tempObj=node.parentNode.lastChild;
		while(tempObj.nodeType!=1 && tempObj.previousSibling!=null){
			tempObj=tempObj.previousSibling;
		}
		return (tempObj.nodeType==1)?tempObj:false;
	},
	firstSibling:function(node){
		var tempObj=node.parentNode.firstChild;
		while(tempObj.nodeType!=1 && tempObj.nextSibling!=null){
			tempObj=tempObj.nextSibling;
		}
		return (tempObj.nodeType==1)?tempObj:false;
	},
	getText:function(node){
		if(!node.hasChildNodes()){return false;}
		var reg=/^\s+$/;
		var tempObj=node.firstChild;
		while(tempObj.nodeType!=3 && tempObj.nextSibling!=null || reg.test(tempObj.nodeValue)){
			tempObj=tempObj.nextSibling;
		}
		return tempObj.nodeType==3?tempObj.nodeValue:false;
	},
	setText:function(node,txt){
		if(!node.hasChildNodes()){return false;}
		var reg=/^\s+$/;
		var tempObj=node.firstChild;
		while(tempObj.nodeType!=3 && tempObj.nextSibling!=null || reg.test(tempObj.nodeValue)){
			tempObj=tempObj.nextSibling;
		}
		if(tempObj.nodeType==3){tempObj.nodeValue=txt}else{return false;}
	},
	createLink:function(to,txt){
		var tempObj=document.createElement('a');
		tempObj.appendChild(document.createTextNode(txt));
		tempObj.setAttribute('href',to);
		return tempObj;
	},
	createTextElm:function(elm,txt){
		var tempObj=document.createElement(elm);
		tempObj.appendChild(document.createTextNode(txt));
		return tempObj;
	},
	closestSibling:function(node,direction){
		var tempObj;
		if(direction==-1 && node.previousSibling!=null){
			tempObj=node.previousSibling;
			while(tempObj.nodeType!=1 && tempObj.previousSibling!=null){
				 tempObj=tempObj.previousSibling;
			}
		}else if(direction==1 && node.nextSibling!=null){
			tempObj=node.nextSibling;
			while(tempObj.nodeType!=1 && tempObj.nextSibling!=null){
				 tempObj=tempObj.nextSibling;
			}
		}
		return tempObj.nodeType==1?tempObj:false;
	},
	initDebug:function(){
		if(DOMhelp.debug){DOMhelp.stopDebug();}
		DOMhelp.debug=document.createElement('div');
		DOMhelp.debug.setAttribute('id',DOMhelp.debugWindowId);
		document.body.insertBefore(DOMhelp.debug,document.body.firstChild);
	},
	setDebug:function(bug){
		if(!DOMhelp.debug){DOMhelp.initDebug();}
		DOMhelp.debug.innerHTML+=bug+'\n';
	},
	stopDebug:function(){
		if(DOMhelp.debug){
			DOMhelp.debug.parentNode.removeChild(DOMhelp.debug);
			DOMhelp.debug=null;
		}
	},
	getKey:function(e){
		if(window.event){
	      var key = window.event.keyCode;
	    } else if(e){
	      var key=e.keyCode;
	    }
		return key;
	},
/* helper methods */
	getTarget:function(e){
		var target = window.event ? window.event.srcElement : e ? e.target : null;
		if (!target){return false;}
		while(target.nodeType!=1 && target.nodeName.toLowerCase()!='body'){
			target=target.parentNode;
		}
		return target;
	},
	stopBubble:function(e){
		if(window.event && window.event.cancelBubble){
			window.event.cancelBubble = true;
		} 
		if (e && e.stopPropagation){
			e.stopPropagation();
		}
	},
	stopDefault:function(e){
		if(window.event && window.event.returnValue){
			window.event.returnValue = false;
		} 
		if (e && e.preventDefault){
			e.preventDefault();
		}
	},
	cancelClick:function(e){
		if (window.event){
			window.event.cancelBubble = true;
			window.event.returnValue = false;
		}
		if (e && e.stopPropagation && e.preventDefault){
			e.stopPropagation();
			e.preventDefault();
		}
	},
	addEvent: function(elm, evType, fn, useCapture){
		if (elm.addEventListener){
			elm.addEventListener(evType, fn, useCapture);
			return true;
		} else if (elm.attachEvent) {
			var r = elm.attachEvent('on' + evType, fn);
			return r;
		} else {
			elm['on' + evType] = fn;
		}
	},
	cssjs:function(a,o,c1,c2){
		switch (a){
			case 'swap':
				o.className=!DOMhelp.cssjs('check',o,c1)?o.className.replace(c2,c1):o.className.replace(c1,c2);
			break;
			case 'add':
				if(!DOMhelp.cssjs('check',o,c1)){o.className+=o.className?' '+c1:c1;}
			break;
			case 'remove':
				var rep=o.className.match(' '+c1)?' '+c1:c1;
				o.className=o.className.replace(rep,'');
			break;
			case 'check':
				var found=false;
				var temparray=o.className.split(' ');
				for(var i=0;i<temparray.length;i++){
					if(temparray[i]==c1){found=true;}
				}
				return found;
			break;
		}
	},
    safariClickFix:function(){
      return false;
    }
}
DOMhelp.addEvent(window, 'load', DOMhelp.init, false);



sn={ 
    dynamicClass:'dyn', 
    showClass:'show', 
    parentClass:'parent', 
    openClass:'open', 
    navID:'nav', 
    init:function(){ 
        var triggerLink; 
        if(!document.getElementById || !document.createTextNode){return;} 
        var nav=document.getElementById(sn.navID); 
        if(!nav){return;} 
        DOMhelp.cssjs('add',nav,sn.dynamicClass);         
        var nested=nav.getElementsByTagName('ul'); 
        for(var i=0;i<nested.length;i++){ 
            triggerLink=nested[i].parentNode.getElementsByTagName('a')[0]; 
            DOMhelp.cssjs('add',triggerLink.parentNode,sn.parentClass);         
            DOMhelp.addEvent(triggerLink,'click',sn.changeSection,false); 
            triggerLink.onclick=DOMhelp.safariClickFix; 
            if(nested[i].parentNode.getElementsByTagName('strong').length>0){ 
                DOMhelp.cssjs('add',triggerLink.parentNode,sn.openClass);         
                DOMhelp.cssjs('add',nested[i],sn.showClass); 
            } 
        } 
    }, 
    changeSection:function(e){ 
        var t=DOMhelp.getTarget(e); 
        var firstList=t.parentNode.getElementsByTagName('ul')[0]; 
        if(DOMhelp.cssjs('check',firstList,sn.showClass)){ 
            DOMhelp.cssjs('remove',firstList,sn.showClass) 
            DOMhelp.cssjs('swap',t.parentNode,sn.openClass,sn.parentClass); 
        } else { 
            DOMhelp.cssjs('add',firstList,sn.showClass) 
            DOMhelp.cssjs('swap',t.parentNode,sn.openClass,sn.parentClass); 
        } 
        DOMhelp.cancelClick(e); 
    } 
} 
DOMhelp.addEvent(window,'load',sn.init,false); 