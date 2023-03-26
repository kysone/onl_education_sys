package com.zwf.ones.wechat.controller;


import com.alibaba.fastjson.JSONObject;
import com.zwf.ones.exception.OnesExceptionHandler;
import com.zwf.ones.model.wechat.Menu;
import com.zwf.ones.result.Result;
import com.zwf.ones.vo.wechat.MenuVo;
import com.zwf.ones.wechat.utils.ConstantPropertiesUtil;
import com.zwf.ones.wechat.service.MenuService;
import com.zwf.ones.wechat.utils.HttpClientUtils;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 订单明细 订单明细 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2023-03-24
 */
@RestController
@RequestMapping("/admin/wechat/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;



    @ApiOperation(value = "删除菜单")
    @DeleteMapping("removeMenu")
    public Result removeMenu() {
        menuService.removeMenu();
        return Result.ok(null);
    }

    @ApiOperation(value = "同步菜单")
    @GetMapping("syncMenu")
    public Result createMenu() throws WxErrorException {
        menuService.syncMenu();
        return Result.ok(null);
    }



    @GetMapping("getAccessToken")
    public Result getAccessToken() {
        //拼接请求地址
        StringBuffer buffer = new StringBuffer();
        buffer.append("https://api.weixin.qq.com/cgi-bin/token");
        buffer.append("?grant_type=client_credential");
        buffer.append("&appid=%s");
        buffer.append("&secret=%s");

        //设置路径中的参数
        String url = String.format(buffer.toString(),
                ConstantPropertiesUtil.ACCESS_KEY_ID,
                ConstantPropertiesUtil.ACCESS_KEY_SECRET);
        try {
            String tokenString = HttpClientUtils.get(url);
            //获取access_token
            JSONObject jsonObject = JSONObject.parseObject(tokenString);
            String access_token = jsonObject.getString("access_token");
            return Result.ok(access_token);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OnesExceptionHandler(20001, "获取access_token失败");
        }
    }


    //获取二级菜单
    @GetMapping("findMenuInfo")
    public Result findMenuInfo() {
        List<MenuVo> list = menuService.findMenuInfo();
        return Result.ok(list);
    }



    //获取一级菜单
    @GetMapping("findOneMenuInfo")
    public Result findOneMenuInfo(){

        List<Menu> list=menuService.findMenuOneInfo();
        return Result.ok(list);
    }



    @ApiOperation("获取")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id){
        Menu byId = menuService.getById(id);
        return Result.ok(byId);
    }


    @ApiOperation("新增")
    @PostMapping("save")
    public Result save(@RequestBody Menu menu){
        menuService.save(menu);
        return Result.ok(null);
    }

    @ApiOperation("修改")
    @PutMapping("update")
    public Result updateById(@RequestBody Menu menu){
        menuService.updateById(menu);
        return Result.ok(null);
    }

    @ApiOperation("删除")
    @DeleteMapping("remove/{id}")
    public Result removeById(@PathVariable Long id){
        menuService.removeById(id);
        return Result.ok(null);
    }

}

