package com.ge;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

@Any
@ApplicationScoped
public class BeanExplorer {
    @Inject BeanManager beanManager;
    private static final Logger logger = LoggerFactory.getLogger(BeanExplorer.class);
    public void getbeans(BeanManager beanManager){
        final String SPACE = " ";
        final AtomicInteger counter = new AtomicInteger();
        final Set<Bean<?>> beans = beanManager.getBeans(Object.class, new AnnotationLiteral<Any>() {});
        for (final Bean<?> bean : beans) {
            if (bean.getBeanClass().getName().contains("com.ge")) {
                logger.info(counter.getAndIncrement() + SPACE + bean.getBeanClass().getName());
            }
        }
    }
}
