package in.cdac.eraktkosh.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.cdac.eraktkosh.dto.ErrorResponse;
import in.cdac.eraktkosh.provider.JwtTokenProvider;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;

	public JwtAuthorizationFilter(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = getJwtFromRequest(request);

		try {
			// Validate the token
			if (token != null && jwtTokenProvider.validateToken(token)) {
				// Extract the username from the token
				String username = jwtTokenProvider.getUsernameFromToken(token);

				// If the token is valid, set authentication in SecurityContext
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,
						null, null);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (JwtAuthenticationException ex) {
			// If token is invalid, the exception is thrown and handled by global exception
			// handler
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			// Write the error message in JSON format using the ErrorResponse object
			ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
			return;
		}

		// Continue the request filter chain
		filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

}
