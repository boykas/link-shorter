package ioc.crossDependencies;

import ioc.ApplicationBean;
import ioc.Inject;

@ApplicationBean
public class FirstComponentDependency {
    final SecondComponentDependency secondComponent;

    @Inject
    public FirstComponentDependency(SecondComponentDependency secondComponent) {
        this.secondComponent = secondComponent;
    }

    public SecondComponentDependency getSecondComponent() {
        return secondComponent;
    }
}
