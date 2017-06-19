package com.udelblue.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.udelblue.domain.Account;
import com.udelblue.repositories.UserRepository;
import com.udelblue.services.AccountService;

@Controller
public class AccountController {

	UserRepository userrepository;
	AccountService accountService;

	@Autowired
	public AccountController(UserRepository userrepository, AccountService accountService) {
		super();
		this.userrepository = userrepository;
		this.accountService = accountService;
	}

	@SuppressWarnings("null")
	@CrossOrigin(origins = "*")
	@RequestMapping("/account/logout/{client}")
	@ResponseBody
	public Principal logout(Principal user, @PathVariable(value = "client") String clientid) {
		if (clientid == null && clientid.equals("")) {
			userrepository.deleteAllAccessTokens(user.getName());
		} else {
			userrepository.deleteAccessToken(user.getName(), clientid);
		}
		return user;
	}

	@CrossOrigin(origins = "*")
	@RequestMapping("/account/logout")
	@ResponseBody
	public Principal logoutall(Principal user) {
		userrepository.deleteAllAccessTokens(user.getName());
		return user;
	}

	@CrossOrigin(origins = "*")
	@RequestMapping("/account/user")
	@ResponseBody
	public Principal user(Principal user) {
		return user;
	}

	// create account
	@CrossOrigin(origins = "*")
	@RequestMapping(value ="/account/create", method = RequestMethod.POST)
	@ResponseBody
	//@PreAuthorize("hasRole('OAuthAdmin')")
	public  ResponseEntity<String> createUser(HttpServletRequest request)  { 
		
		String email = request.getParameter("email");
		String display_name = request.getParameter("display_name");
		String last_name = request.getParameter("last_name");
		String first_name = request.getParameter("first_name");
		String password = request.getParameter("password");
		String password_confirm = request.getParameter("password_confirm");

		MultiValueMap<String, String>   message = new LinkedMultiValueMap<String, String>(); 
		List<String> messages = new ArrayList<String>();
		
		
		// ensure passwords match
		if (!password.trim().equals(password_confirm.trim())) {
			messages.add("Password and password conformation need to match.");
		}

		// ensure special char for password
		Pattern regex = Pattern.compile("[$&+,:;=?@#|]");
		Matcher matcher = regex.matcher(password.trim());
		if (!matcher.find()) {
			messages.add("Password must contain one of the following characters $&+,:;=?@#|");
		}

		// ensure email is not registered
		String id = userrepository.getIDFromEmail(email.trim());
		int idint = Integer.parseInt(id);
		if (idint >= 1) {
			messages.add("Email provided is already registered with an account. Please provide another email.");
		}

		// ensure username is not registered
		boolean userexists = userrepository.usernameExists(display_name.trim());
		if (userexists) {
			messages.add("The username " + display_name.trim()
					+ " is already registered with an account. Please provide another username");
		}

		// return if messages
		if (!messages.isEmpty()) {
				StringBuilder sb = new StringBuilder();
				for(String s : messages){
					sb.append(s);
					sb.append("\n");
				}
				return new ResponseEntity<String>(sb.toString(), message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		//create user account
		try {
			accountService.createAccount(display_name, password_confirm, email, first_name, last_name);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error Creating Account" , message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		//return response after account created
		return new ResponseEntity<String>(display_name, message, HttpStatus.OK);
	}

}
