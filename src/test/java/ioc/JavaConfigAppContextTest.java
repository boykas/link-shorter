package ioc;

import ioc.autoScanComponent.FirstComponent;
import ioc.autoScanComponent.SecondComponent;
import ioc.component.InjectComponent;
import ioc.component.InjectConstructor;
import ioc.crossDependencies.FirstComponentDependency;
import ioc.crossDependencies.SecondComponentDependency;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

@Ignore
public class JavaConfigAppContextTest {

    @Test
    public void createEmptyContext() {
        BeanFactory javaConfigAppContext = new JavaConfigAppContext();
        assertNotNull(javaConfigAppContext);
    }

    @Test
    public void beanDefinitionIsEmpty() {
        BeanFactory javaConfigAppContext = new JavaConfigAppContext();
        BeanDefinition[] beanDefinitionNames = javaConfigAppContext.getBeanDefinitionNames();
        assertEquals(0, beanDefinitionNames.length);
    }

    @Test
    public void autoCreatingBeans() throws Exception {
        //given
        BeanFactory beanFactory = new JavaConfigAppContext();

        //when
        beanFactory.run("ioc.component");
        BeanDefinition[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        FirstComponent firstComponent = beanFactory.getBean("firstComponent");
        SecondComponent secondComponent = beanFactory.getBean("secondComponent");

        //then
        assertEquals(2, beanDefinitionNames.length);
        assertNotNull(firstComponent);
        assertNotNull(secondComponent);
    }

    @Test
    public void registerBean() throws Exception {
        //given
        BeanFactory beanFactory = new JavaConfigAppContext();
        BeanDefinition[] beanDefinitionNamesBeforeRegister = beanFactory.getBeanDefinitionNames();
        FirstComponent firstComponentBeforeRegister = beanFactory.getBean("firstComponent");

        //when
        beanFactory.registerBean("firstComponent", FirstComponent.class);

        //then
        FirstComponent firstComponentAfterRegister = beanFactory.getBean("firstComponent");
        BeanDefinition[] beanDefinitionNamesAfterRegister = beanFactory.getBeanDefinitionNames();
        assertNull(firstComponentBeforeRegister);
        assertNotNull(firstComponentAfterRegister);
        assertEquals(0, beanDefinitionNamesBeforeRegister.length);
        assertEquals(1, beanDefinitionNamesAfterRegister.length);

    }


    @Test
    public void beanDefinition() throws Exception {
        //given
        BeanFactory beanFactory = new JavaConfigAppContext();
        beanFactory.run("ioc.component");

        //when
        BeanDefinition stringBeanDefinition = beanFactory.getBeanDefinition("firstComponent");

        //then
        assertNotNull(stringBeanDefinition);
    }

    @Test
    public void injectFieldViaAnnotation() throws Exception {
        //given
        BeanFactory beanFactory = new JavaConfigAppContext();
        beanFactory.registerBean("injectComponent", InjectComponent.class);

        //when
        beanFactory.registerBean("injectConstructor", InjectConstructor.class);
        InjectConstructor injectConstructor = beanFactory.getBean("injectConstructor");
        InjectComponent injectComponent = beanFactory.getBean("injectComponent");

        //then
        assertSame(injectComponent, injectConstructor.getInjectComponent());
    }


    @Test
    public void singletonBeansReturnsSameObject() throws Exception {
        //given
        BeanFactory beanFactory = new JavaConfigAppContext();
        beanFactory.registerBean("injectComponent", InjectComponent.class);

        //when
        InjectComponent injectComponent = beanFactory.getBean("injectComponent");
        InjectComponent injectComponent2 = beanFactory.getBean("injectComponent");

        //then
        assertEquals(injectComponent, injectComponent2);
    }

    @Test
    public void crossDependencies() throws Exception {
        //given
        BeanFactory beanFactory = new JavaConfigAppContext();

        //when
        beanFactory.run("ioc.crossDependencies");
        FirstComponentDependency firstComponent = beanFactory.getBean("firstComponentDependency");
        SecondComponentDependency secondComponent = beanFactory.getBean("secondComponentDependency");

        //then
        assertEquals(firstComponent.getSecondComponent(), secondComponent);
        assertEquals(secondComponent.getFirstComponent(), firstComponent);
    }


}