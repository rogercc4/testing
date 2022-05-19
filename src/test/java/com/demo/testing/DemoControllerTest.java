package com.demo.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.packt.mockito.advanced.voidmethods.DemoController;
import com.packt.mockito.advanced.voidmethods.Error;
import com.packt.mockito.advanced.voidmethods.ErrorHandler;
import com.packt.mockito.advanced.voidmethods.LoginController;
import com.packt.mockito.advanced.voidmethods.MessageRepository;

@SpringBootTest
public class DemoControllerTest {

	@Mock HttpServletRequest request;
	@Mock HttpServletResponse response;
	@Mock RequestDispatcher dispatcher;
	@Mock LoginController loginController;
	@Mock MessageRepository repository;
	@Mock ErrorHandler errorHandler;
	
	@InjectMocks DemoController controller;
	
	@Test
	@DisplayName("When subsystem throws any exception then routes to error page")
	public void whenSubsystemThrowsAnyExceptionThenRoutesToErrorPage() throws Exception {

		doThrow(new IllegalStateException("LDAP error")).when(loginController).process(request, response);

		when(request.getServletPath()).thenReturn("/logon.do");
		when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

		controller.doGet(request, response);

		verify(request).getRequestDispatcher(eq("error.jsp"));
		verify(dispatcher).forward(request, response);
	}

	@Test
	@DisplayName("When subsystem throws any exception then finds error message and routes to error page")
	public void whenSubsystemThrowsAnyExceptionThenFindsErrorMessageAndRoutesToErrorPage() throws Exception {

		doThrow(new IllegalStateException("LDAP error")).when(loginController).process(request, response);

		doAnswer(invocation -> {
			Error err = (Error) invocation.getArguments()[0];
			err.setErrorCode("123");
			return err;
		}).when(errorHandler).mapTo(isA(Error.class));

		when(request.getServletPath()).thenReturn("/logon.do");
		when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

		controller.doGet(request, response);
		ArgumentCaptor<String[]> captor = ArgumentCaptor.forClass(String[].class);
		verify(this.repository).lookUp(captor.capture());
		assertEquals("123", captor.getValue());

		verify(request).getRequestDispatcher(eq("error.jsp"));
		verify(dispatcher).forward(request, response);
	}	
}