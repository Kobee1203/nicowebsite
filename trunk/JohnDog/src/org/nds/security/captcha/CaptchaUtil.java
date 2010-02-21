package org.nds.security.captcha;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;

public class CaptchaUtil {

	protected final static Log log = LogFactory.getLog(CaptchaUtil.class);
	
	/**
	 * Return true if the response is correct.
	 * @param request
	 * @param captchaService
	 * @return
	 */
	public static boolean validateCaptchaForId(HttpServletRequest request, ImageCaptchaService captchaService) {
		Boolean isResponseCorrect = Boolean.FALSE;
        // Remember that we need an id to validate!
        String captchaId = request.getSession().getId();
        //retrieve the response
        String response = request.getParameter("j_captcha_response");
        // Call the Service method
         try {
             isResponseCorrect = captchaService.validateResponseForID(captchaId, response);
         } catch (CaptchaServiceException e) {
             // Should not happen, may be thrown if the id is not valid
        	 log.error("The captcha id is not valid: "+e);
         }
         
         return isResponseCorrect;
	}
}
