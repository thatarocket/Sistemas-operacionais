import java.util.ArrayList;

public class Reader extends Object {
//Acessa a base para a leitura

	private static String currentWord;

	public Reader(ArrayList<String> words,int posicBase) {
		this.currentWord = words.get(posicBase);
	}

	@Override
	public void run() {
		
	}

}
