package ioc.crossDependencies;

import ioc.ApplicationBean;
import ioc.Inject;

@ApplicationBean
public class SecondComponentDependency {
    final FirstComponentDependency firstComponent;

    @Inject
    public SecondComponentDependency(FirstComponentDependency firstComponent) {
        this.firstComponent = firstComponent;
    }

    public FirstComponentDependency getFirstComponent() {
        return firstComponent;
    }
}
