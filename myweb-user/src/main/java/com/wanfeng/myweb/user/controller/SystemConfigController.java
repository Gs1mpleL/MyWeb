package com.wanfeng.myweb.user.controller;

import com.wanfeng.myweb.api.dto.SystemConfigDto;
import com.wanfeng.myweb.user.entity.SystemConfigEntity;
import com.wanfeng.myweb.user.service.SystemConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
public class SystemConfigController {

    @Resource
    private SystemConfigService systemConfigService;
    @GetMapping("/getSystemConfig")
    public SystemConfigDto getSystemConfig(){
        SystemConfigDto systemConfigDto = new SystemConfigDto();
        BeanUtils.copyProperties(systemConfigService.getById(1),systemConfigDto);
        return systemConfigDto;
    }

    @PostMapping("/updateSystemConfig")
    public boolean updateSystemConfig(@RequestBody SystemConfigDto systemConfigDto){
        SystemConfigEntity systemConfigEntity = new SystemConfigEntity();
        BeanUtils.copyProperties(systemConfigDto,systemConfigEntity);
        return systemConfigService.updateById(systemConfigEntity);
    }
}
