package cn.itcast.bos.mq;

import cn.itcast.bos.utils.MailUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ricky on 2018/1/5
 */
@Service("emailColligation")
public class EmaileColligation implements MessageListener {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void onMessage(Message message) {
        MapMessage mapMessage = (MapMessage) message;
        //发送一封激活邮件
        //生成激活码
        String activecode = RandomStringUtils.randomNumeric(32);
        //将激活码保存到Redis中并设置24小时过期
        try {
            redisTemplate.opsForValue().set(mapMessage.getString("telephone"), activecode, 24, TimeUnit.HOURS);
            // 调用MailUtils发送激活邮件
            String content = "尊敬的客户您好，请于24小时内，进行邮箱账户的绑定，点击下面地址完成绑定:<br/><a href='"
                    + MailUtils.activeUrl + "?telephone=" + mapMessage.getString("telephone")
                    + "&activecode=" + activecode + "'>速运快递邮箱绑定地址</a>";
            MailUtils.sendMail("速运快递激活邮件", content, mapMessage.getString("email"));
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
