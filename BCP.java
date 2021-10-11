public class BCP {
    private String nome;    //nome do programa
    private String estadoProcesso;  //estado que o programa se encontra (executando, bloqueado ou pronto)
    private int registradorX;    //estado do registrador de uso GERAL X
    private int registradorY;    //estado do registrador de uso GERAL Y
    private int contadorPrograma;    //contador de programa (registrador de uso ESPECIFICO) - posicao do arranjo segTextProg
    private String[] segTextoProg;    //segmento de texto do programa;
    private int tempoEspera;
    private int trocas;
    private int instrucoes;


    public BCP(String nome, String[] segTextoProg) {
        this.nome = nome;
        this.segTextoProg = segTextoProg;
        this.tempoEspera = 0;
        this.estadoProcesso = "executando";
        this.contadorPrograma = 0;
        this.registradorX = 0;
        this.registradorY = 0;
        this.trocas = 0;
    }


    /* METODOS GETTERS*/

    public String getNome() {
        return nome;
    }

    public int getTempoEspera() {
        return tempoEspera;
    }

    public String getEstadoProcesso() {
        return estadoProcesso;
    }

    public int getRegistradorX() {
        return registradorX;
    }

    public int getRegistradorY() {
        return registradorY;
    }

    public int getContadorPrograma() {
        return contadorPrograma;
    }

    public String[] getSegTextoProg() {
        return segTextoProg;
    }

    public int getTrocas(){
        return trocas;
    }

    public int getInstrucoes(){
        return instrucoes;
    }


    /* METODOS SETTERS*/

    public void setEstadoProcesso(String estadoProcesso) {
        this.estadoProcesso = estadoProcesso;
    }

    public void setRegistradorX(int registradorX) {
        this.registradorX = registradorX;
    }

    public void setRegistradorY(int registradorY) {
        this.registradorY = registradorY;
    }

    public void setContadorPrograma(int contadorPrograma) {
        this.contadorPrograma = contadorPrograma;
    }

    public void setTempoEspera(int tempoEspera) {
        this.tempoEspera = tempoEspera;
    }

    public void setTrocas(int trocas) {
        this.trocas = trocas;
    }

    public void setInstrucoes(int instrucoes) {
      this.instrucoes = instrucoes;
    }

}
