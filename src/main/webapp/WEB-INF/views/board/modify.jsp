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
		$("#modifyBT").click(function() {
			$("#updateForm").submit();
		});

		$("#cancelBT").click(function() {
			history.back();
		});

	});

	function upload() {
		var formData = new FormData();
		formData.append($("#uploadBt").val(), uploadBt.files[0])
		$.ajax({
			url : "/board/fileUpload",
			data : formData,
			dataType : 'text',
			processData : false,
			contentType : false,
			type : 'POST',
			success : function(data) {

				if (data.search("upload:overSize") != -1) {
					$("#uploadBt").val('');
					alert("10MB 까지만 지원됩니다.");
				} else if (data.search("upload:false") != -1) {
					$("#uploadBt").val('');
					alert("서버 문제로 인한 실패");
				} else
					$("#filePath").val($(data).text());
			},
			error : function(data) {
				alert('error');
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
		<h2>글 수정</h2>
		<form id="updateForm" method="post" action="/board/updateWriting"
			enctype="multipart/form-data">
			<table width="400px" >
				<tr style="display: none;">
					<td>writingNum</td>
					<td><input id="writingNum" name="writingNum" type="text"
						value="${writing.writingNum}" /></td>
				</tr>
				<tr height="30px" align="left">
					<td width="25%">제목</td>
					<td width="75%"><input id="title" name="title" type="text"
						maxlength="50" size="40" value="${writing.title}" /></td>
				</tr>
				<tr height="30px" align="left">
					<td width="25%">Email</td>
					<td>${writing.email}</td>
				</tr>
				<tr height="30px" align="left">
					<td colspan="2">본문</td>
				</tr>
				<tr>
					<td colspan="2"><textarea name="content" cols="56" rows="12"><c:if
								test="${not empty writing.content}">${writing.content}</c:if></textarea></td>
				</tr>
			</table>
			<input id="filePath" name="filePath" type="text" style="display: none" value="<c:if test='${not empty writing.filePath}'>${writing.filePath}</c:if>">
		</form>
		<form id="uploadForm" method="post" enctype="multipart/form-data">
			<table width="400px" >
				<tr height="30px" align="left">
					<td >파일첨부</td>
					<td ><c:if test="${not empty writing.filePath}">
						${requestScope.fileName}
					</c:if> <c:if test="${empty writing.filePath}">
							<input type="file" name="uploadBt" id="uploadBt" onchange="upload()">
						</c:if></td>
				</tr>
			</table>
		</form>
		<br>
		<table style="border: none;">
			<tr>
				<td colspan="2" style="border: none;"><input type="button"
					id="cancelBT" value="취소">&nbsp;&nbsp;<input type="button"
					id="modifyBT" value="수정"></td>
			</tr>
		</table>

	</div>
</body>
</html>