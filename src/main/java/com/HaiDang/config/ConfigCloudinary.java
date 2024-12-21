package com.HaiDang.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConfigCloudinary {
    @Bean
    public Cloudinary configKey(){
        Map config = new HashMap();
        config.put("cloud_name", "dfjufwrqu");
        config.put("api_key", "965673322382438");
        config.put("api_secret", "L3v0sMuQON0wUz1IegYAr0Bzav0");
        Cloudinary cloudinary = new Cloudinary(config);
        return cloudinary;
    }
}
