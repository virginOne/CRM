package pri.crm.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ErrController implements ErrorController{
	
	String BasicErrorPath="/crm/view/error/";
	
	@RequestMapping("/error")
	public ModelAndView  handleError() {
		return new ModelAndView("/error/"+String.valueOf(getErrorCode()));
	}
	
	@Autowired
	HttpServletRequest request;
	
	public int getErrorCode( ) {
		return (int) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	}
	
	
	@Override
	public String getErrorPath() {
		System.out.println(getErrorCode());
		return "/error";
	}
}
