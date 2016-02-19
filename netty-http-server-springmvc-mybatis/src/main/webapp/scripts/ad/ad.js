// 加载所有广告
$(function() {
	initAds();
});

/**
 * 加载所有广告.
 * <pre>
 * 广告为带有adType属性的元素中的内容（通常为div，且设置display:none）.
 * adType的可选值：
 * tanchu - 弹出广告
 * piaofu - 漂浮广告
 * duilian - 对联广告
 * </pre>
 */
function initAds() {
	$("div[adType]").each(function() {
		var adType = $(this).attr("adType");
		if (adType == "tanchu") {
			popAd(this);
		} else if (adType == "piaofu") {
			floatAd(this);
		} else if (adType == "duilian") {
			coupletAd(this);
		}
	});
}

/**
 * 右下角弹出广告.
 */
function popAd(adcontainer, opt) {
	opt = $.extend({
		speed : 1, // 弹出速度
		showTime : 0, // 显示持续时间（秒），设置为0永远显示
		allowClose : true // 是否允许关闭
	}, opt || {});

	function currentStyle(element) {
        return element.currentStyle || document.defaultView.getComputedStyle(element, null);
    }

	var adId = "ad" + (new Date().getTime());
	var html = '<div id="' + adId + '" style="position:absolute;display:none;right:0px;bottom:0px;">';
	if (opt.allowClose) {
		var closeHtml = '<div align="right" style="position:relative;top:18px;right:1px;cursor:pointer;font-size:small;" class="closeFloat">关闭</div>';
		html += closeHtml;
	}
	html += $(adcontainer).html() + "</div>";
	$("body").append(html);

	var ad = document.getElementById(adId);
	
	var sh = window.setInterval(function() {
		if (opt.showTime > 0) {
			var t = (new Date().getTime() - startTime) / 1000;
			if (t > opt.showTime) {
				window.clearInterval(sh);
				return;
			}
		}
		if (ad.style.display == "none") {
			window.clearInterval(sh);
			return;
		}
		var bottomHeight =  "-"+document.documentElement.scrollTop;
		ad.style.bottom = bottomHeight + "px";
		var rightWidth =  "-"+document.documentElement.scrollLeft;
		ad.style.right = rightWidth + "px";
	}, 10);
	
	$(ad).slideDown(4000 / opt.speed);
	
	// 关闭按钮
	$(".closeFloat", ad).click(function() {
		window.clearInterval(sh);
		ad.style.display = "none";
	});
}
/**
 * 漂浮广告.
 */
function floatAd(adcontainer, opt) {
	opt = $.extend( {
		speed : 1, // 漂浮速度
		stopOnMouseOver : true, // 是否在鼠标移入时停止异动
		showTime : 0, // 显示持续时间（秒），设置为0永远显示
		allowClose : true // 是否允许关闭
	}, opt || {});

	var closeHtml = '<div align="right" style="position:absolute;bottom:1px;right:1px;cursor:pointer;font-size:small;" class="closeFloat">关闭</div>';
	var adId = "ad" + (new Date().getTime());
	var html = '<div id="' + adId + '" style="position:absolute;">';
	if (opt.allowClose) {
		html += closeHtml;
	}
	html += $(adcontainer).html() + "</div>";
	$("body").append(html);

	var ad = document.getElementById(adId);
	var xPos = 20;
	var yPos = 10;
	ad.style.left = xPos;
	ad.style.top = yPos;
	var step = opt.speed;
	var width, height, Hoffset, Woffset;
	var y = 1;
	var x = 1;
	var interval;
	var startTime = new Date().getTime();
	var mouseover = false;
	var sh = window.setInterval(function() {
		if (mouseover && opt.stopOnMouseOver) {
			return;
		}
		
		if (opt.showTime > 0) {
			var t = (new Date().getTime() - startTime) / 1000;
			if (t > opt.showTime) {
				window.clearInterval(sh);
				return;
			}
		}
		if (ad.style.display == "none") {
			window.clearInterval(sh);
			return;
		}
		width = document.documentElement.clientWidth;
		height = document.documentElement.clientHeight;
		Hoffset = ad.offsetHeight;
		Woffset = ad.offsetWidth;
		if (y) {
			yPos = yPos + step;
		} else {
			yPos = yPos - step;
		}
		if (yPos < 0) {
			y = 1;
			yPos = 0;
		}
		if (yPos >= (height - Hoffset)) {
			y = 0;
			yPos = (height - Hoffset);
		}
		if (x) {
			xPos = xPos + step;
		} else {
			xPos = xPos - step;
		}
		if (xPos < 0) {
			x = 1;
			xPos = 0;
		}
		if (xPos >= (width - Woffset)) {
			x = 0;
			xPos = (width - Woffset);
		}
		ad.style.left = xPos + $(document).scrollLeft() + "px";
		ad.style.top = yPos + $(document).scrollTop() + "px";
	}, 30);
	
	$(ad).hover(function() {
		mouseover = true;
	}, function() {
		mouseover = false;
	});
	
	// 关闭按钮
	$(".closeFloat", ad).click(function() {
		window.clearInterval(sh);
		ad.style.display = "none";
	});
}

/**
 * 对联广告. 参数： adcontainer - 广告内容容器，一般为div，并设置display:none opt：{ top: 60,
 * //广告距页面顶部距离 left: 10, //广告左侧距离 right: 10, //广告右侧距离 position: "both",
 * //对联广告的位置left-在左侧出现,right-在右侧出现,both-两侧出现 allowClose: true //是否允许关闭 }
 */
function coupletAd(adcontainer, opt) {
	opt = $.extend( {
		top : 60, // 广告距页面顶部距离
		left : 10, // 广告左侧距离
		right : 10, // 广告右侧距离
		position : "both", // 对联广告的位置left-在左侧出现,right-在右侧出现,both-两侧出现
		allowClose : true // 是否允许关闭
	}, opt || {});

	var leftAd, rightAd;
	if (opt.position == "both") {
		leftAd = generate("left");
		rightAd = generate("right");
	} else if (opt.position == "left") {
		leftAd = generate("left");
	} else if (opt.position == "right") {
		rightAd = generate("right");
	}

	function generate(position) {
		var content = $(adcontainer).html();
		var closeHtml = '<div align="right" style="position:absolute;bottom:1px;right:1px;cursor:pointer;font-size:small;" class="closeFloat">关闭</div>';
		var adId = "ad" + (new Date().getTime());
		var html = '<div id="' + adId + '" style="';
		if (position == "left") {
			html += 'left:' + opt.left;
		} else { // right
			html += 'right:' + opt.right;
		}
		html += 'px;position:absolute;top:' + opt.top + 'px;">';
		if (opt.allowClose) {
			html += closeHtml;
		}
		html += content + "</div>";
		$("body").append(html);
		var ad = document.getElementById(adId);
		var showAd = true;
		var lastScrollY = 0;
		var sh = window.setInterval(
				function() {
					if (ad.style.display == "none") {
						window.clearInterval(sh);
						return;
					}

					var diffY = $(document).scrollTop();

					percent = .1 * (diffY - lastScrollY);
					if (percent > 0) {
						percent = Math.ceil(percent);
					} else {
						percent = Math.floor(percent);
					}

					ad.style.top = parseInt(ad.style.top) + percent + "px";
					lastScrollY += percent;
				}, 10);

		// 关闭按钮
		$(".closeFloat", ad).click(function() {
			window.clearInterval(sh);
			if (leftAd) {
				leftAd.style.display = "none";
			}
			if (rightAd) {
				rightAd.style.display = "none";
			}
		});

		return ad;
	}
}