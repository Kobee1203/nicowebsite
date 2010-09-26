package org.nds.web.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nds.security.captcha.CaptchaUtil;
import org.nds.web.formbean.RegisterForm;
import org.nds.web.service.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.octo.captcha.service.image.ImageCaptchaService;

@Controller
public class RegisterController implements InitializingBean {
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
	
	@RequestMapping(value = "/unsecuredregister")
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {		
		RegisterForm form = (RegisterForm) command;
		
		ServletContext sc = request.getSession().getServletContext();
		
		String dir = sc.getRealPath("/");
		log.debug("dir: "+dir);

        if(log.isDebugEnabled()) {
	        log.debug("ServletPath: "+request.getServletPath());
	        log.debug("ContextPath: "+request.getContextPath());
	        log.debug("RequestURI: "+request.getRequestURI());
	        log.debug("RequestURL: "+request.getRequestURL());
	        log.debug("PathInfo: "+request.getPathInfo());
        }

        ModelAndView mav = new ModelAndView();
        
        // Validate the captcha
        if(CaptchaUtil.validateCaptchaForId(request, captchaService)) {
        	mav.setView(new RedirectView("home.html"));
        }
        else {
        	mav.addObject("captcha_error","captcha.incorrect.value");
        }
        
		return mav;
	}

}
