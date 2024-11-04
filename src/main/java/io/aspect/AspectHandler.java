package io.aspect;


import io.annotations.AuditAction;
import io.annotations.Loggable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AspectHandler implements InvocationHandler {

    private final Object target;

    public AspectHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Аудит действия
        if (method.isAnnotationPresent(AuditAction.class)) {
            AuditAction auditAction = method.getAnnotation(AuditAction.class);
            System.out.println("Auditing action: " + auditAction.value());
        }

        // Логирование времени выполнения
        if (method.isAnnotationPresent(Loggable.class)) {
            long start = System.currentTimeMillis();
            Object result = method.invoke(target, args);
            long end = System.currentTimeMillis();
            System.out.println("Execution time of " + method.getName() + ": " + (end - start) + " ms");
            return result;
        }

        return method.invoke(target, args);
    }

    public static <T> T createProxy(T target) {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new AspectHandler(target)
        );
    }
}