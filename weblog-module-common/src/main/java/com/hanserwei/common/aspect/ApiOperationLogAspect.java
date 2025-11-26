package com.hanserwei.common.aspect;

import com.hanserwei.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class ApiOperationLogAspect {

    /**
     * 以自定义 @ApiOperationLog 注解为切点，凡是添加 @ApiOperationLog 的方法，都会执行环绕中的代码
     */
    @Pointcut("@annotation(com.hanserwei.common.aspect.ApiOperationLog)")
    public void apiOperationLog() {
    }

    /**
     * 环绕通知处理方法
     *
     * @param joinPoint 连接点对象，包含目标方法的信息
     * @return 目标方法执行结果
     * @throws Throwable 目标方法可能抛出的异常
     */
    @Around("apiOperationLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            // 记录请求开始时间
            long startTime = System.currentTimeMillis();

            // 设置追踪ID到MDC中，用于日志追踪
            MDC.put("traceId", UUID.randomUUID().toString());

            // 获取被请求的类名和方法名
            String className = joinPoint.getTarget().getClass().getSimpleName();
            String methodName = joinPoint.getSignature().getName();

            // 获取请求参数
            Object[] args = joinPoint.getArgs();
            // 将请求参数转换为JSON字符串格式
            String argsJsonStr = Arrays.stream(args).map(toJsonStr()).collect(Collectors.joining(", "));

            // 获取API操作日志注解中的描述信息
            String description = getApiOperationLogDescription(joinPoint);

            // 记录请求开始日志，包括功能描述、请求参数、类名和方法名
            log.info("====== 请求开始: [{}], 入参: {}, 请求类: {}, 请求方法: {} =================================== ",
                    description, argsJsonStr, className, methodName);

            // 执行目标方法
            Object result = joinPoint.proceed();

            // 计算方法执行耗时
            long executionTime = System.currentTimeMillis() - startTime;

            // 记录请求结束日志，包括功能描述、执行耗时和返回结果
            log.info("====== 请求结束: [{}], 耗时: {}ms, 出参: {} =================================== ",
                    description, executionTime, JsonUtils.toJsonString(result));

            return result;
        } finally {
            // 清理MDC中的追踪ID
            MDC.clear();
        }
    }

    /**
     * 获取API操作日志注解的描述信息
     *
     * @param joinPoint 连接点对象
     * @return 注解中的描述信息
     */
    private String getApiOperationLogDescription(ProceedingJoinPoint joinPoint) {
        // 1. 从 ProceedingJoinPoint 获取 MethodSignature
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // 2. 使用 MethodSignature 获取当前被注解的 Method
        Method method = signature.getMethod();

        // 3. 从 Method 中提取 ApiOperationLog 注解
        ApiOperationLog apiOperationLog = method.getAnnotation(ApiOperationLog.class);

        // 4. 从 ApiOperationLog 注解中获取 description 属性
        return apiOperationLog.description();
    }

    /**
     * 转换对象为JSON字符串的函数
     *
     * @return 转换函数
     */
    private Function<Object, String> toJsonStr() {
        return JsonUtils::toJsonString;
    }

}
