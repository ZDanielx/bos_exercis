package cn.itcast.bos.mq;


import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;


/**
 * Created by Ricky on 2018/1/5
 */
@Service("smsConsumer")
public class SmsConsumer implements MessageListener {

    @Override
    public void onMessage(Message message) {
        MapMessage mapMessage = (MapMessage) message;

        //调用Sms发送短信
        String result = null;
        try {
           // result = SmsUtils.sendSmsByHTTP(mapMessage.getString("telephone"), mapMessage.getString("msg"));
            result = "000/xxx";
            if (result.startsWith("000")) {
                //发送成功
                System.out.println("手机号："+mapMessage.getString("telephone")+"验证码："+mapMessage.getString("msg"));
            }
        } catch (JMSException e) {
            e.printStackTrace();
            //发送失败
            throw new RuntimeException("短信发送失败,信息码" + result);
        }

    }
}
