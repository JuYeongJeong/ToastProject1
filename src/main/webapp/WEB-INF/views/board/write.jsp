<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="http://code.jquery.com/jquery-2.1.3.js"></script>
<script>
$(document).ready(function() {
	$("#addWritingBt").click(function() {
		$("#writingForm").submit();
	});
	
	$("#cancelBt").click(function() {
		//todo
	});
	
	
});
</script>	

</head>
<body>
<div align="center">

 <form id="writingForm" method="post" action="/board/addWriting" enctype="multipart/form-data">
	<table>
		<tr>
			<td>����</td>
			<td><input id="title" name="title" type="text" /></td>
		</tr>
		<tr>
			<td>Email</td>
			<td><input name="email" type="text"/></td>
		</tr>
		<tr>
			<td>Password</td>
			<td><input name="password" type="password"/></td>
		</tr>
		<tr>
			<td colspan="2">
			<textarea name="content" cols="40" rows="10"></textarea>
			</td>
		</tr>
		<tr>
			<td>����÷��</td>
			<td><input type="file"></td>
		</tr>
	</table>
	
	<table>
		<tr><td><a href="#" id="addWritingBt">Ȯ��</a> <a href="#" id="cancelBt">���</a></td></tr>
	</table>
</form>	
	
</div>
</body>
</html>