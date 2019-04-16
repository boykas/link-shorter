package ioc;


public interface BeanFactory {

    <T> T getBean(String beanName);

    BeanDefinition getBeanDefinition(String beanName);

    BeanDefinition[] getBeanDefinitionNames();

    void run(String... packages) throws Exception;

    void registerBean(String beanName, Class<?> annotatedClass) throws Exception;

}
