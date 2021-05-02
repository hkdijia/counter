package com.gotkx.counter.controller;

import com.gotkx.counter.bean.CounterRes;
import com.gotkx.counter.bean.res.CaptchaRes;
import com.gotkx.counter.cache.CacheType;
import com.gotkx.counter.cache.RedisStringCache;
import com.gotkx.counter.util.Captcha;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thirdpart.uuid.GudyUuid;

import java.io.IOException;

@RestController
@RequestMapping("/login")
@Log4j2
public class LoginController {

    @RequestMapping("/captcha")
    public CounterRes captcha() throws IOException {
        Captcha captcha = new Captcha(120, 40, 4, 10);

        //2.将验证码<ID,验证码数值>放入缓存
        String uuid = String.valueOf(GudyUuid.getInstance().getUUID());
        RedisStringCache.cache(uuid, captcha.getCode(), CacheType.CAPTCHA);

        //3.使用base64编码图片，并返回给前台
        //uuid,base64
        CaptchaRes res = new CaptchaRes(uuid, captcha.getBase64ByteStr());
        return new CounterRes(res);
    }

    //@RequestMapping("/userlogin")

}
