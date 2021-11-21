/**
 * EP 2 - Sistemas Operacionais (Turma 94)
 *
 * @author Thais de Souza Rodrigues, 11796941
 * @author Melissa Akie Inui, 11908865
 * @author Silas Bovolin Reis, 11796739
 * @author Gabriel de Oliveira Theodoro, 11202569
 */

import java.util.Scanner;
import java.util.ArrayList;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class FileText {

	protected Scanner input;
	protected static ArrayList<String> words;

   /**
    * Le o nome do arquivo, separa e armazena as palavras do arquivo 
    *
    * @param String fileName
    * @throws FileNotFoundException
    */
	public FileText(String fileName) throws FileNotFoundException {
		input = new Scanner(new File(fileName));
		words = new ArrayList<String>();
		while(input.hasNext()) {
			words.add(input.next());
		}
	}

    public String getWord(int index) {
        return words.get(index);
    }

    public int getSize() {
    	return words.size();
    }
    
    public void getPrint() {
        for(String palavras : words) System.out.println(palavras);
    }
}
