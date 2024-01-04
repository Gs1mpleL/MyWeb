package com.wanfeng.myweb.api.client;

import com.wanfeng.myweb.api.dto.SystemConfigDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("myweb-user")
public interface UserClient {
    @GetMapping("/getSystemConfig")
    SystemConfigDto getSystemConfig();
    @PostMapping("/updateSystemConfig")
    public boolean updateSystemConfig(@RequestBody SystemConfigDto systemConfigDto);
}
