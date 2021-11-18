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

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileNotFoundException;

public class Main {

	protected static final int numThreads = 100;
	protected static final int numAccess = 100;
	protected static ArrayList<Integer> emptyPositions;
	protected static ObjectThread[] storage = new ObjectThread[numThreads];
	protected static RandomPosition random;
	protected static long time;

	protected static Semaphore readSemaphore = new Semaphore(1);
    protected static Semaphore writeSemaphore = new Semaphore(1);
    protected static int threadsReads = 0;


	/**
	 * Cria e armazena os objetos das threads, de acordo com a proporção entre
	 * readers e writers
	 *
	 * @param int numReaders
	 * @return void
	 */
	public static void createObjects(int numReaders) {
    	emptyPositions = new ArrayList<Integer>();
     	for (int i = 0; i < numThreads; i++) emptyPositions.add(i);
     	random = new RandomPosition();

     	for(int i = 0; i < numThreads; i++) {
    		int posic = random.getRandom(emptyPositions.size()); // Pega-se uma posição vazia e ve qual indice esta nessa posicao
    		int posicInserc = emptyPositions.get(posic); //pega qual posicao esta nesse indice

    		ObjectThread obj;
    		if(numReaders <= 0) obj = new Writer();
    		else obj = new Reader();
    		storage[posicInserc] = obj;

    		emptyPositions.remove(posic);
    		numReaders--;
    	}
			time = System.currentTimeMillis();
    }

    /**
		 * Método para o acesso para os que são readers e writers
		 *
		 * @param FileText file
		 * @throws InterruptedException
		 * @return void
		 */
    public static void accessRW(FileText file) throws InterruptedException {

    	random = new RandomPosition();
    	int posicBase;

    	for(int i = 0; i < numThreads; i++) {
    		ObjectThread obj = storage[i];
    		obj.start();
    		for(int j = 0; j < numAccess; j++) {
    			posicBase = random.getRandom(file.getSize());    			
    			obj.lock(threadsReads,readSemaphore,writeSemaphore);
    			obj.acessFiles(file.words,posicBase);
    			obj.unlock(threadsReads,readSemaphore,writeSemaphore);
    		}
			obj.sleep(1);
			if(obj.isAlive()) obj.join();
		}

		time = System.currentTimeMillis() - time;
		//System.out.println("TEMPO TOTAL READER E WRITER " + time); //pensar em algo melhor
	}

	/**
	 * Método para o acesso para os que não são readers e writers
	 *
	 * @param FileText file
	 * @throws InterruptedException
	 * @return void
	 */
	public static void accessNonRW(FileText file) throws InterruptedException {

	}

	public static void main(String[] args) {
		try {
			FileText file = new FileText("bd.txt");
			// for(int i = 0; i < 101;i++) createObjects(i); // teste (como pode ser 100 e 0, vai ate 100)
			createObjects(100);
			accessRW(file);
			accessNonRW(file);
		}
		catch(FileNotFoundException e) {}
		catch(InterruptedException e2) {}
	}
}
