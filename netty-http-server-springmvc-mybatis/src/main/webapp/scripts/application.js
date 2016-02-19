$(function() {
	
	// 输入框内的提示信息
	// 用法：<input type="text" hint="请输入标题"/>
	$(":text[hint]").each(function() {
		var hint = $(this).attr("hint");
		var val = $(this).val();
		if (val.length == 0) {
			$(this).css("color","#999");
			$(this).val(hint); 
		}
		
		$(this).click(function() { 
			var val = $(this).val(); 
			if (val == hint) { 
				$(this).val("");
			}
		}).blur(function() { 
			var val = $(this).val(); 
			if ($.trim(val).length == 0) {
				$(this).val(hint);
			}
		});
	});
	
	//去除所以文本框空格不用单独处理
	$(":text").each(function() {

		$(this).blur(function() { 
				$(this).val($.trim($(this).val()));
		});
	});
	//提交之前清空默认提示信息
	$(":input[type=submit]").each(function() {
	$(this).click(function() { 
		
		$(":text[hint]").each(function() {
			var hint = $(this).attr("hint");
			var val = $(this).val();
				if (val == hint) { 
					$(this).val("");
				}
		});
	});
	});
 Date.prototype.formate=function(fmt) {           
        var o = {           
        "M+" : this.getMonth()+1, //月份           
        "d+" : this.getDate(), //日           
        "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时           
        "H+" : this.getHours(), //小时           
        "m+" : this.getMinutes(), //分           
        "s+" : this.getSeconds(), //秒           
        "q+" : Math.floor((this.getMonth()+3)/3), //季度           
        "S" : this.getMilliseconds() //毫秒           
        };           
        var week = {           
        "0" : "\u65e5",           
        "1" : "\u4e00",           
        "2" : "\u4e8c",           
        "3" : "\u4e09",           
        "4" : "\u56db",           
        "5" : "\u4e94",           
        "6" : "\u516d"          
        };           
        if(/(y+)/.test(fmt)){           
            fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));           
        }           
        if(/(E+)/.test(fmt)){           
            fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "\u661f\u671f" : "\u5468") : "")+week[this.getDay()+""]);           
        }           
        for(var k in o){           
            if(new RegExp("("+ k +")").test(fmt)){           
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));           
            }           
        }           
        return fmt;           
    };       
	//日期控件
/*$(".Wdate").click(function(){
		WdatePicker({el:$(this).attr("name")});
});
	
$(".Wtime").click(function(){
	WdatePicker({el:$(this).attr("name"),dateFmt:'yyyy-MM-dd HH:mm:ss'});
});*/
    jQuery(".Wdate").each(function (idx, elem) {
         	var value =jQuery(this).val();
        if (value !="") {
           jQuery(this).val(jQuery.format.date(value, jQuery.format.date.defaultShortDateFormat));
        }
        $(this).click(function(){
		WdatePicker({el:$(this).attr("name")});
});
    });
    jQuery(".Wtime").each(function () {
    	var value =jQuery(this).val();
        if (value !="") {
           jQuery(this).val(jQuery.format.date(value, jQuery.format.date.defaultLongDateFormat));
        }
        $(this).click(function(){
	WdatePicker({el:$(this).attr("name"),dateFmt:'yyyy-MM-dd HH:mm:ss'});
});
       
    });
});
    

function previewImage(inputId, imgId) {
    var imgPath;
    var Browser_Agent = navigator.userAgent;
    width = 100;
    height = 100;
    var upload = document.getElementById(inputId);
    var preview = document.getElementById(imgId);
    
     if(Browser_Agent.indexOf("Firefox")!=-1)//判断浏览器的类型
    {
        //火狐浏览器
        imgPath = upload.files[0].getAsDataURL();  //图片路径  
        preview.innerHTML = "<img id='" + imgId + "' src='"+imgPath+"' width='"+width+"' height='"+height+"'/>";
        
    }
    else
    {
        //IE浏览器
        preview.innerHTML = "";
        preview.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = upload.value;
        preview.style.width = width;
        preview.style.height = height;
     }	
}

function batchOperate(action,actionName,checkboxName,form) {
    if (!hasOneChecked(checkboxName)){
        alert('请选择要操作的对象!');
        return;
	}
	if (confirm('确定执行[' + actionName + ']操作?')){
		openwindow("", "result");
		form.target = "result";
	    form.action = action;
	    form.submit();
	}
}

function batchDelete(action,checkboxName,form){
    if (!hasOneChecked(checkboxName)){
            alert('请选择要操作的对象!');
            return;
    }
    if (confirm('确定执行[删除]操作?')){
		openwindow("", "result");
		form.target = "result";
        form.action = action;
        form.submit();
    }
}
function batchDeletedevice(action,checkboxName,form){
    if (!hasOneChecked(checkboxName)){
            alert('请选择要操作的对象!');
            return;
    }
    if (confirm('确定执行[删除]操作?')){
        form.action = action;
        form.submit();
    }
}

function batchSampleCommends(action, checkboxName, form, sampleGroupIds, sampleUserIds){
    if (!hasOneChecked(checkboxName)){
        alert('请选择要操作的对象!');
        return;
    }
    
    if(sampleGroupIds || sampleUserIds){
        var items = document.getElementsByName(checkboxName);
        if (items.length > 0) {
        	var postData = '[';
        	var flag = -1;
            for (var i = 0; i < items.length; i++){
                if (items[i].checked == true){
                	if(flag != -1 && i > flag){
                		postData  += ",";
                	}
                    postData  += '{"resId":' +  items[i].value + ',"sampleGroupIds":"' + sampleGroupIds + '","sampleUserIds":"' + sampleUserIds + '"}';
                    flag++;
                }
            }
            document.getElementById('postData').value = postData + "]";
        }
    }

    form.action = action;
    form.submit();
}

function batchDisable(action,checkboxName,form){
    if (!hasOneChecked(checkboxName)){
            alert('请选择要操作的对象!');
            return;
    }
    if (confirm('确定执行[禁用]操作?')){
		openwindow("", "result");
		form.target = "result";
        form.action = action;
        form.submit();
    }
}


function batchAbled(action,checkboxName,form){
    if (!hasOneChecked(checkboxName)){
            alert('请选择要操作的对象!');
            return;
    }
    if (confirm('确定执行[恢复]操作?')){
		openwindow("", "result");
		form.target = "result";
        form.action = action;
        form.submit();
    }
}

function batchdel(ids,model,form){
    if (!hasOneChecked(ids)){
    	  $.messager.show("请选择要删除对象！",false);
        return;
	}
    if (confirm('确定执行[删除]操作?')){
        		        
    	$.ajax({
            loading: '正在保存数据中...',
            type: 'POST',
            url: model+"/delete.do",                  
            data: $("#"+form).serialize(),
            success: function (message)
            {
          	  	$.messager.show("删除成功！",true);
          		location.href=model+"/list.do";
            },
            error: function (message)
            {
                 $.messager.show("删除失败",false);
                 return false;
            }
        }); 
    }
}

function ajaxSubmit(form ,model){
       $.ajax({
             loading: '正在保存数据中...',
             type: 'POST',
             url: model+'save.do',                  
             data: $("#"+form).serialize(),
             success: function (message)
             {
             	
             	$.messager.show("保存成功！",true);
             	window.setTimeout(function(){
             		location.href=model+"list.do";
             	},600);
           	  
                
             },
             error: function (message)
             {
                  $.messager.show("保存失败",false);
                  return false;
             }
         }); 

}

function batchrebuildindex(model, ids, form) {
	if (!hasOneChecked(ids)) {
		$.messager.show("请选择要重建索引的对象！", false);
		return;
	}
	$.ajax({
		loading : '正在保存数据中...',
		type : 'POST',
		url : model + "/rebuildindex.do",
		data : $("#" + form).serialize(),
		success : function(message) {
			$.messager.show("修改成功，稍后会重建索引！", true);
			location.href = model + "/list.do";
		},
		error : function(message) {
			$.messager.show(message, false);
			return false;
		}
	});

}

function hasOneChecked(name){
    var items = document.getElementsByName(name);
    if (items.length > 0) {
        for (var i = 0; i < items.length; i++){
            if (items[i].checked == true){
                return true;
            }
        }
    } else {
        if (items.checked == true) {
            return true;
        }
    }
    return false;
}

function setAllCheckboxState(name,state) {
	var elms = document.getElementsByName(name);
	for(var i = 0; i < elms.length; i++) {
		elms[i].checked = state;
	}
}

function getReferenceForm(elm) {
	while(elm && elm.tagName != 'BODY') {
		if(elm.tagName == 'FORM') return elm;
		elm = elm.parentNode;
	}
	return null;
}

function openwindow(url, target, width, height) {
	if (width == undefined) width = 300;
	if (height == undefined) height = 300;
	var iTop = (window.screen.availHeight-30-height)/2; // 获得窗口的垂直位置
	var iLeft = (window.screen.availWidth-10-width)/2; // 获得窗口的水平位置
	window.open(url, target, 
			'height=' + height + ',innerHeight=' + height +
			',width=' + width + ',innerWidth=' + width + 
			',top=' + iTop + ',left=' + iLeft +
			',toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no');
}

function openfullwindow(url,target) {
	window.open(url, target,
			"height=" + (window.screen.availHeight-30) + ",width=" + (window.screen.availWidth-10) +
			",top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no");
}

// 返回值示例：客户名称#clienteleName;订单类型#orderType;订购时间#resBeginTime;
var countH = 0;
function getExcelHeader() {
	var header = "";
	countH = 0;
	$("th").each(
		function(){
			if ($(this).attr("id") != "undefinded" && $(this).attr("id") != "") {
				//alert($(this).text());
				//alert($(this).attr("id"));
				header += "h_" + $(this).attr("id") + "_222_" +countH+ "=" + encodeURIComponent($(this).text()) + "&";
				countH += 1;
			}
		}
	); 
	//alert(header);
	return header;
}

function cleanField(fieldName, defaultVal) {
	if($(fieldName).attr("value") == defaultVal){
		$(fieldName).attr("value","");
	}
}

/**
 * 字符串缓冲对象
 * 用于拼接字符串
 * 
 * @author Andy
 * 
 * @param initValue		   初始值
 * @returns {StringBuffer} 字符串缓冲对象
 */
StringBuffer = function(initValue){
	this.cachMap = new Array();
	if(initValue){
		this.cachMap.push(initValue);
	}
};

/**
 * 拼接某个字符串
 * @param value				待拼接的字符串
 * @returns {StringBuffer}  字符串缓冲对象
 */
StringBuffer.prototype.append = function(value){
	this.cachMap.push(value);
	return this;
};

/**
 * 返回拼接后的字符串
 */
StringBuffer.prototype.toString = function(){
	return this.cachMap.join("");
};

/**
* 去掉字符串首尾空格工具方法
* @returns 处理后的字符串
*/
String.prototype.trim = function(){
	return this.replace(/(^\s*)|(\s*$)/g, "");
}; 

/**
 * 获取字符串字节长度
 * @returns  字符串字节长度
 */
String.prototype.getByteLength = function(){
	return this.replace(/[^\x00-\xff]/g, "mm").length;
}; 

