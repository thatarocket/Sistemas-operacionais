/**
 * EP 2 - Sistemas Operacionais (Turma 94)
 *
 * @author Thais de Souza Rodrigues, 11796941
 * @author Melissa Akie Inui, 11908865
 * @author Silas Bovolin Reis, 11796739
 * @author Gabriel de Oliveira Theodoro, 11202569
 */

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ObjectThread extends Thread {

	public void acessFiles(ArrayList<String> words,int posicBase){}
	public synchronized void lock(int threadsReads,ArrayList<String> words,int posicBase,Semaphore readSemaphore,Semaphore writeSemaphore) throws InterruptedException {}
	public synchronized void unlock(int threadsReads,Semaphore readSemaphore,Semaphore writeSemaphore) throws InterruptedException {}

	@Override
	public void run(){}
}
