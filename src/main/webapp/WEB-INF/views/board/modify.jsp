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
		<h2>글 수정</h2>
		<form id="updateForm" method="post" action="/board/updateWriting"
			enctype="multipart/form-data">
			<table width="400px">
				<tr style="display: none;">
					<td>writingNum</td>
					<td><input id="writingNum" name="writingNum" type="text"
						value="${writing.writingNum}" /></td>
				</tr>
				<tr height="30px">
					<td width="25%">제목</td>
					<td width="75%"><input id="title" name="title" type="text"
						maxlength="50" size="42" value="${writing.title}" /></td>
				</tr>
				<tr height="30px">
					<td width="25%">Email</td>
					<td>${writing.email}</td>
				</tr>
				<tr height="30px">
					<td colspan="2">본문</td>
				</tr>
				<tr>
					<td colspan="2"><textarea name="content" cols="56" rows="10"><c:if
								test="${not empty writing.content}">${writing.content}</c:if></textarea></td>
				</tr>
				<tr height="30px">
					<td width="10%">파일첨부</td>
					<td width="90%"><input type="file"
						<c:if test="${not empty writing.filePath}">value="${writing.filePath}"</c:if>></td>
				</tr>
			</table>
			<br>
			<table style="border: none;">
				<tr>
					<td colspan="2" style="border: none;"><input type="button"
						id="cancelBT" value="취소">&nbsp;&nbsp;<input type="button"
						id="modifyBT" value="수정"></td>
				</tr>
			</table>
		</form>

	</div>
</body>
</html>