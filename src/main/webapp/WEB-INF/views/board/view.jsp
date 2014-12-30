<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<!-- ie8이하 버전은 jquery 2.1버전 미지원 -->
<script src="http://code.jquery.com/jquery-1.8.3.js"></script>
<script src="http://code.jquery.com/jquery-2.1.3.js"></script>
<script>
	$(document).ready(function() {
		$("#backBT").click(function() {
			history.back();
		});

	});
</script>

<script type="text/javascript">
	function modifyBtClick() {

		var inputPass = window.prompt("password를 입력하세요");
		if (inputPass == null || inputPass == '')
			return;

		jQuery.ajax({
			type : "POST",
			url : "/board/isCorrectPassword",
			data : {
				writingNum : $("#writingNum").val(),
				password : inputPass
			},
			success : function(data) {
				if(data.search("true") != -1)
					$("#modifyForm").submit();
				else if(data.search("false") != -1)
					alert("비밀번호가 틀립니다.");
				else
					alert("ajax 실패");
			},
			error : function(data) {
				alert("error");
			}
		});
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
		<form method="post" action="/board/modifyView" id="modifyForm">
			<table width="400px" style="table-layout: fixed">
				<tr height="30px">
					<td width="25%">글 번호</td>
					<td width="75%"><input type="text" id="writingNum" name="writingNum"
						style="border: none;" readonly="readonly"
						value="${writing.writingNum}"></td>
				</tr>
				<tr height="30px">
					<td>제목</td>
					<td
						style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis">${writing.title}</td>
				</tr>
				<tr height="30px">
					<td>Email</td>
					<td><input type="text" id="email" name="email"
						style="border: none;" readonly="readonly" value="${writing.email}"></td>
				</tr>
				<tr height="30px">
					<td colspan="2">본문</td>
				</tr>
				<tr height="150px" align="left" valign="top">
					<td colspan="2"><c:if test="${not empty writing.content}">${writing.content}</c:if></td>
				</tr>
				<tr height="30px">
					<td>파일첨부</td>
					<td><c:if test="${not empty requestScope.fileName}"><a href="/board/downloadFile?filePath=${writing.filePath}">${requestScope.fileName}</a></c:if></td>
				</tr>
			</table>
			<br>
			<table style="border: none;">
				<tr>
					<td colspan="2" style="border: none;"><input type="button"
						id="backBT" value="뒤로" />&nbsp;&nbsp; <input type="button"
						id="modifyBT" onclick="modifyBtClick()" value="수정" /></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>