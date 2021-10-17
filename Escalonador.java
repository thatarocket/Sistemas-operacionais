import java.io.*;
import java.util.*;


public class Escalonador {

    private static List<File> arquivos;
    private static int quantum;
    private static List<BCP> tabelaProcessos = new ArrayList<>();    //lista contendo o BCP de todos os programas
    private static Queue<BCP> processosProntos = new ArrayDeque<>();  //fila de processos prontos
    private static Queue<BCP> processosBloqueados = new ArrayDeque<>(); //fila de processos bloqueados
    private static FileWriter fileout;
    private static List<BCP> listaProcessos;

    /*Responsavel por pegar os arquivos da pasta "programas" e armazenar na variavel "arquivos". Ele tb inicializa a variavel "quantum"*/
    public static void catch_inputs() throws FileNotFoundException {
        File dir = new File("programas"); //Pasta contendo os arquivos-programa
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


    /***********************************************************************
     * Responsavel por pegar cada programa(arquivo.txt) e criar seu BCP    *
     * *********************************************************************/
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
            e.getStackTrace();
            return null;
        }
    }


    public static boolean instrucaoValida(String instrucao) {
        return instrucao.equals("COM") || instrucao.equals("E/S") || instrucao.equals("SAIDA") || instrucao.contains("X=") || instrucao.contains("Y=");
    }


    /*  Responsavel por fazer as devidas alteracoes quando houver uma instrucao "SAIDA"  */
    public static void manipulaSAIDA(BCP bcp, int quantum) throws IOException {

        fileout.write(bcp.getNome() + " terminado. X=" + bcp.getRegistradorX() + ". " + "Y=" + bcp.getRegistradorY() + "\r\n");

        bcp.setInstrucoes(bcp.getInstrucoes() + quantum);
        bcp.setTrocas(bcp.getTrocas() + 1);
        bcp.setEstadoProcesso("pronto");
        processosProntos.remove(bcp);
        processosBloqueados.remove(bcp);
        tabelaProcessos.remove(bcp);
    }


    /**********************************************************************************
     * Responsavel por fazer as devidas alteracoes quando houver uma instrucao "E/S"  *
     **********************************************************************************/
    public static void manipulaES(BCP bcp, int quantum) throws IOException {

        fileout.write("E/S iniciada em " + bcp.getNome() + "\r\n");
        if (quantum > 1) fileout.write("Interrompendo " + bcp.getNome() + " após " + quantum + " instruções" + "\r\n");
        else fileout.write("Interrompendo " + bcp.getNome() + " após " + quantum + " instrução" + "\r\n");

        bcp.setInstrucoes(bcp.getInstrucoes() + quantum);
        bcp.setTrocas(bcp.getTrocas() + 1);
        bcp.setEstadoProcesso("bloqueado");
        bcp.setContadorPrograma(bcp.getContadorPrograma() + 1);
        processosBloqueados.add(bcp);
        processosProntos.remove(bcp);
        bcp.setTempoEspera(2);
    }


    /**************************************************************************************************
     *  Verifica se existe algum processo na fila de Processos Bloqueados que tem seu tempo ZERADO e   *
     *  portantanto precisa voltar para a fila de Processos Prontos                                    *
     ***************************************************************************************************/
    public static boolean atualizaProcessosBloqueados() {
        List<BCP> adicionar = new ArrayList<>();
        List<BCP> retirar = new ArrayList<>();
        boolean atualizou = false;
        if (!processosBloqueados.isEmpty()) {
            for (BCP bcp : processosBloqueados) {
                if (bcp.getTempoEspera() == 0) {
                    bcp.setEstadoProcesso("pronto");
                    adicionar.add(bcp);
                    retirar.add(bcp);
                    atualizou = true;
                }
            }
            adicionar.forEach(bcp -> processosProntos.add(bcp));
            retirar.forEach(bcp -> processosBloqueados.remove(bcp));
        }
        return atualizou;
    }


    /**************************************************************************************
     * Decrementa o tempo de todos os processos que estao na fila de Processos Bloqueados *
     ***************************************************************************************/
    public static void decrementaTempo() {
        if (processosBloqueados.size() == tabelaProcessos.size()) {
            boolean stop = false;
            List<BCP> adicionar = new ArrayList<>();
            List<BCP> retirar = new ArrayList<>();
            for (BCP bcp_ : processosBloqueados) {
                if (bcp_.getTempoEspera() == 0) {
                    bcp_.setEstadoProcesso("pronto");
                    adicionar.add(bcp_);
                    retirar.add(bcp_);
                    stop = true;
                }
            }
            if (stop) {
                adicionar.forEach(b -> processosProntos.add(b));
                retirar.forEach(b -> processosBloqueados.remove(b));
                adicionar.clear();
                retirar.clear();
            } else {
                while (!stop) { //espera ate que algum processa tenha seu tempo de espera igual a ZERO
                    processosBloqueados.forEach(processo -> processo.setTempoEspera(processo.getTempoEspera() - 1));
                    stop = atualizaProcessosBloqueados();
                }
            }
        }
    }


    public static void roundRobin() throws IOException {
        /*    ALGORITMO ROUND ROBIN    */
        // 1)
        arquivos.forEach(arq -> tabelaProcessos.add(manipulaBCP(arq))); //monta a Tabela de processos
        List<String> nomesProcessos = new ArrayList<>();
        tabelaProcessos.forEach(bcp -> nomesProcessos.add(bcp.getNome()));

        //ordena o nome dos processos em ordem alfabetica
        Collections.sort(nomesProcessos, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return extractInt(o1) - extractInt(o2);
            }

            int extractInt(String s) {
                String num = s.replaceAll("\\D", "");
                // retorna 0 se nao ha digitos no nome
                return num.isEmpty() ? 0 : Integer.parseInt(num);
            }
        });

        //add os processos na fila de Processos Prontos
        for (String nome : nomesProcessos) {
            for (BCP bcp : tabelaProcessos) {
                boolean found = false;
                if (bcp.getNome().equals(nome)) {
                    processosProntos.add(bcp);
                    found = true;
                }
                if (found) break;
            }
        }

        listaProcessos = new ArrayList<>(tabelaProcessos);

        for (BCP bcp : processosProntos) fileout.write("Carregando " + bcp.getNome() + "\r\n");

        // 2)
        while (!tabelaProcessos.isEmpty()) {     // enquanto ainda houver processos para serem executados
            atualizaProcessosBloqueados();
            if (processosProntos.isEmpty()) decrementaTempo();

            else {
                BCP bcp = processosProntos.poll();
                boolean entradaSaida = false;
                fileout.write("Executando " + bcp.getNome() + "\r\n");

                for (int i = 1; i <= quantum; i++) {
                    String instrucao = bcp.getSegTextoProg()[bcp.getContadorPrograma()];

                    if (instrucao.equals("E/S")) {
                        manipulaES(bcp, i);
                        entradaSaida = true;
                        break;
                    } else if (instrucao.equals("SAIDA")) {
                        manipulaSAIDA(bcp, i);
                        entradaSaida = true;
                        break;
                    } else if (instrucao.equals("COM")) {
                        bcp.setContadorPrograma(bcp.getContadorPrograma() + 1);
                    } else if (instrucao.contains("X=")) {
                        bcp.setContadorPrograma(bcp.getContadorPrograma() + 1);
                        bcp.setRegistradorX(Integer.parseInt(instrucao.substring(2, instrucao.length())));
                    } else if (instrucao.contains("Y=")) {
                        bcp.setContadorPrograma(bcp.getContadorPrograma() + 1);
                        bcp.setRegistradorY(Integer.parseInt(instrucao.substring(2, instrucao.length())));
                    }
                }
                if (!entradaSaida) {
                    fileout.write("Interrompendo " + bcp.getNome() + " após " + quantum + " instruções" + "\r\n");
                    bcp.setInstrucoes(bcp.getInstrucoes() + quantum);
                }

                if (!processosBloqueados.isEmpty()) {
                    processosBloqueados.forEach(processo -> processo.setTempoEspera(processo.getTempoEspera() - 1));  //decrementa 1 unidade do tempo de espera de todos na fila de bloqueados
                    atualizaProcessosBloqueados(); //checa se algum processo teve seu tempo de espera zerado
                }

                if (!entradaSaida) {
                    processosProntos.add(bcp);
                }
            }
        }

        double somaTrocas = 0;
        for (BCP bcp : listaProcessos) somaTrocas += bcp.getTrocas();
        double mediaTrocas = somaTrocas / arquivos.size();
        String mt = String.format("%.2f", mediaTrocas).replace(",", ".");
        fileout.write("MEDIA DE TROCAS: " + mt + "\r\n");

        double somaInstrucoes = 0;
        for (BCP bcp : listaProcessos) somaInstrucoes += bcp.getInstrucoes();
        double mediaInstrucoes = somaInstrucoes / arquivos.size();
        String mi = String.format("%.2f", mediaInstrucoes).replace(",", ".");
        fileout.write("MEDIA DE INSTRUCOES: " + mi + "\r\n");
        fileout.write("QUANTUM: " + quantum + "\r\n");

        fileout.close();
    }


    public static void main(String[] args) {

        try {
            catch_inputs();
            create_files();
            roundRobin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
