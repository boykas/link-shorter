package ioc;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringApp {

	public static void main(String[] args) {

			AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
			ctx.getBean("beanName");
			ctx.getBeanDefinition("beanName");
			ctx.getBeanDefinitionNames();


	}

}
