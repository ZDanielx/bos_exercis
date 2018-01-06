package cn.itcast.bos.web.action;

import cn.itcast.crm.domain.Customer;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Ricky on 2018/1/3
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class CustomerAction extends BaseAction<Customer> {

    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsTemplate;

    @Action(value = "customer_sendSms")
    public String sendSms() throws UnsupportedEncodingException {
        //手机号已经保存在customer对象中
        //生成短信验证码
        String randomCode = RandomStringUtils.randomNumeric(4);
        //将生成的短信验证码保存到session中
        ServletActionContext.getRequest().getSession().setAttribute(model.getTelephone(), randomCode);
        System.out.println("生成的短信验证码为:" + randomCode);
        //编辑短信内容
        final String msg = "尊敬的用户您好，本次获取的验证码为" + randomCode + ",服务电话：4006184000";
        //调用MQ服务发送消息

        jmsTemplate.send("bos_sms", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("telephone", model.getTelephone());
                mapMessage.setString("msg", msg);
                return mapMessage;
            }
        });
        return NONE;
    }

    //属性驱动
    private String checkcode;

    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Action(value = "customer_regist", results = {
            @Result(name = "success", location = "signup-success.html", type = "redirect"),
            @Result(name = "input", location = "signup.html", type = "redirect")})
    public String customerRegist() {
        //先校验短信验证码,如果不通过跳回到注册页面
        //在session中获取之前生成的验证码
        String checkCodeSession = (String) ServletActionContext.getRequest().getSession().getAttribute(model.getTelephone());
        if (checkCodeSession == null || !checkCodeSession.equals(checkcode)) {
            //验证码错误
            return INPUT;
        } else {
            //调用webservice保存客户信息
            WebClient.create("http://localhost:9001/crm_management/services/customerService/customer").type(MediaType.APPLICATION_JSON).post(model);
            System.out.println("客户注册成功....");
            jmsTemplate.send("bos_mail", new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    MapMessage mapMessage = session.createMapMessage();
                    //在Queue中保存客户的电话和邮箱地址
                    mapMessage.setString("telephone",model.getTelephone());
                    mapMessage.setString("email",model.getEmail());
                    return mapMessage;
                }
            });
            return SUCCESS;
        }
    }

    //属性驱动
    private String activecode;

    public void setActivecode(String activecode) {
        this.activecode = activecode;
    }

    /**
     * 验证邮箱绑定的方法
     *
     * @return
     */
    @Action("customer_activeMail")
    public String activeMail() throws IOException {
        ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
        //判断验证码是否失效
        String activecodeRedis = redisTemplate.opsForValue().get(model.getTelephone());
        if (activecodeRedis == null || !activecodeRedis.equals(activecodeRedis)) {
            //验证码无效
            ServletActionContext.getResponse().getWriter().println("激活码无效，请登录系统，重新绑定邮箱！");
        } else {
            // 激活码有效
            // 防止重复绑定
            // 调用CRM webService 查询客户信息，判断是否已经绑定
            Customer customer = WebClient
                    .create("http://localhost:9001/crm_management/services"
                            + "/customerService/customer/telephone/"
                            + model.getTelephone())
                    .accept(MediaType.APPLICATION_JSON).get(Customer.class);
            if (customer.getType() == null || customer.getType() != 1) {
                WebClient.create(
                        "http://localhost:9001/crm_management/services"
                                + "/customerService/customer/updatetype/"
                                + model.getTelephone()).get();
                ServletActionContext.getResponse().getWriter().print("邮箱绑定成功!");
            } else {
                //已经绑定
                ServletActionContext.getResponse().getWriter().print("邮箱已经绑定过，无需重复绑定！");
            }
        }
        //删除redis的激活码
        redisTemplate.delete(model.getTelephone());
        return NONE;
    }
}
