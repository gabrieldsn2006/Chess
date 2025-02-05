public class ChessboardException extends Exception {

    public ChessboardException(int ExceptionCode) {
        String message = "CHESSBOARD EXCEPTION ERROR";
        if (ExceptionCode == 9999) message = "INPUT INVÁLIDO";
        if (ExceptionCode == 1000) message = "INSIRA UM MOVIMENTO VÁLIDO";
        if (ExceptionCode == 2000) message = "SELECIONE UMA PEÇA";
        // super(message);
    }
}
