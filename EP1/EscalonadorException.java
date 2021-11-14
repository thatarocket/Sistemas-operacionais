public class EscalonadorException extends Throwable {
    private static final long serialVersionUID = 22222222222222L;
    private String msg;

    // constrói um objeto EscalonadorException com a mensagem passada por parâmetro
    public EscalonadorException(String msg) {
        super(msg);
        this.msg = msg;
    }

    // contrói um objeto EscalonadorException com mensagem e a causa dessa exceção, utilizado para encadear exceptions
    public String getMessage() {
        return this.msg;
    }
}
