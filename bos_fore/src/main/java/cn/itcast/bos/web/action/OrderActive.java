package cn.itcast.bos.web.action;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.constant.Constants;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.crm.domain.Customer;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.ws.rs.core.MediaType;

/**
 * Created by Ricky on 2018/1/10
 * 前端系统订单数据处理
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class OrderActive extends BaseAction<Order> {

    private String sendAreaInfo; //发件人的省市信息
    private String recAreaInfo; //收件人的省市信息

    public void setSendAreaInfo(String sendAreaInfo) {
        this.sendAreaInfo = sendAreaInfo;
    }

    public void setRecAreaInfo(String recAreaInfo) {
        this.recAreaInfo = recAreaInfo;
    }

    /**
     * 自动分单的方法
     *
     * @return
     */
    @Action(value = "order_add", results = {@Result(name = "success", type = "redirect", location = "index.html")})
    public String add() {
        //手动封装Area关联
        Area sendArea = new Area();
        String[] sendAreaData = sendAreaInfo.split("/");
        sendArea.setProvince(sendAreaData[0]);
        sendArea.setCity(sendAreaData[1]);
        sendArea.setDistrict(sendAreaData[2]);

        Area recArea = new Area();
        String[] recAreaData = recAreaInfo.split("/");
        recArea.setProvince(recAreaData[0]);
        recArea.setCity(recAreaData[1]);
        recArea.setDistrict(recAreaData[2]);

        model.setSendArea(sendArea);
        model.setRecArea(recArea);

        //关联当前登录客户
        Customer customer = (Customer) ServletActionContext.getRequest().getSession().getAttribute("customer");
        model.setCustomer_id(customer.getId());
        //调用webservice将数据传递bos_management系统
        WebClient.create(Constants.BOS_MANAGEMENT_URL + "/services/orderService/order").type(MediaType.APPLICATION_JSON).post(model);

        return SUCCESS;
    }
}
