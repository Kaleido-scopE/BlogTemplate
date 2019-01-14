package utils;



import com.cloopen.rest.sdk.CCPRestSmsSDK;

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

        CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
        restAPI.init("app.cloopen.com", "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
        restAPI.setAccount("8a216da86812593601684b043b8219a1", "de612d08507d4f6689a410449070623d");// 初始化主帐号和主帐号TOKEN
        restAPI.setAppId("8a216da86812593601684b043bd419a7");// 初始化应用ID
        HashMap<String, Object> result = restAPI.sendTemplateSMS(targetTel,"1" ,new String[]{verificationCode, validTime});

        System.out.println("SDKTestSendTemplateSMS result=" + result);
        System.out.println(verificationCode);
        return verificationCode;
    }

    public static void main(String[] args) {
        SMSManager manager = new SMSManager("18374803700", "1");
        System.out.println(manager.sendMessage());
    }
}
