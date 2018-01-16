package cn.itcast.bos.web.action.system;

import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.web.action.common.BaseAction;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * Created by Ricky on 2018/1/15
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class UserAction extends BaseAction<User> {

    @Action(value = "user_login", results = {@Result(name = "success", type = "redirect", location = "index.html"),
            @Result(name = "login", type = "redirect", location = "login.html")})
    public String login() {
        //用户名密码 都保存在model中
        //基于shiro实现登录
        Subject subject = SecurityUtils.getSubject();

        //用户名和密码信息
        AuthenticationToken token = new UsernamePasswordToken(model.getUsername(),model.getPassword());
        try {
            //登录成功
            //将用户信息保存到session中

            subject.login(token);
            return SUCCESS;
        }catch (Exception e){
            //登陆失败
            e.printStackTrace();
            return LOGIN;
        }

    }
}
