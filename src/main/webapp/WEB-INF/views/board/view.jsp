<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<table>
		<tr>
			<td>제목</td>
			<td><input id="title" name="title" type="text" ></td>
		</tr>
		<tr>
			<td>Email</td>
			<td><input name="email" type="text"></td>
		</tr>
		<tr>
			<td>Password</td>
			<td><input name="password" type="password"></td>
		</tr>
		<tr>
			<td colspan="2">
			<textarea name="content" cols="40" rows="10"></textarea>
			</td>
		</tr>
		<tr>
			<td>파일첨부</td>
			<td><input type="file"></td>
		</tr>
	</table>
	
	<table>
		<tr><td><a href="#" id="addWritingBt">확인</a> <a href="#" id="cancelBt">취소</a></td></tr>
	</table>
</body>
</html>