package com.ge;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;

@Startup
@ApplicationScoped
public class App {
    @Inject VertxBean vertx;
    @Inject BeanManager beanManager;
    void onStart(@Observes StartupEvent ev) {
        vertx.startVertx();
        final String SPACE = " ";
        final AtomicInteger counter = new AtomicInteger();
        final Set<Bean<?>> beans = beanManager.getBeans(Object.class, new AnnotationLiteral<Any>() {});
        for (final Bean<?> bean : beans) {
            System.out.println(counter.getAndIncrement() + SPACE + bean.getBeanClass().getName());
        }
        System.out.println("The application has started");
    }
    void onStop(@Observes ShutdownEvent ev) {
        vertx.stopVertx();
        System.out.println("The application is stopping...");
    }
}