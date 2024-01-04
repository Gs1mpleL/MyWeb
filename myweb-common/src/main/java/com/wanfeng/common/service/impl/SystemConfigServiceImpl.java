package com.wanfeng.common.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanfeng.common.entity.SystemConfigEntity;
import com.wanfeng.common.mapper.SystemConfigMapper;
import com.wanfeng.common.service.SystemConfigService;
import org.springframework.stereotype.Service;

@Service
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfigEntity> implements SystemConfigService {
}
