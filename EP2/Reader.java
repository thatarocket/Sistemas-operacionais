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
public class Reader extends ObjectThread {

  protected static String currentWord; 
  protected static RandomPosition random;
  protected int posicBase;
  protected static ArrayList<String> words; 
  protected static int numAcess;   
  protected static Semaphore readSemaphore;
  protected static Semaphore writeSemaphore; 
  protected static int threadsReads = 0; 

  public Reader(ArrayList<String> words,int numAcess,Semaphore readSemaphore,Semaphore writeSemaphore) {
    this.words = words;
    this.numAcess = numAcess;
    this.readSemaphore = readSemaphore;
    this.writeSemaphore = writeSemaphore;
  }

  /**
   * Acessa os arquivos, lê a posicao da base e a armazena
   * @param int posicBase
   * @return void
   */
  public void acessFiles(int posicBase) {
    this.currentWord = words.get(posicBase);
  }

  /**
   * Trava o acesso a base do escritor, para o leitor acessar
   * @param int posicBase
   * @throws InterruptedException
   * @return void
   */
  public synchronized void lock(int posicBase) throws InterruptedException {
    readSemaphore.acquire();
    if(threadsReads == 0){
      writeSemaphore.acquire();
    }
    acessFiles(posicBase);
    threadsReads++;
    readSemaphore.release();
  }

  /**
   * Desbloqueia o acesso a base
   * @throws InterruptedException
   * @return void
   */
  public synchronized void unlock() throws InterruptedException {
    readSemaphore.acquire();
    threadsReads--;
    if(threadsReads == 0){
      writeSemaphore.release();
    }
    readSemaphore.release();
  }

  @Override
  public void run() {
    try {
      random = new RandomPosition();
      for(int k=0; k<numAcess;k++) {
        posicBase = random.getRandom(words.size());
        this.lock(posicBase);
        this.unlock();
      }
      this.sleep(1);
    }
    catch(InterruptedException e) {}

  }

}
