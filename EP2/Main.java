////////////////////////////////////////////////
//	EXERCICIO PROGRAMA 2                      //
//	SISTEMAS OPERACIONAIS                     //
//	TURMA 94                                  //
//	Thais de Souza Rodrigues NUSP 11796941    //
//	Melissa Akie Inui NUSP 11908865           //
// 	Silas Bovolin Reis NUSP 11796739          //
// 	Gabriel de Oliveira Theodoro NUSP 11202569//
////////////////////////////////////////////////

import java.util.ArrayList;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {

	protected static final int numThreads = 100;
	protected static final int numAccess = 100;
	protected static ArrayList<Integer> emptyPositions;
	protected static ObjectThread[] storage = new ObjectThread[numThreads];
	protected static RandomPosition random;

	// Cria e armazena os objetos das threads, de acordo com a proporção entre readers e writers
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
    }

    //Método para o acesso para os que não são readers e writers
    public static void access(FileText file) throws InterruptedException {
    	random = new RandomPosition();
    	int posicBase; 

    	for(int i = 0; i < numThreads; i++) { 
    		ObjectThread obj = storage[i];
    		obj.start();
    		for(int j = 0; j < numAccess; j++) { 
    			posicBase = random.getRandom(file.getSize());
    			obj.doAction(file.words,posicBase);
    		}
			obj.sleep(1); 
			obj.join();
		}
	}

	public static void main(String[] args) {
		try {
			FileText file = new FileText("bd.txt");
			// for(int i = 0; i < 101;i++) createObjects(i); // teste (como pode ser 100 e 0, vai ate 100)
			createObjects(0);
			//accessRW();
			access(file);
		}
		catch(FileNotFoundException e) {}
		catch(InterruptedException e2) {}
	}
}
