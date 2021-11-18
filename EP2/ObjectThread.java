import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ObjectThread extends Thread {

	public void acessFiles(ArrayList<String> words,int posicBase){}
	public void lock(int threadsReads,Semaphore readSemaphore, Semaphore writeSemaphore) throws InterruptedException {}
	public void unlock(int threadsReads,Semaphore readSemaphore, Semaphore writeSemaphore) throws InterruptedException {}
	
	@Override
	public void run(){}
}
