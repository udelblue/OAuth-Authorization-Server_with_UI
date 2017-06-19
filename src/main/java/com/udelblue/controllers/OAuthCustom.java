package com.udelblue.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.udelblue.util.SpelView;

@Controller
@SessionAttributes("authorizationRequest")
public class OAuthCustom {

	private static String CSRF = "<input type='hidden' name='${_csrf.parameterName}' value='${_csrf.token}' />";

	private static String DENIAL = "<form id='denialForm' name='denialForm' action='${path}/oauth/authorize' method='post'><input name='user_oauth_approval' value='false' type='hidden'/>%csrf%<label><input class='btn btn-warning' name='deny' value='Deny' type='submit'/></label></form>";

	private static String SCOPE = "<li><div class='form-group'>%scope%: <input type='radio' name='%key%'"
			+ " value='true'%approved%>Approve</input> <input type='radio' name='%key%' value='false'%denied%>Deny</input></div></li>";

	private static String TEMPLATE = "<html><head>" + "<meta charset='UTF-8'/> " + "<title>Authorize Approval</title> "
			+ "<link rel='stylesheet' href='/uaa/scripts/css/bootstrap.min.css' /> "
			+ "<link rel='stylesheet' href='/uaa/scripts/css/font-awesome.min.css'/> "
			+ "<link rel='stylesheet' href='/uaa/scripts/css/app/oauthcustom.css'/> "
			+ "<link rel='stylesheet' href='/uaa/scripts/css/globel.css'/> "
			+ "<script src='/uaa/scripts/js/jquery-3.2.0.min.js'></script> "
			+ "<script src='/uaa/scripts/js/bootstrap.min.js'></script>    " + "</head>" + "<body>"
			+ "<div class='container'>" + "<div class='row'>" + " <div class='col-sm-6 col-md-4 col-md-offset-4'>"
			+ " <h1 class='text-center login-title'>Authorize approval to continue</h1>" + " <div class='account-wall'>"
			+ "<p>Do you authorize '${authorizationRequest.clientId}' to access your protected resources?</p>"
			+ "<form id='confirmationForm' name='confirmationForm' action='${path}/oauth/authorize' method='post'><input name='user_oauth_approval' value='true' type='hidden'/>%csrf%%scopes%<label><input  class='btn btn-success' name='authorize' value='Authorize' type='submit'/></label></form>%denial%</div></div></div></div>"
			+ "</body></html>";

	private CharSequence createScopes(Map<String, Object> model, HttpServletRequest request) {
		StringBuilder builder = new StringBuilder("<ul>");
		@SuppressWarnings("unchecked")
		Map<String, String> scopes = (Map<String, String>) (model.containsKey("scopes") ? model.get("scopes")
				: request.getAttribute("scopes"));
		for (String scope : scopes.keySet()) {
			String approved = "true".equals(scopes.get(scope)) ? " checked" : "";
			String denied = !"true".equals(scopes.get(scope)) ? " checked" : "";
			String value = SCOPE.replace("%scope%", scope).replace("%key%", scope).replace("%approved%", approved)
					.replace("%denied%", denied);
			builder.append(value);
		}
		builder.append("</ul>");
		return builder.toString();
	}

	protected String createTemplate(Map<String, Object> model, HttpServletRequest request) {
		String template = TEMPLATE;
		if (model.containsKey("scopes") || request.getAttribute("scopes") != null) {
			template = template.replace("%scopes%", createScopes(model, request)).replace("%denial%", "");
		} else {
			template = template.replace("%scopes%", "").replace("%denial%", DENIAL);
		}
		if (model.containsKey("_csrf") || request.getAttribute("_csrf") != null) {
			template = template.replace("%csrf%", CSRF);
		} else {
			template = template.replace("%csrf%", "");
		}
		return template;
	}

	// custom approval page
	@RequestMapping("/oauth/confirm_access")
	public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
		String template = createTemplate(model, request);
		if (request.getAttribute("_csrf") != null) {
			model.put("_csrf", request.getAttribute("_csrf"));
		}
		return new ModelAndView(new SpelView(template), model);
	}

}