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
	$("#writingBT").click(function() {
		location.replace("/board/writingView");;
	});
});
</script>
</head>
<body>
	<table>
		<thead>
			<tr>
				<th scope="col">번호</th>
				<th scope="col">제목</th>
				<th scope="col">날짜</th>
				<th scope="col">email</th>
			</tr>
		</thead>
		<tbody>
			<c:set value="${logList}" var="logList"></c:set>
			<c:forEach items="${writingList}" var="writing" varStatus="status">
				<tr>
					<td>${writing.writingNum}</td>
					<td><a href="/board/showWriting?writingNum=${writing.writingNum}" >${writing.title}</a></td>
					<td>${logList[status.index].changeTime}</td>
					<td>${writing.email}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<table>
		<tr>
			<td colspan="2">
				<c:forEach begin="1" end="${pageCount}" varStatus="status">
					&nbsp;
					<a href="/board/showList?listNum=${status.index}">${status.index}</a>
					&nbsp;
				</c:forEach>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<button id="writingBT">글쓰기</button>
			</td>
		</tr>
	</table>
	
</html>