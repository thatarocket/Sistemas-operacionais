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

public class Writer extends ObjectThread {
//Escrevem na base

   /**
    * Acessa os arquivos, modifica determina posicao da base
    * @param ArrayList<String> words
    * @param int posicBase
    * @return void
    */
	public void acessFiles(ArrayList<String> words,int posicBase) {
		words.set(posicBase,"MODIFICADO");
	}

   /**
   	* Trava o acesso a base para todos os acessos
   	* @param int threadsReads
   	* @param ArrayList<String> words
   	* @param int posicBase
   	* @param Semaphore readSemaphore
   	* @param Semaphore writeSemaphore
   	* @throws InterruptedException
   	* @return void
   	*/
	public synchronized void lock(int threadsReads,ArrayList<String> words,int posicBase,Semaphore readSemaphore,Semaphore writeSemaphore) throws InterruptedException {
		writeSemaphore.acquire();
	}

   /**
    * Desbloqueia o acesso a base
    * @param int threadsReads
    * @param Semaphore readSemaphore
    * @param Semaphore writeSemaphore
    * @throws InterruptedException
    * @return void
    */
	public synchronized void unlock(int threadsReads,Semaphore readSemaphore,Semaphore writeSemaphore) throws InterruptedException {
		writeSemaphore.release();
	}

	@Override
	public void run() {

	}

}
