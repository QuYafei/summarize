package com.yf.summarize.summarize.service;

import com.yf.summarize.summarize.entity.Family;
import com.yf.summarize.summarize.mapper.FamilyMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class FamilyService {

    @Autowired
    private FamilyMapper familyMapper;

    @Transactional
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

    @Cacheable(cacheNames = "family",key = "#key")
    public Family selectFamily(String key) {
        Family family = familyMapper.selectByPrimaryKey(key);
        return family;
    }

    public List<Family> selectFamilyAll() {
        List<Family> families = familyMapper.selectAll();
        return families;
    }

    // mybatis 自定义sql
    public List<Family> selectFamily(Family family) {
        Example example = new Example(Family.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",family.getId());
        if (StringUtils.isBlank(family.getName())){
            criteria.andLike("name","%"+family.getName()+"%");
        }
        List<Family> families = familyMapper.selectByExample(example);
        return families;
    }

}
