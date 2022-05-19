package com.demo.testing;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.packt.mockito.advanced.voidmethods.LDAPManager;
import com.packt.mockito.advanced.voidmethods.LoginController;

@SpringBootTest
public class LoginControllerTest {
	@Mock
	HttpServletRequest request;
	@Mock
	HttpServletResponse response;
	@Mock
	LDAPManager ldapManager;
	@Mock
	HttpSession session;
	@Mock
	RequestDispatcher dispatcher;

	@InjectMocks
	LoginController controller;

	@Test
	@DisplayName("when valid user credentials for login then routes to home page")
	void whenValidUserCredentialsForLoginThenRoutesToHomePage() throws Exception {
		when(ldapManager.isValidUser(anyString(), anyString())).thenReturn(true);
		when(request.getSession(true)).thenReturn(session);
		when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
		when(request.getParameter(anyString())).thenReturn("user", "pwd");

		this.controller.process(request, response);

		verify(ldapManager).isValidUser(anyString(), anyString());
		verify(request).getSession(true);
		verify(session).setAttribute(anyString(), anyString());
		verify(request).getRequestDispatcher(eq("home.jsp"));
		verify(dispatcher).forward(request, response);
	}

	@Test
	@DisplayName("when invalid user credentials then route to login page")
	public void whenInvalidUserCredentialsThenRoutesToLoginPage() throws Exception {
		when(ldapManager.isValidUser(anyString(), anyString())).thenReturn(false);
		when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
		when(request.getParameter(anyString())).thenReturn("user", "pwd");

		controller.process(request, response);

		verify(request).getRequestDispatcher(eq("login.jsp"));
		verify(dispatcher).forward(request, response);
	}
}