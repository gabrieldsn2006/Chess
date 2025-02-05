public class PieceException extends Exception {

    public PieceException(int ExceptionCode) {
        String message = "PIECE EXCEPTION ERROR";
        if (ExceptionCode == 3000) message = "SELECIONE UMA PEÇA SUA";
        if (ExceptionCode == 4000) message = "INSIRA UM MOVIMENTO VÁLIDO";
        if (ExceptionCode == 5000) message = "REI EM XEQUE, INSIRA UM MOVIMENTO VÁLIDO";
        // super(message);
    }
}
