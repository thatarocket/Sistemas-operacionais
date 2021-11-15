import java.util.ArrayList;

public class Writer extends Object {
//Escrevem na base
	
	public Writer(ArrayList<String> words,int posicBase) {
		words.set(posicBase,"MODIFICADO");
	}

	@Override
	public void run() {
		

	}
	
}
