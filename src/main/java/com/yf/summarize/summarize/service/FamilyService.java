package com.yf.summarize.summarize.service;

import com.yf.summarize.summarize.entity.Family;
import com.yf.summarize.summarize.mapper.FamilyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FamilyService {

    @Autowired
    private FamilyMapper familyMapper;

    public int insertFamily(Family family) {
        int insert = familyMapper.insert(family);
        return insert;
    }

    public int deleteFamily(String key) {
        int deleteByPrimaryKey = familyMapper.deleteByPrimaryKey(key);
        return deleteByPrimaryKey;
    }

    public int updateFamily(Family family) {
        int updateByPrimaryKey = familyMapper.updateByPrimaryKey(family);
        return updateByPrimaryKey;
    }

    public Family selectFamily(String key) {
        Family family = familyMapper.selectByPrimaryKey(key);
        return family;
    }

    public List<Family> selectFamilyAll() {
        List<Family> families = familyMapper.selectAll();
        return families;
    }

}
