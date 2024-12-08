package com.example.shop.util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtil {

    // Gmail SMTP服务器地址
    private static final String SMTP_HOST = "";
    private static final String SMTP_PORT = "";

    // 发件人邮箱和密码
    private static final String EMAIL_FROM = "";
    private static final String EMAIL_PASSWORD = "";

    /**
     * 发送简单的文本邮件
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param body 邮件正文
     * @return 是否发送成功
     */
    public static boolean sendEmail(String to, String subject, String body) {
        // 配置邮件属性
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // TLS
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        // 创建会话
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EMAIL_FROM, EMAIL_PASSWORD);
                    }
                });

        try {
            // 创建消息
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_FROM));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );
            message.setSubject(subject);
            message.setText(body);

            // 发送邮件
            Transport.send(message);

            System.out.println("邮件发送成功到 " + to);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}

