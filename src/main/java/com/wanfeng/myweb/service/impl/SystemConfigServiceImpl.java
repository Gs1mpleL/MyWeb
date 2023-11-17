package com.wanfeng.myweb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanfeng.myweb.Entity.SystemConfigEntity;
import com.wanfeng.myweb.mapper.SystemConfigMapper;
import com.wanfeng.myweb.service.SystemConfigService;
import org.springframework.stereotype.Service;

@Service
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfigEntity> implements SystemConfigService {
}
