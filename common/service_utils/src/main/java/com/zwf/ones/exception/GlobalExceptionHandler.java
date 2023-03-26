package com.zwf.ones.exception;


import com.zwf.ones.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

     //1、全局异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        System.out.println("全局异常.....");
        e.printStackTrace();
        return Result.fail(null).message("执行全局异常....");
    }


    //2、特定异常
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result ArithmeticExceptionerror(Exception e){
        System.out.println("特定异常.....");
        e.printStackTrace();
        return Result.fail(null).message("执行ArithmeticException全局异常....");
    }

    //3、自定义异常
    @ExceptionHandler(OnesExceptionHandler.class)
    @ResponseBody
    public Result onesExceptionHandler(OnesExceptionHandler e){
        System.out.println("自定义异常.....");
        e.printStackTrace();
        return Result.fail(null).code(e.getCode()).message(e.getMessage());
    }
}
