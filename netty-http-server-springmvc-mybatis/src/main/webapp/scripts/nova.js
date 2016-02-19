var _avonTable;

function _novaTalbe(obj){
	//style
	this.leftHeadTableClass;
	this.leftHeadTableTrClass;
	this.leftHeadTableTdClass;
	
	this.leftBodyTableClass;
	this.leftBodyTableTrOneClass;
	this.leftBodyTableTdTwoClass;
	this.leftBodyTableTrOneClass;
	this.leftBodyTableTdTwoClass;
	
	this.rightHeadTableClass;
	this.rightHeadTableTdClass;
	this.rightHeadTableTrClass;
	
	this.rightBodyTableClass;
	this.rightBodyTableTrOneClass;
	this.rightBodyTableTdTwoClass;
	this.rightBodyTableTrOneClass;
	this.rightBodyTableTdTwoClass;
	
	this.selectColor;
	
	this.leftDivClass;
	
	this.rightDivClass;
	
	
	this.toRightBtnClass
	this.toRightBtnMouseOverClass
	this.toRightBtnMouseOutClass
	this.toRightBtnMouseDownClass
	this.toRightBtnMouseUpClass
	this.upBtnClass
	this.upBtnMouseOverClass
	this.upBtnMouseOutClass
	this.upBtnMouseDownClass
	this.upBtnMouseUpClass
	this.toLeftBtnClass
	this.toLeftBtnMouseOverClass
	this.toLeftBtnMouseOutClass
	this.toLeftBtnMouseDownClass
	this.toLeftBtnMouseUpClass
	this.downBtnClass
	this.downBtnMouseOverClass
	this.downBtnMouseOutClass
	this.downBtnMouseDownClass
	this.downBtnMouseUpClass
	
	//style
	this.div=obj;
	this.head;
	this.data;
	this.setHead=function(value){
			var headArray = value.replace(/(^\$\,\$)|(\$\,\$$)/g,"").split("$,$");
			//_setHead(headArray);
			this.head=headArray;
	};
	this.setData=function(value){
			var dataArray = new Array();
			var rowsArray = value.replace(/(^\$\@\$)|(\$\@\$$)/g,"").split("$@$");
			var length = rowsArray.length;
			for (var i =0 ;i<length;i++){
				var valuesArray = rowsArray[i].replace(/(^\$\,\$)|(\$\,\$$)/g,"").split("$,$");
				dataArray.push(valuesArray);
			}
			//_setData(dataArray);
			this.data=dataArray;
	};
	this.buttonClass="";
	this.row_up=function(){
		if (_setToFromList==null||_setToFromList.length!=1)return;
		var _ToBodyTable = document.getElementById("_ToBodyTable");
		if(row_up(_ToBodyTable,_setToFromList[0])){
			_setToFromList[0]-=1;
		}
		_ToBodyTable=null;
	};
	this.row_down=function(){
		if (_setToFromList==null||_setToFromList.length!=1)return;
		var _ToBodyTable = document.getElementById("_ToBodyTable");
		if(row_down(_ToBodyTable,_setToFromList[0])){
			_setToFromList[0]+=1;
		}
		_ToBodyTable=null;
	};
	this.getResult=function(){
		var _FromBodyTable = document.getElementById("_FromBodyTable");
		var fromlength = _FromBodyTable.rows.length;
		if (fromlength>1){
			return "wrong";
		}
		var _ToBodyTable = document.getElementById("_ToBodyTable");
		var length = _ToBodyTable.rows.length-1;
		var ret = [];
		for(var j = 0; j< length-1 ; j++){
			var _TableRow=_ToBodyTable.rows(j);
			var clength = _TableRow.cells.length;
			ret.push(_TableRow.cells(0).innerText);
			for (var i=1; i< clength;i++){
				ret.push("$,$"+_TableRow.cells(i).innerText);
			}
			ret.push("$@$");
		}
		var _TableRow=_ToBodyTable.rows(length-1);
		var clength = _TableRow.cells.length;
		ret.push(_TableRow.cells(0).innerText);
		for (var i=1; i< clength;i++){
			ret.push("$,$"+_TableRow.cells(i).innerText);
		}
		
		_ToBodyTable=null;
		return ret.join("");
	};
	this.Process=function(){
		_avonTable=this;
		var _datas = "";
		var _heads = "";
		//process head info
		if (this.head==null)return;
		var head_length = this.head.length;
		var _tdheads =[];
		_tdheads.push("<tr class='"+this.leftHeadTableTrClass+"'>");
		for (var i=0;i<head_length;i++){
			_tdheads.push("<td onclick='sort_tab("+i+",true);' class='"+this.leftHeadTableTdClass+"'>"+this.head[i]+"</td>");
		}
		_tdheads.push("</tr>");
		_heads=_tdheads.join("");

		//process body info
		if (this.data==null)return;
		var body_length = this.data.length;
		var _trdatas= [];
		var body_value_length;
		for (var i=0;i<body_length;i++){
			body_value_length = this.data[i].length
			if (i%2>0){
				_trdatas.push("<tr class='"+this.leftBodyTableTrTwoClass+"'  onclick='_setFromToTable(this)'>");
			}else{
				_trdatas.push("<tr class='"+this.leftBodyTableTrOneClass+"'  onclick='_setFromToTable(this)'>");
			}
			for (var j=0;j<body_value_length;j++){
				_trdatas.push("<td class='"+this.leftBodyTableTdOneClass+"'>"+this.data[i][j]+"</td>");
			}
			_trdatas.push("</tr>");
		}
		_datas=_trdatas.join("");
		//process view
		
		this.div.innerHTML="<table border='0' style='cursor:default' onselectstart='return false;'>"+
	"<tr>"+
		"<td rowspan='2'>"+
			"<table border='0'>"+
				"<tr>"+
					"<td>"+
						"<table class='"+this.leftHeadTableClass+"' id='_FromHeadTable'>"+
							_heads+
						"</table>"+
					"</td>"+
				"</tr>"+
				"<tr>"+
					"<td>"+
						"<div id='_FromBodyDiv' class='"+this.leftDivClass+"'>"+
							"<table cellspacing='0' cellpadding='0' class='"+this.leftBodyTableClass+"' id='_FromBodyTable'>"+
								_datas+
								"<tr style='display:none;' >"+
									"<td colspan='"+body_value_length+"'></td>"+
								"</tr>"+
							"</table>"+
						"</div>"+
					"</td>"+
				"</tr>"+
			"</table>"+
		"</td>"+
		"<td>" +
		"<table><tr><td><input type='button' value=' 右  移 ' id='_SetToBtn' onclick='_setToTable();'  class='"+this.toRightBtnClass+"'  onMouseOver='this.className='"+this.toRightBtnMouseOverClass+"''"+ 
				 				"onMouseOut='this.className='"+this.toRightBtnMouseOutClass+"'' "+
				 				"onMouseDown='this.className='"+this.toRightBtnMouseDownClass+"'' "+
				 				"onMouseUp='this.className='"+this.toRightBtnMouseUpClass+"'' "+
								"/></td></tr><tr>" +
				"<td><input type='button' value='全选右移' id='leftselectall' onclick='_leftselectall();'  class='"+this.downBtnClass+"'onMouseOver='this.className='"+this.downBtnMouseOverClass+"''"+ 
				 				"onMouseOut='this.className='"+this.downBtnMouseOutClass+"'' "+
				 				"onMouseDown='this.className='"+this.downBtnMouseDownClass+"'' "+
				 				"onMouseUp='this.className='"+this.downBtnMouseUpClass+"'' "+
				 				"/></td></tr></table>"+
		"</td>"+
		"<td rowspan='2'>"+
			"<table border='0'>"+
				"<tr>"+
					"<td>"+
						"<table class='"+this.rightHeadTableClass+"' id='_ToHeadTable'>"+
								_heads+
						"</table>"+
					"</td>"+
				"</tr>"+
				"<tr>"+
					"<td>"+
						"<div id='_ToBodyDiv' class='"+this.rightDivClass+"'>"+
							"<table cellspacing='0' cellpadding='0' class='"+this.rightBodyTableClass+"' id='_ToBodyTable'>"+
								"<tr style='display:none;' >"+
									"<td colspan='"+body_value_length+"' width=1%></td>"+
								"</tr>"+
							"</table>"+
						"</div>"+
					"</td>"+
				"</tr>"+
			"</table>"+
		"</td><td><input type='button' value=' 上  移 ' id='_SetToBtn' onclick='_rowUp();'  class='"+this.upBtnClass+"'  onMouseOver='this.className='"+this.upBtnMouseOverClass+"''"+ 
				 				"onMouseOut='this.className='"+this.upBtnMouseOutClass+"'' "+
				 				"onMouseDown='this.className='"+this.upBtnMouseDownClass+"'' "+
				 				"onMouseUp='this.className='"+this.upBtnMouseUpClass+"'' "+
								"/></td>"+
	"</tr>"+
	"<tr>"+
		"<td><table><tr><td><input type='button' value=' 左  移 ' id='SetFromBtn' onclick='_setFromTable();'  class='"+this.toLeftBtnClass+"'  onMouseOver='this.className='"+this.toLeftBtnMouseOverClass+"''"+ 
				 				"onMouseOut='this.className='"+this.toLeftBtnMouseOutClass+"'' "+
				 				"onMouseDown='this.className='"+this.toLeftBtnMouseDownClass+"'' "+
				 				"onMouseUp='this.className='"+this.toLeftBtnMouseUpClass+"'' "+
								"/></td></tr><tr>"+
					"<td align='center'><input type='button' value='全选左移' id='rightselectall' onclick='_rightselectall();' class='"+this.downBtnClass+"'onMouseOver='this.className='"+this.downBtnMouseOverClass+"''"+ 
				 				"onMouseOut='this.className='"+this.downBtnMouseOutClass+"'' "+
				 				"onMouseDown='this.className='"+this.downBtnMouseDownClass+"'' "+
				 				"onMouseUp='this.className='"+this.downBtnMouseUpClass+"'' "+
				 				"/></td></tr></table></td>"+	
		"<td><input type='button' value=' 下  移 ' id='SetFromBtn' onclick='_rowDown();'  class='"+this.downBtnClass+"'onMouseOver='this.className='"+this.downBtnMouseOverClass+"''"+ 
				 				"onMouseOut='this.className='"+this.downBtnMouseOutClass+"'' "+
				 				"onMouseDown='this.className='"+this.downBtnMouseDownClass+"'' "+
				 				"onMouseUp='this.className='"+this.downBtnMouseUpClass+"'' "+
				 				"/></td>"+
	"</tr>"+
"</table>";
	}
}
var _setFromToList;
var _setToFromList;
var _setToTempList;
var _ctrlKeyState = 0;
var _shiftKeyState = 0;

document.onkeydown = function(e){
	event =e;
	if (event.ctrlKey) _ctrlKeyState=1;
	if (event.shiftKey) _shiftKeyState=1;
};
document.onkeyup = _donkeyup;
function _donkeydowm (){
	if (event.ctrlKey) _ctrlKeyState=1;
	if (event.shiftKey) _shiftKeyState=1;
}
function _donkeyup (){
	if (_ctrlKeyState==1) _ctrlKeyState=0;
	if (_shiftKeyState==1) _shiftKeyState=0;
}
function autoColorTable(id){
	var _BodyTable = document.getElementById(id);
	var length = _BodyTable.rows.length-2;
	var _BodyTableRows = _BodyTable.rows;
	var j=-1;
	while(j++<length){
		_BodyTableRows(j).style.backgroundColor="";
		_BodyTableRows(j).className = (j)%2>0?_avonTable.leftBodyTableTrTwoClass:_avonTable.leftBodyTableTrOneClass;
	}
	_BodyTableRows=null;
	_BodyTable=null;
}
function _setFromTable(){
	_getToProcess()
	if (_setToTempList==null) return;
	_insertFromTable();
	_deleteToTable();
	_setToTempList=null;
	_clearFromToList();
	_setFromToList=null;
	_setToFromList=null;	
	autoColorTable("_FromBodyTable");
	autoColorTable("_ToBodyTable");
}

function _getFromProcess(){
	if (_setFromToList==null) return;
	_setFromToList=_unique(_setFromToList);
	_setToTempList=new Array();
	_setFromToList.sort(_sortNumber);
	var _FromBodyTable = document.getElementById("_FromBodyTable");
	var length = _setFromToList.length;
	for (var i=0;i<length;i++){
		_setToTempList.push(_FromBodyTable.rows(_setFromToList[i]));
	}
	_FromBodyTable=null;
}

function _getToProcess(){
	if (_setToFromList==null) return;
	_setToFromList=_unique(_setToFromList);
	_setToTempList=new Array();
	_setToFromList.sort(_sortNumber);
	var _ToBodyTable = document.getElementById("_ToBodyTable");
	var length = _setToFromList.length;
	for (var i=0;i<length;i++){
		_setToTempList.push(_ToBodyTable.rows(_setToFromList[i]));
	}
	_ToBodyTable=null;
}

function _setToTable(){
	_getFromProcess();
	if (_setToTempList==null) return;
	_insertToTable();
	_deleteFromTable();
	_setToTempList=null;
	_clearToFromList();
	_setFromToList=null;
	_setToFromList=null;
	autoColorTable("_FromBodyTable");
	autoColorTable("_ToBodyTable");
}

function _insertToTable(){
	var _ToBodyTableNewRow = null;
	var _ToBodyTable = document.getElementById("_ToBodyTable");
	var _ToBodyTableBody = _ToBodyTable.firstChild;
	var length = _setToTempList.length;
	var rowlength = _ToBodyTable.rows.length;
	if(_setToFromList!=null&&_setToFromList.length==1){
		var j=-1;
		while(j++<length-1){
			_ToBodyTableNewRow=_setToTempList[j].cloneNode(true);
			_ToBodyTableNewRow.style.backgroundColor="";
			_ToBodyTableNewRow.className = (_setToFromList[0]+j)%2>0?_avonTable.leftBodyTableTrTwoClass:_avonTable.leftBodyTableTrOneClass;
			_ToBodyTableNewRow.onclick = function (){_setToFromTable(this);}
			_ToBodyTableBody.insertBefore(_ToBodyTableNewRow,_ToBodyTableBody.childNodes(_setToFromList[0]+j));		
		}
		_setToFromList[0]+=length;
	}else{
		var j=-1;
		while(j++<length-1){
			_ToBodyTableNewRow=_setToTempList[j].cloneNode(true);
			_ToBodyTableNewRow.style.backgroundColor="";
			_ToBodyTableNewRow.className = (rowlength+j-1)%2>0?_avonTable.leftBodyTableTrTwoClass:_avonTable.leftBodyTableTrOneClass;
			_ToBodyTableNewRow.onclick = function (){_setToFromTable(this);}	
			_ToBodyTableBody.insertBefore(_ToBodyTableNewRow,_ToBodyTableBody.childNodes(rowlength+j-1));			
		}
	}
	_ToBodyTableBody=null;
	_ToBodyTableNewRow=null;
	_ToBodyTable=null;
}

function _insertFromTable(){
	var _FromBodyTableNewRow = null;
	var _FromBodyTable = document.getElementById("_FromBodyTable");
	var _FromBodyTableBody = _FromBodyTable.firstChild;
	var length = _setToTempList.length;
	var rowlength = _FromBodyTable.rows.length;
	if(_setFromToList!=null&&_setFromToList.length==1){
		var j=-1;
		while(j++<length-1){
			_FromBodyTableNewRow=_setToTempList[j].cloneNode(true);
			_FromBodyTableNewRow.style.backgroundColor="";
			_FromBodyTableNewRow.className = (_setFromToList[0]+j)%2>0?_avonTable.leftBodyTableTrTwoClass:_avonTable.leftBodyTableTrOneClass;
			_FromBodyTableNewRow.onclick = function (){_setFromToTable(this);}	
			_FromBodyTableBody.insertBefore(_FromBodyTableNewRow,_FromBodyTableBody.childNodes(_setFromToList[0]+j));
		}
		_setFromToList[0]+=length;
	}else{
		var j=-1;
		while(j++<length-1){
			_FromBodyTableNewRow=_setToTempList[j].cloneNode(true);
			_FromBodyTableNewRow.style.backgroundColor="";
			_FromBodyTableNewRow.className = (rowlength+j-1)%2>0?_avonTable.leftBodyTableTrTwoClass:_avonTable.leftBodyTableTrOneClass;
			_FromBodyTableNewRow.onclick = function (){_setFromToTable(this);}
			_FromBodyTableBody.insertBefore(_FromBodyTableNewRow,_FromBodyTableBody.childNodes(rowlength+j-1));
		}
	}
	_FromBodyTableBody = null;
	_FromBodyTableNewRow=null;
	_FromBodyTable=null;
}

function _deleteToTable(){
	var _ToBodyTable = document.getElementById("_ToBodyTable");
	var length = _setToTempList.length-1;
	for(var j =length; j>=0; j--){
		_ToBodyTable.deleteRow(_setToFromList[j]);
	}
	_ToBodyTable=null;
}

function _deleteFromTable(){
	var _FromBodyTable = document.getElementById("_FromBodyTable");
	var length = _setFromToList.length-1;
	for(var j = length; j>=0; j--){
		_FromBodyTable.deleteRow(_setFromToList[j]);
	}
	_FromBodyTable=null;
}

function _setFromToTable(obj){
	if (_setFromToList==null)_setFromToList = new Array();
	
	if (_ctrlKeyState==1){
		if (obj.style.backgroundColor!=_avonTable.selectColor){
			obj.style.backgroundColor=_avonTable.selectColor;
			_setFromToList.push(obj.rowIndex);
		}else{
			obj.style.backgroundColor="";
			obj.className = (obj.rowIndex)%2>0?_avonTable.leftBodyTableTrTwoClass:_avonTable.leftBodyTableTrOneClass;
			_setFromToList = _deleteArray(_setFromToList,obj.rowIndex);
		}
	}else if(_shiftKeyState==1){
		var frontRowIndex = _setFromToList[_setFromToList.length-1];
		var startIndex = (frontRowIndex<obj.rowIndex)?frontRowIndex:obj.rowIndex;
		var endIndex = (frontRowIndex>obj.rowIndex)?frontRowIndex:obj.rowIndex;
		var _FromBodyTable = document.getElementById("_FromBodyTable");
		for (var k = startIndex;k<= endIndex;k++){
			_FromBodyTable.rows(k).style.backgroundColor=_avonTable.selectColor;
			_setFromToList.push(k);
		}
		_FromBodyTable=null;
	}else{
		if (obj.style.backgroundColor!=(_avonTable.selectColor).toLowerCase()){
			_clearFromToList();
			obj.style.backgroundColor=_avonTable.selectColor;
			_setFromToList.push(obj.rowIndex);
		}else{
			
			_clearFromToList();
		}
	}
}

function _clearFromToList(){
	if (_setFromToList==null)return;
	var _FromBodyTable = document.getElementById("_FromBodyTable");
	var length = _setFromToList.length;
	for(var i =0;i<length;i++){
		_FromBodyTable.rows(_setFromToList[i]).style.backgroundColor="";
		_FromBodyTable.rows(_setFromToList[i]).className = (_setFromToList[i])%2>0?_avonTable.leftBodyTableTrTwoClass:_avonTable.leftBodyTableTrOneClass;
	}
	_setFromToList = new Array();
	_FromBodyTable=null;
}
function _clearToFromList(){
	if (_setToFromList==null)return;
	var _ToBodyTable = document.getElementById("_ToBodyTable");
	var length = _setToFromList.length;
	for(var i =0;i<length;i++){
		_ToBodyTable.rows(_setToFromList[i]).style.backgroundColor="";
		_ToBodyTable.rows(_setToFromList[i]).className = (_setToFromList[i])%2>0?_avonTable.leftBodyTableTrTwoClass:_avonTable.leftBodyTableTrOneClass;
	}
	_setToFromList = new Array();
	_ToBodyTable=null;
}
function _setToFromTable(obj){
	if (_setToFromList==null)_setToFromList = new Array();
	if (_ctrlKeyState==1){
		if (obj.style.backgroundColor!=_avonTable.selectColor){
			obj.style.backgroundColor=_avonTable.selectColor;
			_setToFromList.push(obj.rowIndex);
		}else{
			obj.style.backgroundColor="";
			obj.className = (obj.rowIndex)%2>0?_avonTable.leftBodyTableTrTwoClass:_avonTable.leftBodyTableTrOneClass;
			_setToFromList = _deleteArray(_setToFromList,obj.rowIndex);
		}
	}else if(_shiftKeyState==1){
		var frontRowIndex = _setToFromList[_setToFromList.length-1];
		var startIndex = (frontRowIndex<obj.rowIndex)?frontRowIndex:obj.rowIndex;
		var endIndex = (frontRowIndex>obj.rowIndex)?frontRowIndex:obj.rowIndex;
		var _ToBodyTable = document.getElementById("_ToBodyTable");
		for (var k = startIndex;k<= endIndex;k++){
			_ToBodyTable.rows(k).style.backgroundColor=_avonTable.selectColor;
			_setToFromList.push(k);
		}
		_ToBodyTable=null;
	}else{
		if (obj.style.backgroundColor!=(_avonTable.selectColor).toLowerCase()){
			_clearToFromList();
			obj.style.backgroundColor=_avonTable.selectColor;
			_setToFromList.push(obj.rowIndex);
		}else{
			_clearToFromList();
		}
	}
}
function _deleteArray(temparray,obj){
	var array = new Array();
	var length = temparray.length;
	for(var i =0;i<length;i++){
		if(temparray[i]!=obj)
			array.push(temparray[i]);
	}
	return array;
}
function _unique(array) {
	var ret = [], done = {};
	try {
		var length = array.length;
		for ( var i = 0, length = length; i < length; i++ ) {
			var id = array[ i ];
			if ( !done[ id ] ) {
				done[ id ] = true;
				ret.push( array[ i ] );
			}
		}
	}catch(e){
		ret = array;
	}
	return ret;
}
function _sortNumber(a,b)
{
	return a - b
}

function _rowUp(){
	
	if (_setToFromList==null||_setToFromList.length!=1)return;
	var _ToBodyTable = document.getElementById("_ToBodyTable");
	if(row_up(_ToBodyTable,_setToFromList[0])){
		_setToFromList[0]-=1;
	}
	_ToBodyTable=null;
}
function _rowDown(){
	
	if (_setToFromList==null||_setToFromList.length!=1)return;
	var _ToBodyTable = document.getElementById("_ToBodyTable");
	if(row_down(_ToBodyTable,_setToFromList[0])){
		_setToFromList[0]+=1;
	}
	_ToBodyTable=null;
}

function _leftselectall(){
	var _FromBodyTable = document.getElementById("_FromBodyTable");
	var rowslength =_FromBodyTable.rows.length;
	if (_setFromToList==null)_setFromToList = new Array();
	for (var k = 0; k< rowslength-1;k++){
		//_FromBodyTable.rows(k).style.backgroundColor=_avonTable.selectColor;
		_setFromToList.push(k);
	}
	_FromBodyTable=null;
	_setToTable();
}
function _rightselectall(){
	var _ToBodyTable = document.getElementById("_ToBodyTable");
	var rowslength =_ToBodyTable.rows.length;
	if (_setToFromList==null)_setToFromList = new Array();
	for (var k = 0; k< rowslength-1;k++){
		//_ToBodyTable.rows(k).style.backgroundColor=_avonTable.selectColor;
		_setToFromList.push(k);
	}
	_ToBodyTable=null;
	_setFromTable()
}


function change_row(the_tab,line1,line2){
	var line1Class = the_tab.rows[line1].className;
	var line2Class = the_tab.rows[line2].className;
	the_tab.rows[line1].swapNode(the_tab.rows[line2]);
	the_tab.rows[line1].style.backgroundColor="";
	the_tab.rows[line1].className=line1Class;
	the_tab.rows[line2].className=line2Class;
	the_tab=null;
	return true;
 }
function row_up(the_tab,line1){
	if(line1==null || line1<=0)return false;
	return change_row(the_tab,line1,--line1);
}
function row_down(the_tab,line1){
	if(line1==null || line1>=the_tab.rows.length-2)return false;
	return change_row(the_tab,line1,++line1);
 }

function col_left(){
	event.cancelBubble=true;
	if(this.cur_col==null || this.cur_col==0)return;
	change_col(this.obj,this.cur_col,--this.cur_col);
	if(this.cur_col==sort_col)sort_col=this.cur_col+1;
	else if(this.cur_col+1==sort_col)sort_col=this.cur_col;
}
function col_right(){
	event.cancelBubble=true;
	if(this.cur_col==null || this.cur_col==this.obj.rows[0].cells.length-1)return;
	change_col(this.obj,this.cur_col,++this.cur_col);
	if(this.cur_col==sort_col)sort_col=this.cur_col-1;
	else if(this.cur_col-1==sort_col)sort_col=this.cur_col;
 }

function change_col(the_tab,line1,line2){
	for(var i=0;i<the_tab.rows.length;i++)
		the_tab.rows[i].cells[line1].swapNode(the_tab.rows[i].cells[line2]);
 }
/*
*  排序方法
*/
function sort_tab(col,mode){
	var the_tab = document.getElementById("_FromBodyTable");
	var tab_arr = new Array();
	var i;
	var start=new Date;
	for(i=0;i<the_tab.rows.length;i++){
		tab_arr.push(new Array(the_tab.rows[i].cells[col].innerText.toLowerCase(),the_tab.rows[i]));
	}
	function SortArr(mode) {
		return function (arr1, arr2){
			var flag;
			var a,b;
			a = arr1[0];
			b = arr2[0];
			if(/^(\+|-)?\d+($|\.\d+$)/.test(a) && /^(\+|-)?\d+($|\.\d+$)/.test(b)){
				a=eval(a);
				b=eval(b);
				flag=mode?(a>b?1:(a<b?-1:0)):(a<b?1:(a>b?-1:0));
			}else{
				a=a.toString();
				b=b.toString();
				if(a.charCodeAt(0)>=19968 && b.charCodeAt(0)>=19968){
					flag = judge_CN(a,b,mode);
				}else{
					flag=mode?(a>b?1:(a<b?-1:0)):(a<b?1:(a>b?-1:0));
				}
			}
			return flag;
		};
	}
	tab_arr.sort(SortArr(mode));
	var tab_length= tab_arr.length;
	for(i=1;i<tab_length;i++){
		the_tab.lastChild.appendChild(tab_arr[i][1]);
	}
	the_tab.lastChild.appendChild(tab_arr[0][1]);
	autoColorTable("_FromBodyTable");
 }
 /*
*  判断是否为中文

*/
function judge_CN(char1,char2,mode){
	var charSet=charMode?charPYStr:charBHStr;
	for(var n=0;n<(char1.length>char2.length?char1.length:char2.length);n++){
		if(char1.charAt(n)!=char2.charAt(n)){
			if(mode) return(charSet.indexOf(char1.charAt(n))>charSet.indexOf(char2.charAt(n))?1:-1);
			else	 return(charSet.indexOf(char1.charAt(n))<charSet.indexOf(char2.charAt(n))?1:-1);
			break;
		}
	}
	return(0);
 }
/*
*  排序所需汉字参照
*/
var charPYStr = "啊阿埃挨哎唉哀皑癌蔼矮艾碍爱隘鞍氨安俺按暗岸胺案肮昂盎凹敖熬翱袄傲奥懊澳芭捌扒叭吧笆八疤巴拔跋靶把耙坝霸罢爸白柏百摆佰败拜稗斑班搬扳般颁板版扮拌伴瓣半办绊邦帮梆榜膀绑棒磅蚌镑傍谤苞胞包褒剥薄雹保堡饱宝抱报暴豹鲍爆杯碑悲卑北辈背贝钡倍狈备惫焙被奔苯本笨崩绷甭泵蹦迸逼鼻比鄙笔彼碧蓖蔽毕毙毖币庇痹闭敝弊必辟壁臂避陛鞭边编贬扁便变卞辨辩辫遍标彪膘表鳖憋别瘪彬斌濒滨宾摈兵冰柄丙秉饼炳病并玻菠播拨钵波博勃搏铂箔伯帛舶脖膊渤泊驳捕卜哺补埠不布步簿部怖擦猜裁材才财睬踩采彩菜蔡餐参蚕残惭惨灿苍舱仓沧藏操糙槽曹草厕策侧册测层蹭插叉茬茶查碴搽察岔差诧拆柴豺搀掺蝉馋谗缠铲产阐颤昌猖场尝常长偿肠厂敞畅唱倡超抄钞朝嘲潮巢吵炒车扯撤掣彻澈郴臣辰尘晨忱沉陈趁衬撑称城橙成呈乘程惩澄诚承逞骋秤吃痴持匙池迟弛驰耻齿侈尺赤翅斥炽充冲虫崇宠抽酬畴踌稠愁筹仇绸瞅丑臭初出橱厨躇锄雏滁除楚础储矗搐触处處揣川穿椽传船喘串疮窗幢床闯创吹炊捶锤垂春椿醇唇淳纯蠢戳绰疵茨磁雌辞慈瓷词此刺赐次聪葱囱匆从丛凑粗醋簇促蹿篡窜摧崔催脆瘁粹淬翠村存寸磋撮搓措挫错搭达答瘩打大呆歹傣戴带殆代贷袋待逮怠耽担丹单郸掸胆旦氮但惮淡诞弹蛋当挡党荡档刀捣蹈倒岛祷导到稻悼道盗德得的蹬灯登等瞪凳邓堤低滴迪敌笛狄涤翟嫡抵底地蒂第帝弟递缔颠掂滇碘点典靛垫电佃甸店惦奠淀殿碉叼雕凋刁掉吊钓调跌爹碟蝶迭谍叠丁盯叮钉顶鼎锭定订丢东冬董懂动栋侗恫冻洞兜抖斗陡豆逗痘都督毒犊独读堵睹赌杜镀肚度渡妒端短锻段断缎堆兑队对墩吨蹲敦顿囤钝盾遁掇哆多夺垛躲朵跺舵剁惰堕蛾峨鹅俄额讹娥恶厄扼遏鄂饿恩而儿耳尔饵洱二贰发罚筏伐乏阀法珐藩帆番翻樊矾钒繁凡烦反返范贩犯饭泛坊芳方肪房防妨仿访纺放菲非啡飞肥匪诽吠肺废沸费芬酚吩氛分纷坟焚汾粉奋份忿愤粪丰封枫蜂峰锋风疯烽逢冯缝讽奉凤佛否夫敷肤孵扶拂辐幅氟符伏俘服浮涪福袱弗甫抚辅俯釜斧脯腑府腐赴副覆赋复傅付阜父腹负富讣附妇缚咐噶嘎该改概钙盖溉干甘杆柑竿肝赶感秆敢赣冈刚钢缸肛纲岗港杠篙皋高膏羔糕搞镐稿告哥歌搁戈鸽胳疙割革葛格蛤阁隔铬个各给根跟耕更庚羹埂耿梗工攻功恭龚供躬公宫弓巩汞拱贡共钩勾沟苟狗垢构购够辜菇咕箍估沽孤姑鼓古蛊骨谷股故顾固雇刮瓜剐寡挂褂乖拐怪棺关官冠观管馆罐惯灌贯光广逛瑰规圭硅归龟闺轨鬼诡癸桂柜跪贵刽辊滚棍锅郭国果裹过哈骸孩海氦亥害骇酣憨邯韩含涵寒函喊罕翰撼捍旱憾悍焊汗汉夯杭航壕嚎豪毫郝好亳耗号浩呵喝荷菏核禾和何合盒貉阂河涸赫褐鹤贺嘿黑痕很狠恨哼亨横衡恒轰哄烘虹鸿洪宏弘红喉侯猴吼厚候后呼乎忽瑚壶葫胡蝴狐糊湖弧虎唬护互沪户花哗华猾滑画划化话槐徊怀淮坏欢环桓还缓换患唤痪豢焕涣宦幻荒慌黄磺蝗簧皇凰惶煌晃幌恍谎灰挥辉徽恢蛔回毁悔慧卉惠晦贿秽会烩汇讳诲绘荤昏婚魂浑混豁活伙火获或惑霍货祸击圾基机畸稽积箕肌饥迹激讥鸡姬绩缉吉极棘辑籍集及急疾汲即嫉级挤几脊己蓟技冀季伎祭剂悸济寄寂计记既忌际妓继纪嘉枷夹佳家加荚颊贾甲钾假稼价架驾嫁歼监坚尖笺间煎兼肩艰奸缄茧检柬碱硷拣捡简俭剪减荐槛鉴践贱见键箭件健舰剑饯渐溅涧建僵姜将浆江疆蒋桨奖讲匠酱降蕉椒礁焦胶交郊浇骄娇嚼搅铰矫侥脚狡角饺缴绞剿教酵轿较叫窖揭接皆秸街阶截劫节茎睛晶鲸京惊精粳经井警景颈静境敬镜径痉靖竟竞净炯窘揪究纠玖韭久灸九酒厩救旧臼舅咎就疚鞠拘狙疽居驹菊局咀矩举沮聚拒据巨具距踞锯俱句惧炬剧捐鹃娟倦眷卷绢撅攫抉掘倔爵桔杰捷睫竭洁结解姐戒藉芥界借介疥诫届巾筋斤金今津襟紧锦仅谨进靳晋禁近烬浸尽劲荆兢觉决诀绝均菌钧军君峻俊竣浚郡骏喀咖卡咯开揩楷凯慨刊堪勘坎砍看康慷糠扛抗亢炕考拷烤靠坷苛柯棵磕颗科壳咳可渴克刻客课肯啃垦恳坑吭空恐孔控抠口扣寇枯哭窟苦酷库裤夸垮挎跨胯块筷侩快宽款匡筐狂框矿眶旷况亏盔岿窥葵奎魁傀馈愧溃坤昆捆困括扩廓阔垃拉喇蜡腊辣啦莱来赖蓝婪栏拦篮阑兰澜谰揽览懒缆烂滥琅榔狼廊郎朗浪捞劳牢老佬姥酪烙涝勒乐雷镭蕾磊累儡垒擂肋类泪棱楞冷厘梨犁黎篱狸离漓理李里鲤礼莉荔吏栗丽厉励砾历利傈例俐痢立粒沥隶力璃哩俩联莲连镰廉怜涟帘敛脸链恋炼练粮凉梁粱良两辆量晾亮谅撩聊僚疗燎寥辽潦了撂镣廖料列裂烈劣猎琳林磷霖临邻鳞淋凛赁吝拎玲菱零龄铃伶羚凌灵陵岭领另令溜琉榴硫馏留刘瘤流柳六龙聋咙笼窿隆垄拢陇楼娄搂篓漏陋芦卢泸颅庐炉掳卤虏鲁麓碌露路赂鹿潞禄录陆戮驴吕铝侣旅履屡缕虑氯律率滤绿峦挛孪滦卵乱掠略抡轮伦仑沦纶论萝螺罗逻锣箩骡裸落洛骆络妈麻玛码蚂马骂嘛吗埋买麦卖迈脉瞒馒蛮满蔓曼慢漫谩芒茫盲氓忙莽猫茅锚毛矛铆卯茂冒帽貌贸么玫枚梅酶霉煤没眉媒镁每美昧寐妹媚门闷们萌蒙檬盟锰猛梦孟眯醚靡糜迷谜弥米秘觅泌蜜密幂棉眠绵冕免勉娩缅面苗描瞄藐秒渺庙妙蔑灭民抿皿敏悯闽明螟鸣铭名命谬摸摹蘑模膜磨摩魔抹末莫墨默沫漠寞陌谋牟某拇牡亩姆母墓暮幕募慕木目睦牧穆拿哪呐钠那娜纳氖乃奶耐奈南男难囊挠脑恼闹淖呢馁内嫩能妮霓倪泥尼拟你匿腻逆溺蔫拈年碾撵捻念娘酿鸟尿捏聂孽啮镊镍涅您柠狞凝宁拧泞牛扭钮纽脓浓农弄奴努怒女暖虐疟挪懦糯诺哦欧鸥殴藕呕偶沤啪趴爬帕怕琶拍排牌徘湃派攀潘盘磐盼畔判叛乓庞旁耪胖抛咆刨炮袍跑泡呸胚培裴赔陪配佩沛喷盆砰抨烹澎彭蓬棚硼篷膨朋鹏捧碰坯砒霹批披劈琵毗啤脾疲皮匹痞僻屁譬篇偏片骗飘漂瓢票撇瞥拼频贫品聘乒坪苹萍平凭瓶评屏坡泼颇婆破魄迫粕剖扑铺仆莆葡菩蒲埔朴圃普浦谱曝瀑期欺栖戚妻七凄漆柒沏其棋淇奇歧畦崎脐齐旗祈祁骑起岂乞企启契砌器气迄弃汽泣讫掐洽牵扦钎铅千迁签仟谦乾黔钱钳前潜遣浅谴堑嵌欠歉枪呛腔羌墙蔷强抢橇锹敲悄桥瞧乔侨巧鞘撬翘峭俏窍切茄且怯窃钦侵亲秦琴勤芹擒禽寝沁青轻氢倾卿清擎晴氰情顷请庆琼穷秋丘邱球求囚酋泅趋区蛆曲躯屈驱渠取娶龋衢趣去圈颧权醛泉全痊拳犬券劝缺炔瘸却鹊榷确雀裙群然燃冉染瓤壤攘嚷让饶扰绕惹热壬仁人忍韧任认刃妊纫扔仍日戎茸蓉荣融熔溶容绒冗揉柔肉茹蠕儒孺如辱乳汝入褥软阮蕊瑞锐闰润若弱撒洒萨腮鳃塞赛三叁伞散桑嗓丧搔骚扫嫂瑟色涩森僧莎砂杀刹沙纱傻啥煞筛晒珊苫杉山删煽衫闪陕擅赡膳善汕扇缮墒伤商赏晌上尚裳梢捎稍烧芍勺韶少哨邵绍奢赊蛇舌舍赦摄射慑涉社设砷申呻伸身深娠绅神沈审婶甚肾慎渗声生甥牲升绳省盛剩胜圣嵊师失狮施湿诗尸虱十石拾时什食蚀实识史矢使屎驶始式示士世柿事拭誓逝势是嗜噬适仕侍释饰氏市恃室视试收手首守寿授售受瘦兽蔬枢梳殊抒输叔舒淑疏书赎孰熟薯暑曙署蜀黍鼠属术述树束戍竖墅庶数漱恕刷耍摔衰甩帅栓拴霜双爽谁水睡税吮瞬顺舜说硕朔烁斯撕嘶思私司丝死肆寺嗣四伺似饲巳松耸怂颂送宋讼诵搜艘擞嗽苏酥俗素速粟僳塑溯宿诉肃酸蒜算虽隋随绥髓碎岁穗遂隧祟孙损笋蓑梭唆缩琐索锁所塌他它她塔獭挞蹋踏胎苔抬台跆泰酞太态汰坍摊贪瘫滩坛檀痰潭谭谈坦毯袒碳探叹炭汤塘搪堂棠膛唐糖倘躺淌趟烫掏涛滔绦萄桃逃淘陶讨套特藤腾疼誊梯剔踢锑提题蹄啼体替嚏惕涕剃屉天添填田甜恬舔腆挑条迢眺跳贴铁帖厅听烃汀廷停亭庭挺艇通桐酮瞳同铜彤童桶捅筒统痛偷投头透凸秃突图徒途涂屠土吐兔湍团推颓腿蜕褪退吞屯臀拖托脱鸵陀驮驼椭妥拓唾挖哇蛙洼娃瓦袜歪外豌弯湾玩顽丸烷完碗挽晚皖惋宛婉万腕汪王亡枉网往旺望忘妄威巍微危韦违桅围唯惟为潍维苇萎委伟伪尾纬未蔚味畏胃喂魏位渭谓尉慰卫瘟温蚊文闻纹吻稳紊问嗡翁瓮挝蜗涡窝我斡卧握沃巫呜钨乌污诬屋无芜梧吾吴毋武五捂午舞伍侮坞戊雾晤物勿务悟误昔熙析西硒矽晰嘻吸锡牺稀息希悉膝夕惜熄烯溪汐犀檄袭席习媳喜铣洗系隙戏细瞎虾匣霞辖暇峡侠狭下厦夏吓掀锨先仙鲜纤咸贤衔舷闲涎弦嫌显险现献县腺馅羡宪陷限线相厢镶香箱襄湘乡翔祥详想响享项巷橡像向象萧硝霄削哮嚣销消宵淆晓小孝校肖啸潇笑效楔些歇蝎鞋协挟携邪斜胁谐写械卸蟹懈泄泻谢屑薪芯锌欣辛新忻心信衅星腥猩惺兴刑型形邢行醒幸杏性姓兄凶胸匈汹雄熊休修羞朽嗅锈秀袖绣墟戌需虚嘘须徐许蓄酗叙旭序畜恤絮婿绪续轩喧宣悬旋玄选癣眩绚靴薛学穴雪血勋熏循旬询寻驯巡殉汛训讯逊迅压押鸦鸭呀丫芽牙蚜崖衙涯雅哑亚讶焉咽阉烟淹盐严研蜒岩延言颜阎炎沿奄掩眼衍演艳堰燕厌砚雁唁彦焰宴谚验殃央鸯秧杨扬佯疡羊洋阳氧仰痒养样漾邀腰妖瑶摇尧遥窑谣姚咬舀药要耀椰噎耶爷野冶也页掖业叶曳腋夜液一壹医揖铱依伊衣颐夷遗移仪胰疑沂宜姨彝椅蚁倚已乙矣以艺抑易邑屹亿役臆逸肄疫亦裔意毅忆义益溢诣议谊译异翼翌绎茵荫因殷音阴姻吟银淫寅饮尹引隐印英樱婴鹰应缨莹萤营荧蝇迎赢盈影颖硬映哟拥佣臃痈庸雍踊蛹咏泳涌永恿勇用幽优悠忧尤由邮铀犹油游酉有友右佑祐釉诱又幼迂淤于盂榆虞愚舆余俞逾鱼愉渝渔隅予娱雨与屿禹宇语羽玉域芋郁吁遇喻峪御愈欲狱育誉浴寓裕预豫驭鸳渊冤元垣袁原援辕园员圆猿源缘远苑愿怨院曰约越跃钥岳粤月悦阅耘云郧匀陨允运蕴酝晕韵孕匝砸杂栽哉灾宰载再在咱攒暂赞赃脏葬遭糟凿藻枣早澡蚤躁噪造皂灶燥责择则泽贼怎增憎曾赠扎喳渣札轧铡闸眨栅榨咋乍炸诈摘斋宅窄债寨瞻毡詹粘沾盏斩辗崭展蘸栈占战站湛绽樟章彰漳张掌涨杖丈帐账仗胀瘴障招昭找沼赵照罩兆肇召遮折哲蛰辙者锗蔗这浙珍斟真甄砧臻贞针侦枕疹诊震振镇阵蒸挣睁征狰争怔整拯正政帧症郑证芝枝支吱蜘知肢脂汁之织职直植殖执值侄址指止趾只旨纸志挚掷至致置帜峙制智秩稚质炙痔滞治窒中盅忠钟衷终种肿重仲众舟周州洲诌粥轴肘帚咒皱宙昼骤珠株蛛朱猪诸诛逐竹烛煮拄瞩嘱主著柱助蛀贮铸筑住注祝驻抓爪拽专砖转撰赚篆桩庄装妆撞壮状椎锥追赘坠缀谆准捉拙卓桌琢茁酌啄着灼浊兹咨资姿滋淄孜紫仔籽滓子自渍字鬃棕踪宗综总纵邹走奏揍租足卒族祖诅阻组钻纂嘴醉最罪尊遵昨左佐柞做作坐座";
var charBHStr = "一乙丁七乃九了二人儿入八几刀刁力十卜厂又万丈三上下个丫丸久么义乞也习乡于亏亡亿凡刃勺千卫叉口土士夕大女子寸小尸山川工己已巳巾干广弓才门飞马不与丑专中丰丹为之乌书予云互五井亢什仁仅仆仇今介仍从仑仓允元公六内冈冗凤凶分切劝办勾勿匀化匹区升午卞厄厅历及友双反壬天太夫孔少尤尹尺屯巴币幻开引心忆戈户手扎支文斗斤方无日曰月木欠止歹毋比毛氏气水火爪父片牙牛犬王瓦艺见计订讣认讥贝车邓长队韦风且世丘丙业丛东丝主乍乎乏乐仔仕他仗付仙仟代令以仪们兄兰冉册写冬冯凸凹出击刊功加务包匆北匝卉半占卡卢卯厉去发古句另只叫召叭叮可台史右叶号司叹叼囚四圣处外央夯失头奴奶孕宁它对尔尼左巧巨市布帅平幼弗弘归必戊扑扒打扔斥旦旧未末本札术正母民永汀汁汇汉灭犯玄玉瓜甘生用甩田由甲申电白皮皿目矛矢石示礼禾穴立纠艾节讨让讫训议讯记轧边辽闪饥驭鸟龙丢乒乓乔买争亚交亥亦产仰仲件价任份仿企伊伍伎伏伐休众优伙会伞伟传伤伦伪充兆先光全共关兴再军农冰冲决刑划列刘则刚创劣动匈匠匡华协印危压厌吁吃各合吉吊同名后吏吐向吓吕吗回因团在圭地场圾壮多夷夸夹夺奸她好如妄妆妇妈字存孙宅宇守安寺寻导尖尘尧尽屹屿岁岂州巡巩帆师年并庄庆延廷异式弛当忙戌戍戎戏成托扛扣扦执扩扫扬收旨早旬旭曲曳有朱朴朵机朽杀杂权次欢此死毕氖汐汕汗汛汝江池污汤汲灯灰爷牟百祁竹米红纤约级纪纫网羊羽老考而耳肉肋肌臣自至臼舌舟色芋芍芒芝虫血行衣西观讲讳讶许讹论讼讽设访诀贞负轨达迁迂迄迅过迈邢那邦邪闭问闯阮防阳阴阵阶页驮驯驰齐两严串丽乱亨亩伯估伴伶伸伺似佃但位低住佐佑体何余佛作你佣克免兑兵况冶冷冻初删判刨利别助努劫励劲劳匣医卤即却卵县君吝吞吟吠否吧吨吩含听吭吮启吱吴吵吸吹吻吼吾呀呆呈告呐呕员呛呜囤园困囱围址均坊坍坎坏坐坑块坚坛坝坞坟坠声壳妊妒妓妖妙妥妨孜孝宋完宏寿尾尿局屁层岔岗岛希帐庇床序庐库应弃弄弟张形彤役彻忌忍志忘忧快忱忻怀我戒扭扮扯扰扳扶批扼找技抄抉把抑抒抓投抖抗折抚抛抠抡抢护报拒拟改攻旱时旷更杆杉李杏材村杖杜束杠条来杨极步歼每求汞汪汰汹汽汾沁沂沃沈沉沏沙沛沟没沤沥沦沧沪泛灵灶灸灼灾灿牡牢状犹狂狄狈玖玛甫男甸疗皂盯矣社秀私秃究穷系纬纯纱纲纳纵纶纷纸纹纺纽罕羌肖肘肚肛肝肠良芜芥芦芬芭芯花芳芹芽苇苍苏补角言证评诅识诈诉诊诌词译谷豆贡财赤走足身轩辛辰迎运近返还这进远违连迟邑邮邯邱邵邹邻酉里针钉闰闲间闷阻阿陀附际陆陇陈韧饭饮驱驳驴鸡麦龟丧乖乳事些享京佩佬佯佰佳使侄侈例侍侗供依侠侣侥侦侧侨侩兔其具典净凭凯函刮到制刷券刹刺刻刽剁剂势卑卒卓单卖卧卷厕叁参叔取呢周味呵呸呻呼命咀咆咋和咎咏咐咒咕咖咙哎固国图坡坤坦坪坯坷垂垃垄备夜奄奇奈奉奋奔妮妹妻姆始姐姑姓委孟季孤学宗官宙定宛宜宝实宠审尚居屈屉届岩岭岳岸岿巫帕帖帘帚帛帜幸底店庙庚府庞废建弥弦弧录彼往征径忠念忽忿态怂怔怕怖怜性怪怯或房所承抨披抬抱抵抹押抽抿拂拄担拆拇拈拉拌拍拎拐拓拔拖拘拙招拢拣拥拦拧拨择放斧斩旺昂昆昌明昏易昔朋服杭杯杰松板构枉析枕林枚果枝枢枣枪枫柜欣欧武歧殴氓氛沫沮河沸油治沼沽沾沿泄泅泊泌法泞泡波泣泥注泪泳泻泼泽浅炉炊炎炒炔炕炙炬爬爸版牧物狐狗狙狞玩玫环现瓮画畅疙疚疟疡的盂盲直知矽矾矿码祈秆秉空线练组绅细织终绊绍绎经罗者耶肃股肢肤肥肩肪肮肯育肺肾肿胀胁舍艰苑苔苗苛苞苟若苦苫苯英苹茁茂范茄茅茎虎虏虱表衫衬规觅视试诗诚诛话诞诡询诣该详诧责贤败账货质贩贪贫贬购贮贯转轮软轰迢迪迫迭述郁郊郎郑采金钎钒钓闸闹阜陋陌降限陕隶雨青非顶顷饯饰饱饲驶驹驻驼驾鱼鸣齿临举亭亮亲侮侯侵便促俄俊俏俐俗俘保俞信俩俭修兹养冒冠剃削前剐剑勃勇勉勋南卸厘厚受变叙叛咨咬咯咱咳咸咽哀品哄哆哇哈哉响哑哗哟哪型垒垛垢垣垦垫垮城复奎奏契奖姚姜姥姨姻姿威娃娄娇娜孩孪客宣室宦宪宫封将尝屋屎屏峙峡峦差巷帝带帧帮幽度庭弯彦彪待很徊律怎怒思怠急怨总恃恍恒恢恤恨恫恬恼战扁拜括拭拯拱拴拷拼拽拾持挂指按挎挑挖挝挞挟挠挡挣挤挥挪挺政故施既星映春昧昨昭是昼显枯架枷柄柏某柑柒染柔柞柠查柬柯柱柳柿栅标栈栋栏树歪殃殆残段毒毖毗毡氟氢泉泵洁洋洒洗洛洞津洪洱洲活洼洽派浇浊测济浑浓涎炭炮炯炳炸点炼炽烁烂烃牲牵狠狡独狭狮狰狱玲玻珊珍珐甚甭界畏疤疥疫疮疯癸皆皇盅盆盈相盼盾省眉看眨矩砂砌砍砒研砖砚祖祝神禹秋种科秒穿突窃竖竿类籽绑绒结绕绘给绚络绝绞统缸罚美耍耐胃胆背胎胖胚胜胞胡脉茧茨茫茬茵茶茸茹荆草荐荒荔荚荡荣荤荧荫药虐虹虽虾蚀蚁蚂蚤衍袄要览觉诫诬语误诱诲说诵贰贱贴贵贷贸费贺赴赵趴轴轻迷迸迹追退送适逃逆选逊郝郡郧酋重钙钝钞钟钠钡钢钥钦钧钨钩钮闺闻闽阀阁阂陛陡院除陨险面革韭音项顺须食饵饶饺饼首香骂骄骆骇骨鬼鸥鸦乘俯俱俺倍倒倔倘候倚借倡倦倪债值倾健党兼冤凄准凉凋凌剔剖剥剧匪匿卿原哥哦哨哩哭哮哲哺哼唁唆唇唉唐唤啊圃圆埂埃埋埔壶夏套姬娘娟娠娥娩娱宰害宴宵家容宽宾射屑展峨峪峭峰峻席座弱徐徒恋恐恕恩恭息恳恶悄悍悔悟悦悯扇拳拿挚挛挨挫振挽捂捅捆捉捌捍捎捏捐捕捞损捡换捣效敌敖斋料旁旅晃晋晌晒晓晕晚朔朗柴栓栖栗校株样核根格栽桂桃桅框案桌桐桑桓桔档桥桨桩梆梢梧梨殉殊殷毙氦氧氨泰流浆浙浚浦浩浪浮浴海浸涂涅消涉涌涕涛涝涟涡涣涤润涧涨涩烈烘烙烛烟烤烦烧烩烫烬热爱爹特牺狸狼珠班瓶瓷畔留畜疲疹疼疽疾病症痈痉皋皱益盎盏盐监真眠眩砧砰破砷砸砾础祟祥离秘租秤秦秧秩积称窄窍站竞笆笋笑笔粉紊素索紧绢绣绥绦继缺罢羔羞翁翅耕耗耘耙耸耻耽耿聂胯胰胳胶胸胺能脂脆脊脏脐脑脓臭致舀航般舰舱艳荷莆莉莎莫莱莲获莹莽虑蚊蚌蚕蚜衰衷袁袍袒袖袜被请诸诺读诽课谁调谅谆谈谊豹豺贼贾贿赁赂赃资赶起躬载轿较辱透逐递途逗通逛逝逞速造逢部郭郴郸都酌配酒釜钱钳钵钻钾铀铁铂铃铅铆阅陪陵陶陷难顽顾顿颁颂预饿馁骋验骏高鸭鸯鸳鸵乾假偏做停偶偷偿傀兜兽冕减凑凰剪副勒勘匙厢厩唬售唯唱唾啃啄商啡啤啥啦啪啮啸圈域埠培基堂堆堑堕堵够奢娶婆婉婚婪婴婶孰宿寂寄寅密寇尉屠崇崎崔崖崩崭巢常庶康庸廊弹彩彬得徘恿悉悠患您悬悸悼情惊惋惕惜惟惦惧惨惭惮惯戚捧据捶捷捻掀掂掇授掉掏掐排掖掘掠探接控推掩措掳掷掸掺敏救教敛敝敢斜断旋族晤晦晨曹曼望桶梁梅梗梦梭梯械梳检欲毫涪涯液涵涸淀淄淆淋淌淑淖淘淡淤淫淬淮深淳混淹添清渊渍渐渔渗渠烯烷烹烽焉焊焕爽犁猎猖猛猜猪猫率球琅理琉琐甜略畦疵痊痒痔痕皑盒盔盖盗盘盛眯眶眷眺眼着睁矫硅硒硕票祭祷祸秸移秽窑窒竟章笛符笨第笺笼粒粕粗粘累绩绪续绰绳维绵绷绸综绽绿缀羚翌聊聋职脖脚脯脱脸舵舶舷船菇菊菌菏菜菠菩菱菲萄萌萍萎萝萤营萧萨著虚蛀蛆蛇蛊蛋衅衔袋袭袱谋谍谎谐谓谗谚谜象赊赦趾跃距躯辅辆逮逸逻鄂酗酚酝酞野铜铝铡铣铬铭铰铱铲银阉阎阐隅隆隋随隐雀雪颅领颇颈馅馆骑鸽鸿鹿麻黄龚傅傈傍傣储傲凿剩割募博厦厨啼喀喂善喇喉喊喘喜喝喧喳喷喻堡堤堪堰塔壹奠奥婿媒媚嫂富寐寒寓尊就属屡嵌帽幂幅强彭御循悲惑惠惩惫惰惶惹惺愉愤愧慌慨掌掣揉揍描提插揖握揣揩揪揭援揽搀搁搂搅搓搔搜搭搽敞散敦敬斌斑斯普景晰晴晶智晾暂暑曾替最朝期棉棋棍棒棕棘棚棠森棱棵棺椅植椎椒椭椰榔欺款殖毯氮氯氰渝渡渣渤温渭港渴游渺湃湍湖湘湛湾湿溃溅溉滁滋滑滞焙焚焦焰然煮牌犀犊猩猴猾琢琳琴琵琶琼甥番畴疏痘痛痞痢痪登皖短硝硫硬确硷禄禽稀程稍税窖窗窘窜窝竣童等筋筏筐筑筒答策筛粟粤粥粪紫絮缄缅缆缉缎缓缔缕编缘羡翔翘联脾腆腊腋腑腔腕舒舜艇落葛葡董葫葬葱葵蒂蒋蛔蛙蛛蛤蛮蛰蜒街裁裂装裕裙裤谢谣谤谦赋赌赎赏赐赔趁超越趋跋跌跑践辈辉辊辜逼逾遁遂遇遍遏道遗酣酥釉释量铸铺链销锁锄锅锈锋锌锐锑阑阔隔隘隙雁雄雅集雇韩颊馈馋骗骚鲁鹃鹅黍黑鼎催傻像剿勤叠嗅嗓嗜嗡嗣塌塑塘塞填墓媳嫁嫉嫌寝寞幌幕廉廓微想愁愈意愚感慈慎慑搏搐搞搪搬携摄摆摇摈摊摸数斟新暇暖暗椽椿楔楚楞楷楼概榆槐歇歌殿毁源溜溢溪溯溶溺滇滓滔滚满滤滥滦滨滩漓漠煌煎煞煤照献猿瑚瑞瑟瑰甄畸痰痴痹瘁盟睛睡督睦睫睬睹瞄矮硼碉碌碍碎碑碗碘碰禁福稗稚稠窟窥筷筹签简粮粱粳缚缝缠罩罪置署群聘肄肆腥腮腰腹腺腻腾腿舅蒙蒜蒲蒸蓄蓉蓑蓖蓝蓟蓬虞蛹蛾蜂蜕蜗衙裔裸褂解触詹誉誊谨谩谬豢貉赖跟跨跪路跳跺躲辐辑输辞辟遣遥鄙酪酬酮酱鉴锗错锚锡锣锤锥锦锨锭键锯锰障雍雏零雷雹雾靖靳靴靶韵颐频颓颖馏魁魂鲍鹊鹏鼓鼠龄僚僧僳兢凳嗽嘉嘎嘘嘛境墅墒墙嫡嫩孵察寡寥寨廖弊彰愿慕慢慷截摔摘摧摹撂撇敲斡旗榜榨榴榷槛模歉滴漂漆漏演漫漱漳漾潍煽熄熊熏熔熙熬瑶璃疑瘟瘦瘩瞅碟碧碱碳碴磁磋稳竭端箍箔箕算管箩粹精缨缩翟翠聚肇腐膀膊膏膜舆舔舞蔑蔓蔗蔚蔡蔫蔷蔼蔽蜀蜘蜜蜡蝇蝉裳裴裹褐褪誓谭谰谱豪貌赘赚赛赫踊踌辕辖辗辣遭遮酵酶酷酸酿锹锻镀镁隧雌需静韶颗馒骡魄鲜鼻僵僻凛劈嘱嘲嘶嘻嘿噎噶增墟墨墩履幢影德慧慰憋憎憨懂戮摩撅撑撒撕撞撤撩撬播撮撰撵擒敷暮暴槽樊樟横樱橡毅潘潜潦潭潮澄澈澎澜澳熟瘤瘪瘫瞎瞒碾磅磊磐磕稻稼稽稿箭箱篆篇篓糊缮聪膘膛膝艘蔬蕉蕊蕴蝎蝗蝴蝶褒褥谴豌豫趟趣踏踞踢踩踪躺遵醇醉醋镇镊镍镐镑霄震霉靠鞋鞍题颜额飘骸鲤鹤黎儒冀凝嘴器噪噬壁憾懈懊懒撼擂擅操擎擞整橇橙橱潞澡激濒燃燎燕獭瓢瘴瘸瞥磨磺穆窿篙篡篮篱篷糕糖糙缴翰翱耪膨膳臻蕾薄薛薪薯融螟衡赞赠蹄辙辨辩避邀醒醚醛镜雕霍霓霖靛鞘颠餐鲸黔默儡嚎嚏壕孺徽懦戴擦曙檀檄檬燥爵癌瞧瞩瞪瞬瞳磷礁穗簇簧糜糟糠繁翼臀臂臃臆藉藏藐螺襄豁赡赢蹈蹋辫镣霜霞鞠骤魏鳃龋嚣彝戳瀑瞻翻藕藤藩襟覆蹦躇镭镰鞭鬃鹰孽攀攒曝爆瓣疆癣簿羹藻蘑蟹警蹬蹭蹲蹿靡颤鳖麓嚷嚼壤巍攘灌籍糯纂耀蠕譬躁魔鳞蠢赣露霸霹髓囊瓤蘸镶攫罐颧矗";