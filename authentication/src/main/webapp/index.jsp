<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://richfaces.org/rich" prefix="rich"%>

<html>
	<head>
		<title>Hello World</title>
	</head>
	<body>
		<f:view>
			<h1>Hello world</h1>
			<h:outputText value="Nom:" />
			<h:outputText value="#{homeBean.test}"/>
			
			<rich:calendar></rich:calendar>
		</f:view>
	</body>
</html>
