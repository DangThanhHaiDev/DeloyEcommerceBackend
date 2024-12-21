package com.HaiDang.controller;

import com.HaiDang.request.EmailRequest;
import com.HaiDang.response.EmailResponse;
import com.HaiDang.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
    @Autowired
    EmailService emailService;

    @PostMapping ("/email/send")
    public ResponseEntity<EmailResponse> sendEmail(@RequestBody EmailRequest request){
        return  emailService.sendEmail(request);
    }
}
