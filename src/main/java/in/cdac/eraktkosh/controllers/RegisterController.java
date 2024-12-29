package in.cdac.eraktkosh.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.cdac.eraktkosh.entity.RegisterEntity;
import in.cdac.eraktkosh.services.RegisterService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("eraktkosh")
public class RegisterController {

    @Autowired
    private RegisterService userService;
    
    
 // 1.. OTP generation endpoint
    @PostMapping("/generateOtp")
    public ResponseEntity<Map<String, String>> generateOtp(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();

        String mobileNumber = request.get("mobileNumber");
        if (mobileNumber == null || mobileNumber.isEmpty()) {
            response.put("message", "Mobile number is required.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            String otpResponse = userService.generateOtpWithCount(mobileNumber);

            if ("OTP limit exceeded".equals(otpResponse)) {
                response.put("message", otpResponse);
                return new ResponseEntity<>(response, HttpStatus.TOO_MANY_REQUESTS);
            }

            response.put("otp", otpResponse);
            response.put("message", "OTP generated successfully.");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Error generating OTP.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


   
    // 2.. CAPTCHA generation end point
    @GetMapping("/generateCaptchaforRegistration")
    public ResponseEntity<Map<String, String>> generateCaptcha() {
        Map<String, String> response = new HashMap<>();

        try {
            // Generate captcha text
            String captchaText = userService.generateCaptchaText();

            // Generate captcha image
            BufferedImage captchaImage = userService.generateCaptchaImage(captchaText);

            // Log captcha text to console for debugging
            System.out.println("Generated CAPTCHA Text: " + captchaText);

            // Convert CAPTCHA image to Base64
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(captchaImage, "png", baos);
            String base64Captcha = Base64.getEncoder().encodeToString(baos.toByteArray());

            // Prepare response
            response.put("captchaImage", "data:image/png;base64," + base64Captcha);
            response.put("captchaText", captchaText); 

        } catch (IOException e) {
            e.printStackTrace();
            response.put("error", "Failed to generate CAPTCHA.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }
    
    
    // 3..Registeration endpoint
    @PostMapping("/donor/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterEntity user) {
        String response = userService.registerUser(user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
