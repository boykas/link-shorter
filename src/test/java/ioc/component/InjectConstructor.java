package ioc.component;

import ioc.ApplicationBean;
import ioc.Inject;

@ApplicationBean
public class InjectConstructor {
    private InjectComponent injectComponent;

    @Inject
    public InjectConstructor(InjectComponent injectComponent) {
        this.injectComponent = injectComponent;
    }

    public InjectComponent getInjectComponent() {
        return injectComponent;
    }
}
