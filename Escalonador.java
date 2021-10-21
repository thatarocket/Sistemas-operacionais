import java.io.*;
import java.util.*;


public class Escalonador {

    private static List<File> arquivos;
    private static Map<BCP, String> nomesArquivos = new HashMap<>();
    private static int quantum;
    private static List<BCP> tabelaProcessos = new ArrayList<>();    //TABELA DE PROCESSOS
    private static Queue<BCP> processosProntos = new ArrayDeque<>();  //FILA DE PROCESSOS PRONTOS
    private static Queue<BCP> processosBloqueados = new ArrayDeque<>(); //FILA DE PROCESSOS BLOQUEADOS
    private static FileWriter fileout;
    private static List<BCP> listaProcessos;
    private static int qtdQuantum = 0;


    /***********************************************************************************************
     *  Responsavel por pegar os arquivos da pasta "programas" e armazenar na variavel "arquivos". *
     *  Ele tb inicializa a variavel "quantum".                                                    *
     ***********************************************************************************************/
    public static void catch_inputs() throws FileNotFoundException, EscalonadorException {
        File dir = new File("programas"); //Pasta contendo os arquivos-programa
        arquivos = new LinkedList<>(List.of(dir.listFiles()));

        for (File file : arquivos) {
            if (file.getName().equals("quantum.txt")) {
                Scanner scanner = new Scanner(new FileReader("programas/quantum.txt"));
                while (scanner.hasNext()) {
                    String input = scanner.next();
                    quantum = Integer.parseInt(input);
                    if (quantum <= 0) throw new EscalonadorException("ERRO: Quantum com valor menor ou igual a ZERO");
                }
                arquivos.remove(file);
                return;
            }
        }
    }

    /****************************************
     *  Responsavel por criar o arquivo log  *
     *****************************************/
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
    public static BCP manipulaBCP(File file) throws EscalonadorException {
        String nome = "";
        String[] comandos = new String[21];

        try {
            Scanner sc = new Scanner(new FileReader("programas/" + file.getName()));
            int i = 0;
            while (true) {  //le as linhas do arquivo
                String linha = sc.nextLine();

                if (i == 20 && !linha.equals("SAIDA"))
                    throw new EscalonadorException("ERRO: A 21a instrucao do programa " + nome + " nao eh uma instrucao de SAIDA");

                if (linha.equals("SAIDA")) {  //ultima linha do arquivo
                    comandos[i] = linha;
                    sc.close();
                    BCP bcp = new BCP(nome, comandos);
                    nomesArquivos.put(bcp, file.getName());
                    return bcp;
                }
                if (nome.equals("")) { //pega o nome do arquivo (primeira linha do arquivo)
                    nome = linha;
                } else {
                    if (!instrucaoValida(linha))
                        throw new EscalonadorException("ERRO: o programa " + nome + " possui uma instrucao INVALIDA");
                    comandos[i] = linha;
                    i++;
                }
            }
        } catch (FileNotFoundException e) {
            e.getStackTrace();
            return null;
        }
    }

    /*****************************************************************************************************
     *  Responsavel por fazer a verificacao se a instrucao passada por parametro eh uma instrucao valida *
     *****************************************************************************************************/
    public static boolean instrucaoValida(String instrucao) {
        if (instrucao.equals("COM") || instrucao.equals("E/S") || instrucao.equals("SAIDA")) return true;
        if (instrucao.contains("X=") || instrucao.contains("Y=")) {
            try {
                Double.parseDouble(instrucao.substring(2, instrucao.length()));
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }


    /************************************************************************************
     *  Responsavel por fazer as devidas alteracoes quando houver uma instrucao "SAIDA"  *
     * ***********************************************************************************/
    public static void manipulaSAIDA(BCP bcp, int quantum) throws IOException {

        fileout.write(bcp.getNome() + " terminado. X=" + bcp.getRegistradorX() + ". " + "Y=" + bcp.getRegistradorY() + "\r\n");

        bcp.setInstrucoes(bcp.getInstrucoes() + 1);
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

        bcp.setTrocas(bcp.getTrocas() + 1);
        bcp.setInstrucoes(bcp.getInstrucoes() + quantum);
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


    /***************************
     *  ALGORITMO ROUND ROBIN  *
     ***************************/
    public static void roundRobin() throws IOException {

        //monta a Tabela de processos
        arquivos.forEach(arq -> {
            try {
                tabelaProcessos.add(manipulaBCP(arq));
            } catch (EscalonadorException ee) {
                System.out.println(ee.getMessage());
            } catch (NoSuchElementException e) {
                System.out.println("ERRO: o arquivo " + arq.getName() + " nao possui a instrucao SAIDA");
            }
        });

        List<String> nomesArq = new ArrayList<>();
        for (BCP bcp : nomesArquivos.keySet()) {
            nomesArq.add(nomesArquivos.get(bcp));
        }

        //ordena o nome dos processos em ordem alfabetica
        Collections.sort(nomesArq, new Comparator<String>() {
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
        for (String nome : nomesArq) {
            for (BCP bcp : nomesArquivos.keySet()) {
                boolean found = false;
                if (nomesArquivos.get(bcp).equals(nome)) {
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
                    bcp.setTrocas(bcp.getTrocas() + 1);
                }

                if (!processosBloqueados.isEmpty()) {
                    processosBloqueados.forEach(processo -> processo.setTempoEspera(processo.getTempoEspera() - 1));  //decrementa 1 unidade do tempo de espera de todos na fila de bloqueados
                    atualizaProcessosBloqueados(); //checa se algum processo teve seu tempo de espera zerado
                }

                if (!entradaSaida) {
                    processosProntos.add(bcp);
                }

                qtdQuantum++;
            }
        }

        double somaTrocas = 0;
        for (BCP bcp : listaProcessos) somaTrocas += bcp.getTrocas();
        double mediaTrocas = somaTrocas / arquivos.size();
        String mt = String.format("%.2f", mediaTrocas).replace(",", ".");
        fileout.write("MEDIA DE TROCAS: " + mt + "\r\n");

        double somaInstrucoes = 0;
        for (BCP bcp : listaProcessos) somaInstrucoes += bcp.getInstrucoes();
        double mediaInstrucoes = somaInstrucoes / qtdQuantum;
        String mi = String.format("%.2f", mediaInstrucoes).replace(",", ".");
        fileout.write("MEDIA DE INSTRUCOES: " + mi + "\r\n");
        fileout.write("QUANTUM: " + quantum);

        fileout.close();
    }


    public static void main(String[] args) {

        try {
            catch_inputs();
            create_files();
            roundRobin();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EscalonadorException ee) {
            System.out.println(ee.getMessage());
        }
    }
}
