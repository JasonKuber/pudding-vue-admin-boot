package com.pudding.config.web;

import cn.hutool.core.util.IdUtil;
import com.pudding.common.utils.http.HeaderUtils;
import com.pudding.common.utils.http.IpUtils;
import com.pudding.common.utils.http.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP 日志AOP
 */
@Slf4j
@Aspect
@Component
public class RequestLogAspect {

    private static final int MAX_RESPONSE_LENGTH = 3000;

    @Pointcut("execution(* com.pudding..controller..*(..))")
    public void pointCut() {
    }


    @Around("pointCut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        HttpServletRequest request = RequestUtils.getRequest();

        if (request == null) {
            return joinPoint.proceed();
        }

        Map<String,String> logParamMap = new HashMap<>();
        logParamMap.put("traceId",IdUtil.fastSimpleUUID());
        logParamMap.put("requestURL",RequestUtils.getRequestURL(request));
        logParamMap.put("requestMethode",RequestUtils.getRequestMethode(request));
        logParamMap.put("ipAddress",IpUtils.getIpAddress(request));
        logParamMap.put("userAgent",HeaderUtils.getUserAgent(request));
        String headers = getHeaders(request);
        Object[] args = joinPoint.getArgs();
        logParamMap.put("parameters",Arrays.toString(args));

        Object result = null;
        Exception ex = null;

        try {
            result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            ex = e;
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logInfo(logParamMap,
                    headers,
                    result,
                    ex,
                    duration);
        }

    }


    private void logInfo(Map<String,String> param,String headers, Object result, Exception exception, long duration) {
        boolean isError = exception != null;

        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append("\n");
        logBuilder.append("\u250F").append(repeat("\u2501", 20)).append(" [HTTP LOG] ").append(repeat("\u2501", 20)).append("\n");
        logBuilder.append("\u2503 ▶ Trace ID   : ").append(param.get("traceId")).append("\n");
        logBuilder.append("\u2503 ▶ URL        : ").append(param.get("requestURL")).append("\n");
        logBuilder.append("\u2503 ▶ IP         : ").append(param.get("ipAddress")).append("\n");
        logBuilder.append("\u2503 ▶ Device     : ").append(param.get("userAgent")).append("\n");
        logBuilder.append("\u2503 ▶ Method     : ").append(param.get("requestMethode")).append("\n");
        logBuilder.append("\u2503 ▶ Headers    : ").append(headers).append("\n");
        logBuilder.append("\u2503 ▶ Parameters : ").append(param.get("parameters")).append("\n");

        if (!isError) {
            logBuilder.append("\u2503 \u001B[32m✔ Status     : 200 OK\u001B[0m\n");
            logBuilder.append("\u2503 \u001B[32m✔ Time       : ").append(duration).append(" ms\u001B[0m\n");
            logBuilder.append("\u2503 \u001B[32m✔ Result     : ").append(formatResult(result)).append("\u001B[0m\n");
        } else {
            logBuilder.append("\u2503 \u001B[31m✖ Status     : 500 Internal Server Error\u001B[0m\n");
            logBuilder.append("\u2503 \u001B[31m✖ Time       : ").append(duration).append(" ms\u001B[0m\n");

            // 记录异常堆栈信息
            logBuilder.append("\u2503 \u001B[31m✖ Exception  : ").append(exception.getClass().getSimpleName()).append("\u001B[0m\n");

            logBuilder.append("\u2503 \u001B[31m✖ Message    : ").append(exception.getMessage() != null ? exception.getMessage() : "No message").append("\u001B[0m\n");
            logBuilder.append("\u2503 \u001B[31m✖ Stacktrace : ").append(getStackTrace(exception)).append("\u001B[0m\n");
        }

        logBuilder.append("\u2517").append(repeat("\u2501", 50)).append("\n");
        log.info(logBuilder.toString());
    }

    private String formatResult(Object result) {
        if (result == null) {
            return "null";
        }
        try {
            String resultString = result.toString();
            if (resultString.length() > MAX_RESPONSE_LENGTH) {
                return resultString.substring(0, MAX_RESPONSE_LENGTH) + "... (truncated)";
            }
            return resultString;
        } catch (Exception e) {
            return "[Failed to serialize result: " + e.getClass().getSimpleName() + "]";
        }
    }


    private String getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames != null && headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            // 排除Authorization、Token、refreshToken等敏感信息
            if ("authorization".equalsIgnoreCase(key) || "token".equalsIgnoreCase(key) || "cookie".equalsIgnoreCase(key)) {
                continue;
            }
            String value = request.getHeader(key);
            headers.put(key, value);
        }
        return headers.toString();
    }

    /**
     * 打印异常堆栈信息
     */
    private String getStackTrace(Throwable exception) {
        // 限制堆栈信息的长度或只取前几个关键的堆栈信息
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        String stackTrace = sw.toString();

        // 限制堆栈信息的输出行数，避免堆栈信息过长
        int maxStackLines = 3; // 控制堆栈输出行数
        String[] stackTraceLines = stackTrace.split("\n");
        StringBuilder shortStackTrace = new StringBuilder();
        for (int i = 0; i < Math.min(maxStackLines, stackTraceLines.length); i++) {
            shortStackTrace.append(stackTraceLines[i]).append("\n");
        }
        return shortStackTrace.toString();
    }


    private String repeat(String str, int count) {
        if (str == null || count <= 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder(str.length() * count);
        for (int i = 0; i < count; i++) {
            builder.append(str);
        }
        return builder.toString();
    }


}
