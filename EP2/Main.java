////////////////////////////////////////////////
//	EXERCICIO PROGRAMA 2                      //
//	SISTEMAS OPERACIONAIS                     //
//	TURMA 94                                  //
//	Thais de Souza Rodrigues NUSP 11796941    //
//	Melissa Akie Inui NUSP 11908865           //
// 	Silas Bovolin Reis NUSP 11796739          //
// 	Gabriel de Oliveira Theodoro NUSP 11202569//
////////////////////////////////////////////////

import java.util.Scanner;
import java.util.ArrayList;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {

	private static final int numThreads = 100;
	private static final int numAccess = 100;
	private static ArrayList<String> words;
	private static ArrayList<Integer> emptyPositions;
	private static ObjectThread[] storage = new ObjectThread[numThreads];
	private static RandomPosition random;

	// Separa e guarda as palavras do texto
	public static void separateWords() throws FileNotFoundException {
		Scanner input = new Scanner(new FileReader("bd.txt"));
		words = new ArrayList<String>();
		while(input.hasNext()) {
			words.add(input.next());
		}
	}

	// Cria e armazena os objetos das threads, de acordo com a proporção entre readers e writers
	public static void createObjects(int numReaders) {
    	emptyPositions = new ArrayList<Integer>(); //Guarda em cada indices, as posicoes que ainda estão vazias
     	for (int i = 0; i < numThreads; i++) emptyPositions.add(i);
     	random = new RandomPosition();

     	for(int i = 0; i < numThreads; i++) { //objetos
    		// Pega-se uma posição vazia e ve qual indice esta nessa posicao
    		int posic = random.getRandom(emptyPositions.size());
    		int posicInserc = emptyPositions.get(posic); //pega qual posicao esta nesse indice

    		ObjectThread obj;
    		if(numReaders <= 0) obj = new Writer();  //De acordo com a proporcao
    		else obj = new Reader();
    		storage[posicInserc] = obj;

    		emptyPositions.remove(posic); //Remove a posicao das posicoes vazias, dado que foi ocupado
    		numReaders--;
    	}
    }

    //Método para o acesso para os que não são readers e writers
    public static void access() throws InterruptedException {
    	random = new RandomPosition();
    	int posicBase; //posicao das palavras

    	for(int i = 0; i < numThreads; i++) { //todos os objetos
    		ObjectThread obj = storage[i];
    		obj.start();
    		for(int j = 0; j < numAccess; j++) { //fazer os 100 acessos
    			posicBase = random.getRandom(words.size());
    			obj.doAction(words,posicBase);
    		}
			obj.sleep(1); //dormir por 1ms
			obj.join();
		}
	}



	public static void main(String[] args) {
		try {
			separateWords(); //separa as palavras do arquivo
			// for(int i = 0; i < 101;i++) createObjects(i); // teste (como pode ser 100 e 0, vai ate 100)
			createObjects(0);
			//accessRW();
			access();
		}
		catch(IOException e) {}
		catch(InterruptedException e2) {}
	}
}
