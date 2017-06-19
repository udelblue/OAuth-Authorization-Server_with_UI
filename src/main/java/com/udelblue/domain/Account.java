package com.udelblue.domain;

import java.io.Serializable;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class Account implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Please provide a username.")
	@Size(min = 3, max = 80, message = "Username size must be between 3 to 80 characters")
	String display_name;
	@NotBlank(message = "Please provide an email addresss.")
	@Email
	String email;
	@NotBlank
	@Size(min = 3, max = 80, message = "First name size must be between 3 to 80 characters")
	String first_name;
	@NotBlank
	@Size(min = 3, max = 80, message = "Last name size must be between 3 to 80 characters")
	String last_name;
	@NotBlank
	@Size(min = 8, max = 80, message = "Password size must be between 8 to 80 characters")
	String password;
	@NotBlank
	@Size(min = 8, max = 80, message = "Password confirmation size must be between 8 to 80 characters")
	String password_confirmation;

	public Account() {
	}

	public String getDisplay_name() {
		return display_name;
	}

	public String getEmail() {
		return email;
	}

	public String getFirst_name() {
		return first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public String getPassword() {
		return password;
	}

	public String getPassword_confirmation() {
		return password_confirmation;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPassword_confirmation(String password_confirmation) {
		this.password_confirmation = password_confirmation;
	}

}
