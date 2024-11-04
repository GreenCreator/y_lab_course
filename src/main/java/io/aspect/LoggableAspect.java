package io.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LoggableAspect {

    @Pointcut("within(@io.annotations.Loggable *) && execution(* * (..))")
    public void annotatedByLoggable() {
    }

    @Around("execution(* ylab.impl.*.*(..))")
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Calling logging method " + joinPoint.getSignature().getName());
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Calling logging method " + joinPoint.getSignature() + " finished in " + elapsedTime + " ms");
        return result;
    }
}
