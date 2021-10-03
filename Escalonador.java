
import java.io.*;
import java.util.Scanner;
import java.util.*;


public class Escalonador {

    private static List<File> arquivos;
    private static final String diretorio = "C:programas";
    private static int quantum;
    private static List<BCP> listaBCPs = new ArrayList<>();    //lista contendo o BCP de todos os programas
    private static Queue<BCP> processosProntos = new LinkedList<>();  //fila de processos prontos
    private static Queue<BCP> processosBloqueados = new LinkedList<>(); //fila de processos bloqueados

    public static void catch_inputs() throws FileNotFoundException {

        File dir = new File(diretorio); //Pasta contendo os arquivos-programa
        arquivos = new LinkedList<>(List.of(dir.listFiles()));

        for (File file : arquivos) {
            if (file.getName().equals("quantum.txt")) {
                Scanner scanner = new Scanner(new FileReader("programas/quantum.txt"));
                while (scanner.hasNext()) {
                    String input = scanner.next();
                    quantum = Integer.parseInt(input);
                }
                arquivos.remove(file);
            }
        }

        /* pega o nome da pasta
        pega os arquivos que estao na pasta(programas.txt + quantum)
        guarda o quantum
        programas.txt: 
            Guardar a primeira linha - nome programa
            Linhas posteriores - instruções (ATÉ A PALAVRA SAIDA. MAX 21 COMANDOS = 21 linhas de comando(inclui saida))
        */
    }


    /*Responsavel por pegar cada programa(arquivo.txt) e criar seu BCP*/
    public static BCP manipulaBCP(File file) {
        String nome = "";
        String [] comandos = new String[21];
        try{
            Scanner sc = new Scanner(new FileReader("programas/"+file.getName()));
            int i = 0;

            while (true) {  //le as linhas do arquivo
                String linha = sc.nextLine();

                if(linha.equals("SAIDA")){  //ultima linha do arquivo
                    comandos[i] = linha;
                    sc.close();
                    return new BCP(nome, comandos);
                }
                if(nome.equals("")){ //pega o nome do arquivo (primeira linha do arquivo)
                    nome = linha;
                }
                else{
                    comandos[i] = linha;
                    i++;
                }
            }
        }
        catch(FileNotFoundException e){
            System.out.println("OPS! ERRO no metodo <manipulaBCP>\"");
            return null;
        }
    }

    public static void main(String[] args) {

        try {
            catch_inputs();
        } catch (FileNotFoundException e) {
            System.out.println("OPS! ERRO no metodo <catch_inputs>");
        }


        /*    ALGORITMO ROUND ROBIN    */
        // 1)
        Collections.sort(arquivos); //ordenanos os processos em ordem alfabetica
        arquivos.forEach(arq -> listaBCPs.add(manipulaBCP(arq))); //monta uma lista de BCPs dos processos
        processosProntos.addAll(listaBCPs); //add os processos na fila de Processos Prontos

        // 2)


    }
}