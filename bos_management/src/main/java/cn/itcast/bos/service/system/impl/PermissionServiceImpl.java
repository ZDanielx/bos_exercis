package cn.itcast.bos.service.system.impl;

import cn.itcast.bos.dao.system.PermissionRepsitory;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Ricky on 2018/1/15
 */
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService{

    @Autowired
    private PermissionRepsitory permissionRepsitory;

    /**
     * 根据用户查询权限
     * @param user
     * @return
     */
    @Override
    public List<Permission> findByUser(User user) {
        if (user.getUsername().equals("admin")){
            return permissionRepsitory.findAll();
        }else {
            return permissionRepsitory.findByUser(user.getId());
        }
    }
}
