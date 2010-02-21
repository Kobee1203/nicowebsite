package org.nds.johndog.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nds.johndog.web.formbean.RegisterForm;
import org.nds.johndog.web.service.UserManager;
import org.nds.security.captcha.CaptchaUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

import com.octo.captcha.service.image.ImageCaptchaService;


public class RegisterController extends SimpleFormController implements InitializingBean {
	protected final Log log = LogFactory.getLog(getClass());
	
	ImageCaptchaService captchaService;

	UserManager userManager;
	
	public void setCaptchaService(ImageCaptchaService captchaService) {
		this.captchaService = captchaService;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	public void afterPropertiesSet() throws Exception {
		if (captchaService == null) throw new ApplicationContextException("Must set captchaService bean property on " + getClass());
		if (userManager == null) throw new ApplicationContextException("Must set userManager bean property on " + getClass());
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		return super.referenceData(request, command, errors);
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Map map = new HashMap();
		RegisterForm form = (RegisterForm) command;
		
		ServletContext sc = ((XmlWebApplicationContext) this.getApplicationContext()).getServletContext();
		
		String dir = sc.getRealPath("/");
		log.debug("dir: "+dir);

        if(log.isDebugEnabled()) {
	        log.debug("ServletPath: "+request.getServletPath());
	        log.debug("ContextPath: "+request.getContextPath());
	        log.debug("RequestURI: "+request.getRequestURI());
	        log.debug("RequestURL: "+request.getRequestURL());
	        log.debug("PathInfo: "+request.getPathInfo());
        }

        // Validate the captcha
        if(CaptchaUtil.validateCaptchaForId(request, captchaService)) {
        	return new ModelAndView(new RedirectView(getSuccessView()));
        }
        else {
        	map.put("captcha_error","captcha.incorrect.value");
        }
        
		return showForm(request, response, errors, map);
	}

}
