package org.nds.jam.web.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nds.jam.web.jpa.bean.Rights;
import org.nds.jam.web.jpa.bean.User;
import org.nds.jam.web.service.UserManager;
import org.nds.mail.MailService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

@Controller
public class RegisterController implements InitializingBean {

	@Autowired
	UserManager userManager;

	@Autowired
	ImageCaptchaService captchaService;

	@Autowired
	MailService mailService;

	public void afterPropertiesSet() throws Exception {
		if (userManager == null) {
			throw new ApplicationContextException("Must set userManager bean property on " + getClass());
		}
		if (captchaService == null) {
			throw new ApplicationContextException("Must set captchaService bean property on " + getClass());
		}
		if (mailService == null) {
			throw new ApplicationContextException("Must set mailService bean property on " + getClass());
		}
	}

	/**
	 * For every request for this controller, this will
	 * create a person instance for the form.
	 */
	@ModelAttribute
	public User newRequest(@RequestParam(required = false) Long id) {
		return (id != null ? userManager.getUser(id) : new User());
	}

	@RequestMapping(value = "/captcha", method = RequestMethod.GET)
	public void captcha(HttpServletRequest request, HttpServletResponse response) throws ImageFormatException, IOException {
		// http://forge.octo.com/jcaptcha/confluence/display/general/JCaptcha+and+the+SpringFramework
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
			// return null;
		} catch (CaptchaServiceException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			// return null;
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

		// return null;
	}

	/**
	 * <p>
	 * Register form request.
	 * </p>
	 * <p>
	 * Expected HTTP GET and request '/register'.
	 * </p>
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public void register() {
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public void register(User user, Model model, HttpServletRequest request) {
		// Validate the captcha
		if (!CaptchaUtil.validateCaptchaForId(request, captchaService)) {
			model.addAttribute("captcha_error", "captcha.incorrect.value");
			return;
		}

		Set<Rights> rights = new HashSet<Rights>();
		rights.add(new Rights("ROLE_USER", user));
		user.setRights(rights);
		User result = userManager.saveUser(user);
		if (user.getId() == null) {
			user.setId(result.getId());
		}

		// Send confirmation mail
		mailService.sendMail(user.getUsername());

		model.addAttribute("statusMessageKey", "register.form.msg.success");
	}
}
