package org.nds.security.captcha;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;


public class CaptchaController implements Controller, InitializingBean {

	protected final Log log = LogFactory.getLog(getClass());
	
	ImageCaptchaService captchaService;

	public void setCaptchaService(ImageCaptchaService captchaService) {
		this.captchaService = captchaService;
	}

	public void afterPropertiesSet() throws Exception {
		if (captchaService == null) throw new ApplicationContextException("Must set captchaService bean property on " + getClass());
	}
	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//http://forge.octo.com/jcaptcha/confluence/display/general/JCaptcha+and+the+SpringFramework
    	byte[] captchaChallengeAsJpeg = null;
    	// the output stream to render the captcha image as jpeg into
    	ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
    	try {
    		// get the session id that will identify the generated captcha.
    		// the same id must be used to validate the response, the session id is a good candidate!
    		String captchaId = request.getSession().getId();
    		// call the ImageCaptchaService getChallenge method
    		BufferedImage challenge = captchaService.getImageChallengeForID(captchaId, request.getLocale());

    		// a jpeg encoder
    		JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(jpegOutputStream);
    		jpegEncoder.encode(challenge);
    	} catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        } catch (CaptchaServiceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }

        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();

        // flush it in the response
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
        
        return null;
	}

}