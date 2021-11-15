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

	private static ArrayList<String> palavras;
	private static ArrayList<Integer> posicVazias;
	private static Object[] objectThread = new Object[100];

	// Separa e guarda as palavras do texto
	public static void separa() throws FileNotFoundException {
		Scanner input = new Scanner(new FileReader("bd.txt"));
		palavras = new ArrayList<String>();
		while(input.hasNext()) {
			palavras.add(input.next());
		}
	}

	// Proporção das threads, entre readers e writers
	public static void PropThreads(int readers) {

		int numReaders = readers;
        posicVazias = new ArrayList<Integer>(); //Guarda as posics vazias
        for (int i = 0; i < 100; i++) posicVazias.add(i);

        ThreadLocalRandom random = ThreadLocalRandom.current(); //gera o numero aleatorio

    	for(int i = 0; i < 100; i++) {
    		Object obj;

    		if(numReaders <= 0) obj = new Writer();  //De acordo com a proporcao
    		else obj = new Reader();

    		int posic = random.nextInt(0,posicVazias.size()); // gera uma posic aleatoria para inserir
    		int posicInserc = posicVazias.get(posic); //posicao vazia para inserir

    		objects[posicInserc] = obj; //Insere no objeto de threads
    		posicVazias.remove(posic); //Remove a posicao das posicoes vazias, dado que foi ocupado
	  		numReaders--;
    	}

    }


	public static void main(String[] args) {
		try {
			separa(); //separa as palavras do arquivo

			for(int i = 0; i < 101;i++) {
				storageThreads(); // Proporção de readers and writers
			}
			
		}
		catch(IOException e) {}
	}
}