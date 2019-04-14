package shorter;

public class IdentShorterService implements ShorterService {

	@Override
	public String shorten(String path) {
		return path;
	}
}
