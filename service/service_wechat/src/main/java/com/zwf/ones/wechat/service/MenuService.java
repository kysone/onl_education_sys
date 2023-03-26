package com.zwf.ones.wechat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zwf.ones.model.wechat.Menu;
import com.zwf.ones.vo.wechat.MenuVo;

import java.util.List;

/**
 * <p>
 * 订单明细 订单明细 服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-03-24
 */
public interface MenuService extends IService<Menu> {

    List<Menu> findMenuOneInfo();

    List<MenuVo> findMenuInfo();

    void syncMenu();

    void removeMenu();
}
