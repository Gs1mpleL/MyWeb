package com.wanfeng.myweb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanfeng.myweb.Entity.TestEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper extends BaseMapper<TestEntity> {
}
