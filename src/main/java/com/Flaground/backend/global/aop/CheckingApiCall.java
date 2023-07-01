package com.Flaground.backend.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class CheckingApiCall {
    @Pointcut("execution(public * com.Flaground.backend.module..*Controller.*(..))")
    public void moduleControllerPointcut() { }

    @Around("moduleControllerPointcut()")
    public void checkApiCallAndTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long proceedTime = calculateProceedTime(joinPoint);
        String className = getTargetClassName(joinPoint);
        String methodName = getTargetMethodName(joinPoint);
        log.info("{}.{} Called. Procced Time : {} millisecond", className, methodName, proceedTime);
    }

    private String getTargetMethodName(ProceedingJoinPoint joinPoint) {
        return joinPoint.getSignature().getName();
    }

    private String getTargetClassName(ProceedingJoinPoint joinPoint) {
        return joinPoint.getTarget().getClass().getName();
    }

    private long calculateProceedTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        joinPoint.proceed();
        stopWatch.stop();

        return stopWatch.getTotalTimeMillis();
    }
}
