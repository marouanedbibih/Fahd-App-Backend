package org.fahdpln.backend.utils;

import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyResponse {

    private Object data;
    private String message;
    private Map<String,Object> meta;
    private HttpStatus status;
}
