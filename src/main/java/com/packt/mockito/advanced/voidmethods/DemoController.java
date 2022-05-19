package com.packt.mockito.advanced.voidmethods;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DemoController")
public class DemoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final LoginController loginController;
	private final ErrorHandler errorHandler;
	private final MessageRepository messageRepository;

	public DemoController(LoginController loginController, ErrorHandler errorHandler,
			MessageRepository messageRepository) {
		this.loginController = loginController;
		this.errorHandler = errorHandler;
		this.messageRepository = messageRepository;
	}

	public DemoController() {
		loginController = new LoginController(new LDAPManagerImpl());
		errorHandler = new ErrorHandlerImpl();
		messageRepository = new Repository();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			String urlContext = req.getServletPath();
			if (urlContext.equals("/")) {
				req.getRequestDispatcher("login.jsp").forward(req, res);
			} else if (urlContext.equals("/logon.do")) {
				loginController.process(req, res);
			} else {
				req.setAttribute("error", "Invalid request path '" + urlContext + "'");
				req.getRequestDispatcher("error.jsp").forward(req, res);
			}
		} catch (Exception ex) {

			String errorMsg = ex.getMessage();
			Error errorDto = new Error();
			errorDto.setTrace(ex.getStackTrace());
			errorHandler.mapTo(errorDto);

			if (errorDto.getErrorCode() != null) {
				errorMsg = messageRepository.lookUp(errorDto.getErrorCode());
			}
			req.setAttribute("error", errorMsg);
			req.getRequestDispatcher("error.jsp").forward(req, res);
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
