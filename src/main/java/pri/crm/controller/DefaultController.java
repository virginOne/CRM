package pri.crm.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class DefaultController {
	@RequestMapping("/")
	public void deafault(HttpServletResponse response) throws IOException{
		response.sendRedirect("login.html");
	}
}
