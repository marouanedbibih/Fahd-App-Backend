package org.fahdpln.backend.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyErrorResponse {
    
    private String message;
    private List<MyError> errors;

}
