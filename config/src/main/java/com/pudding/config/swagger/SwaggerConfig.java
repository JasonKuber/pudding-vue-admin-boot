package com.pudding.config.swagger;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                .globalRequestParameters(getGlobalRequestParameters())
                .enable(enableSwagger) // 只有当SpringBoot配置在dev或test环境时，才开启SwaggerApi文档功能
                .select() // 选择需要生成文档的路径和接口
                .apis(RequestHandlerSelectors.basePackage("com.pudding.api.admin.controller")) // 扫描指定包下的 API 控制器
//                .apis(RequestHandlerSelectors.withMethodAnnotation(Api.class)) // 扫描包含注解的方式来确定要显示的接口
                .paths(PathSelectors.any()) // 配置过滤哪些，设置对应的路径才获取
                .build();
    }

    /**
     * 文档信息
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Pudding Vue Admin Boot Api文档")
                .description("后端项目API文档")
                .contact(new Contact("JasonKuber", "https://github.com/JasonKuber", "jasonkuberlove@gmail.com"))
                .version("1.0")
                .build();
    }

    @Bean
    public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
                    customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
                }
                return bean;
            }

            private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
                List<T> copy = mappings.stream()
                        .filter(mapping -> mapping.getPatternParser() == null)
                        .collect(Collectors.toList());
                mappings.clear();
                mappings.addAll(copy);
            }

            @SuppressWarnings("unchecked")
            private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
                try {
                    Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
                    field.setAccessible(true);
                    return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
    }

    /**
     * 配置一个全局的请求头，用于所有 API 请求（例如认证）
     *
     * @return Parameter 对象，表示全局的请求头
     */
    private List<RequestParameter> getGlobalRequestParameters() {

        List<RequestParameter> parameterList = new ArrayList<>();
        parameterList.add(new RequestParameterBuilder()
                .name("Authorization")
                .description("Bearer <token>")
                .in(ParameterType.HEADER)
                .query(param -> param.model(model -> model.scalarModel(ScalarType.STRING)))
                .required(true)
                .build());
        parameterList.add(new RequestParameterBuilder()
                .name("login-type")
                .description("登录方式 password/code")
                .in(ParameterType.HEADER)
                .query(param -> param.model(model -> model.scalarModel(ScalarType.STRING)))
                .required(true)
                .build());
        return parameterList;
    }
}
