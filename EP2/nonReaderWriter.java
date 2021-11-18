/**
 * EP 2 - Sistemas Operacionais (Turma 94)
 *
 * @author Thais de Souza Rodrigues, 11796941
 * @author Melissa Akie Inui, 11908865
 * @author Silas Bovolin Reis, 11796739
 * @author Gabriel de Oliveira Theodoro, 11202569
 */

import java.util.concurrent.Semaphore;

public class nonReaderWriter {

	protected static Semaphore semaphoreAll = new Semaphore(1);

	public void lock() throws InterruptedException {
        semaphoreAll.acquire();
    }

    public void unlock(){
        semaphoreAll.release();
    }
}
