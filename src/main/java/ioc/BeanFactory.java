package ioc;

import org.springframework.beans.factory.config.BeanDefinition;

public interface BeanFactory {

	<T> T getBean(String beanName);

	BeanDefinition getBeanDefinition(String beanName);

	BeanDefinition[] getBeanDefinitionNames();

}
