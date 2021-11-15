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
	private static Object[] objectThread = new Object[100];

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

    		if(numReaders <= 0) Writer writer = new Writer();  //De acordo com a proporcao
    		else Reader reader = new Reader();

    		int posic = random.nextInt(0,emptyPositions.size()); // gera uma posic aleatoria para inserir
    		int posicInserc = emptyPositions.get(posic); //posicao vazia para inserir

    		if(numReaders <= 0) objects[posicInserc] = writer; //Insere no objeto de threads
    		else objects[posicInserc] = reader;
    		
    		emptyPositions.remove(posic); //Remove a posicao das posicoes vazias, dado que foi ocupado
	  		numReaders--;
    	}

    }

    public static void access() {

    	ThreadLocalRandom random = ThreadLocalRandom.current(); 
    	int posicBase; //posicao das palavras

    	for(int i = 0; i < 100; i++) { //todos os 100 objetos
    		Object obj = objectThread[i];
    		for(int j = 0; j < 100; j++) { //fazer 100 acessos
    			posicBase = random.nextInt(0,words.size());
    			obj(words,posicBase).start();
    			obj.sleep(1); //dormir por 1ms
    		}
    	}  		

    }

	public static void main(String[] args) {
		try {
			separate(); //separa as palavras do arquivo
			for(int i = 0; i < 101;i++) storageThreads(); // Proporção de readers and writers (como pode ser 100 e 0, vai ate 100)
			access();
		}
		catch(IOException e) {}
	}
}
