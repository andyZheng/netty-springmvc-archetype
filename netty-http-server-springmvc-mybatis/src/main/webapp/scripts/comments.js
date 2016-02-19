var Class = {
		create : function() {
			return function() {
				this.initialize.apply(this, arguments);
			};
		}
	};
	var Extend = function(destination, source) {
		for ( var property in source) {
			destination[property] = source[property];
		}
	};
	function stopDefault(e) {
		if (e && e.preventDefault) {
			e.preventDefault();
		} else {
			window.event.returnValue = false;
		}
		return false;
	}
	/**
	 * 星星打分组件
	 */
	var Stars = Class.create();
	Stars.prototype = {
		initialize : function(star, options) {
			this.SetOptions(options); //默认属性
			var flag = 5; //定义全局指针
			var isIE = (document.all) ? true : false; //IE?
			var starlist = document.getElementById(star).getElementsByTagName('a'); //星星列表
			var input = document.getElementById(this.options.Input)
					|| document.getElementById(star + "-input"); // 输出结果
			var tips = document.getElementById(this.options.Tips)
					|| document.getElementById(star + "-tips"); // 打印提示
			var nowClass = " " + this.options.nowClass; // 定义选中星星样式名
			var tipsTxt = this.options.tipsTxt; // 定义提示文案
			var len = starlist.length; //星星数量

			for (var i = 0; i < len; i++) { // 绑定事件 点击 鼠标滑过
				starlist[i].value = i;
				starlist[i].onclick = function(e) {
					stopDefault(e);
					this.className = this.className + nowClass;
					flag = this.value;
					input.value = this.getAttribute("star:value");
					tips.innerHTML = tipsTxt[this.value];
				};
				starlist[i].onmouseover = function() {
					if (flag < 5) {
						var reg = RegExp(nowClass, "g");
						starlist[flag].className = starlist[flag].className
								.replace(reg, "");
					}
				};
				starlist[i].onmouseout = function() {
					if (flag < 5) {
						starlist[flag].className = starlist[flag].className
								+ nowClass;
					}
				};
			}
			;
			if (isIE) { //FIX IE下样式错误
				var li = document.getElementById(star).getElementsByTagName(
						'li');
				for ( var i = 0, len = li.length; i < len; i++) {
					var c = li[i];
					if (c) {
						c.className = c.getElementsByTagName('a')[0].className;
					};
				};
			};
		},
		//设置默认属性
		SetOptions : function(options) {
			this.options = {//默认值
				Input : "",//设置触保存分数的INPUT
				Tips : "",//设置提示文案容器
				nowClass : "current-rating",//选中的样式名
				tipsTxt : [ "1分-很不满意", "2分-不满意", "3分-合格", "4分-优秀", "5分-完美" ]
			//提示文案
			};
			Extend(this.options, options || {});
		}
	};
	var Stars1 = new Stars("stars1");