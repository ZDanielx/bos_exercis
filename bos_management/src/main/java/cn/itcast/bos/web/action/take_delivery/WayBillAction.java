package cn.itcast.bos.web.action.take_delivery;

import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.base.WayBillService;
import cn.itcast.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ricky on 2018/1/11
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class WayBillAction extends BaseAction<WayBill> {

    private static final Logger LOGGER = Logger.getLogger(WayBillAction.class);
    //注入service
    @Autowired
    private WayBillService wayBillService;

    /**
     * 保存运单
     *
     * @return
     */
    @Action(value = "waybill_save", results = {@Result(name = "success", type = "json")})
    public String save() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            //去除没有id的order对象
            if (model.getOrder() != null && (model.getOrder().getId() == null || model.getOrder().getId() == 0)) {
                model.setOrder(null);
            }
            wayBillService.save(model);
            //保存成功
            result.put("success", true);
            result.put("msg", "保存运单成功");
            LOGGER.info("保存运单成功,运单号" + model.getWayBillNum());
        } catch (Exception e) {
            e.printStackTrace();
            //保存失败
            result.put("success", false);
            result.put("msg", "保存运单失败");
            LOGGER.info("保存运单成功,运单号" + model.getWayBillNum(), e);
        }
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }

    /**
     * 无条件查询
     *
     * @return
     */
    @Action(value = "waybill_pageQuery", results = {@Result(name = "success", type = "json")})
    public String pageQuery() {
        Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Sort.Order(Sort.Direction.DESC, "id")));
        //调用业务层进行查找
        Page<WayBill> wayBills =wayBillService.pageQuery(model,pageable);
        //将查询结果压入值栈
        pushPageDataToValueStack(wayBills);
        return SUCCESS;
    }

    /**
     * 通过运单编号查找运单的方法
     * @return
     */
    @Action(value = "waybill_findByWayBillNum",results = {@Result(name = "success",type = "json")})
    public String findByWayBillNum(){
        //调用业务层进行查询
        WayBill wayBill = wayBillService.findByWayBillNum(model.getWayBillNum());

        Map<String,Object> result = new HashMap<String,Object>();

        if (wayBill == null){
            //wayBill不存在
            result.put("success",false);
        }else {
            //wayBill存在
            result.put("success",true);
            result.put("wayBillData",wayBill);
        }
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }

    /**
     * 查询所有订单的方法
     * @return
     */
    public List<WayBill> findAll(){

        return wayBillService.findAll();
    }
}
