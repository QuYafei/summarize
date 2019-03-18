package com.yf.summarize.summarize;

import com.yf.summarize.summarize.entity.Family;
import com.yf.summarize.summarize.service.FamilyService;
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

    @Test
    public void selectFamily(){
        List<Family> families = familyService.selectFamilyAll();
        for (Family family:families){
            System.out.println("-------------------------------------"+family);
        }
    }
}
