import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import static java.util.Comparator.comparing;


public class Escalonador {

    private static List<File> arquivos;
    private static final String diretorio = "programas";
    private static int quantum;
    private static List<BCP> tabelaProcessos = new ArrayList<>();    //lista contendo o BCP de todos os programas
    private static Queue<BCP> processosProntos = new LinkedList<>();  //fila de processos prontos
    private static Queue<BCP> processosBloqueados = new LinkedList<>(); //fila de processos bloqueados

    /*Responsavel por pegar os arquivos da pasta "programas" e armazenar na variavel "arquivos". Ele tb inicializa a variavel "quantum"*/
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
                return;
            }
        }
    }


    /*Responsavel por pegar cada programa(arquivo.txt) e criar seu BCP*/
    public static BCP manipulaBCP(File file) {
        String nome = "";
        String[] comandos = new String[21];
        try {
            Scanner sc = new Scanner(new FileReader("programas/" + file.getName()));
            int i = 0;

            while (true) {  //le as linhas do arquivo
                String linha = sc.nextLine();

                if (linha.equals("SAIDA") || i == 20) {  //ultima linha do arquivo
                    comandos[i] = linha;
                    sc.close();
                    return new BCP(nome, comandos);
                }
                if (nome.equals("")) { //pega o nome do arquivo (primeira linha do arquivo)
                    nome = linha;
                } else {
                    comandos[i] = linha;
                    i++;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("OPS! ERRO no metodo <manipulaBCP>\"");
            return null;
        }
    }


    public static boolean instrucaoValida(String instrucao) {
        if (instrucao.equals("COM") || instrucao.equals("E/S") || instrucao.equals("SAIDA")) return true;
        return false;
    }


    /*  Responsavel por fazer as devidas alteracoes quando houver uma instrucao "SAIDA"  */
    public static void manipulaSAIDA(BCP bcp) {
        bcp.setEstadoProcesso("pronto");
        processosProntos.remove(bcp);
        tabelaProcessos.remove(bcp);
    }


    /*  Responsavel por fazer as devidas alteracoes quando houver uma instrucao "E/S"  */
    public static void manipulaES(BCP bcp) {
        bcp.setEstadoProcesso("bloqueado");
        processosBloqueados.add(bcp);
        processosProntos.remove(bcp);
        bcp.setTempoEspera(2);
    }



    public static void atualizaProcessosBloqueados() {
        for (BCP bcp : processosBloqueados) {
            if (bcp.getTempoEspera() == 0) {
                bcp.setEstadoProcesso("pronto");
                processosProntos.add(bcp);
                processosBloqueados.remove(bcp);
            }
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
        arquivos.forEach(arq -> tabelaProcessos.add(manipulaBCP(arq))); //monta a Tabela de processos
        Collections.sort(tabelaProcessos, comparing(BCP::getNome));  //ordenanos os processos em ordem alfabetica
        processosProntos.addAll(tabelaProcessos); //add os processos na fila de Processos Prontos


        // 2)
        while (tabelaProcessos.size() != 0) {     // enquanto ainda houver processos para serem executados

            //se a fila Processos Prontos NAO estiver vazia
            if (processosProntos.size() != 0) {
                for (BCP bcp : processosProntos) {
                    int contQuantum = quantum; //para nao alterar no valor atual
                    String instrucaoAtual = bcp.getSegTextoProg()[bcp.getContadorPrograma()];

                    while (contQuantum > 0) { //o quantum esta com o valor inicial e vai diminuindo conforme avanca
                        if (instrucaoValida(instrucaoAtual)) {  // caso a instrucao seja valida
                            if (instrucaoAtual.equals("COM")) continue;

                            else if (instrucaoAtual.equals("E/S")) {
                                manipulaES(bcp);
                                // necessario ver o momento exato em que ele retorna
                            } else if (instrucaoAtual.equals("SAIDA")) manipulaSAIDA(bcp);

                            else if (instrucaoAtual.substring(0, 1).equals("X=") || instrucaoAtual.substring(0, 1).equals("Y=")) {
                                if (instrucaoAtual.substring(0).equals("X"))
                                    bcp.setRegistradorX(Integer.parseInt(instrucaoAtual.substring(2, instrucaoAtual.length() - 1)));
                                else if (instrucaoAtual.substring(0).equals("Y"))
                                    bcp.setRegistradorY(Integer.parseInt(instrucaoAtual.substring(2, instrucaoAtual.length() - 1)));
                                // ainda nao entendi o que faz
                            }

                            bcp.setContadorPrograma(bcp.getContadorPrograma() + 1); //atualiza o contador de programa
                            contQuantum--;
                        }
                    }

                    processosBloqueados.forEach(processo -> processo.setTempoEspera(processo.getTempoEspera() - 1));  //decrementa 1 unidade do tempo de espera de todos na fila de bloqueados
                    atualizaProcessosBloqueados(); //checa se algum processo teve seu tempo de espera zerado
                }
            }

            //se a fila Processos Prontos ESTIVER vazia (só existem processos na fila Processos Bloqueados)
            else {
                boolean stop = false;
                while (!stop) {
                    for (BCP bcp : processosBloqueados) {
                        if (bcp.getTempoEspera() == 0) {
                            stop = true;
                            bcp.setEstadoProcesso("pronto");
                            processosProntos.add(bcp);
                            processosBloqueados.remove(bcp);
                        }
                    }
                    if (!stop)
                        processosBloqueados.forEach(processo -> processo.setTempoEspera(processo.getTempoEspera() - 1));
                }
            }
        }
    }
}
