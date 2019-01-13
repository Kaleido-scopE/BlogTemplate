package utils;

import sdk.CCPRestSDK;

import java.util.HashMap;
import java.util.Random;

public class SMSManager {
    private String targetTel;//目标号码
    private String validTime;//有效时间
    private Random codeGenerator;//验证码生成器

    public SMSManager(String targetTel, String validTime) {
        this.targetTel = targetTel;
        this.validTime = validTime;
        codeGenerator = new Random(System.currentTimeMillis());//使用当前系统时刻作为随机数种子
    }

    public String sendMessage() {
        String verificationCode = String.valueOf((int)((codeGenerator.nextDouble()*9+1)*10000));

        CCPRestSDK restAPI = new CCPRestSDK();
        restAPI.init("app.cloopen.com", "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
        restAPI.setAccount("8aaf07086812057f016844bfcf0f17fa", "f884b207cdc44477b7632102a752e20c");// 初始化主帐号和主帐号TOKEN
        restAPI.setAppId("8aaf07086812057f016844bfcf5e1800");// 初始化应用ID
        HashMap<String, Object> result = restAPI.sendTemplateSMS(targetTel,"1" ,new String[]{verificationCode, validTime});

        System.out.println("SDKTestSendTemplateSMS result=" + result);
        return verificationCode;
    }

    public static void main(String[] args) {
        SMSManager manager = new SMSManager("15526088820", "1");
        System.out.println(manager.sendMessage());
    }
}
