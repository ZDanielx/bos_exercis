package cn.itcast.bos.service.system.impl;

import cn.itcast.bos.dao.system.RoleRepsitory;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Ricky on 2018/1/15
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleRepsitory roleRepsitory;

    @Override
    public List<Role> findByUser(User user) {
        //基于用户查询角色
        //admin具有所有的角色
        if(user.getUsername().equals("admin")){
            return roleRepsitory.findAll();
        }else {
            return roleRepsitory.findByUser(user.getId());
        }
    }
}
