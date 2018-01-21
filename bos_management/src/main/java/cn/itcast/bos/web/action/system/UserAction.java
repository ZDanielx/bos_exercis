package cn.itcast.bos.web.action.system;

import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.UserService;
import cn.itcast.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Created by Ricky on 2018/1/15
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class UserAction extends BaseAction<User> {

    @Autowired
    private UserService userService;

    /**
     * 用户登录验证的方法
     *
     * @return
     */
    @Action(value = "user_login", results = {@Result(name = "success", type = "redirect", location = "index.html"),
            @Result(name = "login", type = "redirect", location = "login.html")})
    public String login() {
        //用户名密码 都保存在model中
        //基于shiro实现登录
        Subject subject = SecurityUtils.getSubject();
        //用户名和密码信息
        AuthenticationToken token = new UsernamePasswordToken(model.getUsername(), model.getPassword());
        try {
            //登录成功
            //将用户信息保存到session中
            subject.login(token);
            return SUCCESS;
        } catch (Exception e) {
            //登陆失败
            e.printStackTrace();
            return LOGIN;
        }

    }

    /**
     * 用户退出功能
     *
     * @return
     */
    @Action(value = "user_logout", results = {@Result(name = "success", type = "redirect", location = "login.html")})
    public String logout() {
        //基于shiro完成退出
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return SUCCESS;
    }

    /**
     * 查找所有用户的方法
     *
     * @return
     */
    @Action(value = "user_list", results = {@Result(name = "success", type = "json")})
    public String list() {
        //调用业务层进行查找
        List<User> users = userService.findAll();
        //将查找到的结果压入值栈
        ActionContext.getContext().getValueStack().push(users);
        return SUCCESS;
    }

    //属性驱动
    private String[] roleIds;

    public void setRoleIds(String[] roleIds) {
        this.roleIds = roleIds;
    }

    /**
     * 添加用户的方法
     *
     * @return
     */
    @Action(value = "user_save", results = {@Result(name = "success", type = "redirect", location = "pages/system/userlist.html")})
    public String save() {
        //调用业务层进行保存
        userService.save(roleIds, model);
        return SUCCESS;
    }

}
