package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * Created by Ricky on 2017/12/23
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class StandardAction extends ActionSupport implements ModelDriven<Standard> {

    private Standard standard = new Standard();

    @Autowired
    private StandardService standardService;

    @Override
    public Standard getModel() {
        return standard;
    }

    /**
     * 添加的方法
     */
    @Action(value = "standard_save", results = {@Result(name = "success", location = "./pages/base/standard.html", type = "redirect")})
    public String save() {
        standardService.save(standard);
        return SUCCESS;
    }

}
