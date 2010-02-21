<%@ include file="../include.jsp"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<html>
	<head>
		<c:set var="css"><spring:theme code="css"/></c:set>
		<c:if test="${not empty css}">
			<link rel="stylesheet" href="<c:url value="${css}"/>" type="text/css" />
		</c:if>
		<tiles:insertAttribute name="meta"/>
		<title>
			<c:set var="titleNameKey"><tiles:getAsString name="titleKey"/></c:set>
			<c:choose>
				<c:when test="${not empty titleNameKey}">
					<fmt:message key="${titleNameKey}"/>
				</c:when>
				<c:otherwise>
					<fmt:message key="home.title"/>
				</c:otherwise>
			</c:choose>
		</title>
	</head>
	
	<body>
		<div><tiles:insertAttribute name="header"/></div>
		<div><tiles:insertAttribute name="body"/></div>
		<div><tiles:insertAttribute name="footer"/></div>
	</body>
</html>