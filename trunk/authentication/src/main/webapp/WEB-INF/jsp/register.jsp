<%@ include file="include.jsp" %>
<span>Register</span>
<form method="post">
	<f:view>
		<h1><h:outputText value="Hello World avec JSF" /></h1><br>
	</f:view>
	<div>
		<spring:bind path="form.email">
			<div class="fieldname"><fmt:message key="field.email"/></div>
			<div class="fieldvalue"><input type="text" name="${status.expression}" value="${status.value}" /></div>
			<div class="fielderror"><c:out value="${status.errorMessage}" /></div>
		</spring:bind>
	</div>
	<div>
		<spring:bind path="form.password">
			<div class="fieldname"><fmt:message key="field.password"/></div>
			<div class="fieldvalue"><input type="password" name="${status.expression}" value="${status.value}" /></div>
			<div><c:out value="${status.errorMessage}" /></div>
		</spring:bind>
	</div>
	<div>
		<spring:bind path="form.confirmPassword">
			<div class="fieldname"><fmt:message key="field.confirmPassword"/></div>
			<div class="fieldvalue"><input type="password" name="${status.expression}" value="${status.value}" /></div>
			<div class="fielderror"><c:out value="${status.errorMessage}" /></div>
		</spring:bind>
	</div>
	
	<div>
		<div class="fieldname"><img src="captcha.html"/></div>
		<div class="fieldvalue"><input type="text" name="j_captcha_response" /></div>
		<c:if test="${not empty captcha_error}">
			<div class="fielderror"><fmt:message key="${captcha_error}" /></div>
		</c:if>
	</div>
	<div><input type="submit" value="Submit" name="_finish"/></div>
</form>