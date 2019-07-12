package com.yf.summarize.summarize.demo;

import com.yf.summarize.summarize.entity.Family;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestService {


    public PlanDefectCounter getPlanDefectCounter(String planUUID){
        //select * as from test_defect group by groupUUID,caseUUID where ?=?
        List<String> row = new ArrayList<>();
        Map<String,Integer> map = new HashMap<>();
        return new PlanDefectCounter(map);
    }

    public void getField() throws IllegalAccessException {
        Family family = new Family();

        Field[] declaredFields = Family.class.getDeclaredFields();
        for (Field field:declaredFields){
            field.getName();
            Object o = field.get(family);
        }
    }


    public static class PlanDefectCounter{
        private Map<String,Integer> counter;

        public PlanDefectCounter(Map<String,Integer> map){
            this.counter = map;
        }

        public Integer getDefectCount(String groupUUID,String caseUUID){
            return counter.getOrDefault(groupUUID +"-"+caseUUID,0);
        }

    }

}
