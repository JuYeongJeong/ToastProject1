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

			var title = $("#title").val();
			var email = $("#email").val();
			var password = $("#password").val();
			var content = $("#title").val();

			if (title == '') {
				alert("������ �Է��ϼ���");
				return;
			}

			if (email == '') {
				alert("email �Է��ϼ���");
				return;
			}
			if (password == '') {
				alert("password �Է��ϼ���");
				return;
			}
			if (content == '') {
				alert("������ �Է��ϼ���");
				return;
			}

			$("#writingForm").submit();
		});
	});
	
	function cancelClick(curPage) {
		location.replace("/board/showList?pageNum="+curPage);
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
		<h2>�� ����</h2>
		<form id="writingForm" method="post" action="/board/addWriting"
			enctype="multipart/form-data">
			<table width="400px">
				<tr>
					<td width="25%">����</td>
					<td width="75%"><input id="title" name="title" type="text"
						size="40"></td>
				</tr>
				<tr>
					<td width="25%">Email</td>
					<td width="75%"><input name="email" type="text" size="40"></td>
				</tr>
				<tr>
					<td width="25%">Password</td>
					<td width="75%"><input name="password" type="password"
						size="40"></td>
				</tr>
				<tr>
					<td colspan="2"><textarea name="content" cols="54" rows="10">������ �Է��ϼ���</textarea>
					</td>
				</tr>
				<tr>
					<td width="10%">����÷��</td>
					<td width="90%" align="left"><input type="file"></td>
				</tr>
			</table>
			<br>
			<table style="border: none;">
				<tr>
					<td style="border: none;"><input type="button" id="cancelBt" onclick="cancelClick('${curPage}');"
						value="���">&nbsp;&nbsp;<input type="button"
						id="addWritingBt" value="Ȯ��">
				</tr>
			</table>
		</form>

	</div>
</body>
</html>