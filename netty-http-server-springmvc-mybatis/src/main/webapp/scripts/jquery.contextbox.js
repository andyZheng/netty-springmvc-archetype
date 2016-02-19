(function($) {
/**
* 创建一个下拉框
* @name     createContextBox
* @param    options 可选参数，参数包含如下：
*    width:设置下拉框宽度(可选) 
*    offsetX:下拉框的X坐标偏移量(可选)
*    offsetY:下拉框的Y坐标偏移量(可选)
*    content:下拉框中说呈现的内容
* @author   hu wei
* @type     jQuery
* @example $("#contextbox").createContextBox({'width':'500px','offsetX':-220,content:'haha,test!'}); 
*/
$.fn.createContextBox = function(options) {
var contextBoxDivE = $('<div class="contextbox" style="display:none;position:absolute;z-index:1005;" ></div>');
this.contextBoxDivE = contextBoxDivE;
contextBoxDivE.contextBox = this;
//设置下拉框的可选数据
if (options) {
   var content = options['content'];
   this.content = $(content);
   if (content)contextBoxDivE.html(this.content);
   var width = options['width'];
   if (width)contextBoxDivE.css("width",width);
}
//存入缓存
var contextBoxs = $('body').data('contextBoxs');
if (!contextBoxs) contextBoxs = [];
contextBoxs.push(this);
$('body').data('contextBoxs',contextBoxs);
if (jQuery.browser.msie) {
   //如果是IE需要加入iframe，以防止<select>这样的控件显现在div的上面
   var ieiframeE = $('<iframe style="display:none;position:absolute;z-index:1000;border-width:0px;" scrolling="no"></iframe>');
   this.ieiframeE = ieiframeE;
   this.after(ieiframeE);
   this.after(contextBoxDivE);
   this.bind("click", function(e){
    var contextBoxs = $('body').data('contextBoxs');
      for (var i = 0;i<contextBoxs.length;i++) {
      contextBoxs[i].hideContextBox();
      }
      var offset = $(this).offset();
          ieiframeE.css("top",offset.top+24+(options&&options['offsetY']?options['offsetY']:0));
          ieiframeE.css("left",offset.left+(options&&options['offsetX']?options['offsetX']:0));
          ieiframeE.show();
          contextBoxDivE.css("top",offset.top+24+(options&&options['offsetY']?options['offsetY']:0));
          contextBoxDivE.css("left",offset.left+(options&&options['offsetX']?options['offsetX']:0));
       contextBoxDivE.show();
       ieiframeE.height(contextBoxDivE.height());
       ieiframeE.width(contextBoxDivE.width());
      });
      contextBoxDivE.resize(function(){
      $(this).next().height($(this).height());
      $(this).next().width($(this).width());
      });
} else {
   //将下拉框放置在你说点击的对象后面
   this.after(contextBoxDivE);
   //点击这个对象后弹出下拉框
   this.bind("click", function(e){
   var contextBoxs = $('body').data('contextBoxs');
   for (var i = 0;i<contextBoxs.length;i++) {
      contextBoxs[i].hideContextBox();
   }
   //设置下拉框的位置
   var offset = $(this).offset();
        contextBoxDivE.css("top",offset.top+24+(options&&options['offsetY']?options['offsetY']:0));
        contextBoxDivE.css("left",offset.left+(options&&options['offsetX']?options['offsetX']:0));
        //显示下拉框
     contextBoxDivE.show();
      });
}
return this;
};
/**
* 隐藏下拉框
* @name     hideContextBox
* @author   hu wei
* @type     jQuery
* @example $("#contextbox").hideContextBox(); 
*/
$.fn.hideContextBox = function() {
this.next().hide();
if (jQuery.browser.msie) this.next().next().hide();
return this;
};

$.hideAllContextBox = function(options) {
var contextBoxs = $('body').data('contextBoxs');
for (var i = 0;i<contextBoxs.length;i++) {
   contextBoxs[i].hideContextBox();
}
};
})(jQuery);