package com.wanfeng.myweb.user.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanfeng.myweb.user.entity.SystemConfigEntity;
import com.wanfeng.myweb.user.mapper.SystemConfigMapper;
import com.wanfeng.myweb.user.service.SystemConfigService;
import org.springframework.stereotype.Service;

@Service
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfigEntity> implements SystemConfigService {
}
