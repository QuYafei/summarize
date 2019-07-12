package com.yf.summarize.summarize;

import com.yf.summarize.summarize.entity.Family;
import com.yf.summarize.summarize.redis.RedisService;
import com.yf.summarize.summarize.service.FamilyService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class FamilyJunit {

    @Autowired
    private FamilyService familyService;

    @Autowired
    private RedisService redisService;

    @Ignore
    @Test
    public void selectFamily(){
        List<Family> families = familyService.selectFamilyAll();
        for (Family family:families){
            System.out.println("-------------------------------------"+family);
        }
    }

    @Ignore
    @Test
    public void random(){
//       int random = (int) ((Math.random()*9+1)*100000);
//        System.out.println((int)((Math.random()*9+1)*100000));
        Object constants = redisService.getString("13271531318");
        System.out.println("-------------"+ constants);

//        String code=(int)((Math.random()*9+1)*100000)+"";
//        System.out.println("-------------"+ code);

    }
}
