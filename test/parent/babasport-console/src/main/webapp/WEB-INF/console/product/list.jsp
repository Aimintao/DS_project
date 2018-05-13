<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>babasport-list</title>
<script type="text/javascript">
//上架 下架
function isShow(flag){
	//请至少选择一个
	var size = $("input[name='ids']:checked").size();
	if(size == 0){
		alert("请至少选择一个");
		return;
	}
	//你确定上架或下架吗
	if(!confirm("你确定上架或下架吗")){
		return;
	}
	//提交 Form表单
	$("#jvForm").attr("action","isShow.do?isShow="+flag);
	$("#jvForm").attr("method","post");
	$("#jvForm").submit();
	
}
</script>
</head>
<body>
<div class="box-positon">
	<div class="rpos">当前位置: 商品管理 - 列表</div>
	<form class="ropt">
		<input class="add" type="button" value="添加" onclick="window.location.href='showAdd.do'"/>
	</form>
	<div class="clear"></div>
</div>
<div class="body-box">
<form action="list.do" method="get" style="padding-top:5px;">
名称: <input type="text" name="name"/>
	<select name="brandId">
		<option value="">请选择品牌</option>
		<c:forEach items="${brands}" var="brand">
		<option value="${brand.id}">${brand.name}</option>	
		</c:forEach>
	</select>
			<script>
				$("select[name=brandId]").val('4');
			</script>
	
	<select name="isShow">
		<option value="1">上架</option>
		<option selected="selected" value="0">下架</option>
	</select>
	<input type="submit" class="query" value="查询"/>
</form>
<form id="jvForm">
<table cellspacing="1" cellpadding="0" width="100%" border="0" class="pn-ltable">
	<thead class="pn-lthead">
		<tr>
			<th width="20"><input type="checkbox" onclick="Pn.checkbox('ids',this.checked)"/></th>
			<th>商品编号</th>
			<th>商品名称</th>
			<th>图片</th>
			<th width="4%">新品</th>
			<th width="4%">热卖</th>
			<th width="4%">推荐</th>
			<th width="4%">上下架</th>
			<th width="12%">操作选项</th>
		</tr>
	</thead>
	<tbody class="pn-ltbody">
	<c:forEach items="${pageProduct.result}" var="product">
	
		<tr bgcolor="#ffffff" onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">
			<td><input type="checkbox" name="ids" value="${product.id}"/></td>
			<td>${product.id}</td>
			<td align="center">${product.name}</td>
			
			<td align="center">
			<c:forTokens items="${product.imgUrl}" delims="," var="iu" begin="0" end="0">
				<img width="50" height="50" src="${iu}"/>
			</c:forTokens>
			
			</td>
			<td align="center">是</td>
			<td align="center">是</td>
			<td align="center">是</td>
			<td align="center">${product.isShow}</td>
			<td align="center">
			<a href="#" class="pn-opt">查看</a> | <a href="#" class="pn-opt">修改</a> | <a href="#" onclick="if(!confirm('您确定删除吗？')) {return false;}" class="pn-opt">删除</a> | 
			<a href="../sku/list.do?productId=${product.id}" class="pn-opt">库存</a>
			</td>
		</tr>
		
		</c:forEach>
	</tbody>
</table>
<div class="page pb15">
	<span class="r inb_a page_b">
	
		<font size="2"><a href="list.do?pageNum=1">首页</a></font>
	
		<font size="2">上一页</font>
	
		<strong>1</strong>
	
		<a href="/product/list.do?&amp;isShow=0&amp;pageNo=2">2</a>
	
		<a href="/product/list.do?&amp;isShow=0&amp;pageNo=3">3</a>
	
		<a href="/product/list.do?&amp;isShow=0&amp;pageNo=4">4</a>
	
		<a href="/product/list.do?&amp;isShow=0&amp;pageNo=5">5</a>
	
		<font size="2"><a href="list.do?pageNum=${pageProduct.pageNum+1}">下一页</a></font>
	
		<a href="/product/list.do?&amp;isShow=0&amp;pageNo=5"><font size="2">尾页</font></a>
	
		共<var>5</var>页 到第<input type="text" size="3" id="PAGENO"/>页 <input type="button" onclick="javascript:window.location.href = '/product/list.do?&amp;isShow=0&amp;pageNo=' + $('#PAGENO').val() " value="确定" class="hand btn60x20" id="skip"/>
	
	</span>
</div>
<div style="margin-top:15px;"><input class="del-button" type="button" value="删除" onclick="optDelete();"/>
<input class="add" type="button" value="上架" onclick="isShow(1);"/>
<input class="del-button" type="button" value="下架" onclick="isShow(0);"/></div>
</form>
</div>
</body>
</html>