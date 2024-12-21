package com.HaiDang.service;

import com.HaiDang.repository.UserRepository;
import com.HaiDang.request.EmailRequest;
import com.HaiDang.response.EmailResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Random;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    private final EmailValidator emailValidator= EmailValidator.getInstance();
    private final String apiKeyZeroBounce = "d8fa44d0a8ed46d8aff622b8dd7a2aca";

    private final String sender = "dangthanhhai22222@gmail.com";
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<EmailResponse> sendEmail(EmailRequest request){
        if(!isValidEmail(request.getEmail())){
            return new ResponseEntity<>(new EmailResponse(false, "Email khong hop le", 0), HttpStatus.BAD_REQUEST);
        }
//        if(!isExistEmail(request.getEmail())){
//            return new ResponseEntity<>(new EmailResponse(false, "Email khong ton tai", 0), HttpStatus.BAD_REQUEST);
//
//        }
        if(userRepository.findByEmail(request.getEmail())!= null)
        {
            return new ResponseEntity<>(new EmailResponse(false, "Email da ton tai", 0), HttpStatus.BAD_REQUEST);

        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("dangthanhhai22222@gmail.com");
        message.setTo(request.getEmail());
        message.setSubject("Xác minh tai khoản");
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        message.setText("Mã của bạn là: "+otp);
        javaMailSender.send(message);
        return new ResponseEntity<>(new EmailResponse(true, "Email hợp lệ", otp), HttpStatus.OK);
    }
    private boolean isValidEmail(String email){
        return emailValidator.isValid(email);
    }
    private boolean isExistEmail(String email) {
        String url = UriComponentsBuilder.fromHttpUrl("https://api.zerobounce.net/v2/validate")
                .queryParam("api_key", apiKeyZeroBounce)
                .queryParam("email", email)
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        String response = "";
        try {
            response = restTemplate.getForObject(url, String.class);
            System.out.println(response);
            if (response == null || response.isEmpty()) {
                return false;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(response);
            String status = responseJson.get("status").asText();
            if ("valid".equals(status)) {
                return true;  // Email hợp lệ
            } else {
                return false;  // Email không hợp lệ
            }
        } catch (Exception e) {
            // Xử lý ngoại lệ nếu gọi API thất bại
            System.out.println("Lỗi khi gọi API ZeroBounce: " + e.getMessage());
            return false;
        }

    }

}
