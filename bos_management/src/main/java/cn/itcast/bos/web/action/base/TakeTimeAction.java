package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.TakeTimeService;
import cn.itcast.bos.web.action.base.common.BaseAction;
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
 * Created by Ricky on 2017/12/30
 */
@Controller
@Scope("prototype")
@ParentPackage("json-default")
@Namespace("/")
public class TakeTimeAction extends BaseAction<TakeTime>{

    @Autowired
    private TakeTimeService takeTimeService;

    /**
     * 查找全部TakeTime的方法
     * @return
     */
    @Action(value = "taketime_findAll",results = {@Result(name = "success",type = "json")})
    public String findAllTakeTime(){
        //调用业务层进行查找
        List<TakeTime>takeTimes = takeTimeService.findAll();
        //压入值栈
        ActionContext.getContext().getValueStack().push(takeTimes);
        return SUCCESS;
    }
}
