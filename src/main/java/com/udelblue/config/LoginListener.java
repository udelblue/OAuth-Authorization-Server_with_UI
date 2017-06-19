package com.udelblue.config;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.udelblue.domain.RequestProcessedResults;
import com.udelblue.repositories.UserRepository;
import com.udelblue.util.DateUtil;
import com.udelblue.util.UserDeviceUtil;

public class LoginListener {

	@SuppressWarnings("rawtypes")
	@Component
	static class SuccessfulLoginListener implements ApplicationListener {

		private final Logger log = LoggerFactory.getLogger(SuccessfulLoginListener.class);

		private @Autowired HttpServletRequest request;

		@Autowired
		UserRepository userrepository;

		public SuccessfulLoginListener() {
			log.info("SuccessfulLoginListener init");
		}

		@Override
		public void onApplicationEvent(ApplicationEvent appEvent) {

			// Successful login
			if (appEvent instanceof AuthenticationSuccessEvent) {

				RequestProcessedResults r = UserDeviceUtil.Details(request);

				if (!r.getuRI().contains("/account/user")) {
					AuthenticationSuccessEvent authenticationSuccessEvent = (AuthenticationSuccessEvent) appEvent;
					/*
					 * Authentication authentication =
					 * authenticationSuccessEvent.getAuthentication();
					 */
					/*
					 * WebAuthenticationDetails details =
					 * (WebAuthenticationDetails) authentication.getDetails();
					 */
					/* String remoteAddress = details.getRemoteAddress(); */

					String fingerprint = request.getParameter("print");
					if (fingerprint == null) {
						fingerprint = "";
					}

					UserDetails userDetails = (UserDetails) authenticationSuccessEvent.getAuthentication()
							.getPrincipal();
					userrepository.setLastLoginFromUsername(userDetails.getUsername());
					String username = userDetails.getUsername();
					String userloginTime = DateUtil.DateTimeNow();

					String eventtype = "";
					if (r.getuRI().contains("/account/logout")) {
						eventtype = "logout";
					}
					if (r.getuRI().contains("/login")) {
						eventtype = "login";
					}
					userrepository.loginAuditAccount(username, userloginTime, eventtype, r.getiPAddress(),
							r.getOperatingSystem(), r.getBrowserName(), r.getuRI(), fingerprint);

				}
			}

			// Unsuccessful login
			if (appEvent instanceof AuthenticationFailureBadCredentialsEvent) {

			}

		}

	}

}
