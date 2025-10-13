package com.hxw.wscrm.common.swagger;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger配置
 *
 * @author lgy
 */
@Slf4j
@Configuration
@EnableOpenApi
public class SwaggerConfiguration {

    @Value("${spring.application.name}")
    private String applicationName;


    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()//RequestHandlerSelectors 配置要扫描接口的方式 //basePackage指定扫描包 any()扫描全部 none（）都不扫描 withClassAnnotation（）扫描类上的注解
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                //paths() 过滤什么路径
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        //作者信息
        Contact xiaohe = new Contact("xiaohe", "", "xiaohe@gmail.com");
        return new ApiInfoBuilder()
                .title(applicationName)
                .description(applicationName + "接口文档。")
                .contact(xiaohe)
                .version("1.0")
                .build();
    }

    @Bean
    public EnumPropertyBuilderPlugin enumPropertyBuilderPlugin() {
        return new EnumPropertyBuilderPlugin();
    }


    @Bean
    public ValidatePropertyBuilderPlugin validatePropertyBuilderPlugin() {
        return new ValidatePropertyBuilderPlugin();
    }

    @Bean
    public SwaggerConsole swaggerConsole() {
        return new SwaggerConsole();
    }
}
