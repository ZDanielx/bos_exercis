package cn.itcast.bos.service.system;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;

import java.util.List;

/**
 * Created by Ricky on 2018/1/16
 */
public interface MenuService {
    /**
     * 查询所有菜单的方法
     * @return
     */
    List<Menu> findAll();

    /**
     * 菜单保存的方法
     * @param model
     */
    void save(Menu model);

    /**
     * 通过用户查询菜单列表的方法
     * @param user
     * @return
     */
    List<Menu> findByUser(User user);
}
