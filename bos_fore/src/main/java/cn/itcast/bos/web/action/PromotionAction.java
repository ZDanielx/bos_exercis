package cn.itcast.bos.web.action;

import cn.itcast.bos.domain.contant.Contants;
import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import com.opensymphony.xwork2.ActionContext;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.ws.rs.core.MediaType;


/**
 * Created by Ricky on 2018/1/6
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class PromotionAction extends BaseAction<Promotion> {

    @Action(value = "promotion_pageQuery",results = {@Result(name = "success",type = "json")})
    public String pageQuery() {
        //基于webservice获取,bos_management的活动数据列表
        PageBean<Promotion> pageBean = WebClient
                .create(Contants.BOS_MANAGEMENT_URL
                        + "/bos_management/services/promotionService/pageQuery?page="
                        + page + "&rows=" + rows)
                .accept(MediaType.APPLICATION_JSON).get(PageBean.class);
        ActionContext.getContext().getValueStack().push(pageBean);

        return SUCCESS;
    }
}
