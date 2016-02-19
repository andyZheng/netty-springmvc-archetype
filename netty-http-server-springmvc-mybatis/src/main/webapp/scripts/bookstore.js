/**
 * Common functionalities for the book store application.
 */


var browseHistory = new BrowseHistory();

$(function() {
	$("#cartCountContainer").text(cart.count);
});

function toggleList(btn, list) {
	var text = $(btn).text();
	text = (text == "收起" ? "展开" : "收起");
	$(btn).text(text);
	$(list).toggle();
}

function showBook(id) {
	window.open("/bookstore/detail.do?bookId=" + encodeURIComponent(id));
}

// 显示弹出层（购买确认框）
var dlg;
function showDlg(btn, tpl, params) {
	// 先删除其他弹出框
	$(".bylayer").remove();
	var top = $(btn).offset().top - 100;
	var left = $(btn).offset().left - 800;
	dlg = $("<div class='bylayer'>" + $(tpl).parse(params) + "</div>");
	dlg.appendTo($("body"));
	dlg.css({"top":top}).show();
}

function onlineRead(baseurl,bookId) {
	window.open(baseurl+"/bookstore/onlineRead.do?bookId="+ encodeURIComponent(bookId));
}

function download(bookId){
	window.open("/bookstore/download.do?userBookid="+ encodeURIComponent(bookId));
}

// 收藏
function collect(guid) {
	jQuery.get("/bookstore/collect.do", {bookId: guid}, function(rt) {
		if ("ok" == rt) {
			alert("收藏成功");
		}
	});
}

function buyBook(btn, bookId, title, coverUrl, publisher, author, price) {
	$.get("/bookstore/canSale.do", {id: bookId}, function(rt) {
		if (rt == "false") {
			alert("本书不允许销售，请购买其他图书!");
			return;
		}
		
		cart.buy({
			id : bookId,
			title : title,
			coverUrl : coverUrl,
			publisher : publisher,
			author : author,
			price : price
		}, function(item) {
			$("#cartCountContainer").text(cart.count);
			showDlg(btn, '#confirmBuyDlg');
		});
	});
}

function removeBuy(bookId) {
	cart.removeBuy(bookId, function() {
		window.location.reload();
	});
}

function confirmRent(btn, bookId, title, coverUrl, publisher, author, price) {
	$.get("/bookstore/canSale.do", {id: bookId}, function(rt) {
		if (rt == "false") {
			alert("本书不允许销售，请购买其他图书!");
			return;
		}
		
		if (cart.isRentExist(bookId)) {
			alert("购物车中已经包含选择的商品！");
			return;
		}
		var t = new Date();
		t = t.addWeeks(1);
		showDlg(btn, "#confirmRentDlg", {
			"id" : bookId,
			"title" : title,
			"coverUrl" : coverUrl,
			"publisher" : publisher,
			"author" : author,
			"price" : price,
			"fixedPrice" : price.toFixed(2),
			"expireTime" : t.format("yyyy-MM-dd")
		});
	});
}

function rentBook(bookId, title, coverUrl, publisher, author, price, duration) {
	cart.rent({
		id : bookId,
		title : title,
		coverUrl : coverUrl,
		publisher : publisher,
		author : author,
		price : price,
		duration : parseInt(duration)
	}, function(item) {
		$("#cartCountContainer").text(cart.count);
	});
}

//选择租阅期限
function changeRentDuration(price) {
	var duration = $("#duration", dlg).val();
	var total = price * duration;
	total = total.toFixed(2);
	$("#fee", dlg).text(total);
	
	var t = new Date();
	t = t.addWeeks(duration);
	$('#expireTime', dlg).text(t.format("yyyy-MM-dd"));
}

function setRentDuration(id, duration, input, orgDuration) {
	duration = parseInt(duration); 
	if (isNaN(duration) || (duration < 1) || (duration > 4)) {
		if (input) {
			input.value = orgDuration;
		}
		return;
	} 
	cart.setRentDuration(id, duration);
	window.location.reload();
}

function removeRent(bookId) {
	cart.removeRent(bookId, function() {
		window.location.reload();
	});
}

//确认购买服务
function confirmService(btn) {
	$.getJSON("/bookstore/getServiceInfo.do", function(model) {
		if (cart.isServiceExist(model.id.toString())) {
			alert("购物车中已经包含选择的商品！");
			return;
		}
		var t = new Date();
		t = t.addMonths(1);
		model.expireTime = t.format("yyyy-MM-dd");
		model.fixedPrice = model.price.toFixed(2);
		showDlg(btn, "#confirmServiceDlg", model);
	});
}

// 选择服务期限
function changeServiceDuration(price) {
	var duration = $("#duration", dlg).val();
	var total = price * duration;
	total = total.toFixed(2);
	$("#fee", dlg).text(total);
	
	var t = new Date();
	t = t.addMonths(duration);
	$('#expireTime', dlg).text(t.format("yyyy-MM-dd"));
}

// 添加服务到购物车
function buyService(id, title, price) {
	var duration = $("#duration", dlg).val();
	cart.service({
		id : id,
		title : title,
		price : price,
		duration : parseInt(duration)
	}, function() {
		$("#cartCountContainer").text(cart.count);
	});
	dlg.hide();
}

function setServiceDuration(id, duration, input, orgDuration) {
	duration = parseInt(duration); 
	if (isNaN(duration) || (duration < 1) || (duration > 6)) {
		if (input) {
			input.value = orgDuration;
		}
		return;
	} 
	cart.setServiceDuration(id, duration);
	window.location.reload();
}

function removeService(id) {
	cart.removeService(id, function() {
		window.location.reload();
	});
}

function clearCart() {
	if (confirm("确定要清空购物车？")) {
		cart.clear();
		window.location.reload();
	}
}

// 打开购物车页面
function showShoppingCart() {
	window.open("/bookstore/showShoppingCart.do", "shoppingCart");
}

function clickCategory(code) {
	$("#pageNo").val(1);
	$("#pageSize").val(5);
	$("#searchField").val("");
    $("#searchTerm").val("");
	$("#s_kfmpColumnCode").val(code);
	$("#searchForm").submit();
}

function gotoPage(pageNo) {
	$("#pageNo").val(pageNo);
	$("#searchForm").submit();
}

function setTab(m,n) {
	var menu = document.getElementById("newMenu"+m).getElementsByTagName("li");  
	var showdiv=document.getElementById("contT"+m).getElementsByTagName("div");  
	for(i=0;i<menu.length;i++)
	{
		menu[i].className=i==n?"sel":"";  
		showdiv[i].style.display=i==n?"block":"none";  
	}
}

function BrowseHistory() {
    this.cookieName = "browseHistory";
	this.cookieOpts = {
		path : "/"
	};
	this.itemDelimiter = ",";
	this.attrDelimiter = ":";
	this.maxLength = 10;

	this.add = function(guid, title) {
		var item = guid + this.attrDelimiter + title;
		var datas = $.cookie(this.cookieName);
		if (datas) {
			datas = datas.split(this.itemDelimiter);
			for (var i = 0; i < datas.length; i++) {
				var data = datas[i].split(this.attrDelimiter);
				if (data[0] == guid) {
					var tmp = datas.splice(i, 1);
					datas.unshift(tmp[0]);
					break;
				}
			}
			if (i == datas.length) {
				datas.unshift(item);
			}
			if (datas.length > this.maxLength) {
				datas.pop();
			}
		} else {
			datas = new Array();
			datas.unshift(item);
		}
	
		datas = datas.join(this.itemDelimiter);
		$.cookie(this.cookieName, datas, this.cookieOpts);
	};

	this.print = function(container, callback) {
		var html = "";
		var datas = $.cookie(this.cookieName);
		if (datas) {
			datas = datas.split(this.itemDelimiter);
			for (var i = 0; i < datas.length; i++) {
				var data = datas[i].split(this.attrDelimiter);
				var guid = data[0];
				var title = data[1];
				html += callback(guid, title);
			}
		}
		$(container).html(html);
	}
}

/**
 * Constructor for a {ShoppingCar} object.
 * 
 * @returns {ShoppingCart}
 */
function ShoppingCart() {
	this.buyCookieName = "buy";
	this.rentCookieName = "rent";
	this.serviceCookieName = "service";
	this.cookieOpts = {
		expires : 7,
		path : "/"
	};
	this.count = 0;
	var items = this.getBuyItems();
	for (var id in items) {
		this.count++;
	}
	for (var id in this.getRentItems()) {
		this.count++;
	}
	for (var id in this.getServiceItems()) {
		this.count++;
	}
}

ShoppingCart.prototype = {
	encodeFields : ['id', 'title', 'coverUrl', 'publisher', 'author'],
	totalCount : function() {
		var count = 0;
		var items = this.getBuyItems();
		for (var id in items) {
			count++;
		}
		for (var id in this.getRentItems()) {
			count++;
		}
		for (var id in this.getServiceItems()) {
			count++;
		}
		return count;
	},
	_checkExist : function(cookieName, toCheckId) {
		toCheckId = $.base64Encode(toCheckId);
		var items = $.JSONCookie(cookieName);
		for (var id in items) {
			if (id == toCheckId) {
				return true;
			}
		}
		return false;
	},
	isBuyExist : function(id) {
		return this._checkExist(this.buyCookieName, id);
	},
	isRentExist : function(id) {
		return this._checkExist(this.rentCookieName, id);
	},
	isServiceExist : function(id) {
		return this._checkExist(this.serviceCookieName, id);
	},
	_add : function(cookieName, newItem, successCallback) {		
		for (var i = 0; i < this.encodeFields.length; i++) {
			var field = this.encodeFields[i];
			if (newItem[field]) {
				newItem[field] = $.base64Encode(newItem[field]);
			}
		}
		var items = $.JSONCookie(cookieName);
		for (var id in items) {
			if (id == newItem.id) {
				alert("购物车中已经包含选择的商品！");
				return;
			}
		}
		items[newItem.id] = newItem;
		$.JSONCookie(cookieName, items, this.cookieOpts);
		this.count = this.totalCount();
		if (successCallback) {
			successCallback(newItem);
		}
	},
	buy : function(newItem, successCallback) {
		this._add(this.buyCookieName, newItem, successCallback);
	},
	rent : function(newItem, successCallback) {
		this._add(this.rentCookieName, newItem, successCallback);
	},
	service : function(newItem, successCallback) {
		this._add(this.serviceCookieName, newItem, successCallback);
	},
	_remove : function(cookieName, toRemoveId, successCallback) {
		toRemoveId = $.base64Encode(toRemoveId);
		var items = $.JSONCookie(cookieName);
		for (var id in items) {
			if (id == toRemoveId) {
				delete items[toRemoveId];
				$.JSONCookie(cookieName, items, this.cookieOpts);
				this.count = this.totalCount();
				if (successCallback) {
					successCallback();
				}
				return;
			}
		}
	},
	removeBuy : function(id, successCallback) {
		this._remove(this.buyCookieName, id, successCallback);
	},
	removeRent : function(id, successCallback) {
		this._remove(this.rentCookieName, id, successCallback);
	},
	removeService : function(id, successCallback) {
		this._remove(this.serviceCookieName, id, successCallback);
	},
	_getItems : function(cookieName, callback) {
		var items = $.JSONCookie(cookieName);
		for (var id in items) {
			var item = items[id];
			for (var i = 0; i < this.encodeFields.length; i++) {
				var field = this.encodeFields[i];
				if (item[field]) {
					item[field] = $.base64Decode(item[field]);
				}
			}
			if (callback) {
				callback(item);
			}
		}
		return items;
	},
	getBuyItems : function(callback) {
		return this._getItems(this.buyCookieName, callback);
	},
	getRentItems : function(callback) {
		return this._getItems(this.rentCookieName, callback);
	},
	getServiceItems : function(callback) {
		return this._getItems(this.serviceCookieName, callback);
	},
	buyTotalPrice : function() {
		var total = 0;
		this.getBuyItems(function(item) {
			total += item.price;
		});
		return total.toFixed(2);
	},
	rentTotalPrice : function() {
		var total = 0;
		this.getRentItems(function(item) {
			total += item.price * item.duration;
		});
		return total.toFixed(2);
	},
	serviceTotalPrice : function() {
		var total = 0;
		this.getServiceItems(function(item) {
			total += item.price * item.duration;
		});
		return total.toFixed(2);
	},
	totalPrice : function() {
		var total = parseFloat(this.buyTotalPrice()) + parseFloat(this.rentTotalPrice()) + parseFloat(this.serviceTotalPrice());
		return total.toFixed(2);
	},
	clear : function() {
		$.cookie(this.buyCookieName, null, this.cookieOpts);
		$.cookie(this.rentCookieName, null, this.cookieOpts);
		$.cookie(this.serviceCookieName, null, this.cookieOpts);
	},
	setRentDuration : function(id, duration) {
		if ((duration < 1) || (duration > 4)) {
			return;
		}
		id = $.base64Encode(id);
		var items = $.JSONCookie(this.rentCookieName);
		for (var itemId in items) {
			if (itemId == id) {
				this.count -= (items[id].duration - duration);
				items[id].duration = duration;
			}
		}
		$.JSONCookie(this.rentCookieName, items, this.cookieOpts);
	},
	setServiceDuration : function(id, duration) {
		if ((duration < 1) || (duration > 6)) {
			return;
		}
		id = $.base64Encode(id);
		var items = $.JSONCookie(this.serviceCookieName);
		for (var itemId in items) {
			if (itemId == id) {
				this.count -= (items[id].duration - duration);
				items[id].duration = duration;
			}
		}
		$.JSONCookie(this.serviceCookieName, items, this.cookieOpts);
	}
};

var cart = new ShoppingCart();	//Expose a global {ShoppingCar} object