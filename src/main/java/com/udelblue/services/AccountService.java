package com.udelblue.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.udelblue.domain.RequestProcessedResults;
import com.udelblue.repositories.UserRepository;
import com.udelblue.util.DateUtil;
import com.udelblue.util.UserDeviceUtil;

@Service
public class AccountService {

	@Autowired
	UserRepository userrepository;

	public boolean createAccount(String display_name, String password, String email, String first_name,
			String last_name) throws Exception {

		// encode password
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		password = passwordEncoder.encode(password);

		// create account
		String c = userrepository.createAccount(email.trim(), display_name.trim(), password.trim(), first_name.trim(),
				last_name.trim());
		if (c.equals("0")) {
			throw new Exception("Creating account error");
		}

		String a = userrepository.createAuthority(display_name.trim(), "ROLE_USER");
		if (a.equals("0")) {
			throw new Exception("Creating authority error");
		}

		return true;

	}

}
