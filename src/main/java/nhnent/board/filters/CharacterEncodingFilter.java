package nhnent.board.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class CharacterEncodingFilter implements Filter {

	private String characterSet;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		this.characterSet = filterConfig.getInitParameter("characterSet");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub

		if (!isAjax(request) && request.getCharacterEncoding() == null) {
			request.setCharacterEncoding(characterSet);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	private boolean isAjax(ServletRequest request) {
		// Return if current request header is equals to "XMLHttpRequest"
		return "XMLHttpRequest".equals(((HttpServletRequest) request)
				.getHeader("X-Requested-With"));
	}
}
