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

/**
 * Acessa a base para a leitura
 */
public class ReaderN extends ObjectThread {

   protected static String currentWord; 
   protected static RandomPosition random;
   protected int posicBase;
   protected static ArrayList<String> wordsN; 
   protected static int numAcess;   
   protected static Semaphore readSemaphore;
   protected static Semaphore writeSemaphore; 

   public ReaderN(ArrayList<String> wordsN,int numAcess,Semaphore readSemaphore,Semaphore writeSemaphore) {
      this.wordsN = wordsN;
      this.numAcess = numAcess;
      this.readSemaphore = readSemaphore;
      this.writeSemaphore = writeSemaphore;
   }

   /**
    * Acessa os arquivos, lê determinada posicao da base
    * @param int posicBase
    * @return void
    */
	public void acessFiles(int posicBase) {
		this.currentWord = wordsN.get(posicBase);
	}

   /**
    * Trava o acesso a base para todos 
    * @param int posicBase
    * @throws InterruptedException
    * @return void
    */
	public void lock(int posicBase) throws InterruptedException {
		readSemaphore.acquire();
      writeSemaphore.acquire();
      acessFiles(posicBase);
	}

   /**
    * Desbloqueia o acesso a base
    * @throws InterruptedException
    * @return void
    */
	public void unlock() throws InterruptedException {
		readSemaphore.release();
      writeSemaphore.release();
	}

	@Override
	public void run() {
      try {
         random = new RandomPosition();
         for(int k=0; k<numAcess;k++) {
           posicBase = random.getRandom(wordsN.size());
           this.lock(posicBase);
           this.unlock();
           this.sleep(1);
         }
      }
      catch(InterruptedException e) {}
	}

}
