package com.yf.summarize.summarize.mapper;

import com.yf.summarize.summarize.entity.Family;
import com.yf.summarize.summarize.util.BaseMapper;
import org.springframework.cache.annotation.CacheConfig;

@CacheConfig(cacheNames = "family")
public interface FamilyMapper extends BaseMapper<Family> {
}