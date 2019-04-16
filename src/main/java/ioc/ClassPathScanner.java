package ioc;

import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassPathScanner implements PathScanner {

    public Map<String, BeanDefinition> scanClassPathForCandidates(String... scanedPackage) {
        return Stream.of(scanedPackage)
                .flatMap(el -> scanPackage(el).entrySet()
                        .stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<String, BeanDefinition> scanPackage(String scanedPackage) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String adoptPackageName = scanedPackage.replace(".", "/");
        List<File> files = obtainFiles(classLoader, adoptPackageName);
        return files
                .stream()
                .flatMap(el -> createDefinition(el, scanedPackage).stream())
                .collect(Collectors.toMap(BeanDefinition::getBeanName, el -> el));
    }


    private List<File> obtainFiles(ClassLoader classLoader, String packageName) {
        List<File> files = new ArrayList<>();
        try {
            Enumeration<URL> resources = classLoader.getResources(packageName);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                files.add(new File(url.getFile()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    private Set<BeanDefinition> createDefinition(File file, String packageName) {
        Set<BeanDefinition> definitions = new HashSet<>();
        File[] files = file.listFiles();
        if (files == null) {
            return definitions;
        }
        for (File f : files) {
            if (f.isDirectory()) {
                definitions.addAll(Objects.requireNonNull(createDefinition(f, packageName + "." + f.getName())));
            } else if (f.getName().endsWith(".class")) {
                try {
                    Class<?> aClass = Class.forName(packageName + '.' + f.getName().replace(".class", ""));
                    if (!aClass.isInterface() && aClass.isAnnotationPresent(ApplicationBean.class)) {
                        definitions.add(new BeanDefinition(StringUtils.uncapitalize(aClass.getName().replace(packageName + ".", "")), aClass));
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        return definitions;
    }


}
