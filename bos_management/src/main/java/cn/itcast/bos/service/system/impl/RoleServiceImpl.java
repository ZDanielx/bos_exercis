package cn.itcast.bos.service.system.impl;

import cn.itcast.bos.dao.system.MenuRepsitory;
import cn.itcast.bos.dao.system.PermissionRepsitory;
import cn.itcast.bos.dao.system.RoleRepsitory;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Ricky on 2018/1/15
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepsitory roleRepsitory;
    @Autowired
    private PermissionRepsitory permissionRepsitory;
    @Autowired
    private MenuRepsitory menuRepsitory;

    @Override
    public List<Role> findByUser(User user) {
        //基于用户查询角色
        //admin具有所有的角色
        if (user.getUsername().equals("admin")) {
            return roleRepsitory.findAll();
        } else {
            return roleRepsitory.findByUser(user.getId());
        }
    }

    @Override
    public List<Role> findAll() {
        return roleRepsitory.findAll();
    }

    @Override
    public void save(Role model, String[] permissionIds, String menuIds) {
        //保存角色信息
        roleRepsitory.save(model);
        //关联权限
       if (permissionIds != null) {
            for (String permissionId : permissionIds) {
                Permission permission = permissionRepsitory.findOne(Integer.parseInt(permissionId));
                model.getPermissions().add(permission);
            }
        }
        //关联菜单
        if (StringUtils.isNoneBlank(menuIds)) {
            String[] menuIdArray = menuIds.split(",");
            for (String menuId : menuIdArray) {
                Menu menu = menuRepsitory.findOne(Integer.parseInt(menuId));
                model.getMenus().add(menu);
            }
        }

    }

}
