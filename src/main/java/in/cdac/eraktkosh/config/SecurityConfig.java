package in.cdac.eraktkosh.config;
//

import static org.springframework.security.config.Customizer.withDefaults;

//import in.cdac.eraktkosh.filters.JwtRequestFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    private JwtRequestFilter jwtRequestFilter;
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/validate").permitAll() 
//                .anyRequest().authenticated();
//       
//        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//    }
//}

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import in.cdac.eraktkosh.filter.JwtAuthenticationFilter;
import in.cdac.eraktkosh.filter.JwtAuthorizationFilter;
import in.cdac.eraktkosh.provider.JwtTokenProvider;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final JwtTokenProvider jwtTokenProvider;

	public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).authorizeRequests(requests -> requests
				/*
				 * .antMatchers("/eraktkosh/donor/register",
				 * "/eraktkosh/donor/registerValidate").permitAll()
				 * .antMatchers("/eraktkosh/validate", "/eraktkosh/generateOTP",
				 * "/eraktkosh/regenerateCaptcha", "/eraktkosh/generateOtpDonorRegistration",
				 * "/eraktkosh/generateCaptchaforRegistration").permitAll()
				 * .antMatchers("/auth/welcome/", "/auth/login/**").permitAll()
				 * .antMatchers("/login", "/register").permitAll()
				 */

				.antMatchers("/eraktkosh/fetchDonorDetails", "/eraktkosh/fetchCertificateDetails",
						"/eraktkosh/updateOrInsertDonorDetails", "/auth/welcome")
				.authenticated().antMatchers("/**", "/auth/login").permitAll().anyRequest().authenticated())
				.formLogin(login -> login.disable()) // Disable Spring Security default form login
				.httpBasic(basic -> basic.disable()) // Disable HTTP Basic Authentication
				.cors(withDefaults());

		// Add custom JWT filters
		http.addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtTokenProvider)).addFilterBefore(
				new JwtAuthorizationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Configure your authentication manager here, e.g., using a custom user details
		// service
		auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
