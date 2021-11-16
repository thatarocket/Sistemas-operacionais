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
import java.util.concurrent.ThreadLocalRandom;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {

	private static ArrayList<String> words;
	private static ArrayList<Integer> emptyPositions;
	private static ObjectThread[] storage = new ObjectThread[100];

	// Separa e guarda as palavras do texto
	public static void separate() throws FileNotFoundException {
		Scanner input = new Scanner(new FileReader("bd.txt"));
		words = new ArrayList<String>();
		while(input.hasNext()) {
			words.add(input.next());
		}
	}

	// Proporção das threads, entre readers e writers
	public static void PropThreads(int readers) {
		int numReaders = readers;
    	emptyPositions = new ArrayList<Integer>(); //Guarda as posics vazias
     	for (int i = 0; i < 100; i++) emptyPositions.add(i);

      	ThreadLocalRandom random = ThreadLocalRandom.current();

      for(int i = 0; i < 100; i++) {
      	ObjectThread obj;
    		if(numReaders <= 0) obj = new Writer();  //De acordo com a proporcao
    		else obj = new Reader();

    		int posic = random.nextInt(0,emptyPositions.size()); // gera uma posic aleatoria para inserir
    		int posicInserc = emptyPositions.get(posic); //posicao vazia para inserir

    		if(numReaders <= 0) storage[posicInserc] = obj; //Insere no objeto de threads
    		else storage[posicInserc] = obj;

    		emptyPositions.remove(posic); //Remove a posicao das posicoes vazias, dado que foi ocupado
    		numReaders--;
    	}

    }

    public static void access() throws InterruptedException {

    	ThreadLocalRandom random = ThreadLocalRandom.current();
    	int posicBase; //posicao das palavras

    	for(int i = 0; i < 100; i++) { //todos os 100 objetos
    		ObjectThread obj = storage[i];
    		obj.start();
    		for(int j = 0; j < 100; j++) { //fazer 100 acessos
    			posicBase = random.nextInt(0,words.size());
    			obj.doAction(words,posicBase);
    		}
			obj.sleep(1); //dormir por 1ms
		}

	}

	public static void main(String[] args) {
		try {
			separate(); //separa as palavras do arquivo
			for(int i = 0; i < 101;i++) PropThreads(i); // Proporção de readers and writers (como pode ser 100 e 0, vai ate 100)
			access();
		}
		catch(IOException e) {}
		catch(InterruptedException e2) {}
	}
}
