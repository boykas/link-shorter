package ioc;

public class SpringApp {

    public static void main(String[] args) throws Exception {
        JavaConfigAppContext javaConfigAppContext = new JavaConfigAppContext();
        javaConfigAppContext.run("shorter");
    }
}
