package cn.itcast.bos.utils;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.Properties;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * 使用QQ发送邮件工具类
 *
 * @author canye
 * 2018年1月3日星期三
 */
public class QQMailUtis {

    private final static String SERVICE_HOST = "smtp.qq.com";//QQ服务器
    private final static int PORT = 465; //smtp的端口号
    private final static String PROTOCOL = "smtp"; //协议名称。smtp表示简单邮件传输协议
    private final static String ACCOUNT = "2421137562@qq.com"; //发送邮件的QQ账号
    private final static String AUTH_CODE = "hvuwbnvicjvgdjbc"; //QQ授权码(需要到https://mail.qq.com/申请)

    private static final JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();

    /*
     *发送QQ邮件的初始化配置
     */
    static {
        senderImpl.setHost(SERVICE_HOST); //设置 使用QQ邮箱发送邮件的主机名
        senderImpl.setPort(PORT); //设置端口号
        senderImpl.setProtocol(PROTOCOL); //协议名称
        senderImpl.setUsername(ACCOUNT); // 设置自己的邮箱帐号名称
        senderImpl.setPassword(AUTH_CODE); // 设置对应账号申请到的授权码
        Properties prop = new Properties();
        prop.put(" mail.smtp.auth ", "true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //使用ssl协议来保证连接安全
        prop.put(" mail.smtp.timeout ", "25000"); //传输超时时间
        senderImpl.setJavaMailProperties(prop);
    }

    /**
     * 发送简单邮件
     *
     * @param accounts 被发邮件的用户数组
     * @param info     邮件信息
     * @param title    邮件主题
     */
    public static void sendSimpleMail(String[] accounts, String info, String title) {
//创建简单邮件对象
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(accounts);  //设置邮件接收者账号数组
        mailMessage.setFrom(ACCOUNT); //设置邮件的发送者
        mailMessage.setSubject(title);//设置邮件的主题
        mailMessage.setText(info);    //设置邮件的内容
//发送邮件
        senderImpl.send(mailMessage);
    }

    /**
     * 函数入口（主要用于shell调用）
     *
     * @param args 有三个参数，分别是文件名（主要是邮箱账户），邮件主题  和邮件内容
     */
    public static void main(String args[]) {
        if (args.length != 3) {//判断输入参数是否正确
            System.out.println("请输入正确的参数，分别是文件名、邮件主题和邮件内容");
            return;
        }
        try {
//建立输入缓冲流，读取邮箱账号文件信息
            BufferedReader reader = new BufferedReader(new FileReader(args[0]));
//创建一个存储账号信息
            StringBuilder builder = new StringBuilder();
//遍历账号信息
            String line = "";
            int index = 0;//索引
            while ((line = reader.readLine()) != null) {
//取出两边空格
                line = line.trim();
                //判断改行是否为空
                if (line.equals(""))
                    continue;
//把读取到的邮箱帐号添加到builder中,多个值用逗号分隔
                if (index == 0) {
                    builder.append(line);
                } else {
                    builder.append("," + line);
                }
                index++;
            }
//把字符串切割成数组array
            String[] accounts = builder.toString().split(",");
//发送邮件,args[1] 邮件主题，args[2] 邮件内容
            sendSimpleMail(accounts, args[1], args[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(" 邮件发送成功.. ");
    }
}
