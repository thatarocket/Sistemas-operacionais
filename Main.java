import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.BufferedReader;


public class Main {
    
    private static File [] arquivos;
    private static String diretorio = "C:programas";
    private static int quantum;

    public static void catch_inputs () throws Exception{

        File dir = new File(diretorio); //Pasta
        File nomes_arq[] = dir.listFiles();
        arquivos = new File[nomes_arq.length];
        int i = 0;
        for (int j = nomes_arq.length; i < j; i++) {
            File file = nomes_arq[i];
            if(file.getName().equals("quantum.txt")){
                Scanner scanner = new Scanner(new FileReader("programas/quantum.txt"));
                while(scanner.hasNext()) {
                    String input = scanner.next();
                    quantum = Integer.parseInt(input);
                }
            }
            else arquivos[i] = file;
        }
        
        /* pega o nome da pasta
        pega os arquivos que estao na pasta(programas.txt + quantum)
        guarda o quantum
        programas.txt: 
            Guardar a primeira linha - nome programa
            Linhas posteriores - instruções (ATÉ A PALAVRA SAIDA. MAX 21 COMANDOS = 21 linhas de comando(inclui saida))
        */
    }

    public static void main(String[] args) {     
        
        try{
            catch_inputs();
        }

        catch(Exception e){
            //something
        }

    }      
}