import java.util.ArrayList;

public class Reader extends ObjectThread {
//Acessa a base para a leitura

	private static String currentWord;

	public void doAction(ArrayList<String> words,int posicBase) {
		this.currentWord = words.get(posicBase);
		//System.out.println("READER");
	}

	@Override
	public void run() {

	}

}
