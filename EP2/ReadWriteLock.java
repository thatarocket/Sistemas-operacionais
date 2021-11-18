/**
 * EP 2 - Sistemas Operacionais (Turma 94)
 *
 * @author Thais de Souza Rodrigues, 11796941
 * @author Melissa Akie Inui, 11908865
 * @author Silas Bovolin Reis, 11796739
 * @author Gabriel de Oliveira Theodoro, 11202569
 */

// https://programmer.help/blogs/simple-code-of-reader-writer-problem-in-java.html

import java.util.concurrent.Semaphore;

public class ReadWriteLock {

    protected static Semaphore readSemaphore = new Semaphore(1);
    protected static Semaphore writeSemaphore = new Semaphore(1);
    protected static int threadsReads = 0;
    
    public void readLock() throws InterruptedException {
        readSemaphore.acquire();
        if(threadsReads == 0){
            writeSemaphore.acquire();
        }
        threadsReads++;
        readSemaphore.release();
    }

    public void writeLock() throws InterruptedException {
        writeSemaphore.acquire();
    }

    public void readUnlock() throws InterruptedException {
        readSemaphore.acquire();
        threadsReads--;
        if(threadsReads == 0){
          writeSemaphore.release();
        }
        readSemaphore.release();
    }

    public void writeUnlock() throws InterruptedException{
        writeSemaphore.release();
    }
}
