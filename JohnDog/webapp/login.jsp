<%@ include file="views/jsp/include.jsp" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html">
		<title>My Login Page</title>
	</head>
	<body>
		<h2>Please log in</h2>
		<br/>
		<f:view>
			<h:form id="loginForm" prependId="false">
				<h:outputText value="Adresse Email" /><t:inputText id="j_username" forceId="true" required="true"/><br/>
				<h:outputText value="Password" /><t:inputSecret id="j_password" forceId="true" required="true"/><br/>
				<h:outputText value="Remember me" /><t:selectBooleanCheckbox id="_spring_security_remember_me" forceId="true" /><br/>
				<h:commandButton action="#{loginBean.doLogin}" value="Log in >>" />
				<!--h:commandButton action="login" value="Log in >>" /-->
				<h:messages id="messages" layout="table" showSummary="true" showDetail="false" />
			</h:form>
		</f:view>
		<a href="unsecuredregister.html">Register</a>
	</body>
</html>