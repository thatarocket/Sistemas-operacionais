public class BCP {
    private String nome;    //nome do programa
    private String estadoProcesso;  //estado que o programa se encontra (executando, bloqueado ou pronto)
    private String registradorX;    //estado do registrador de uso GERAL X
    private String registradorY;    //estado do registrador de uso GERAL Y
    private int contadorPrograma;    //contador de programa (registrador de uso ESPECIFICO) - posicao do arranjo segTextProg
    private String [] segTextoProg;    //segmento de texto do programa;

    public BCP(String nome, String[]segTextoProg){
        this.nome = nome;
        this.segTextoProg = segTextoProg;
    }


    /* METODOS GETTERS*/

    public String getNome() {
        return nome;
    }

    public String getEstadoProcesso() {
        return estadoProcesso;
    }

    public String getRegistradorX() {
        return registradorX;
    }

    public String getRegistradorY() {
        return registradorY;
    }

    public int getContadorPrograma() {
        return contadorPrograma;
    }

    public String[] getSegTextoProg() {
        return segTextoProg;
    }


    /* METODOS SETTERS*/

    public void setEstadoProcesso(String estadoProcesso) {
        this.estadoProcesso = estadoProcesso;
    }

    public void setRegistradorX(String registradorX) {
        this.registradorX = registradorX;
    }

    public void setRegistradorY(String registradorY) {
        this.registradorY = registradorY;
    }

    public void setContadorPrograma(int contadorPrograma) {
        this.contadorPrograma = contadorPrograma;
    }

}
