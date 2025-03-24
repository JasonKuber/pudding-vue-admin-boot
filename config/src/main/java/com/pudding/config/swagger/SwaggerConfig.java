package com.pudding.config.swagger;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger 配置
 */
@Configuration
@EnableOpenApi
@EnableKnife4j
public class SwaggerConfig {

    @Bean
    public Docket docket(Environment environment) {
        // 设置要显示的swagger环境信息，判断是否处于自己设定的环境中，为了安全生产环境不开放Swagger
        Profiles profiles = Profiles.of("dev", "test");
        boolean enableSwagger = environment.acceptsProfiles(profiles);
        return new Docket(DocumentationType.OAS_30) // 指定文档类型
                .apiInfo(apiInfo()) // 配置文档信息
                .enable(enableSwagger) // 只有当SpringBoot配置在dev或test环境时，才开启SwaggerApi文档功能
                .select() // 选择需要生成文档的路径和接口
                .apis(RequestHandlerSelectors.basePackage("com.pudding.api.controller")) // 扫描指定包下的 API 控制器
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)) // 扫描包含注解的方式来确定要显示的接口
                .paths(PathSelectors.any()) // 配置过滤哪些，设置对应的路径才获取
                .build()
                .globalRequestParameters(authorizationHeader());
    }

    /**
     * 文档信息
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Pudding Vue Admin Boot Api文档")
                .description("后端项目API文档")
                .contact(new Contact("JasonKuber","https://github.com/JasonKuber","jasonkuberlove@gmail.com"))
                .version("1.0")
                .build();
    }

    /**
     * 配置一个全局的请求头，用于所有 API 请求（例如认证）
     *
     * @return Parameter 对象，表示全局的请求头
     */
    private List<RequestParameter> authorizationHeader() {

        List<RequestParameter> parameterList = new ArrayList<>();
        RequestParameterBuilder parameterBuilder = new RequestParameterBuilder();
        parameterBuilder.name("Authorization")
                .description("Bearer <token>")
                .in(ParameterType.HEADER)
                .required(true)
                .build();
        parameterList.add(parameterBuilder.build());

        return parameterList;
    }
}
