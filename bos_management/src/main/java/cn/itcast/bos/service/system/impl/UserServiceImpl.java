package cn.itcast.bos.service.system.impl;

import cn.itcast.bos.dao.system.RoleRepsitory;
import cn.itcast.bos.dao.system.UserRepsitory;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Ricky on 2018/1/15
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepsitory userRepsitory;
    @Autowired
    private RoleRepsitory roleRepsitory;

    @Override
    public User findByUsername(String username) {

        return userRepsitory.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepsitory.findAll();
    }

    @Override
    public void save(String[] roleIds, User model) {
        //保存用户
        userRepsitory.save(model);
        //授予角色
        if (roleIds != null) {
            for (String roleId : roleIds) {
                Role role = roleRepsitory.findOne(Integer.parseInt(roleId));
                model.getRoles().add(role);

            }
        }
    }
}
