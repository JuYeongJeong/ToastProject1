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

			var email = $("#emailFirst").val() + '@' + $("#emailSecond").val();
		
			if ($("#title").val() == '') {
				alert("제목을 입력하세요");
				return;
			}

			if (!isCollectEmail(email)) {
				alert("올바른 email을 입력하세요");
				return;
			}

			if ($("#password").val() == '') {
				alert("password 입력하세요");
				return;
			}

			if ($("#content").val() == '') {
				alert("내용을 입력하세요");
				return;
			}

			$("#writingForm").submit();
		});
	});

	function isCollectEmail(email) {
		var result = true;
	
		//client check
		var regex =/^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;
		if (regex.test(email))
			result = true;
		else
			return false;
		
		//server check
		$.ajax({
			type : "POST",
			url : "/board/isCollectEmail",
			data : {
				email : email
			},
			success : function(data) {
				if(data.search("true") != -1)
					result = true;
				else
					result = false;
			},
			error : function(data) {
				alert("error");
			}
		});
		
		
		return result;
	}

	function cancelClick(curPage) {
		location.replace("/board/showList?pageNum=" + curPage);
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
		<h2>글 쓰기</h2>
		<form id="writingForm" method="post" action="/board/addWriting"
			enctype="multipart/form-data">
			<table width="400px">
				<tr height="30px">
					<td width="25%">제목</td>
					<td width="75%"><input id="title" name="title" type="text"
						maxlength="50" size="40"></td>
				</tr>
				<tr height="30px">
					<td width="25%">Email</td>
					<td width="75%"><input id="emailFirst" name="emailFirst"
						type="text" size="15">@<input id="emailSecond"
						name="emailSecond" type="text" size="15"></td>
				</tr>
				<tr height="30px">
					<td width="25%">Password</td>
					<td width="75%"><input name="password" type="password"
						size="40"></td>
				</tr>
				<tr>
					<td colspan="2"><textarea name="content" cols="54" rows="10">내용을 입력하세요</textarea>
					</td>
				</tr>
				<tr height="30px">
					<td width="10%">파일첨부</td>
					<td width="90%" align="left"><input type="file"></td>
				</tr>
			</table>
			<br>
			<table style="border: none;">
				<tr>
					<td colspan="2" style="border: none;"><input type="button"
						id="cancelBt" onclick="cancelClick('${curPage}');" value="취소">&nbsp;&nbsp;<input
						type="button" id="addWritingBt" value="확인">
				</tr>
			</table>
		</form>

	</div>
</body>
</html>