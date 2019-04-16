package ioc;

import java.util.Map;

public interface PathScanner {

    Map<String, BeanDefinition> scanClassPathForCandidates(String... scanedPackage);
}
