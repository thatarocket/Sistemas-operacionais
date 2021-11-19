/**
 * EP 2 - Sistemas Operacionais (Turma 94)
 *
 * @author Thais de Souza Rodrigues, 11796941
 * @author Melissa Akie Inui, 11908865
 * @author Silas Bovolin Reis, 11796739
 * @author Gabriel de Oliveira Theodoro, 11202569
 */

import java.util.concurrent.ThreadLocalRandom;

public class RandomPosition {

	public int getRandom(int size) {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		return random.nextInt(0,size);
	}
}
