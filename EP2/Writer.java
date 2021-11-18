import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Writer extends ObjectThread {
//Escrevem na base

	public void acessFiles(ArrayList<String> words,int posicBase) {
		words.set(posicBase,"MODIFICADO");
	}

	public void lock(int threadsReads,Semaphore readSemaphore, Semaphore writeSemaphore) throws InterruptedException {
		writeSemaphore.acquire();
	}
	public void unlock(int threadsReads,Semaphore readSemaphore, Semaphore writeSemaphore) throws InterruptedException {
		writeSemaphore.release();
	}

	@Override
	public void run() {


	}

}
