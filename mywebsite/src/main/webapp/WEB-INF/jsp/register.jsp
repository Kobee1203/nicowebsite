<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h1><fmt:message key="register.form.title"/></h1>

<c:if test="${not empty statusMessageKey}">
    <p><fmt:message key="${statusMessageKey}"/></p>
</c:if>

<c:url var="url" value="/register.html" /> 
<form:form action="${url}" commandName="user">
	<form:hidden path="id" />
	<fieldset>
        <div class="form-row">
            <label for="username"><fmt:message key="register.form.email"/>:</label>
            <span class="input"><form:input path="username" /></span>
        </div>       
        <div class="form-row">
            <label for="password"><fmt:message key="register.form.password"/>:</label>
            <span class="input"><form:password path="password" /></span>
        </div>
        <div class="form-row">
            <label for="password"><fmt:message key="register.form.confirmPassword"/>:</label>
            <span class="input"><form:password path="confirmPassword" /></span>
        </div>
        
    	<div class="form-row">
			<div class="fieldname"><img src="captcha.html"/></div>
			<div class="input"><input type="text" name="j_captcha_response" /></div>
			<c:if test="${not empty captcha_error}">
				<div class="fielderror"><fmt:message key="${captcha_error}" /></div>
			</c:if>
		</div>
		
		<div class="form-buttons">
            <div class="button"><input name="submit" type="submit" value="<fmt:message key="button.save"/>" /></div>
        </div>
    </fieldset>

</form:form>