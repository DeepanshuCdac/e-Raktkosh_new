package in.cdac.eraktkosh.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.cdac.eraktkosh.dto.LoginRegister;
import in.cdac.eraktkosh.dto.TokenResponse;
import in.cdac.eraktkosh.provider.JwtTokenProvider;
import in.cdac.eraktkosh.services.PortalLoginService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class UserController {

	/*
	 * @Autowired private UserInfoDetails userinfoDetails;
	 * 
	 * @Autowired private JwtService jwtService;
	 * 
	 * @Autowired private AuthenticationManager authManager;
	 */

	@Autowired
	PortalLoginService portalLoginService;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome this endpoint is not secure";
	}

	@PostMapping("/adduser")
	public String addUser() {
		return "you hit addUser";
	}

//	 @GetMapping("/test")
//	 public List<PortalLoginEntity> Try(){
//		 return portalLoginService.Test();
//	 }

	@PostMapping("/login1")
	public TokenResponse login(@RequestParam String username) {
		if (!username.isEmpty()) { // "admin".equals(username) && "password".equals(password)
			// Generate JWT token
			String token = jwtTokenProvider.generateToken(username);

			return new TokenResponse(token);
		}
		throw new RuntimeException("Invalid credentials");
	}

	@PostMapping("/login")
	public TokenResponse bhavya(@RequestBody LoginRegister lg) {
		String client_id = lg.getClient_id();
		String client_secret = lg.getClient_secret();
		if (!client_id.isEmpty()) { // "admin".equals(username) && "password".equals(password)
			// Generate JWT token
			String token = jwtTokenProvider.generateToken(client_id);

			return new TokenResponse(token);
		}
		throw new RuntimeException("Invalid credentials");
	}

}
