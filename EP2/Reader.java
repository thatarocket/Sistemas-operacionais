import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Reader extends ObjectThread {
//Acessa a base para a leitura

	private static String currentWord;
	protected static ReadWriteLock dataBase;

	public void acessFiles(ArrayList<String> words,int posicBase) {
		this.currentWord = words.get(posicBase);
	}

	public void lock(int threadsReads,Semaphore readSemaphore, Semaphore writeSemaphore) throws InterruptedException  {
		readSemaphore.acquire();
		if(threadsReads == 0){
			writeSemaphore.acquire();
		}
		threadsReads++;
		readSemaphore.release();
	}
	public void unlock(int threadsReads,Semaphore readSemaphore, Semaphore writeSemaphore) throws InterruptedException {
		readSemaphore.acquire();
        threadsReads--;
        if(threadsReads == 0){
          writeSemaphore.release();
        }
        readSemaphore.release();
	}

	@Override
	public void run() {
		

	}

}
