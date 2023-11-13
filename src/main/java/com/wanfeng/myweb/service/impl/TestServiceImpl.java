package com.wanfeng.myweb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanfeng.myweb.Entity.TestEntity;
import com.wanfeng.myweb.mapper.TestMapper;
import com.wanfeng.myweb.service.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, TestEntity> implements TestService {
}
