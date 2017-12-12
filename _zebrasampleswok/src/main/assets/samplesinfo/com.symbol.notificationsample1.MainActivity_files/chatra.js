/*!
 * ··· Chatra | https://chatra.io/
 */
!function(a,b){function c(){}function d(a,b){for(var c in b)b.hasOwnProperty(c)&&(a[c]=b[c])}function e(a){var b=0;for(var c in a)a.hasOwnProperty(c)&&b++;return b}function f(a,b){if(e(a)!=e(b))return!1;for(var c in a)if(a.hasOwnProperty(c)&&b[c]!=a[c])return!1;return!0}function g(a,b){new RegExp("(\\s|^)"+b+"(\\s|$)").test(a.className)||(a.className+=" "+b)}function h(a,b){a.className=a.className.replace(new RegExp("(\\s+|^)"+b+"(\\s+|$)","g")," ").replace(/^\s+|\s+$/g,"")}function i(a){a.parentNode&&a.parentNode.removeChild(a)}function j(a){var c=["Webkit","Moz","ms"],d=b.createElement("div");if(void 0!==d.style[a])return!0;a=a.charAt(0).toUpperCase()+a.slice(1);for(var e in c)if(void 0!==d.style[c[e]+a])return!0;return!1}function k(a,b,c,d){return b?(a.addEventListener(b,c,!!d),function(){l(a,b,c,d)}):void 0}function l(a,b,c,d){b&&a.removeEventListener(b,c,!!d)}function m(a,c){var d=new Date;d.setTime(d.getTime()+31536e7),b.cookie=a+"="+c+";expires="+d.toGMTString()+";"}function n(a){var c=b.cookie.split(";");a+="=";for(var d=0;d<c.length;d++){for(var e=c[d];" "==e.charAt(0);)e=e.substring(1);if(0==e.indexOf(a))return e.substring(a.length,e.length)}}function o(a){a.preventDefault?a.preventDefault():a.returnValue=!1}function p(a,b){return null==b&&(b=a,a=0),a+Math.floor(Math.random()*(b-a+1))}function q(a){return a%2==1}function r(b){var c=!0;for(var d in b)b.hasOwnProperty(d)&&!{string:1,"boolean":1,number:1}[typeof b[d]]&&null!==b[d]&&(a.console&&a.console.warn&&a.console.warn("Chatra integration error: We accept Strings, Numbers, Booleans and null as integration property values. `"+d+"`’s type is `"+typeof b[d]+"`."),c=!1);return c}function s(a){for(var b in a)null!==a[b]&&(a[b]=a[b].toString());return a}function t(a){if(!a)return{};var b=document.createElement("a");return b.href=a,b}function u(){function e(a){return function(){A[a].apply(A,arguments)}}var l=function(){arguments.length&&("function"==typeof l[arguments[0]]&&"_"!=arguments[0][0]?l[arguments[0]].apply(l,Array.prototype.slice.call(arguments,1)):console.warn("Chatra: No such method: "+arguments[0]))},A=l;d(l,{_init:function(){if(!a.ChatraID)return void console.warn("Chatra: No ChatraID specified, shutting down");if(v){var c=A._sniff=v(a.navigator.userAgent),d=A._setup=a.ChatraSetup||{},e=A._displayMode=d.mode||"widget";if(A._isMobile=c.features.mobile,"trident"==c.browser.engine&&c.browser.majorVersion&&c.browser.majorVersion<=8)return void A.kill();if("safari"==c.browser.name&&(A._isSafari=!0),"widget"==e&&d.mobileOnly===!0&&!A._isMobile)return void A.kill();if("widget"==e&&d.disabledOnMobile===!0&&A._isMobile)return void A.kill();A._features={transform:j("transform"),transition:j("transition")};var f=A._iframe=b.createElement("iframe"),g=A._wrapper=b.createElement("div"),h=A._style=b.createElement("style"),i=b.getElementsByTagName("head")[0],k=a.ChatraProtocol||("https:"===location.protocol?"https:":"http:"),l=a.ChatraHost||"chat.chatra.io",m=a.ChatraID,n=A._clientId=A._getClientId(),p=function(c){var e=function(){f||(!A._killed&&c(),f=!0)},f=!1;d.deferredLoading?"complete"===b.readyState?e():a.addEventListener("load",e,!1):(b.attachEvent?"complete"===b.readyState:"loading"!==b.readyState)?e():(b.addEventListener("DOMContentLoaded",e,!1),a.addEventListener("load",e,!1))};i.appendChild(h),h.textContent=y,f.setAttribute("frameborder","0"),f.setAttribute("id","chatra__iframe"),f.setAttribute("allowtransparency","true"),g.setAttribute("id","chatra"),A._addAutoRemovableEvent(a,"message",function(a){var b;try{b=JSON.parse(a.data)}catch(c){return}b.type&&"string"==typeof b.type&&"Chatra"===b.sender&&A._messageHandler(b.type,b.data)}),A._addAutoRemovableEvent(a,"focus",function(){A._postMessage("focus")}),A._addAutoRemovableEvent(a,"blur",function(){A._postMessage("blur")}),d.groupId=window.ChatraGroupID||d.groupId,A._setReferrer();var q=(navigator.language||navigator.userLanguage||"en").split("-")[0],r="hostId="+m+"&mode="+encodeURIComponent(e)+(A._isMobile?"&isMobile=1":"")+(d.buttonStyle?"&buttonType="+d.buttonStyle:"")+"&lang="+q+(d.language&&"string"==typeof d.language?"&langOverride="+d.language:"")+(d.locale?"&locale="+encodeURIComponent(JSON.stringify(d.locale)):"")+("undefined"!=typeof d.groupId?"&groupId="+d.groupId:"")+"&clientId="+n+"&currentPage="+encodeURIComponent(location.href)+"&currentPageTitle="+encodeURIComponent(b.title)+"&prevPage="+encodeURIComponent(b.referrer)+"&referrer="+encodeURIComponent(A._referrer),s=k+"//"+l+"/?"+r.replace("&clientId","#clientId");switch(e){case"frame":var t,u=d.injectTo;if(!u)return console.warn("Chatra: `ChatraSetup.injectTo` is required when using `frame` mode!"),void A.kill();p(function(){return"string"==typeof u?t=b.getElementById(u):u[0]&&u[0].appendChild?t=u[0]:u.appendChild&&(t=u),t?(f.src=s,t.innerHTML="",t.appendChild(f),void(A._attachedToDom=!0)):(console.warn("Chatra: something is wrong with your `ChatraSetup.injectTo`"),void A.kill())});break;default:A._isMobile&&A._addClass("mobile-widget"),A._setButtonType("tab"),A._setChatPosition("br"),f.src=s,f.style.position="absolute",g.appendChild(f),p(function(){if("webkit"==c.browser.engine&&A._addAutoRemovableEvent(g,"wheel",function(a){A._chatExpanded&&o(a)}),A._isMobile&&A._features.transform){setTimeout(function(){A._adjustZoomLevel()},100);var d=A._adjustZoomLevel.bind(A);A._addAutoRemovableEvent(b.body,"touchend",d),A._addAutoRemovableEvent(a,"scroll",d),A._addAutoRemovableEvent(a,"orientationchange",d),A._addAutoRemovableEvent(a,"resize",d)}if(b.body.appendChild(g),A._attachedToDom=!0,a.Shopify&&"function"==typeof a.doShift){var e=a.doShift;a.doShift=function(){var a,b=g.style.position;return g.style.position="absolute",a=e.apply(this,arguments),g.style.position=b,a}}})}a.ChatraIntegration&&A.setIntegrationData(a.ChatraIntegration),d.startHidden&&A.hide(),A.setZIndex("number"==typeof d.zIndex?d.zIndex:A._zIndex),"function"==typeof d.onInit&&d.onInit(),d.colors&&A.setColors(d.colors),d.chatWidth&&A.setChatWidth(d.chatWidth),d.buttonSize&&A.setButtonSize(d.buttonSize),d.chatHeight&&A.setChatHeight(d.chatHeight),d.buttonPosition&&A.setButtonPosition(d.buttonPosition),A.pageView();for(var w=0;w<z.length;w++)A.apply(A,z[w])}},_chatWidth:340,_chatMinWidth:150,_chatHeight:480,_labelHeight:40,_mobileLabelHeight:45,_roundButtonSize:x,_labelMinWidth:80,_zIndex:9999,setIntegrationData:function(a){r(a)&&A._postMessage("integrationData",s(a))},updateIntegrationData:function(a){r(a)&&A._postMessage("updateIntegrationData",s(a))},pageView:function(){var a={currentPage:b.location.href,currentPageTitle:b.title==A._titleBlink.newTitle?A._titleBlink.originalTitle:b.title,referrer:A._referrer};f(A._lastPageData,a)||(A._lastPageData=a,A._chatReady?A._sendPageInfo(a):A._pageInfoQ.push(a),A._setStoredItem("Chatra.lastPageViewAt",+new Date+""))},setChatWidth:function(a){"number"==typeof a&&(A._chatWidth=a,A._recalcChatSize())},setButtonSize:function(a){"number"==typeof a&&(20>a&&(a=20),A._roundButtonSize=a,A._recalcChatSize())},setChatHeight:function(a){"number"==typeof a&&(A._chatHeight=a,A._recalcChatSize())},setZIndex:function(a){"number"==typeof a&&(A._zIndex=a,A._refreshZIndex())},setButtonPosition:function(a){return-1==["lt","lm","lb","bl","bc","br","rt","rm","rb"].indexOf(a)?void console.warn("Chatra: invalid `positionCode`!"):void A._postMessage("setPosition",a)},resetButtonPosition:function(){A._postMessage("resetPosition")},setColors:function(a){A._postMessage("colors",a)},resetColors:function(){A._postMessage("resetColors")},_openChat:function(a){A._postMessage("openChat",!!a)},openChat:function(a){"widget"==A._displayMode&&A._openChat(!!a)},expandWidget:function(a){"widget"==A._displayMode&&(A._isMobile||A._openChat(!!a))},minimizeWidget:function(){"widget"==A._displayMode&&A._postMessage("collapseChat")},expandChat:e("expandWidget"),collapseChat:e("minimizeWidget"),closeChat:e("minimizeWidget"),hide:function(){"widget"==A._displayMode&&(A._chatHiddenByUser=!0,A._refreshChatVisibility())},show:function(){"widget"==A._displayMode&&(A._chatHiddenByUser=!1,A._refreshChatVisibility())},hideChat:e("hide"),showChat:e("show"),setGroupId:function(a){"undefined"!=typeof a&&A._postMessage("setGroupId",a)},_messageHandler:function(a,b){switch(a){case"setHostedItem":if(!b||"object"!=typeof b||"string"!=typeof b.key)return;A._setHostedItem(b.key,b.value);break;case"apiReady":A._apiReady=!0,A._sendMessageQ();break;case"readyToRetriveHostedStorage":A._sendHostedStorage();break;case"readyToRetriveData":A._chatReady=!0,A._sendFocus(),A._resolvePageInfoQ();break;case"headerReady":A._chatRendered=!0,"function"==typeof A._setup.onRendered&&A._setup.onRendered();break;case"collapseWindow":A._collapseChatWindow();break;case"expandWindow":A._expandChatWindow();break;case"hideChat":A._hideChatFromFrame();break;case"showChat":A._showChatFromFrame();break;case"setPosition":if(!b||"string"!=typeof b)return;A._setChatPosition(b);break;case"buttonType":if(!b||"string"!=typeof b)return;A._setButtonType(b);break;case"titleBlink":A._titleBlink(b);break;case"banned":A._setStoredItem("Chatra.banned",b?A._clientId:""),A._refreshChatVisibility();break;case"restart":A.restart();break;case"analyticsEvent":if(!b||"string"!=typeof b)return;A._logAnalyticsEvent(b);break;case"labelWidth":if("number"!=typeof b&&null!==b)return;A._animating(),A._computedLabelWidth=b,A._recalcChatSize();break;case"s":A.kill()}},_hideChatFromFrame:function(){A._chatHiddenByFrame=!0,A._refreshChatVisibility()},_showChatFromFrame:function(){A._chatHiddenByFrame=!1,A._refreshChatVisibility()},_getScaleLevel:function(){var c,d=a.screen,e=d.width;return{90:1,"-90":1}[a.orientation]&&("ios"==A._sniff.os.name&&"webkit"==A._sniff.browser.engine||"winphone"==A._sniff.os.name&&"trident"==A._sniff.browser.engine)&&(e=d.height),c=e<b.documentElement.clientWidth?a.innerWidth/e:a.innerWidth/b.documentElement.clientWidth},_adjustZoomLevel:function(){if(A._isMobile&&A._features.transform){var a=1;A._chatExpanded||(a=A._getScaleLevel()),a>.9&&1.1>a&&(a=1),a=a.toFixed(2),a>1&&"webkit"==A._sniff.browser.engine&&"ios"==A._sniff.os.name&&(a=1),a!=A._scale&&(A._transform.scale=a,A._recalcTransform(),A._scale=a)}},_transform:{},_recalcTransform:function(){var a=(A._transform,"");for(var b in A._transform)A._transform[b]&&A._transform.hasOwnProperty(b)&&(a+=" "+b+"("+A._transform[b]+")");a||(a="none"),A._wrapper.style.transform=A._wrapper.style.WebkitTransform=A._wrapper.style.MozTransform=A._wrapper.style.msTransform=a},_refreshZIndex:function(){A._wrapper.style.zIndex=A._chatExpanded&&A._isMobile?2147483647:A._zIndex},_refreshChatVisibility:function(){!A._chatRendered||A._chatHiddenByUser||A._chatHiddenByFrame||A._getStoredItem("Chatra.banned")===A._clientId?(A._visible=!1,A._removeClass("visible"),A._postMessage("visible",!1)):(A._visible=!0,A._addClass("visible"),A._postMessage("visible",!0),A._isSafari&&A._safariForceRedraw()),A._refreshMobileBodyFix(),A._refreshMobileViewportFix()},_refreshMobileBodyFix:function(){if(A._isMobile&&"widget"==A._displayMode)if(A._visible&&A._chatExpanded){if(A._mobileBodyFixEnabled)return;A._lastScrollTop=a.pageYOffset,A._lastScrollLeft=a.pageXOffset,g(b.body,"chatra-mobile-widget-expanded"),g(b.documentElement,"chatra-mobile-widget-expanded"),A._mobileBodyFixEnabled=!0}else{if(!A._mobileBodyFixEnabled)return;h(b.body,"chatra-mobile-widget-expanded"),h(b.documentElement,"chatra-mobile-widget-expanded"),a.scrollTo(A._lastScrollLeft,A._lastScrollTop),A._mobileBodyFixEnabled=!1}},_refreshMobileViewportFix:function(){if(A._isMobile&&"widget"==A._displayMode&&("android"==A._sniff.os.name&&"webkit"==A._sniff.browser.engine&&A._sniff.os.majorVersion>=4||"ios"==A._sniff.os.name&&"webkit"==A._sniff.browser.engine))if(A._visible&&A._chatExpanded){var a=b.querySelector('meta[name="viewport"]');a||(a=b.createElement("meta"),a.setAttribute("name","viewport"),document.head.appendChild(a)),A._lastMetaViewportValue=a.getAttribute("content")||"",a.setAttribute("content","width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no")}else{var a=b.querySelector('meta[name="viewport"]');a&&"string"==typeof A._lastMetaViewportValue&&a.setAttribute("content",A._lastMetaViewportValue)}},_chatExpanded:!1,_expandChatWindow:function(){A._chatExpanded=!0,A._addClass("expanded"),A._refreshZIndex(),A._adjustZoomLevel(),A._isSafari&&(A._isToggling=!0),A._animating(function(){A._isSafari&&(A._isToggling=!1,A._recalcChatSize()),A._refreshMobileViewportFix(),setTimeout(A._refreshMobileBodyFix.bind(A),250)}),A._recalcChatSize()},_collapseChatWindow:function(){A._chatExpanded=!1,A._refreshMobileViewportFix(),A._refreshMobileBodyFix(),A._removeClass("expanded"),A._isSafari&&(A._isToggling=!0),A._animating(function(){A._isSafari&&(A._isToggling=!1,A._recalcChatSize()),A._adjustZoomLevel(),A._refreshZIndex()}),A._recalcChatSize()},_animating:function(a){if(!A._features.transition||!A._chatRendered||"round"==A._buttonType||A._isMobile)return void(a&&a());A._addClass("animating"),clearTimeout(A._animatingTimer);var b=w+30;A._animatingTimer=setTimeout(function(){A._removeClass("animating"),a&&a()},b)},_setChatPosition:function(a){var b={r:"right",l:"left",c:"center",t:"top",b:"bottom",m:"middle"},c=b[a.charAt(0)],d=b[a.charAt(1)];if(A._isMobile){var e=["0%","0%"];"center"==d&&(e[0]="50%"),"right"!=d&&"right"!=c||(e[0]="100%"),"middle"==d&&(e[1]="50%"),"bottom"==c&&(e[1]="100%"),A._wrapper.style.transformOrigin=A._wrapper.style.WebkitTransformOrigin=A._wrapper.style.MozTransformOrigin=A._wrapper.style.msTransformOrigin=e.join(" ")}A._side&&A._removeClass("side-"+A._side),A._position&&A._removeClass("pos-"+A._position),A._addClass("side-"+c),A._addClass("pos-"+d),A._side=c,A._position=d,A._recalcChatSize()},_setButtonType:function(a){A._buttonType&&A._removeClass("style-"+A._buttonType),A._addClass("style-"+a),A._buttonType=a,A._recalcChatSize()},_recalcChatSize:function(){var a,b,c=A._chatExpanded&&A._isMobile;A._chatExpanded?(a=Math.max(A._chatWidth,A._chatMinWidth),b=A._chatHeight):"round"==A._buttonType?a=b=A._roundButtonSize+10:(a=Math.max(A._computedLabelWidth||A._chatWidth,A._labelMinWidth),b=A._isMobile?A._mobileLabelHeight:A._labelHeight,{left:1,right:1}[A._side]&&(b=[a,a=b][0])),"center"==A._position&&!c&&q(a)?A._addClass("correct-odd-width"):A._removeClass("correct-odd-width"),"middle"==A._position&&!A._chatExpanded&&q(b)?A._addClass("correct-odd-height"):A._removeClass("correct-odd-height"),c?(A._wrapper.style.width="100%",A._wrapper.style.height="100%"):(A._wrapper.style.width=a+"px",A._wrapper.style.height=b+"px"),"middle"!=A._position||A._chatExpanded?"top"!=A._position||A._chatExpanded||"round"==A._buttonType?A._transform.translateY=void 0:A._transform.translateY=A._isToggling&&A._isSafari?b+"px":"100%":A._transform.translateY=A._isToggling&&A._isSafari?b/2+"px":"50%","center"!=A._position||c?A._transform.translateX=void 0:A._transform.translateX="-50%",A._recalcTransform()},_postMessageQ:[],_postMessage:function(a,b){A._apiReady?A._iframe.contentWindow&&A._iframe.contentWindow.postMessage(JSON.stringify({type:a,data:b,sender:"Chatra"}),"*"):A._postMessageQ.push([a,b])},_sendMessageQ:function(){for(;A._postMessageQ.length;)A._postMessage.apply(A,A._postMessageQ.shift())},_pageInfoQ:[],_resolvePageInfoQ:function(){for(;A._pageInfoQ.length;)A._sendPageInfo(A._pageInfoQ.shift())},_sendPageInfo:function(a){A._postMessage("clientData",a)},_sendFocus:function(){var a=b.hasFocus?b.hasFocus():!0;A._postMessage(a?"focus":"blur")},_getClientId:function(){var a=A._getStoredItem("Chatra.clientId"),b=A._setup.clientId;if(b){var c=typeof b;"string"!==c&&"number"!==c||(a=b)}if(a)return a;for(var d=(+new Date+"").split(""),e="abcdefghijklmnopqrstuvwxyz".split("");e.length;)d.splice(p(d.length-1),0,e.splice(p(e.length-1),1)[0]);return a=d.join(""),A._setStoredItem("Chatra.clientId",a),a},_setReferrer:function(){var a=b.referrer;if(a&&t(a).host!==location.host)A._referrer=a;else{var c=+(A._getStoredItem("Chatra.lastPageViewAt")||"");new Date-c>36e5?A._referrer=a:A._referrer=A._getStoredItem("Chatra.referrer")||""}A._setStoredItem("Chatra.referrer",A._referrer||"")},_getHostedStorage:function(){var a;try{a=JSON.parse(A._getStoredItem("Chatra.hostedItems"))}catch(b){a={},A._setStoredItem("Chatra.hostedItems","{}")}return a},_sendHostedStorage:function(){A._postMessage("hostedStorage",A._getHostedStorage())},_setHostedItem:function(a,b){if("string"==typeof a){var c=A._getHostedStorage();c[a]=b,A._setStoredItem("Chatra.hostedItems",JSON.stringify(c))}},_getStoredItem:function(a){var b;try{b=localStorage.getItem(a)}catch(c){}return b||n(a)},_setStoredItem:function(a,b){try{localStorage.setItem(a,b)}catch(c){m(a,b)}},_addClass:function(a){for(var b=A._wrapper,c=[].concat(a),d=c.length-1;d>=0;d--)g(b,"chatra--"+c[d])},_removeClass:function(a){for(var b=A._wrapper,c=[].concat(a),d=c.length-1;d>=0;d--)h(b,"chatra--"+c[d])},_safariForceRedraw:function(){A._wrapper.style.zIndex=A._zIndex-1,A._wrapper.offsetWidth,A._refreshZIndex()},_titleBlink:function(a){clearTimeout(A._titleBlink._timeout),A._titleBlink.originalNow&&a?(A._titleBlink.originalTitle=b.title,b.title=A._titleBlink.newTitle=a):(b.title!==A._titleBlink.newTitle&&(A._titleBlink.originalTitle=b.title),b.title=A._titleBlink.originalTitle),A._titleBlink.originalNow=!a||!A._titleBlink.originalNow,a&&(A._titleBlink._timeout=setTimeout(function(){A._titleBlink(a)},p(1e3,2e3)))},_logAnalyticsEvent:function(b){try{"function"==typeof A._setup.onAnalyticEvent&&A._setup.onAnalyticEvent(b)}catch(c){console.warn("Chatra: Error while trying to execute `ChatraSetup.onAnalyticEvent` function:",c)}try{var d;if(a.GoogleAnalyticsObject&&"function"==typeof a[a.GoogleAnalyticsObject]?d=a[a.GoogleAnalyticsObject]:"function"==typeof a.ga&&(d=a.ga),d)if(A._setup.gaTrackerName){if("function"==typeof d.getByName){var e=d.getByName(A._setup.gaTrackerName);e&&"function"==typeof e.send&&e.send("event","Chatra",b),e||console.warn("Chatra: Could’t find Google Analytics tracker named “"+A._setup.gaTrackerName+"”.")}}else if(A._setup.gaTrackingId){if("function"==typeof d.getAll){var f=(d.getAll()||[]).some(function(a){return a&&"function"==typeof a.get&&a.get("trackingId")==A._setup.gaTrackingId?("function"==typeof a.send&&a.send("event","Chatra",b),!0):void 0});f||console.warn("Chatra: Could’t find Google Analytics tracker with tracking ID “"+A._setup.gaTrackingId+"”.")}}else{var e="function"==typeof d.getAll?(d.getAll()||[])[0]:void 0;e&&"function"==typeof e.send?e.send("event","Chatra",b):d("send","event","Chatra",b)}else a._gaq&&"function"==typeof a._gaq.push&&a._gaq.push(["_trackEvent","Chatra",b])}catch(c){console.warn("Chatra: Error while trying to create Google Analytics event:",c)}try{var g,h=a.Ya&&(a.Ya.Metrika||a.Ya.Metrika2),i=h&&"function"==typeof h.counters&&h.counters(),j=i&&i[0]&&i[0].id;j&&a["yaCounter"+j]&&(g=a["yaCounter"+j]),g&&g.reachGoal&&g.reachGoal("Chatra_"+b.replace(/\s/g,"_"))}catch(c){console.warn("Chatra: Error while trying to create Yandex.Metrika event:",c)}},_eventRemovers:[],_addAutoRemovableEvent:function(){A._eventRemovers.push(k.apply(A,arguments))},_cleanEventListeners:function(){for(var a=A._eventRemovers.length-1;a>=0;a--)A._eventRemovers[a]()},restart:function(){u()},kill:function(){A._cleanEventListeners(),A._iframe&&(i(A._iframe),A._iframe.src=""),A._wrapper&&i(A._wrapper),A._killed=!0;for(var a in A)A.hasOwnProperty(a)&&"function"==typeof A[a]&&"restart"!=a&&(A[a]=c)}}),a.Chatra&&a.Chatra.kill&&a.Chatra.kill(),a.Chatra=l,a.Chatra._init()}var v=function(a){function b(){for(var a in g)c(g[a])}function c(b){for(var c=0;c<b.length;c++){for(var e=!0,f=0;f<b[c].test.length;f++)if(b[c].test[f]instanceof RegExp){if(!b[c].test[f].test(a)){e=!1;break}}else if(-1==a.indexOf(b[c].test[f])){e=!1;break}if(e){d(b[c]);break}}}function d(a){for(var b in g)if(g.hasOwnProperty(b)&&a[b]){if(a[b].$version){var c=e(a[b].$version.search);if(c){var d=c.split("."),h=a[b].$version.names,i=a[b].$version.altNames;if(a[b].version=c,d[0]&&(a[b].majorVersion=parseInt(d[0])),d[1]&&(a[b].minorVersion=parseInt(d[1])),d[2]&&(a[b].patchVersion=parseInt(d[2])),h)for(var j,k=[],l=0;l<d.length;l++)k.push(d[l]),j=k.join("."),h[j]&&(a[b].versionName=h[j]),i&&i[j]&&(a[b].versionAltNames=i[j])}}for(var m in a[b])a[b].hasOwnProperty(m)&&"$"!==m[0]&&(f[b][m]=a[b][m])}}function e(b){var c;if(b instanceof RegExp){if(c=(a.match(b)||[])[0],!c)return}else c=b;var d,e=a.indexOf(c);if(-1!=e&&(d=a.substring(e+c.length),regexpResult=/^(\d+(\.|_)){0,2}\d+/.exec(d),regexpResult))return regexpResult[0].replace(/_/g,".")}var f={browser:{fullName:"",name:"",version:"",majorVersion:null,minorVersion:null,patchVersion:null,engine:""},os:{fullName:"",name:"",version:"",versionName:"",versionAltNames:[],majorVersion:null,minorVersion:null,patchVersion:null},features:{bw:!1,mobile:!1,tv:!1,proxy:!1}},g={browser:[{test:["SailfishBrowser"],browser:{fullName:"Sailfish Browser",name:"sailfishbrowser",engine:"gecko",$version:{search:"SailfishBrowser/"}},features:{mobile:!0}},{test:["Edge/"],browser:{fullName:"Edge",name:"edge",engine:"edgehtml",$version:{search:"Edge/"}}},{test:["MSIE"],browser:{fullName:"Internet Explorer",name:"ie",engine:"trident",$version:{search:"MSIE "}}},{test:["Trident"],browser:{fullName:"Internet Explorer",name:"ie",engine:"trident",$version:{search:"rv:"}}},{test:["OPR/"],browser:{fullName:"Opera",name:"opera",engine:"webkit",$version:{search:"OPR/"}}},{test:["Chrome"],browser:{fullName:"Chrome",name:"chrome",engine:"webkit",$version:{search:"Chrome/"}}},{test:["Firefox"],browser:{fullName:"Firefox",name:"firefox",engine:"gecko",$version:{search:"Firefox/"}}},{test:["NokiaBrowser"],browser:{fullName:"Nokia Browser",name:"nokiabrowser",engine:"webkit",$version:{search:"NokiaBrowser/"}},features:{mobile:!0}},{test:["Opera Mini","Presto"],browser:{fullName:"Opera Mini",name:"operamini",engine:"presto",$version:{search:"Version/"}},features:{mobile:!0,proxy:!0}},{test:["Opera Mini","WebKit"],browser:{fullName:"Opera Mini",name:"operamini",engine:"webkit"},features:{mobile:!0,proxy:!0}},{test:["Opera"],browser:{fullName:"Opera",name:"opera",engine:"presto",$version:{search:"Version/"}}},{test:["OviBrowser"],browser:{fullName:"Ovi Browser",name:"ovi",engine:"gecko",$version:{search:"OviBrowser/"}},features:{mobile:!0,proxy:!0}},{test:["CriOS/"],browser:{fullName:"Chrome",name:"chrome",engine:"webkit",$version:{search:"CriOS/"}}},{test:["Coast/"],browser:{fullName:"Opera Coast",name:"coast",engine:"webkit",$version:{search:"Coast/"}}},{test:["Safari","Version/",/(iPhone|iPod|iPad|Macintosh|Windows)/],browser:{fullName:"Safari",name:"safari",engine:"webkit",$version:{search:"Version/"}}},{test:["WebKit"],browser:{engine:"webkit"}},{test:["Gecko/"],browser:{engine:"gecko"}}],os:[{test:["Sailfish"],os:{fullName:"Sailfish OS",name:"sailfish"},features:{mobile:!0}},{test:["Windows Phone"],os:{fullName:"Windows Phone",name:"winphone",$version:{search:"Windows Phone "}},features:{mobile:!0}},{test:["Windows"],os:{fullName:"Windows",name:"win",$version:{search:"Windows NT ",names:{"10.0":"10",6.3:"8.1",6.2:"8",6.1:"7","6.0":"Vista",5.2:"XP",5.1:"XP",5.01:"2000","5.0":"2000"},altNames:{5.2:["Server 2003"]}}}},{test:["Macintosh","OS X 10"],os:{fullName:"Mac OS X",name:"osx",$version:{search:/OS X /,names:{10.6:"Snow Leopard",10.7:"Lion",10.8:"Mountain Lion",10.9:"Mavericks","10.10":"Yosemite",10.11:"El Capitan"}}}},{test:["Ubuntu"],os:{fullName:"Ubuntu",name:"ubuntu"}},{test:["Fedora"],os:{fullName:"Fedora",name:"fedora",$version:{search:"Fedora/",names:{20:"Heisenbug",19:"Schrödinger's Cat",18:"Spherical Cow",17:"Beefy Miracle",16:"Verne",15:"Lovelock",14:"Laughlin",13:"Goddard",12:"Constantine",11:"Leonidas",10:"Cambridge",9:"Sulphur",8:"Werewolf",7:"Moonshine"}}}},{test:["Kindle"],os:{fullName:"Kindle",name:"kindle",$version:{search:"Kindle/"}},features:{bw:!0,mobile:!0}},{test:[/(BlackBerry|BB\d+)/],os:{fullName:"BlackBerry",name:"blackberry",$version:{search:"Version/"}},features:{mobile:!0}},{test:["Symbian"],os:{fullName:"Symbian",name:"symbian"},features:{mobile:!0}},{test:["Series40"],os:{fullName:"Symbian",name:"symbian"},features:{mobile:!0}},{test:["PlayStation Vita"],os:{fullName:"PlayStation Vita",name:"psvita"},features:{mobile:!0}},{test:["Nintendo DSi"],os:{fullName:"Nintendo DSi",name:"dsi"},features:{mobile:!0}},{test:["New Nintendo 3DS"],os:{fullName:"New Nintendo 3DS",name:"n3ds"},browser:{engine:"webkit"},features:{mobile:!0}},{test:["Nintendo 3DS"],os:{fullName:"Nintendo 3DS",name:"3ds"},browser:{engine:"webkit"},features:{mobile:!0}},{test:["Viera"],os:{fullName:"Viera",name:"viera"},browser:{engine:"webkit"},features:{tv:!0}},{test:["SonyDTV"],features:{tv:!0}},{test:["Android"],os:{fullName:"Android",name:"android",$version:{search:"Android "}},features:{mobile:!0}},{test:[/iPhone|iPod|iPad/],os:{fullName:"iOS",name:"ios",$version:{search:"OS "}},features:{mobile:!0}}],features:[{test:[/mobile/i],features:{mobile:!0}},{test:[/smart-{0,1}tv/i],features:{tv:!0}}]};return a?(b(),f):f},w=350,x=60,y="#chatra{visibility:hidden;opacity:0;position:fixed;max-height:95%;max-width:90%;max-width:calc(100% - 40px);transition:0.2s linear;transition-property:visibility, opacity;overflow:hidden;-webkit-backface-visibility:hidden;backface-visibility:hidden;width:auto;height:auto;min-height:0;min-width:0;display:block;box-sizing:content-box;padding:0;margin:0} #chatra.chatra--animating{transition:"+w+"ms cubic-bezier(.04,.74,.4,.98);transition-property:height, width, max-width, max-height, top, bottom, left, right, margin-bottom, visibility, opacity, transform, -webkit-transform, -moz-transform;}#chatra.chatra--animating.chatra--mobile-widget{transition:none;} #chatra.chatra--animating.chatra--style-round{transition:none;} #chatra.chatra--side-bottom{bottom:0;} #chatra.chatra--side-bottom.chatra--style-round:not(.chatra--expanded){bottom:15px;} #chatra.chatra--side-left{left:0;} #chatra.chatra--side-left.chatra--style-round:not(.chatra--expanded){left:15px;} #chatra.chatra--side-right{right:0;} #chatra.chatra--side-right.chatra--style-round:not(.chatra--expanded){right:15px;} #chatra.chatra--side-left.chatra--expanded{bottom:0;left:20px;} #chatra.chatra--side-right.chatra--expanded{bottom:0;right:20px;} #chatra.chatra--pos-right{right:20px;} #chatra.chatra--pos-right.chatra--style-round:not(.chatra--expanded){right:15px;} #chatra.chatra--pos-left{left:20px;} #chatra.chatra--pos-left.chatra--style-round:not(.chatra--expanded){left:15px;} #chatra.chatra--pos-center{left:50%;} #chatra.chatra--pos-top.chatra--style-tab:not(.chatra--expanded){bottom:100%;margin-bottom:-40px;max-height:90%;max-height:calc(100% - 80px);} #chatra.chatra--pos-top.chatra--style-round:not(.chatra--expanded){top:15px;} #chatra.chatra--pos-bottom:not(.chatra--expanded){bottom:40px;max-height:90%;max-height:calc(100% - 80px);} #chatra.chatra--pos-bottom.chatra--style-round:not(.chatra--expanded){bottom:15px;} #chatra.chatra--pos-middle:not(.chatra--expanded){bottom:50%;max-height:90%;max-height:calc(100% - 80px);} #chatra.chatra--visible{visibility:visible;opacity:1;} #chatra.chatra--mobile-widget.chatra--pos-right{right:15px;} #chatra.chatra--mobile-widget.chatra--pos-left{left:15px;} #chatra.chatra--mobile-widget.chatra--expanded{max-width:none;max-height:none;} #chatra.chatra--mobile-widget.chatra--expanded.chatra--pos-right{right:0;} #chatra.chatra--mobile-widget.chatra--expanded.chatra--pos-left{left:0;} #chatra.chatra--mobile-widget.chatra--expanded.chatra--pos-center{left:0;} #chatra.chatra--mobile-widget.chatra--expanded.chatra--side-right{right:0;} #chatra.chatra--mobile-widget.chatra--expanded.chatra--side-left{left:0;} html.chatra-mobile-widget-expanded, body.chatra-mobile-widget-expanded{overflow:hidden !important;height:100% !important;width:100% !important;position:fixed !important;margin:0 !important;}#chatra__iframe{left:0 !important;top:0 !important;width:100% !important;min-width:100% !important;max-width:100% !important;height:100% !important;min-height:100% !important;max-height:100% !important;margin:0 !important;display:block !important;background:transparent !important;} #chatra.chatra--correct-odd-width{padding-left:1px;}#chatra.chatra--correct-odd-width #chatra__iframe{left:1px !important;width:calc(100% - 1px) !important;}#chatra.chatra--correct-odd-height{padding-top:1px;}#chatra.chatra--correct-odd-height #chatra__iframe{top:1px !important;height:calc(100% - 1px) !important;}@media print {#chatra{display:none;}}",z=[];a.Chatra&&a.Chatra.q&&(z=a.Chatra.q),u()}(window,document);