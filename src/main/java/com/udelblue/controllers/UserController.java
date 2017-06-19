package com.udelblue.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.udelblue.domain.Account;
import com.udelblue.domain.RequestProcessedResults;
import com.udelblue.repositories.UserRepository;
import com.udelblue.services.AccountService;
import com.udelblue.services.EmailService;
import com.udelblue.util.DateUtil;
import com.udelblue.util.EncryptUtil;
import com.udelblue.util.UserDeviceUtil;

@Controller
public class UserController {

	final Logger log = LoggerFactory.getLogger(UserController.class);

	@Value("${app.name}")
	private String appName;

	EmailService emailservice;

	UserRepository userrepository;

	AccountService accountService;
	
	
	@Autowired
	public UserController(EmailService emailservice, UserRepository userrepository, AccountService accountService) {
		super();
		this.emailservice = emailservice;
		this.userrepository = userrepository;
		this.accountService = accountService;
	}

	@RequestMapping(value = "user/activate", method = RequestMethod.GET)
	public ModelAndView activateGet(@RequestParam(value = "token", required = false) String token) {

		String email = "";
		EncryptUtil eu;
		try {
			eu = new EncryptUtil();
			email = eu.decryptEncoded(token);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		log.info("Account activated for: " + email);
		userrepository.activeAccountFromEmail(email);
		ModelAndView mav = new ModelAndView("user/activate");
		return mav;
	}

	@RequestMapping(value = "user/create", method = RequestMethod.GET)
	public ModelAndView createget(@RequestParam(value = "redirect", required = false) String redirect) {

		ModelAndView mav = new ModelAndView("user/create", "createAccount", new Account());
		mav.addObject("redirect", redirect);
		return mav;
	}

	// todo add ip and device logging
	@RequestMapping(value = "user/create", method = RequestMethod.POST)
	public ModelAndView createpost(@Valid @ModelAttribute Account createAccount, BindingResult bindingResult,
			@RequestHeader(value = "User-Agent") String userAgent, HttpServletRequest request) {

		String email = request.getParameter("email");
		String display_name = request.getParameter("display_name");
		String last_name = request.getParameter("last_name");
		String first_name = request.getParameter("first_name");
		String password = request.getParameter("password");

		// String redirect = request.getParameter("redirect");

		// log.info("redirect: " + redirect);

		// ensure no errors
		if (bindingResult.hasErrors()) {
			ModelAndView mav = new ModelAndView("user/create", "createAccount", createAccount);
			// mav.addObject("redirect", redirect);
			return mav;
		}

		List<String> messages = new ArrayList<String>();

		// ensure passwords match
		if (!createAccount.getPassword().trim().equals(createAccount.getPassword_confirmation().trim())) {
			messages.add("Password and password conformation need to match.");
		}

		// ensure special char for password
		Pattern regex = Pattern.compile("[$&+,:;=?@#|]");
		Matcher matcher = regex.matcher(createAccount.getPassword().trim());
		if (!matcher.find()) {
			messages.add("Password must contain one of the following characters $&+,:;=?@#|");
		}

		// ensure email is not registered
		String id = userrepository.getIDFromEmail(createAccount.getEmail().trim());
		int idint = Integer.parseInt(id);
		if (idint >= 1) {
			messages.add("Email provided is already registered with an account. Please provide another email.");
		}

		// ensure username is not registered
		boolean userexists = userrepository.usernameExists(createAccount.getDisplay_name().trim());
		if (userexists) {
			messages.add("The username " + createAccount.getDisplay_name().trim()
					+ " is already registered with an account. Please provide another username");
		}

		// return if messages
		if (!messages.isEmpty()) {
			ModelAndView mav = new ModelAndView("user/create", "createAccount", createAccount);
			mav.addObject("messages", messages);
			// mav.addObject("redirect", redirect);
			return mav;
		}

		//create account
		
		try {
			accountService.createAccount(display_name, password, email, first_name, last_name);
		} catch (Exception e) {
			log.error(e.getMessage());
			
			ModelAndView mav = new ModelAndView("redirect:create?error");
			// mav.addObject("redirect", redirect);
			return mav;
			
		}
		
		
		
		
		String fingerprint = request.getParameter("print");
		if (fingerprint == null) {
			fingerprint = "";
		}

		// write to audit log
		RequestProcessedResults r = UserDeviceUtil.Details(request);
		userrepository.loginAuditAccount(display_name.trim(), DateUtil.DateTimeNow(), "Create Account",
				r.getiPAddress(), r.getOperatingSystem(), r.getBrowserName(), r.getuRI(), fingerprint);
		

		// send email to activate
		String url = request.getRequestURL().toString();
		String urlsub = url.substring(0, (url.length() - 6));

		String link = "";
		EncryptUtil eu;
		try {
			eu = new EncryptUtil();
			link = eu.encryptEncoded(createAccount.getEmail());
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		link = urlsub + "activate?token=" + link;
		emailservice.SendEmail(createAccount.getEmail(), appName + " - " + "Activate account",
				"To activate your account please go to  <br> " + link + "  <br> Link will be valid til "
						+ DateUtil.DateNowPlusDay(2));

		/*
		 * if (!redirect.equals("")) { if (redirect.startsWith("http://") |
		 * redirect.startsWith("https://")) { ModelAndView mav = new
		 * ModelAndView("redirect:" + redirect); return mav; } else {
		 * ModelAndView mav = new ModelAndView("redirect:http://" + redirect);
		 * return mav; }
		 * 
		 * }
		 */

		ModelAndView mav = new ModelAndView("redirect:create?sent");
		return mav;
	}

	// sends email allowing user to reset password
	@RequestMapping(value = "user/reset", method = RequestMethod.GET)
	public ModelAndView helpGet(@RequestParam(value = "redirect", required = false) String redirect) {
		ModelAndView mav = new ModelAndView("user/reset");
		mav.addObject("redirect", redirect);
		return mav;
	}

	@RequestMapping(value = "user/reset", method = RequestMethod.POST)
	public ModelAndView helpPost(@RequestHeader(value = "User-Agent") String userAgent, HttpServletRequest request) {
		log.info("Header Request Log: " + UserDeviceUtil.DetailsAsString(request));
		ModelAndView mav = null;

		String email = request.getParameter("email");
		// String redirect = request.getParameter("redirect");
		String username = userrepository.getUsernameFromEmail(email);

		if (username.equals("")) {

			mav = new ModelAndView("redirect:reset?notvalid");
			// mav.addObject("redirect", redirect);

		} else {

			String id = userrepository.getIDFromEmail(email);
			email = email.trim();
			String url = request.getRequestURL().toString();
			String urlsub = url.substring(0, (url.length() - 5));
			String link = "";
			EncryptUtil eu;
			try {
				eu = new EncryptUtil();
				link = eu.encryptEncoded(id + "---" + email + "---" + DateUtil.DateNow());
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			log.info("token: " + link);
			link = urlsub + "resetpassword?token=" + link;
			emailservice.SendEmail(email, appName + " - " + "Reset password",
					"You have request for the password to be reset for the account " + username
							+ ". <br> To reset the password for your account please go to <br> " + link
							+ " <br> This link will be valid til " + DateUtil.DateNowPlusDay(2) + ". <br> ");

			/*
			 * if (!redirect.equals("")) { if (redirect.startsWith("http://") |
			 * redirect.startsWith("https://")) { //mav = new
			 * ModelAndView("redirect:" + redirect); return mav; } else { mav =
			 * new ModelAndView("redirect:http://" + redirect); return mav; }
			 * 
			 * }
			 */

			mav = new ModelAndView("redirect:reset?sent");

		}
		return mav;
	}

	@RequestMapping("/")
	public ModelAndView home(@RequestParam(value = "client_id", required = false) String client_id) {
		/*
		 * ModelAndView mav = new ModelAndView(
		 * "redirect:/oauth/authorize?client_id=oauth-profile&redirect_uri=/uaa/user/profile&scope=trust&response_type=token"
		 * );
		 */

		// todo may want to replace with a default user profile page

		ModelAndView mav = new ModelAndView("user/noredirect");
		return mav;
	}

	@RequestMapping(value = "user/profile", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ModelAndView profile(HttpServletRequest request) {

		String url = request.getRequestURL().toString();
		String urlsub = url.substring(0, (url.length() - 13));
		urlsub = (urlsub + "/account/user");

		ModelAndView mav = new ModelAndView("user/profile");
		mav.addObject("url", urlsub);
		return mav;
	}

	@RequestMapping(value = "user/resetpassword", method = RequestMethod.GET)
	public ModelAndView resetuserpasswordget(@RequestParam(value = "token", required = false) String token) {
		ModelAndView mav = null;
		if (token == null) {
			return mav = new ModelAndView("redirect:reset");
		}
		log.info("token: " + token);

		mav = new ModelAndView("user/resetpassword");
		mav.addObject("accounttoken", token);
		return mav;
	}

	@RequestMapping(value = "user/resetpassword", method = RequestMethod.POST)
	public ModelAndView resetuserpasswordpost(HttpServletRequest request) {
		log.info("Header Request Log: " + UserDeviceUtil.DetailsAsString(request));
		ModelAndView mav = null;

		String token = request.getParameter("accounttoken");

		List<String> messages = new ArrayList<String>();

		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");

		// ensure passwords match
		if (!password1.trim().equals(password2.trim())) {
			messages.add("Password and password conformation need to match.");
		}

		// ensure special char for password
		Pattern regex = Pattern.compile("[$&+,:;=?@#|]");
		Matcher matcher = regex.matcher(password1.trim());
		if (!matcher.find()) {
			messages.add("Password must contain one of the following characters $&+,:;=?@#|");
		}

		if (password1.trim().length() < 8) {
			messages.add("Password must length must be between 8 to 80 characters");
		}

		// return if messages
		if (!messages.isEmpty()) {
			mav = new ModelAndView("user/resetpassword");
			mav.addObject("messages", messages);
			mav.addObject("accounttoken", token);
			return mav;
		}

		log.info("token: " + token);
		if (token == null) {
			return mav = new ModelAndView("redirect:reset");
		}

		String tokendecrypt = "";
		EncryptUtil eu;
		try {
			eu = new EncryptUtil();
			tokendecrypt = eu.decryptEncoded(token);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		log.info("decrypted token: " + tokendecrypt);

		String datesent = "";
		String email = "";
		String id = "";
		try {
			String[] valarr = tokendecrypt.split("---");
			id = valarr[0];
			email = valarr[1];
			datesent = valarr[2];

		} catch (Exception e) {
			log.error(e.getMessage());
			// not a valid token
			mav = new ModelAndView("redirect: reset?notvalidtoken");
			return mav;

		}

		// check if valid tokenn within timeframe
		if (datesent.equals(DateUtil.DateNow()) | datesent.equals(DateUtil.DateNowPlusOne())) {
			// valid token
			String matchid = userrepository.getIDFromEmail(email);
			if (matchid.trim().equals(id.trim())) {

				// encode password
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				password1 = passwordEncoder.encode(password1);

				// save password
				userrepository.setAccountPasswordFromEmail(email, password1);

				// get username
				String username = userrepository.getUsernameFromEmail(email);

				// get fingerprint
				String fingerprint = request.getParameter("print");
				if (fingerprint == null) {
					fingerprint = "";
				}

				// write to audit log
				RequestProcessedResults r = UserDeviceUtil.Details(request);
				userrepository.loginAuditAccount(username, DateUtil.DateTimeNow(), "Password Changed", r.getiPAddress(),
						r.getOperatingSystem(), r.getBrowserName(), r.getuRI(), fingerprint);

				// send email to notify user
				emailservice.SendEmail(email, appName + " - " + "Password has been changed",
						"Your password has been changed for your account: " + username + " <br> ");

				mav = new ModelAndView("redirect:../login?passwordreset");
				return mav;
			}
		}

		// not a valid token
		mav = new ModelAndView("redirect: reset?notvalidtoken");
		return mav;
	}

}
