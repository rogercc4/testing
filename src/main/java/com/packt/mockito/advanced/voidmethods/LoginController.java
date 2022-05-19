package com.packt.mockito.advanced.voidmethods;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginController {

	private final LDAPManager ldapManager;

	public LoginController(LDAPManager ldapMngr) {
		this.ldapManager = ldapMngr;
	}

	public void process(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		String userName = req.getParameter("userName");
		String encrypterPassword = req.getParameter("encrypterPassword");
		if (this.ldapManager.isValidUser(userName, encrypterPassword)) {
			req.getSession(true).setAttribute("user", userName);
			req.getRequestDispatcher("home.jsp").forward(req, res);
		} else {
			req.setAttribute("error", "Invalid user name or password");
			req.getRequestDispatcher("login.jsp").forward(req, res);
		}
	}

}
