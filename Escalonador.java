import java.io.*;
import java.util.*;

import static java.util.Comparator.comparing;


public class Escalonador {

    private static List<File> arquivos;
    private static final String diretorio = "programas";
    private static int quantum;
    private static List<BCP> tabelaProcessos = new ArrayList<>();    //lista contendo o BCP de todos os programas
    private static Queue<BCP> processosProntos = new LinkedList<>();  //fila de processos prontos
    private static Queue<BCP> processosBloqueados = new LinkedList<>(); //fila de processos bloqueados
    private static FileWriter fileout;
    private static List<BCP> listaProcessos;

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

    // Cria o arquivo de saida
    public static void create_files() throws IOException {
        String filename = "";
        if (String.valueOf(quantum).length() > 1) {
            filename = ("log" + quantum + ".txt");
        } else {
            filename = ("log0" + quantum + ".txt");
        }
        File file = new File(filename);
        fileout = new FileWriter(file);
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
        return instrucao.equals("COM") || instrucao.equals("E/S") || instrucao.equals("SAIDA") || instrucao.contains("X=") || instrucao.contains("Y=");
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

    public static void roundRobin() throws IOException {
        /*    ALGORITMO ROUND ROBIN    */
        // 1)
        arquivos.forEach(arq -> tabelaProcessos.add(manipulaBCP(arq))); //monta a Tabela de processos
        Collections.sort(tabelaProcessos, comparing(BCP::getNome));  //ordenanos os processos em ordem alfabetica
        processosProntos.addAll(tabelaProcessos); //add os processos na fila de Processos Prontos
        listaProcessos = new ArrayList<>(tabelaProcessos);

        for (BCP bcp : processosProntos) fileout.write("Carregando " + bcp.getNome() + "\r\n");

        // 2)
        while (!tabelaProcessos.isEmpty()) {     // enquanto ainda houver processos para serem executados
            boolean pare = false;
            //se a fila Processos Prontos NAO estiver vazia
            if (!processosProntos.isEmpty()) {
                for (BCP bcp : processosProntos) {
                    fileout.write("Executando " + bcp.getNome() + "\r\n");
                    int contQuantum = 1; //para nao alterar no valor atual
                    String instrucaoAtual = bcp.getSegTextoProg()[bcp.getContadorPrograma()];

                    while (contQuantum <= quantum) { //o quantum esta com o valor inicial e vai diminuindo conforme avanca
                        if (instrucaoValida(instrucaoAtual)) {  // caso a instrucao seja valida
                            if (instrucaoAtual.equals("COM")) bcp.setInstrucoes(bcp.getInstrucoes() + 1);

                            if (instrucaoAtual.equals("E/S")) {
                                bcp.setTrocas(bcp.getTrocas() + 1);
                                fileout.write("E/S iniciada em " + bcp.getNome() + "\r\n");
                                fileout.write("Interrompendo " + bcp.getNome() + " apos " + contQuantum + " instrucoes" + "\r\n");
                                manipulaES(bcp);
                                pare = true;
                            } else if (instrucaoAtual.equals("SAIDA")) {
                                manipulaSAIDA(bcp);
                                bcp.setTrocas(bcp.getTrocas() + 1);
                                fileout.write(bcp.getNome() + " terminado. X=" + bcp.getRegistradorX() + ". " + "Y=" + bcp.getRegistradorY() + "\r\n");
                                pare = true;
                            } else if (instrucaoAtual.substring(0, 1).equals("X=") || instrucaoAtual.substring(0, 1).equals("Y=")) {
                                bcp.setInstrucoes(bcp.getInstrucoes() + 1);
                                if (instrucaoAtual.substring(0).equals("X"))
                                    bcp.setRegistradorX(Integer.parseInt(instrucaoAtual.substring(2, instrucaoAtual.length() - 1)));
                                else if (instrucaoAtual.substring(0).equals("Y"))
                                    bcp.setRegistradorY(Integer.parseInt(instrucaoAtual.substring(2, instrucaoAtual.length() - 1)));
                            }

                            bcp.setContadorPrograma(bcp.getContadorPrograma() + 1); //atualiza o contador de programa
                            contQuantum++;
                            if (pare == true) break;
                        }
                    }

                    if (contQuantum > quantum) { //Passou do quantum que podia
                        fileout.write("Interrompendo " + bcp.getNome() + " após " + contQuantum + " instruções" + "\r\n");
                        bcp.setTrocas(bcp.getTrocas() + 1);
                    }
                    processosBloqueados.forEach(processo -> processo.setTempoEspera(processo.getTempoEspera() - 1));  //decrementa 1 unidade do tempo de espera de todos na fila de bloqueados
                    atualizaProcessosBloqueados(); //checa se algum processo teve seu tempo de espera zerado
                    if (pare == true) break;
                }
                if (pare == true) break;
            }

            //se a fila Processos Prontos ESTIVER vazia (existem somente processos na fila Processos Bloqueados)
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
        double somaTrocas = 0;
        for (BCP bcp : listaProcessos) somaTrocas += bcp.getTrocas();
        double mediaTrocas = somaTrocas / arquivos.size();
        fileout.write("MEDIA DE TROCAS: " + mediaTrocas + "\r\n");

        double somaInstrucoes = 0;
        for (BCP bcp : listaProcessos) somaInstrucoes += bcp.getInstrucoes();
        double mediaInstrucoes = somaInstrucoes / arquivos.size();
        fileout.write("MEDIA DE INSTRUCOES: " + mediaInstrucoes + "\r\n");

        fileout.write("QUANTUM: " + quantum + "\r\n");

        fileout.close();
    }


    public static void main(String[] args) {

        try {
            catch_inputs();
            create_files();
            roundRobin();
        } catch (FileNotFoundException fe) {
            System.out.println("OPS! ERRO no metodo <catch_inputs>");
        } catch (IOException io) {
            //
        }

    }
}
