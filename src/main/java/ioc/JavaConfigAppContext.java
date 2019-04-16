package ioc;


import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class JavaConfigAppContext implements BeanFactory {

    private PathScanner classPathScanner;
    private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();
    private Map<String, Object> singletonBeans = new HashMap<>();

    public JavaConfigAppContext() {
        this.classPathScanner = new ClassPathScanner();
    }

    @Override
    public void run(String... packages) throws Exception {
        createBeanDefinitions(packages);
        createSingletonBeans();
    }

    @Override
    public <T> T getBean(String beanName) {
        return (T) singletonBeans.get(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitions.get(beanName);
    }

    @Override
    public BeanDefinition[] getBeanDefinitionNames() {
        return beanDefinitions.entrySet().stream().map(Map.Entry::getValue).toArray(BeanDefinition[]::new);
    }

    @Override
    public void registerBean(String beanName, Class<?> annotatedClass) throws Exception {
        BeanDefinition beanDefinition = new BeanDefinition(beanName, annotatedClass);
        beanDefinitions.put(beanName, beanDefinition);
        Object bean = createBean(beanDefinition);
        singletonBeans.put(beanName, bean);
        injectIntoBean(bean);
    }

    private void createBeanDefinitions(String... packages) {
        beanDefinitions.putAll(classPathScanner.scanClassPathForCandidates(packages));
    }

    private void createSingletonBeans() throws Exception {
        prepareSingletonBeans();
        injectDependencyInSingletonBeans();
    }

    private void prepareSingletonBeans() throws Exception {
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitions.entrySet()) {
            singletonBeans.put(entry.getValue().getBeanName(), createBean(entry.getValue()));
        }
    }

    private Object createBean(BeanDefinition beanDefinition) throws Exception {
        return Optional.ofNullable(createBeanViaDefaultConstructor(beanDefinition)).orElse(createBeanViaParameterConstructor(beanDefinition));
    }

    private Object createBeanViaDefaultConstructor(BeanDefinition beanDefinition) {
        try {
            return beanDefinition.getClazz().getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            System.out.println("There is no default constructor");
        }
        return null;
    }

    private Object createBeanViaParameterConstructor(BeanDefinition beanDefinition) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor[] constructors = beanDefinition.getClazz().getConstructors();
        for (Constructor constructor : constructors) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                return constructor.newInstance(new Object[constructor.getParameterCount()]);
            }
        }
        return null;
    }


    private void injectDependencyInSingletonBeans() throws Exception {
        for (Map.Entry entry : singletonBeans.entrySet()) {
            injectIntoBean(entry.getValue());
        }
    }

    private void injectIntoBean(Object target) throws Exception {

        Constructor<?>[] constructors = target.getClass().getConstructors();

        Optional<Constructor<?>> constructor = Stream.of(constructors).filter(constr -> constr.isAnnotationPresent(Inject.class)).findFirst();

        Parameter[] parameters = constructor.map(Executable::getParameters).orElse(new Parameter[0]);

        for (Parameter parameter : parameters) {
            Class<?> type = parameter.getType();

            Object injectValue = getBeanByType(type).orElseThrow(() -> new Exception("Bean not found. Type: " + type.getName()));
            Field injectField = getFieldByType(target.getClass(), type).orElseThrow(() -> new Exception("Can not find such field by type: " + type.getName()));

            injectValueToBeanObject(target, injectValue, injectField.getName());
        }


    }

    private Optional<Object> getBeanByType(Class<?> clazz) {
        return singletonBeans.entrySet().stream().filter(entry -> clazz.isAssignableFrom(entry.getValue().getClass())).map(Map.Entry::getValue).findFirst();
    }

    private Optional<Field> getFieldByType(Class<?> targetClazz, Class<?> fieldType) {
        Field[] fields = targetClazz.getDeclaredFields();
        return Stream.of(fields).filter(field -> field.getType().isAssignableFrom(fieldType)).findFirst();
    }

    private void injectValueToBeanObject(Object targer, Object value, String fieldName) {
        try {
            Field declaredField = targer.getClass().getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            declaredField.set(targer, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
