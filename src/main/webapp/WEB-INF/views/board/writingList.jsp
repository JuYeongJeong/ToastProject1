<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="http://code.jquery.com/jquery-2.1.3.js"></script>
<script>
	$(document).ready(function() {
		$("#writingBT").click(function() {
			location.replace("/board/writingView");
		});
	});
</script>
<style type="text/css">
table, th, td {
	border-top: 1px solid #D5D5D5;
	border-bottom: 1px solid #D5D5D5;
	border-collapse: collapse;
	
}

a:link,a:visited,a:active {text-decoration:none;}

a{
	color:Black;
}
</style>


</head>
<body>
	<div align="center">
		<h2>게시판</h2>
		<table width='600px' style="table-layout: fixed">
			<thead>
				<tr height="40px">
					<th scope="col" width="8%">번호</th>
					<th scope="col" width="57%">제목</th>
					<th scope="col" width="15%">날짜</th>
					<th scope="col" width="20%">email</th>
				</tr>
			</thead>
			<tbody>
				<c:set value="${logList}" var="logList"></c:set>
				<c:forEach items="${writingList}" var="writing" varStatus="status">
					<tr align="center" height="30px">
						<td>${writing.writingNum}</td>
						<td align="left" style="padding-left: 20px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis"><a
							href="/board/showWriting?writingNum=${writing.writingNum}">${writing.title}</a></td>
						<td style="font-size: 8px;"><fmt:formatDate
								var="dateTempParse" pattern="yyyy-MM-dd HH:mm:ss"
								value="${logList[status.index].changeTime}" /> ${dateTempParse}
						</td>
						<td style="font-size: 8px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">${writing.email}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<br>
		<table width='600px' style="border: none">
			<tr align="center">
				<td colspan="2" style="border: none">
					<c:forEach	items="${pageList}" varStatus="status">
						<c:choose>
							 <c:when test="${pageList[status.index] == 'next'}">
							 	<a href="/board/showList?pageNum=${pageList[status.index-1]+1}">next</a>
							 </c:when>
							 <c:when test="${pageList[status.index] == 'prev'}">
							 	<a href="/board/showList?pageNum=${pageList[1]-1}">prev</a>
							 </c:when>
							 <c:when test="${pageList[status.index] == requestScope.curPage}">
							 	<a href="/board/showList?pageNum=${pageList[status.index]}"><b>${pageList[status.index]}</b></a>
							 </c:when>
							<c:otherwise>
								<a href="/board/showList?pageNum=${pageList[status.index]}">
									${pageList[status.index]}</a>
							</c:otherwise>	
						</c:choose>				
					</c:forEach>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="right" style="border: none">
					<input type="Button" id="writingBT" value="글쓰기"/>
				</td>
			</tr>
		</table>
	</div>
</html>