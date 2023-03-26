package com.zwf.ones.result;


import lombok.Data;

@Data
public class Result <T>{


    private Integer code;

    private String message;

    private T data;

    public Result(){};

    public static<T> Result<T> ok(T data){
        Result<T> result=new Result<>();

        if (data!=null){
            result.setData(data);
        }
        result.setCode(20000);
        result.setMessage("成功");
        return result;
    }


    public static<T> Result<T> fail(T data){
        Result<T> result=new Result<>();
        if(data!=null){
            result.setData(data);
        }
        result.setCode(20001);
        result.setMessage("失败");
        return result;
    }

    public Result<T> message(String msg){
        this.setMessage(msg);
        return this;
    }

    public Result<T> code(Integer code){
        this.setCode(code);
        return this;
    }



}
