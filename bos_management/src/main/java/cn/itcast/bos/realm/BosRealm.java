package cn.itcast.bos.realm;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.PermissionService;
import cn.itcast.bos.service.system.RoleService;
import cn.itcast.bos.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 自定义的realm实现安全数据的连接
 * Created by Ricky on 2018/1/15
 */
@Service("bosRealm")
public class BosRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RoleService roleService;

    @Override
    //授权的方法
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        SimpleAuthorizationInfo ationInfo = new SimpleAuthorizationInfo();
        //根据当前登录的用户 查询对应角色和权限
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        //调用业务层查讯角色
        List<Role> roles = roleService.findByUser(user);
        for (Role role : roles) {
            ationInfo.addRole(role.getKeyword());
        }
        //调用业务层查询权限
        List<Permission> permissions = permissionService.findByUser(user);
        for (Permission permission : permissions) {
            ationInfo.addStringPermission(permission.getKeyword());
        }
        return ationInfo;
    }

    @Override
    //认证的方法
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //转换token
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;

        //根据用户名查询用户信息
        User user = userService.findByUsername(usernamePasswordToken.getUsername());

        if (user == null) {
            //用户名不存在
            // 参数一： 期望登录后，保存在Subject中信息
            // 参数二： 如果返回为null 说明用户不存在，报用户名
            // 参数三 ：realm名称
            return null;
        } else {
            // 用户名存在
            // 当返回用户密码时，securityManager安全管理器，自动比较返回密码和用户输入密码是否一致
            // 如果密码一致 登录成功， 如果密码不一致 报密码错误异常
            return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
        }
    }
}
