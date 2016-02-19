function batchDeleteForPurchase(action,form){
    if($("input:checkbox:checked[id!='checkboxAll']").length==0){
            alert('请选择要操作的对象!');
            return;
    }
    if (confirm('确定执行[删除]操作?')){
        form.action = action;
        form.submit();
    }
}
/**
 * 此方法暂用
 * @param sourceId
 */
//购物数量增加1
function plusPur(sourceId){
	var num = parseInt($("#purNum_"+sourceId).val());
	if(num>=999){
		alert("数量最大为999!");
		return;
	}
	var isLogin = $("#isLogin").val();
	if(isLogin=="false"){
		ajaxOperateCookieForCart(sourceId,"add",num);
	}
	else{
		ajaxOperateDatabaseForCart(sourceId,"add",num);
	}
	$("#purNum_"+sourceId).val(num+1);
	var totalPrice = $("#totalPrice").html();
	var purPrice = $("#purPrice_"+sourceId).html();
	if(purPrice==null || purPrice==""){
		return;
	}
	$("#totalPrice").html(FloatAdd(parseFloat(totalPrice),parseFloat(purPrice)));
}
/**
 * 此方法暂用
 * @param sourceId
 */
//购物数量减1
function subtractPur(sourceId){
	var flag = ajaxGetUserLoginState();
	if(flag == "false"){
		return;
	}
	var isLogin = $("#isLogin").val();
	var num = parseInt($("#purNum_"+sourceId).val());
	var purPrice = $("#purPrice_"+sourceId).html();
	//当前购买数量为1，如果再“减”，则将删除购买该商品
	if(num == 1){
		if (confirm('确定取消购买该商品?')){
			//如果用户未登录，则从cookie中删除
			if(isLogin == "false"){
				ajaxOperateCookieForCart(sourceId,"substract",num);
			}
			//用户已登录，则从数据库中删除
			else{
				ajaxOperateDatabaseForCart(sourceId,"substract",num);
			}
			$("#tr_"+sourceId).remove();
		}
		else{
			return;
		}
	}
	else{
		if(isLogin == "false"){
			ajaxOperateCookieForCart(sourceId,"substract",num);
		}
		//用户已登录，则从数据库中删除
		else{
			ajaxOperateDatabaseForCart(sourceId,"substract",num);
		}
		$("#purNum_"+sourceId).val(num-1);
	}
	var totalPrice = $("#totalPrice").html();
	if(purPrice==null || purPrice==""){
		return;
	}
	$("#totalPrice").html(FloatSub(parseFloat(totalPrice),parseFloat(purPrice)));
}

/**
 * 此方法暂用
 *操作cookie
 *只有当operation=“set”的时候才会用到sourceNum这个参数
 */
function ajaxOperateCookieForCart(sourceId,operation,sourceNum){
	$.ajax({
		   type: "POST",
		   async: false,
		   url: "ajaxOperateCookieForCart.do",
		   data: "sourceId="+sourceId+"&operation="+operation+"&sourceNum="+sourceNum,
		   success: function(msg){
		   }
		});
}
//操作数据库
function ajaxOperateDatabaseForCart(sourcheId,operation,num){
	$.ajax({
		   type: "POST",
		   async: false,
		   url: "ajaxOperateDatabaseForCart.do",
		   data: "sourceId="+sourcheId+"&operation="+operation+"&sourceNum="+num,
		   success: function(msg){
		   }
		});
}
function ajaxGetUserLoginState(){
	var isLogin = $("#isLogin").val();
	var flag = "true";
	$.ajax({
		   type: "POST",
		   async: false,
		   url: "ajaxgetuserinfo.do",
		   data: "isLogin="+isLogin,
		   success: function(msg){
		     if(msg=="false"){
		    	 alert("登录状态不一致，即将刷新页面！");
		    	 flag = "false";
		    	 window.location.reload();
		     }
		   }
		});
	return flag;
}
/**
 * 数量输入框onblur事件
 */
function operateTotalPrice(sourceId){
	var isLogin = $("#isLogin").val();
	//检查登录状态书否一致
	var flag = ajaxGetUserLoginState();
	if(flag == "false"){
		return;
	}
	var num = $("#purNum_"+sourceId).val();
	if(num==""||num=="0"){
		if (confirm('确定取消购买该商品?')){
			//如果用户未登录，则从cookie中删除
			if(isLogin == "false"){
				ajaxOperateCookieForCart(sourceId,"delete",1);
			}
			//用户已登录，则从数据库中删除,传值“1”，表示即删除
			else{
				ajaxOperateDatabaseForCart(sourceId,"delete",1);
			}
			$("#tr_"+sourceId).remove();
			calculateTotalPrice();
			return;
		}
		else{
			$("#purNum_"+sourceId).val(1);
		}
	}
	//再次取值商品数量的值，此时能保证此数量大于等于1
	num = $("#purNum_"+sourceId).val();
	//如果用户未登录，则从cookie中删除
	if(isLogin == "false"){
		ajaxOperateCookieForCart(sourceId,"set",num);
	}
	//用户已登录，则从数据库中删除,传值“1”，表示即删除
	else{
		ajaxOperateDatabaseForCart(sourceId,"set",num);
	}
	calculateTotalPrice();
}
/**
 * 购物商品checkbox点击事件
 */
function changeCheckboxStatus(obj){
	//checkbox未选中
	if(!$(obj).attr("checked")){
		$("#checkboxAll").attr("checked","");
	}
	//checkbox选中
	else{
		var flag = true;
		//遍历所有的checkbox
		$("input[type='checkbox'][id!='checkboxAll']").each(function(index,domEle){
			if(!$(domEle).attr("checked")){
				flag=false;
				$("#checkboxAll").attr("checked","");
			}
		});
		if(flag==true){
			$("#checkboxAll").attr("checked","checked");
		}
	}
	calculateTotalPrice();
}
/**
 * 检查购买物品的购买类型是否是同一类，
 * 是同一类返回：true
 * 不是同一类返回：false
 */
function checkSoldType(){
	var n=1;
	var betype="";
	var flag = true;
	$("input:checkbox:checked[id!='checkboxAll']").each(function(index,domEle){
		var type = $(domEle).attr("alt");
		if(n>=2){
			if(betype!=type){
				alert("购买类型不一致，请重新选择！");
				flag = false;
			}
		}
		betype =type;
		n++;
	});
	$("#circulateType").val(betype);
	return flag;
}
/**
 * 计算本页面购物车的总金额
 */
 function calculateTotalPrice(){
	var soldTotalPrice = 0;
	var podTotalPrice = 0;
	//遍历取得页面所有商品行
	$("input:checkbox:checked[id!='checkboxAll']").each(function(index,domEle){
		var docIdAndType = $(domEle).val()+"_"+$(domEle).attr("alt");
		var soldPrice = $("#"+docIdAndType+"_soldPrice").val();
		var podPrice = $("#"+docIdAndType+"_podPrice").val();
		soldTotalPrice = FloatAdd(parseFloat(soldTotalPrice) , parseFloat(soldPrice));
		podTotalPrice = FloatAdd(parseFloat(podTotalPrice) , parseFloat(podPrice));
	});
	$("#soldTotalPrice").html(soldTotalPrice);
	$("#podTotalPrice").html(podTotalPrice);
}
 function submitFromCart(sellTypeTemp,action){
	 var checkPass = true ;
	 var checkMsg = "\r\n不支持当前操作";
	 $("input:checkbox:checked[id!='checkboxAll']").each(function(index,domEle){
			var docIdAndType = $(domEle).val();
			var sellType = $("#"+docIdAndType+"_typeDesc").val();
			if(sellType.indexOf(sellTypeTemp) < 0 ){
				//当提交的资源中有不支持当前操作的时候，禁止提交！
				checkPass = false;
				var sourceName = $("#"+docIdAndType+"_sourceName").val();
				checkMsg = sourceName + "\r\n" + checkMsg;
			}
		});
	 if(sellTypeTemp == 'pod'){
		 document.simpleTableForm.action = action;
	 }
	 if(checkPass){
		 document.simpleTableForm.submit();
	 }else{
		 alert(checkMsg);
	 }
 }
 //全选点击按钮
 function setAllCheckboxStatePurchase(name,state) {
	 $("input:checkbox[id!='checkboxAll']").each(function(index,domEle){
		domEle.checked = state;
	 });
}