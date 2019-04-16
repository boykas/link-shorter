package ioc;

public class BeanDefinition {

    private String beanName;

    private Class clazz;

    public BeanDefinition(String beanName, Class clazz) {

        this.beanName = beanName;
        this.clazz = clazz;
    }

    public String getBeanName() {
        return beanName;
    }

    public Class getClazz(){
        return clazz;
    }

}
