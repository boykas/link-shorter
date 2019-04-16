package shorter;

import ioc.BeanFactory;
import ioc.JavaConfigAppContext;
import shorter.model.Link;
import shorter.service.ShortenLinkService;

import java.util.Optional;

import static shorter.model.Link.linkTo;

public class ShrorterApp {

    public static void main(String[] args) throws Exception {
        String url = "https://www.facebook.com/groups/KyivKUG/";

//        ShortLinksRepo inMemShortLinksRepo = new InMemShortLinksRepo();
//        ShorterService identShorterService = new IdentShorterService();
//
//        ShortenLinkService shortenLinkService = new DefaultShortenLinkService(inMemShortLinksRepo, identShorterService);

        BeanFactory beanFactory = new JavaConfigAppContext();
        beanFactory.run("shorter");
        ShortenLinkService shortenLinkService = beanFactory.getBean("defaultShortenLinkService");

        Link shortLink = shortenLinkService.shortLink(linkTo(url));
        System.out.println("Short link: " + shortLink.link());

        Optional<Link> fullLink = shortenLinkService.fullLink(shortLink);
        System.out.println("Full link: " + fullLink.get().link());

    }

}
