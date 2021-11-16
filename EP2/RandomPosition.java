import java.util.concurrent.ThreadLocalRandom;

public class RandomPosition { 

	public int getRandom(int size) {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		return random.nextInt(0,size);
	}
}