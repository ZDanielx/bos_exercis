package cn.itcast.bos.service.system.impl;

import cn.itcast.bos.dao.system.MenuRepsitory;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Ricky on 2018/1/16
 */
@Service
@Transactional
public class MenuServiceImpl implements MenuService {

    //注入DAO
    @Autowired
    private MenuRepsitory menuRepsitory;

    @Override
    public List<Menu> findAll() {
        return menuRepsitory.findAll();
    }

    @Override
    public void save(Menu model) {
        //防止用户没有选中父菜单
        if (model.getParentMenu() != null && model.getParentMenu().getId() == 0) {
            model.setParentMenu(null);
        }

        //调用DAO进行保存
        menuRepsitory.save(model);
    }

    @Override
    public List<Menu> findByUser(User user) {
        //如果为admin则返回所有菜单
        if (user.getUsername().equals("admin")){
            return menuRepsitory.findAll();
        }else {
            return menuRepsitory.findByUser(user.getId());
        }
    }
}
