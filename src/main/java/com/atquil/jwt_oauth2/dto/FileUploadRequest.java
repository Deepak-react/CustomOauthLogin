package com.atquil.jwt_oauth2.dto;

import org.springframework.web.multipart.*;

public record FileUploadRequest(MultipartFile multipartFile, String name) {
}
