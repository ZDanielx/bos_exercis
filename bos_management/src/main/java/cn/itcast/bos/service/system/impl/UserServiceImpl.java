package cn.itcast.bos.service.system.impl;

import cn.itcast.bos.dao.system.UserRepsitory;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Ricky on 2018/1/15
 */
@Service
@Transactional
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepsitory userRepsitory;
    @Override
    public User findByUsername(String username) {

        return userRepsitory.findByUsername(username);
    }
}
