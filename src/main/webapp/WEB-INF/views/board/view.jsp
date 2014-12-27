<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="http://code.jquery.com/jquery-2.1.3.js"></script>
<script>
	$(document).ready(function() {
		$("#backBT").click(function() {
			history.back();
		});

	});
</script>
<script type="text/javascript">
	function modifyBtClick(writingNum) {
		var url = "/board/modifyView?writingNum=" + writingNum;
		$(location).attr('href',url);
	}
</script>

<style type="text/css">
table, th, td {
	border-top: 1px solid #D5D5D5;
	border-bottom: 1px solid #D5D5D5;
	border-collapse: collapse;
}
</style>
</head>
<body>
<div align="center">
	<h2>글 보기</h2>
	<table width="400px">
		<tr height="30px">
			<td width="25%">글 번호</td>
			<td id="writingNumCol" width="75%">${writing.writingNum}</td>
		</tr>
		<tr height="30px">
			<td>제목</td>
			<td>${writing.title}</td>
		</tr>
		<tr height="30px">
			<td>Email</td>
			<td>${writing.email}</td>
		</tr>
		<tr height="30px">
			<td colspan="2">본문</td>
		</tr>
		<tr height="150px" align="left" valign="top">
			<td colspan="2"><c:if test="${not empty writing.content}">${writing.content}</c:if></td>
		</tr>
		<tr height="30px">
			<td>파일첨부</td>
			<td><c:if test="${not empty writing.filePath}">${writing.filePath}</c:if></td>
		</tr>
	</table>
	<br>
	<table>
		<tr>
			<td><button id="backBT">뒤로</button>&nbsp;&nbsp;
				<button id="modifyBT" onclick="modifyBtClick(${writing.writingNum})">수정</button></td>
		</tr>
	</table>
</div>
</body>
</html>