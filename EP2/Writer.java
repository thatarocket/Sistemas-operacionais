import java.util.ArrayList;

public class Writer extends ObjectThread {
//Escrevem na base

	public void doAction(ArrayList<String> words,int posicBase) {
		words.set(posicBase,"MODIFICADO");
		//System.out.println("WRITER");
	}

	@Override
	public void run() {


	}

}
