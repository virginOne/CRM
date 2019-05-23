package pri.crm.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import pri.crm.vo.UserVo;



@Configuration
public class WebSecurityConfig implements WebMvcConfigurer{
	
	@Bean
	public HandlerInterceptorAdapter getSecurityInterceptor()
	{
		return new SecurityInterceptor();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry)
	{
		InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());
		addInterceptor.excludePathPatterns("/**/login.html");
		addInterceptor.excludePathPatterns("/**/user/login");
		addInterceptor.excludePathPatterns("/**/user/checkCode");
		addInterceptor.excludePathPatterns("/**/css/**");
		addInterceptor.excludePathPatterns("/**/js/**");
		addInterceptor.excludePathPatterns("/**/images/**");
		addInterceptor.excludePathPatterns("/**/plugins/**");
		addInterceptor.addPathPatterns("/**/**");
		
		
		InterceptorRegistration sysInterceptor=registry.addInterceptor(new SysSecurityInterceptor());
		sysInterceptor.addPathPatterns("/**/sys/**");
	}
	
	
	private class SecurityInterceptor extends HandlerInterceptorAdapter
	{
		@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception
		{
			if (null  == request.getSession().getAttribute("user"))
			{
				System.err.println(request.getServletPath());
				response.sendRedirect("/crm/login.html");
				return false;
			}
			return true;
		}
	}
	
	private class SysSecurityInterceptor extends HandlerInterceptorAdapter
	{
		@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception
		{	
			UserVo user=(UserVo) request.getSession().getAttribute("user");
			if (user.getPower().intValue()!=1)
			{
				System.err.println(request.getServletPath());
				response.sendRedirect("/crm/error");
				return false;
			}
			return true;
		}
	}
	
	//CORS跨域设置
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
		.allowCredentials(true)
		.allowedOrigins("*")
		.allowedMethods("*")
		.allowedHeaders("*");
	}
}
