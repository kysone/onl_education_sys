package com.zwf.ones.exception;


import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnesExceptionHandler extends RuntimeException{

    private Integer code;
    private String message;







}
