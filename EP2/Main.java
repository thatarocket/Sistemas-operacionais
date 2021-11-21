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
	protected static final int numRepeat = 50;

	protected static ArrayList<Integer> emptyPositions;
	protected static ObjectThread[] storage = new ObjectThread[numThreads];
	protected static RandomPosition random;
	protected static long time;
	protected static long timeRw;

	protected static Semaphore readSemaphore = new Semaphore(1);
	protected static Semaphore writeSemaphore = new Semaphore(1);

   /**
	* Cria e armazena os objetos das threads, de acordo com a proporção entre
	* readers e writers
	* @param int numReaders
	* @param FileText file
	* @return void
	*/
	public static void createObjects(int numReaders,FileText file) {
    	emptyPositions = new ArrayList<Integer>();
     	for (int i = 0; i < numThreads; i++) emptyPositions.add(i);
     	random = new RandomPosition();

     	for(int i = 0; i < numThreads; i++) {
    		int posic = random.getRandom(emptyPositions.size()); // Pega-se uma posição vazia e ve qual indice esta nessa posicao
    		int posicInserc = emptyPositions.get(posic); //pega qual posicao esta nesse indice

    		ObjectThread obj;
    		if(numReaders <= 0) obj = new Writer(FileText.words,numAccess,readSemaphore,writeSemaphore);
    		else obj = new Reader(FileText.words,numAccess,readSemaphore,writeSemaphore);
    		storage[posicInserc] = obj;

    		emptyPositions.remove(posic);
    		numReaders--;
    	}
		timeRw = System.currentTimeMillis();
    }

    /**
	* Método para o acesso para os que são readers e writers
	* @param FileText file
	* @throws InterruptedException
	* @return void
	*/
    public static void accessRW(FileText file) throws InterruptedException {
     	for(int i = 0; i < numThreads; i++) {
    		ObjectThread obj = storage[i];
    		obj.start();
    	}

    	for(ObjectThread objects : storage) {
    		if(objects.isAlive()) {
    			objects.join();
    		}
    	}
		timeRw = System.currentTimeMillis() - timeRw;
		
		System.out.println(timeRw); //pensar em algo melhor
	}

	/**
	 * Cria e armazena os objetos das threads, de acordo com a proporção entre
	 * readers e writers. Não utilizando readers e writers.
	 * @param int numReaders
	 * @param FileText file
	 * @return void
	 */
	public static void createObjectsNonRW(int numReaders,FileText file) {
    	emptyPositions = new ArrayList<Integer>();
     	for (int i = 0; i < numThreads; i++) emptyPositions.add(i);
     	random = new RandomPosition();

     	for(int i = 0; i < numThreads; i++) {
    		int posic = random.getRandom(emptyPositions.size()); // Pega-se uma posição vazia e ve qual indice esta nessa posicao
    		int posicInserc = emptyPositions.get(posic); //pega qual posicao esta nesse indice

    		ObjectThread obj;
    		if(numReaders <= 0) obj = new WriterN(FileText.wordsN,numAccess,readSemaphore,writeSemaphore);
    		else obj = new ReaderN(FileText.wordsN,numAccess,readSemaphore,writeSemaphore);
    		storage[posicInserc] = obj;

    		emptyPositions.remove(posic);
    		numReaders--;
    	}
		time = System.currentTimeMillis();
    }


	/**
	 * Método para o acesso para os que não são readers e writers
	 * @param FileText file
	 * @throws InterruptedException
	 * @return void
	 */
	public static void accessNonRW(FileText file) throws InterruptedException {
		for(int i = 0; i < numThreads; i++) {
    		ObjectThread obj = storage[i];
    		obj.start();
    	}

    	for(ObjectThread objects : storage) {
    		if(objects.isAlive()) {
    			objects.join();
    		}
    	}
		time = System.currentTimeMillis() - time;
		
		System.out.println(time); //pensar em algo melhor
	
	} 

	public static void main(String[] args) {
		try {
			FileText file = new FileText("bd.txt");
			System.out.println("Readers e writers: ");
			for(int i = 0; i < numThreads+1;i++) {//como pode ser 100 e 0, vai ate 100
				System.out.println("Proporcao readers: " + i + " writers " + (numThreads-i));					
				for(int k = 0; k < numRepeat; k++) { 				
					createObjects(0,file);
					accessRW(file);
				}
			}

			System.out.println("Sem readers e writers: ");
			for(int i = 0; i < numThreads+1;i++) {//como pode ser 100 e 0, vai ate 100	
				System.out.println("Proporcao readers: " + i + " writers " + (numThreads-i));				
				for(int k = 0; k < numRepeat; k++) {
					createObjectsNonRW(0,file);
					accessNonRW(file);
				}
			}
		}
		catch(FileNotFoundException e) {
			System.out.println("ERRO - Nome do arquivo digitado não encontrado!");
		}
		catch(InterruptedException e2) {
			System.out.println("ERRO - Alguma thread interrompeu a thread atual!");
		}
	}
}
