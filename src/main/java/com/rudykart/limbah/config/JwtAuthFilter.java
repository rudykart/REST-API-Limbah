package com.rudykart.limbah.config;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.rudykart.limbah.dto.ErrorResponse;
import com.rudykart.limbah.services.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
// import java.util.Arrays;
// import java.util.List;
// import java.util.logging.Level;
// import org.springframework.http.HttpStatus;
// import java.util.logging.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	// private static final Logger LOGGER = Logger.getLogger(
	// JwtAuthFilter.class.getName());

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	// private final ObjectMapper objectMapper;

	public JwtAuthFilter(
			JwtService jwtService,
			UserDetailsService userDetailsService) {
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String username = null;
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			username = jwtService.extractUsername(token);
		}

		System.out.println("Auth Filter = Mulai ");

		// if (username == null && !isPublicEndpoint(request)) {
		// // Pengguna tidak terotentikasi dan endpoint tidak publik, kirim respons 401
		// response.setContentType("application/json");
		// response.setStatus(HttpStatus.UNAUTHORIZED.value());
		// ErrorResponse errorResponse = new
		// ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
		// String jsonErrorResponse = objectMapper.writeValueAsString(errorResponse);
		// response.getWriter().write(jsonErrorResponse);
		// response.getWriter().flush();
		// }else
		if (username != null &&
				SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			if (jwtService.validateToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userDetails,
						null,
						userDetails.getAuthorities());
				authToken.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}

		System.out.println("Auth Filter = Selesai ");

		filterChain.doFilter(request, response);
	}

	// private boolean isPublicEndpoint(HttpServletRequest request) {
	// String path = request.getRequestURI();
	// LOGGER.log(Level.INFO, "Path: {0}", new Object[] { path });

	// // Daftar awalan URL yang dianggap sebagai endpoint publik
	// List<String> publicEndpointPrefixes = Arrays.asList(
	// "/api/auth/login",
	// "/api/auth/register",
	// "/api/auth/tes"
	// );

	// for (String prefix : publicEndpointPrefixes) {
	// if (path.startsWith(prefix)) {
	// return true; // Endpoint publik
	// }
	// }
	// return false; // Bukan endpoint publik
	// }
}
