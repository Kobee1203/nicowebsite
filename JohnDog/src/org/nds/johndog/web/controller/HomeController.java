package org.nds.johndog.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nds.johndog.model.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.context.SecurityContext;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;


public class HomeController extends SimpleFormController implements InitializingBean {
	protected final Log log = LogFactory.getLog(getClass());
	
	public void afterPropertiesSet() throws Exception {
		//if ( == null) throw new ApplicationContextException("Must XXX set bean property on " + getClass());
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		return super.referenceData(request, command, errors);
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Map map = new HashMap();
		//Form form = (Form) command;
		
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

		return showForm(request, response, errors, map);
	}

	public User getUserFromSession(HttpServletRequest request) {
		SecurityContext context = (SecurityContext)request.getSession().getAttribute("ACEGI_SECURITY_CONTEXT");
		String login = ((org.springframework.security.userdetails.User) (context.getAuthentication().getPrincipal())).getUsername();
		if (null != login) {
			return null;//this.getUser(login, request);
		}
		else {
			return null;
		}
	}
}
