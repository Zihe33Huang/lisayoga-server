package com.lht.tams;

import lombok.Data;


public class RegisterService {

    public static String register(RegisterRequest request) {
        //验证手机号是否注册
        checkMobileIsRegisted(request.getMobile());

        //验证码是否正确
        checkSMSCode(request.getMobile(), request.getSmsCode());

        //入库
        UserInfo userInfo = saveUser(request);

        //buryPoint 大数据埋点
        buryRegister(request);

        //通知下游服务派券
        noticeRegister(request);

        //流入不同的用户池，绑定对应的业务人员
        bandingRegister(request);

        //发送短信通知
        sendSMS(request.getMobile());

        //获取token
        String token = getToken(userInfo);

        return token;
    }

    public static void checkMobileIsRegisted(String moblie) {
        try {
            Thread.sleep(100);
            System.out.println("手机号验证成功");
        } catch (Exception e) {
        }
    }

    public static void checkSMSCode(String mobile, String smsCode) {
        try {
            Thread.sleep(100);
            System.out.println("验证码验证成功");
        } catch (Exception e) {
        }
    }

    public static UserInfo saveUser(RegisterRequest request) {
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId("123456789");
            Thread.sleep(100);
            System.out.println("用户入库成功");
            return userInfo;
        } catch (Exception e) {
            return null;
        }
    }

    public static void buryRegister(RegisterRequest request) {
        try {
            Thread.sleep(100);
            System.out.println("埋点成功");
        } catch (Exception e) {
        }

    }

    public static void noticeRegister(RegisterRequest request) {
        try {
            Thread.sleep(200);
        } catch (Exception e) {
            System.out.println("派券成功");
        }
    }

    public static void bandingRegister(RegisterRequest request) {
        try {
            Thread.sleep(200);
            System.out.println("绑定业务人员成功");
        } catch (Exception e) {
        }
    }

    public static void sendSMS(String mobile) {
        try {
            Thread.sleep(200);
            System.out.println("恭喜注册成功");
        } catch (Exception e) {
        }
    }

    public static String getToken(UserInfo userInfo) {
        try {
            Thread.sleep(200);
            System.out.println("获取token成功");
        } catch (Exception e) {
        }
        return "abc123456";
    }

    public static void main(String[] args) {
        RegisterRequest request = new RegisterRequest();
        request.setDevice("IOS");
        request.setMobile("18100000000");
        request.setSmsCode("123456");
        request.setSkuCode("SKU000001");
        request.setPlatform("业务地推");
        long start = System.currentTimeMillis();
        String token = register(request);
        long end = System.currentTimeMillis();
        System.out.println("注册成功，耗时：" + (end - start));
    }


}

@Data
class RegisterRequest {
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 验证码
     */
    private String smsCode;
    /**
     * 商品编码
     */
    private String skuCode;
    /**
     * 设备名称
     */
    private String device;
    /**
     * 渠道
     */
    private String platform;
}

@Data
class UserInfo {
    /**
     * 用户id
     */
    private String userId;
}

