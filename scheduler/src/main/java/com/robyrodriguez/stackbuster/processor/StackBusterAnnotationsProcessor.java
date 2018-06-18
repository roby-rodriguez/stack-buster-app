package com.robyrodriguez.stackbuster.processor;

import com.robyrodriguez.stackbuster.annotation.StackBusterData;
import com.robyrodriguez.stackbuster.annotation.StackBusterData.StructureType;
import com.robyrodriguez.stackbuster.annotation.StackBusterData.ListenerLookup;
import com.robyrodriguez.stackbuster.annotation.StackBusterListener;
import com.robyrodriguez.stackbuster.annotation.StackBusterListener.ListenerType;
import com.robyrodriguez.stackbuster.utils.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

/**
 * Project specific annotations processor:
 *
 * - auto-wires listener beans from the '@StackBusterData' component/service
 */
@Component
public class StackBusterAnnotationsProcessor implements BeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(StackBusterAnnotationsProcessor.class);

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private AutowireCapableBeanFactory factory;

    @Override
    public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(StackBusterData.class)) {
            StructureType structureType = beanClass.getAnnotation(StackBusterData.class).structureType();
            ReflectionUtils.doWithFields(beanClass, field -> {
                if (!field.isAnnotationPresent(StackBusterListener.class) || factory.containsBean(field.getName())) return;

                ListenerType listenerType = field.getAnnotation(StackBusterListener.class).type();
                try {
                    ListenerLookup lookup = ListenerLookup.fromType(structureType, listenerType);
                    if (lookup == null) {
                        LOGGER.error("Fatal error: no listener implementation for types {} {}", structureType, listenerType);
                        SpringApplication.exit(appContext, () -> -1);
                        System.exit(-1);
                    }

                    Object listener = lookup.create();
                    factory.autowireBean(listener);
                    Object initialized = factory.initializeBean(listener, field.getName());
                    ReflectionUtil.updateField(field, bean, initialized);
                } catch (Exception e) {
                    LOGGER.error("Fatal error: could not wire listener bean {}", e);
                    SpringApplication.exit(appContext, () -> -1);
                    System.exit(-1);
                }
            });
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        return bean;
    }
}
