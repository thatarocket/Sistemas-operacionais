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

  /**
   * Acessa os arquivos, lê a posicao da base e a armazena
   * @param ArrayList<String> words
   * @param int posicBase
   * @return void
   */
	public void acessFiles(ArrayList<String> words,int posicBase) {
		this.currentWord = words.get(posicBase);
	}

  /**
   * Trava o acesso a base do escritor, para o leitor acessar
   * @param int threadsReads
   * @param ArrayList<String> words
   * @param int posicBase
   * @param Semaphore readSemaphore
   * @param Semaphore writeSemaphore
   * @throws InterruptedException
   * @return void
   */
  public synchronized void lock(int threadsReads,ArrayList<String> words,int posicBase,Semaphore readSemaphore,Semaphore writeSemaphore) throws InterruptedException {
    readSemaphore.acquire();
    if(threadsReads == 0){
      writeSemaphore.acquire();
    }
    threadsReads++;
    acessFiles(words,posicBase);
    readSemaphore.release();
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
