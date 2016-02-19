<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<html>
  <head>
    <title></title>
	<script language="javascript">
		var oldwidth = "0";
		function closeLeft()
		{
			var myset = parent.document.getElementsByTagName("frameset")[0];
			
			if(myset.children[0].style.display == "none")
			{
				var oldcols = myset.cols;
				var oldcol = oldcols.split(",");
				var newcols = oldwidth;
				oldwidth = oldcol[0];
				for(var i=1;i<oldcol.length;i++)
				{
					newcols += "," + oldcol[i];
				}
				
				myset.cols = newcols;
				myset.children.item(0).style.display="";
				switchImg.src = "${ctx}/images/isopen.gif";
				switchImg.alt="关闭";
			}
			else
			{
				//alert(sets.item(0).cols);
				var oldcols = myset.cols;
				var oldcol = oldcols.split(",");
				var newcols = oldwidth;
				oldwidth = oldcol[0];
				for(var i=1;i<oldcol.length;i++)
				{
					newcols += "," + oldcol[i];
				}
				
				myset.cols = newcols;
				myset.children[0].style.display="none";
				switchImg.src = "${ctx}/images/isclose.gif";
				switchImg.alt="打开";
			}
			//parent.frames.item(0).style.display="none";
		}
	</script>
  </head>
  
  <body topmargin="0" leftmargin="0" bgcolor="#6E7786">
  
  <TABLE cellSpacing=0 cellPadding=0 border=0 height="100%">
  <TBODY>
  <TR>
    <TD style="HEIGHT: 100%" valign="middle">
  		<img id="switchImg" src="${ctx}/images/isopen.gif" onClick="closeLeft();" alt="关闭" />
  	</TD>
  </TR>
  </table>
  </body>
</html>
