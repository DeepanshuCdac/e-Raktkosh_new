
package in.cdac.eraktkosh.provider;

import java.util.Date;

import org.springframework.stereotype.Component;

import in.cdac.eraktkosh.filter.JwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {

	private final String secretKey = "test1234";

	private final int Ttime = 20; // 2 min

	// Generate JWT token
	public String generateToken(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * Ttime))
				.signWith(SignatureAlgorithm.HS512, secretKey).compact();
	}

	/*
	 * public boolean validateToken(String token) { try {
	 * Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token); // Parsing the
	 * token return true; // Token is valid } catch (ExpiredJwtException e) {
	 * System.out.println("JWT token has expired"); } catch (UnsupportedJwtException
	 * e) { System.out.println("Unsupported JWT token"); } catch
	 * (MalformedJwtException e) { System.out.println("Invalid JWT token"); } catch
	 * (SignatureException e) { System.out.println("Invalid JWT signature"); } catch
	 * (Exception e) { System.out.println("JWT token validation error: " +
	 * e.getMessage()); } return false; // If any error occurs during validation,
	 * return false }
	 */

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException e) {
			// Token has expired
			System.out.println("JWT token has expired");
			throw new JwtAuthenticationException("JWT token has expired");
		} catch (UnsupportedJwtException e) {
			// Token is unsupported
			System.out.println("Unsupported JWT token");
			throw new JwtAuthenticationException("Unsupported JWT token");
		} catch (MalformedJwtException e) {
			// Token is malformed
			System.out.println("Invalid JWT token");
			throw new JwtAuthenticationException("Invalid JWT token");
		} catch (SignatureException e) {
			// Token signature is invalid
			System.out.println("Invalid JWT signature");
			throw new JwtAuthenticationException("Invalid JWT signature");
		} catch (Exception e) {
			// Generic error (any other error)
			System.out.println("JWT token validation error: " + e.getMessage());
			throw new JwtAuthenticationException("JWT token validation error: " + e.getMessage());
		}
	}

	public String getUsernameFromToken(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean isTokenExpired(String token) {
		Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
		Date expirationDate = claims.getExpiration();
		return expirationDate.before(new Date());
	}
}
