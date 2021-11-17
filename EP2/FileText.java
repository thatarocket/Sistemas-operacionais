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

    public void setWord(int index, String newWord) {
        words.set(index, newWord);
    } 
}
