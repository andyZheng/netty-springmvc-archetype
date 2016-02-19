/**
 * 本地存储API
 * 
 * 提供JS本地存储兼容解决方案
 * 为了兼容各种客服端，存储数据最好在1M以内
 * 
 * @author Andy
 * @since 2012.07.9 11:17 AM
 * @version 1.1
 * 
 */
(function(window , document){
	
	/**
	 * 默认构造方法
	 * @returns {LocalStorage}
	 */
	LocalStorage = function(){
		this.hostName = null;
		this.isLocalStorage = false;
		this.initially.apply(this);
	};
	
	LocalStorage.prototype = {
			/**
			 * 初始化方法
			 * 
			 * 初始化本地存储实例
			 * @returns
			 */
			initially : function(){
				 this.isLocalStorage = window.localStorage ? true : false;
			},
			/**
			 * 设置某个值
			 * @param key	  待设置的key
			 * @param value	  待设置的value
			 * @param expires 过期时间（日期/毫秒数）
			 */
			put : function(key , value , expires){
				if(!!!key){
					throw new Error("[" + key + "] is invalid!");
				}
				var expireTime = this._doExpireTime(expires);
				if(this.isLocalStorage){
					alert(value.length);
					window.localStorage.setItem(key , value);
					if(!!expireTime){
						window.localStorage.setItem(key + ".expires" , expireTime);
					}
				}else{
					var supportLocalStorage = this._getSupportLocalStorage();
					if(!!expireTime){
						supportLocalStorage.expires = expireTime.toUTCString();
					}
					supportLocalStorage.load(this.hostName);
					supportLocalStorage.setAttribute(key , value);
					supportLocalStorage.save(this.hostName);
				}
			},
			/**
			 * 根据key获取某个值
			 * @param key 	待获取的key
			 * @returns		获取到的值
			 */
			get : function(key){
				if(!!!key){
					throw new Error("[" + key + "] is invalid!");
				}
				var value;
				if(this.isLocalStorage){
					value = window.localStorage.getItem(key);
					if(!!value){
						var expires = window.localStorage.getItem(key + ".expires");
						if(expires < new Date()){
							value = null;
							this.remove(key);
							this.remove(key + ".expires");
						}
					}
					
				}else{
					// userData会自动处理过期时间
					var supportLocalStorage = this._getSupportLocalStorage();
					supportLocalStorage.load(this.hostName);
					value = supportLocalStorage.getAttribute(key);
				}
				return value;
			},
			/**
			 * 移除某一项
			 * @param key	待移除的可以
			 */
			remove : function(key){
				if(!key){
					throw new Error("[" + key + "] is invalid!");
				}
				if(this.isLocalStorage){
					window.localStorage.removeItem(key);
					window.localStorage.remove(key + ".expires");
				}else{
					var supportLocalStorage = this._getSupportLocalStorage();
					supportLocalStorage.load(this.hostName);
					supportLocalStorage.removeAttribute(key);
					supportLocalStorage.save(this.hostName);
				}
			},
			/**
			 * 处理过期时间
			 * @param expires
			 */
			_doExpireTime : function(expires){ 
				// 过期时间必须为日期或者数值类型
				if(expires && (expires instanceof Date || "number" == typeof(expires))){
					if("number" === typeof(expires)){
						var expireTime = expires;
						expires = new Date();
						expires.setTime(expires.getTime() + expireTime);
					}
					return expires;
				}
				return null;
			},
			/**
			 * 获取第三方本地存储支持
			 * 
			 * 主要针对IE低版本提供userData方案
			 */
			_getSupportLocalStorage : function(){
				this.hostName = window.location.hostname ? window.location.hostname : 'localStatus';
				var element = document.createElement("input");
				element.type = "hidden";
				element.style.display = "none";
				element.addBehavior('#default#userData');
				document.body.appendChild(element);
				return element;
			}
	};
	
	LocalStorage = new LocalStorage();
})(window , document);