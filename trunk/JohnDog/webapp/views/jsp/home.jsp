<%@ include file="include.jsp"%>
<a href='<c:url value="j_acegi_logout"/>'><fmt:message key="user.logout"/></a>
<div><span>Hello!</span></div>
<sec:authorize ifAllGranted="ROLE_ADMIN">
	<div><span>USER IS ADMIN</span></div>
</sec:authorize>
