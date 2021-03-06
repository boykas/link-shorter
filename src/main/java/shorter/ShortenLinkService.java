package shorter;

import java.util.Optional;

public interface ShortenLinkService {

	Link shortLink(Link fullLink);

	Optional<Link> fullLink(Link shortLink);

}
