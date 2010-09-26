package org.nds.web.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController implements InitializingBean {
	protected final Log log = LogFactory.getLog(getClass());
	
	public void afterPropertiesSet() throws Exception {
		//if ( == null) throw new ApplicationContextException("Must XXX set bean property on " + getClass());
	}
	
    @RequestMapping(value="/home", method=RequestMethod.POST)
	protected void home(HttpServletRequest request, HttpServletResponse response) {
    	
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
	}

}
