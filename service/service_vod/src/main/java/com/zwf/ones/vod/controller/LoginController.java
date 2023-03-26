package com.zwf.ones.vod.controller;

import com.zwf.ones.result.Result;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("admin/vod/user")
//@CrossOrigin
public class LoginController {

    //Login接口
    //code: 20000, data: {token: "admin-token"}}
    @PostMapping("login")
    public Result LoginController(){
        Map<String,Object> map=new HashMap<>();
        map.put("token","admin-token");
        return Result.ok(map);
    }


    //info接口
    //{code: 20000, data: {roles: ["admin"], introduction: "I am a super administrator",…}}
    @GetMapping("info")
    public Result infoController(){
        Map<String,Object> map=new HashMap<>();
        map.put("roles","admin");
        map.put("introduction","I am a super administrator");
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        map.put("name","Super Admin");
        return Result.ok(map);

    }
}
