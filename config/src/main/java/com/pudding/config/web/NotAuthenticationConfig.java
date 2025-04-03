package com.pudding.config.web;

import com.pudding.common.annotation.NotAuthentication;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

/**
 * 通过自定义注解@NotAuthentication，然后通过实现InitializingBean接口，
 * 实现加载不需要认证的资源，支持类和方法，使用就是通过在方法或者类打上对应的注解
 */
@Service
public class NotAuthenticationConfig implements InitializingBean, ApplicationContextAware {


    private static final String PATTERN = "\\{(.*?)}";

    public static final String ASTERISK = "*";

    private ApplicationContext applicationContext;

    @Getter
    @Setter
    private List<String> permitAllUrls = new ArrayList<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        map.keySet().forEach(x -> {
            HandlerMethod handlerMethod = map.get(x);

            // 获取方法上边的注解 替代path variable 为 *
            NotAuthentication method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), NotAuthentication.class);
            Optional.ofNullable(method).ifPresent(inner -> Objects.requireNonNull(x.getPathPatternsCondition())
                    .getPatternValues().forEach(url -> permitAllUrls.add(url.replaceAll(PATTERN, ASTERISK))));

            // 获取类上边的注解, 替代path variable 为 *
            NotAuthentication controller = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), NotAuthentication.class);
            Optional.ofNullable(controller).ifPresent(inner -> Objects.requireNonNull(x.getPathPatternsCondition())
                    .getPatternValues().forEach(url -> permitAllUrls.add(url.replaceAll(PATTERN, ASTERISK))));
        });
    }


}
