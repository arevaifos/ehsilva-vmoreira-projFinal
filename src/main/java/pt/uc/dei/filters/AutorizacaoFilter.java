package pt.uc.dei.filters;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.uc.dei.implement.UserImpl;

/**
 * Servlet Filter implementation class AutorizacaoFilter
 */
@WebFilter("*.xhtml")
public class AutorizacaoFilter implements Filter {
	
	@Inject
	private UserImpl loggedUser;



	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;
		if (!loggedUser.isLogged() && !request.getRequestURI().contains("/index.xhtml") && !request.getRequestURI().contains("/newPsw.xhtml")  &&  !request.getRequestURI().contains("/newUser.xhtml") 
						&& !request.getRequestURI().contains("/recuperaPws.xhtml")  && !request.getRequestURI().contains("/javax.faces.resource/")) {
			response.sendRedirect(request.getContextPath()
					+ "/index.xhtml");
		} else {
			chain.doFilter(req, res);
		}
	}


	@Override
	public void init(FilterConfig config) throws ServletException {
	}
	@Override
	public void destroy() {
	}

}
