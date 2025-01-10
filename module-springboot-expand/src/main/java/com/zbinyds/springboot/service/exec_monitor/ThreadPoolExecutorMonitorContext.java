package com.zbinyds.springboot.service.exec_monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.boot.context.properties.bind.handler.IgnoreErrorsBindHandler;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 * @Author zbinyds
 * @Create 2024-09-18 08:59
 */

@Service
public class ThreadPoolExecutorMonitorContext implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {
    private static final Logger log = LoggerFactory.getLogger(ThreadPoolExecutorMonitorContext.class);
    private ThreadPoolProperties threadPoolProperties;
    private ConfigurableListableBeanFactory configurableListableBeanFactory;

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        threadPoolProperties = bindConfigProps((ConfigurableEnvironment) environment);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        Optional.ofNullable(threadPoolProperties.getExecutors())
                .ifPresent(executorList -> {
                    executorList.forEach(instance -> {
                        // 注册为bean
                        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder
                                .genericBeanDefinition(ThreadPoolExecutorMonitor.class, instance::toThreadPoolExecutorMonitor)
                                .getBeanDefinition();
                        registry.registerBeanDefinition(instance.getExecutorName(), beanDefinition);
                        log.debug("executor monitor [{}] register.", instance.getExecutorName());
                    });
                    log.debug("executor monitor register success.");
                });
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.configurableListableBeanFactory = beanFactory;
    }

    public ThreadPoolExecutorMonitor getExecutorMonitor(String executorName) {
        return configurableListableBeanFactory.getBean(executorName, ThreadPoolExecutorMonitor.class);
    }

    /*
    // 根据 genericWebApplicationContext 注册bean实例
    private ThreadPoolProperties threadPoolProperties;
    private GenericWebApplicationContext genericWebApplicationContext;

    @Autowired
    public ThreadPoolExecutorManager(final ThreadPoolProperties threadPoolProperties, final GenericWebApplicationContext genericWebApplicationContext) {
        this.threadPoolProperties = threadPoolProperties;
        this.genericWebApplicationContext = genericWebApplicationContext;
    }
    @Override
    public void afterPropertiesSet() {
        Optional.ofNullable(threadPoolProperties.getExecutors())
                .ifPresent(executorList -> {
                    executorList.forEach(instance -> {
                        // 注册为bean
                        genericWebApplicationContext.registerBean(instance.getExecutorName(), ThreadPoolExecutorMonitor.class,
                                instance::toThreadPoolExecutorMonitor);
                        log.debug("executor monitor [{}] register.", instance.getExecutorName());
                    });
                    log.debug("executor monitor register success.");
                });
    }
    public ThreadPoolExecutorMonitor getExecutorMonitor(String executorName) {
        return genericWebApplicationContext.getBean(executorName, ThreadPoolExecutorMonitor.class);
    }*/

    public static ThreadPoolProperties bindConfigProps(ConfigurableEnvironment environment) {
        final BindHandler handler = new IgnoreErrorsBindHandler(BindHandler.DEFAULT);
        final MutablePropertySources propertySources = environment.getPropertySources();
        final Binder binder = new Binder(ConfigurationPropertySources.from(propertySources),
                new PropertySourcesPlaceholdersResolver(propertySources),
                ApplicationConversionService.getSharedInstance());
        final ThreadPoolProperties config = new ThreadPoolProperties();

        final ResolvableType type = ResolvableType.forClass(ThreadPoolProperties.class);
        final Annotation annotation = AnnotationUtils.findAnnotation(ThreadPoolProperties.class,
                ConfigurationProperties.class);
        final Annotation[] annotations = new Annotation[]{annotation};
        final Bindable<?> target = Bindable.of(type).withExistingValue(config).withAnnotations(annotations);

        binder.bind("thread-pool", target, handler);
        return config;
    }
}
