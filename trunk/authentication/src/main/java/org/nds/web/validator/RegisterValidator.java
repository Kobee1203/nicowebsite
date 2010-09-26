package org.nds.web.validator;

import org.nds.web.formbean.RegisterForm;
import org.nds.web.service.UserManager;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class RegisterValidator implements Validator {

	UserManager userManager;
	
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	@Override
	public boolean supports(Class clazz) {
		return clazz.isAssignableFrom(RegisterForm.class);
	}

	@Override
	public void validate(Object object, Errors errors) {
		RegisterForm form = (RegisterForm) object;

		/*
		if () { 
			errors.rejectValue("poolOperator.name", "global.error.required");
        }
		*/
	}

}
