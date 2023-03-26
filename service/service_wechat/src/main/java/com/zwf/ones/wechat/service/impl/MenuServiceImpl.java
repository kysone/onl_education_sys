package com.zwf.ones.wechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zwf.ones.exception.OnesExceptionHandler;
import com.zwf.ones.model.wechat.Menu;
import com.zwf.ones.result.Result;
import com.zwf.ones.vo.wechat.MenuVo;
import com.zwf.ones.wechat.config.WeChatMpConfig;
import com.zwf.ones.wechat.mapper.MenuMapper;
import com.zwf.ones.wechat.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单明细 订单明细 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-03-24
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private WxMpService wxMpService;

    @Override
    public List<Menu> findMenuOneInfo() {
        QueryWrapper<Menu> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("parent_id",0);
        List<Menu> list = baseMapper.selectList(queryWrapper);
        return list;
    }

    @Override
    public List<MenuVo> findMenuInfo() {
        List<MenuVo>finalMenuList=new ArrayList<>();

        List<Menu> menuList = baseMapper.selectList(null);

        List<Menu> oneMenuList = menuList.stream()
                .filter(menu -> menu.getParentId().longValue() == 0)
                .collect(Collectors.toList());

        for (Menu oneMenu :oneMenuList){
            //Menu-->MenuVo
            MenuVo oneMenuVo = new MenuVo();
            BeanUtils.copyProperties(oneMenu,oneMenuVo);

            List<Menu> twoMenuList = menuList.stream()
                    .filter(menu -> menu.getParentId().longValue() == oneMenu.getId())
                    .collect(Collectors.toList());

            //List<Menu> -->List<MenuVo>
            List<MenuVo>children=new ArrayList<>();
            for (Menu twoMenu :twoMenuList){
                MenuVo twoMenuVo = new MenuVo();
                BeanUtils.copyProperties(twoMenu,twoMenuVo);
                children.add(twoMenuVo);
            }
            //把二级菜单数据放到一级菜单里面
            oneMenuVo.setChildren(children);
            //把oneMenuVo放入最终list集合中
            finalMenuList.add(oneMenuVo);
        }


        return finalMenuList;
   }


    //同步菜单方法
    @Override
    public void syncMenu() {
        //获取所有菜单数据
        List<MenuVo> menuVoList = this.findMenuInfo();
        //封装button里面结构，数组格式
        JSONArray buttonList = new JSONArray();
        for(MenuVo oneMenuVo:menuVoList){
            //json对象 一级菜单
            JSONObject one =new JSONObject();
            one.put("name",oneMenuVo.getName());

            //json数组，封住二级菜单
            JSONArray subButton =new JSONArray();
            for (MenuVo twoMenuVo:oneMenuVo.getChildren()) {
                JSONObject view = new JSONObject();
                view.put("type", twoMenuVo.getType());
                if(twoMenuVo.getType().equals("view")) {
                    view.put("name", twoMenuVo.getName());
                    view.put("url", "http://ggkt2.vipgz1.91tunnel.com/#"
                            +twoMenuVo.getUrl());
                } else {
                    view.put("name", twoMenuVo.getName());
                    view.put("key", twoMenuVo.getMeunKey());
                }
                subButton.add(view);
            }
            one.put("sub_button",subButton);
            buttonList.add(one);
        }
        //封装最外层的button部分
        JSONObject button =new JSONObject();
        button.put("button",buttonList);
        try {
            String menuId = this.wxMpService.getMenuService().menuCreate(button.toJSONString());
            System.out.println(menuId);
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new OnesExceptionHandler(20001,"公众号菜单同步失败");
        }
    }

    @SneakyThrows
    @Override
    public void removeMenu() {
        wxMpService.getMenuService().menuDelete();
    }
    }


