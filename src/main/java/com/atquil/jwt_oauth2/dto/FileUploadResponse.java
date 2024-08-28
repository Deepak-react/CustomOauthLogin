package com.atquil.jwt_oauth2.dto;

public record FileUploadResponse(
        Long id,
        String name,
        String url,
        Long size
) {
}
