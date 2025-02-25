package com.shaoyafan.jobhubbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * Web配置
 *
 * @author SYF
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取 userAvatar 文件夹的绝对路径
        String userAvatarPath = new File("file/userAvatar").getAbsolutePath();
        // 将 /userAvatar/** 映射到本地的 file/userAvatar 目录
        registry.addResourceHandler("/file/userAvatar/**")
                .addResourceLocations("file:" + userAvatarPath + "/");

        // 获取 companyLogo 文件夹的绝对路径
        String companyLogoPath = new File("file/companyLogo").getAbsolutePath();
        // 将 /companyLogo/** 映射到本地的 file/companyLogo 目录
        registry.addResourceHandler("/file/companyLogo/**")
                .addResourceLocations("file:" + companyLogoPath + "/");

        // 获取 resume 文件夹的绝对路径
        String applicationInfoPath = new File("file/resume").getAbsolutePath();
        // 将 /applicationInfo/** 映射到本地的 file/applicationInfo 目录
        registry.addResourceHandler("/file/resume/**")
                .addResourceLocations("file:" + applicationInfoPath + "/");
    }
}