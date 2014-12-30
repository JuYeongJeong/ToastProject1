<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
		var regex = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;
		if (regex.test(email))
			result = true;
		else
			return false;

		//server check
		$.ajax({
			type : "POST",
			url : "/board/isCorrectEmail",
			data : {
				email : email
			},
			success : function(data) {
				if (data.search("true") != -1)
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
	
	function upload()
	{
		 var formData = new FormData();
		 formData.append($("#uploadBt").val(),uploadBt.files[0])
		  $.ajax({
		      url: "/board/fileUpload",
		      data: formData,
		      dataType: 'text',
		      processData: false,
		      contentType: false,
		      type: 'POST',
		      success: function (data) {
		 
		    	if(data.search("upload:overSize") !=-1)
		    	{
		    		$("#uploadBt").val('');
		    		alert("10MB 까지만 지원됩니다.");	
		    	}
		    	else if(data.search("upload:false") != -1)
		    	{	
		    		$("#uploadBt").val('');
		    		alert("서버 문제로 인한 실패");
		    	}
		    	else
		    		$("#filePath").val($(data).text());
		 
		      },
		      error: function (data) {
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
		<h2>글 쓰기</h2>
		<form id="writingForm" method="post" action="/board/addWriting"
			enctype="multipart/form-data">
			<table width="400px" >
				<tr height="30px">
					<td width="25%">제목</td>
					<td width="75%"><input id="title" name="title" type="text"
						maxlength="50" size="40"></td>
				</tr>
				<tr height="30px">
					<td width="25%">Email</td>
					<td width="75%"><input id="emailFirst" name="emailFirst"
						type="text" size="15">@<input id="emailSecond"
						name="emailSecond" type="text" size="18"></td>
				</tr>
				<tr height="30px">
					<td width="25%">Password</td>
					<td width="75%"><input name="password" type="password"
						size="40"></td>
				</tr>
				<tr>
					<td colspan="2"><textarea name="content" cols="56" rows="12">내용을 입력하세요</textarea>
					</td>
				</tr>
			</table>
			<input id="filePath" name="filePath" type="text" style="display: none">
		</form>
		<form id="uploadForm" method="post" enctype="multipart/form-data">
			<table width="400px" style="table-layout: fixed">
				<tr height="30px">
					<td align="left" width="100%" colspan="2"><input type="file"
						name="uploadBt" id="uploadBt" onchange="upload()"></td>
				</tr>
			</table>
		</form>
			<br>
			<table width="400px" style="border: none;">
				<tr >
					<td align="center" colspan="2" style="border: none;"><input type="button"
						id="cancelBt" onclick="cancelClick('${curPage}');" value="취소">&nbsp;&nbsp;<input
						type="button" id="addWritingBt" value="확인">
				</tr>
			</table>
		

	</div>
</body>
</html>