package cn.itcast.bos.service.impl;

import cn.itcast.bos.dao.base.AreaRepsitory;
import cn.itcast.bos.dao.base.FixedAreaRepsitory;
import cn.itcast.bos.dao.base.OrderRepsitory;
import cn.itcast.bos.dao.base.WorkBillRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.domain.contant.Constants;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WorkBill;
import cn.itcast.bos.service.base.OrderService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

/**
 * Created by Ricky on 2018/1/11
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    //注入fixedarea的Dao
    @Autowired
    private FixedAreaRepsitory fixedAreaRepsitory;
    //注入area的Dao
    @Autowired
    private AreaRepsitory areaRepsitory;
    //注入Order的Dao
    @Autowired
    private OrderRepsitory orderRepsitory;
    //注入WorkBill
    @Autowired
    private WorkBillRepository workBillRepository;
    //注入MQ
    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsTemplate;

    @Override
    public void addOrder(Order order) {

        order.setOrderNum(UUID.randomUUID().toString()); //设置订单号
        order.setOrderTime(new Date()); //设置下单时间
        order.setStatus("1"); //待取件

        //寄件人省市区
        Area sendArea = order.getSendArea();
        Area persistArea = areaRepsitory.findByProvinceAndCityAndDistrict(sendArea.getProvince(), sendArea.getCity(), sendArea.getDistrict());
        //收件人省市区
        Area recArea = order.getRecArea();
        Area persistRecArea = areaRepsitory.findByProvinceAndCityAndDistrict(recArea.getProvince(), recArea.getCity(), recArea.getDistrict());

        order.setSendArea(persistArea);
        order.setRecArea(persistRecArea);

        //自动分单,基于crm地址库完全匹配,获取定区,匹配快递员
        String fixedAreaId = WebClient.create(Constants.CRM_MANAGEMENT_URL + "/services/customerService/customer/findFixedAreaIdByAddress?address=" + order.getSendAddress()).accept(MediaType.APPLICATION_JSON).get(String.class);
        if (fixedAreaId != null) {
            FixedArea fixedArea = fixedAreaRepsitory.findOne(fixedAreaId);
            Courier courier = fixedArea.getCouriers().iterator().next();
            if (courier != null) {
                //自动分单成功
                saveOrder(order, courier);
                //生工工单发送短信
                generateWorkBill(order);
                System.out.println("自动分单成功");
                return;
            }
        }
        //自动分单逻辑,通过省市区,查找分区关键字,匹配地址,基于分区实现
        for (SubArea subArea : persistArea.getSubareas()) {
            //当前客户的下单地址是否包含分区关键字
            if (order.getSendAddress().contains(subArea.getKeyWords())) {
                //找到分区,找到定区,找到快递员
                Iterator<Courier> iterator = subArea.getFixedArea().getCouriers().iterator();
                if (iterator.hasNext()) {
                    Courier courier = iterator.next();
                    if (courier != null) {
                        saveOrder(order, courier);
                        //生工工单发送短信
                        generateWorkBill(order);
                        //自动分单成功
                        System.out.println("自动分单成功");
                        return;
                    }
                }
            }
        }
        for (SubArea subArea : persistArea.getSubareas()) {
            //当前客户的下单地址 是否包含分区 辅助关键字
            if (order.getSendAddress().contains(subArea.getAssistKeyWords())) {
                //找到分区 找到定区 找到快递员
                Iterator<Courier> iterator = subArea.getFixedArea().getCouriers().iterator();
                if (iterator.hasNext()) {
                    Courier courier = iterator.next();
                    if (courier != null) {
                        //自动分单成功
                        System.out.println("自动分单成功");
                        saveOrder(order, courier);
                        //生工工单发送短信
                        generateWorkBill(order);
                        return;
                    }
                }
            }
        }
        //进行人工分单
        order.setOrderType("2");
        orderRepsitory.save(order);
    }

    /**
     * 自动分单保存
     *
     * @param order
     * @param courier
     */
    private void saveOrder(Order order, Courier courier) {
        //快递员关联订单上
        order.setCourier(courier);
        //设置自动分单
        order.setOrderType("1");
        //保存订单
        orderRepsitory.save(order);
    }

    /**
     * 生成工单,发送短信
     *
     * @param order
     */
    private void generateWorkBill(final Order order) {
        //生成工单
        WorkBill workBill = new WorkBill();
        workBill.setType("新");
        workBill.setPickstate("新单");
        workBill.setBuildtime(new Date());
        final String smsNumber = RandomStringUtils.randomNumeric(4);
        workBill.setSmsNumber(smsNumber);
        workBill.setOrder(order);
        workBill.setCourier(order.getCourier());
        workBillRepository.save(workBill);

        //发送短息
        //调用MQ服务,发送一条短信
        jmsTemplate.send("bos_sms", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("telephone", order.getCourier().getTelephone());
                mapMessage.setString("msg", "短信序号：" + smsNumber + ",取件地址：" + order.getSendAddress()
                        + ",联系人:" + order.getSendName() + ",手机:"
                        + order.getSendMobile() + "，快递员捎话："
                        + order.getSendMobileMsg());
                return mapMessage;
            }
        });
        //修改工单状态
        workBill.setPickstate("已通知");
    }
}
