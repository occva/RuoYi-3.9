package com.ruoyi.web.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;

@Configuration
public class SwaggerConfig {
        /** 是否开启swagger */
        @Value("${swagger.enabled}")
        private boolean enabled;

        /** 系统名称 */
        @Value("${ruoyi.name}")
        private String ruoyiName;

        /** 系统版本 */
        @Value("${ruoyi.version}")
        private String ruoyiVersion;

        /**
         * 创建API
         */
        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("标题：若依管理系统_接口文档")
                                                .description("描述：用于管理集团旗下公司的人员信息,具体包括XXX,XXX模块...")
                                                .contact(new Contact()
                                                                .name(ruoyiName)
                                                                .email(""))
                                                .version("版本号:" + ruoyiVersion))
                                .addSecurityItem(new SecurityRequirement().addList("Authorization"))
                                .components(new Components()
                                                .addSecuritySchemes("Authorization", new SecurityScheme()
                                                                .type(SecurityScheme.Type.HTTP)
                                                                .scheme("bearer")
                                                                .bearerFormat("JWT")));
        }
}
