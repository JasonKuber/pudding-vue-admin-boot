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
import java.util.stream.Collectors;


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
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();

        handlerMethods.forEach((requestMappingInfo, handlerMethod) -> {
            // 获取所有路径模式（兼容新旧版本）
            Set<String> paths = getPaths(requestMappingInfo);

            // 处理类和方法上的注解
            processAnnotations(handlerMethod, paths);
        });
    }

    /**
     * 兼容 Spring Boot 2.7 的路径获取方式
     */
    private Set<String> getPaths(RequestMappingInfo requestMappingInfo) {
        // 先尝试新版本 PathPattern 方式（需要手动开启）
        if (requestMappingInfo.getPathPatternsCondition() != null) {
            return requestMappingInfo.getPathPatternsCondition().getPatterns()
                    .stream()
                    .map(pattern -> pattern.getPatternString())
                    .collect(Collectors.toSet());
        }
        // 回退到旧版 Ant 风格路径
        else if (requestMappingInfo.getPatternsCondition() != null) {
            return requestMappingInfo.getPatternsCondition().getPatterns();
        }
        return Collections.emptySet();
    }

    /**
     * 处理注解并添加路径
     */
    private void processAnnotations(HandlerMethod handlerMethod, Set<String> paths) {
        // 处理方法级注解
        NotAuthentication methodAnnotation = AnnotationUtils.findAnnotation(
                handlerMethod.getMethod(), NotAuthentication.class);
        if (methodAnnotation != null) {
            addProcessedUrls(paths);
        }

        // 处理类级注解
        NotAuthentication classAnnotation = AnnotationUtils.findAnnotation(
                handlerMethod.getBeanType(), NotAuthentication.class);
        if (classAnnotation != null) {
            addProcessedUrls(paths);
        }
    }

    /**
     * 处理路径变量替换
     */
    private void addProcessedUrls(Set<String> paths) {
        permitAllUrls.addAll(
                paths.stream()
                        .map(path -> path.replaceAll(PATTERN, ASTERISK))
                        .collect(Collectors.toList())
        );
    }


}
