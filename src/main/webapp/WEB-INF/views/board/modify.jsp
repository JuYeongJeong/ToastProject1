<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="http://code.jquery.com/jquery-2.1.3.js"></script>
<script>
$(document).ready(function() {
	$("#modifyBT").click(function() {
		$("#updateForm").submit();
	});
	
	$("#cancelBT").click(function() {
		history.back();
	});
	
	
});
</script>	

</head>
<body>
<div align="center">

 <form id="updateForm" method="post" action="/board/updateWriting" enctype="multipart/form-data">
	<table>
		<tr style="display:none;">
			<td>writingNum</td>
			<td><input id="writingNum" name="writingNum" type="text" value="${writing.writingNum}"/></td>
		</tr>
		<tr>
			<td>力格</td>
			<td><input id="title" name="title" type="text" value="${writing.title}"/></td>
		</tr>
		<tr>
			<td>Email</td>
			<td>${writing.email}</td>
		</tr>
		<tr>
			<td colspan="2">
			<textarea name="content" cols="40" rows="10"><c:if test="${not empty writing.content}">${writing.content}</c:if></textarea>
			</td>
		</tr>
		<tr>
			<td>颇老梅何</td>
			<td><input type="file" <c:if test="${not empty writing.filePath}">value="${writing.filePath}"</c:if> ></td>
		</tr>
	</table>
	
	<table>
		<tr><td><a href="#" id="modifyBT">犬牢</a> <a href="#" id="cancelBT">秒家</a></td></tr>
	</table>
</form>	
	
</div>
</body>
</html>