package cn.itcast.bos.web.action.system;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.service.system.MenuService;
import cn.itcast.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Created by Ricky on 2018/1/16
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class MenuAction extends BaseAction<Menu> {

    //注入service
    @Autowired
    private MenuService menuService;

    /**
     * 查询所有菜单的方法
     *
     * @return
     */
    @Action(value = "menu_list", results = {@Result(name = "success", type = "json")})
    public String list() {
        //调用业务层查讯
        List<Menu> menus = menuService.findAll();
        //将结果存入值栈
        ActionContext.getContext().getValueStack().push(menus);
        return SUCCESS;
    }

    /**
     * 菜单数据的保存方法
     * @return
     */
    @Action(value = "menu_save",results = {@Result(name = "success",type = "redirect",location = "pages/system/menu.html")})
    public String save(){
            //调用业务层保存数据
        menuService.save(model);
        return SUCCESS;
    }
}
