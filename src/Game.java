// falta implementar: enPassant, Castling, Input com a notação certa, afogamento

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Game implements Output {
    final char[] lettersIndex = new char[] {'A','B','C','D','E','F','G','H'};

    final char[] numberIndex = new char[] {'8','7','6','5','4','3','2','1'};

    boolean whiteCheck = false;
    boolean blackCheck = false;

    boolean[][] whiteValidMovements = new boolean[8][8];
    boolean[][] whiteCheckEscape = new boolean[8][8];
    boolean[][] blackValidMovements = new boolean[8][8];
    boolean[][] blackCheckEscape = new boolean[8][8];

    boolean whiteCheckMate = false;
    boolean blackCheckMate = false;

    boolean draw = false;

    public Square[][] matrix = new Square[8][8];

    public Game() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (i == 1) matrix[i][j] = new Pawn('B', i, j, true, false);
                else if (i == 6) matrix[i][j] = new Pawn('W', i, j, true, false);
                else if ((i == 0 && j == 0) || (i == 0 && j == 7)) matrix[i][j] = new Rook('B', i, j);
                else if ((i == 7 && j == 0) || (i == 7 && j == 7)) matrix[i][j] = new Rook('W', i, j);
                else if ((i == 0 && j == 1) || (i == 0 && j == 6)) matrix[i][j] = new Knight('B', i, j);
                else if ((i == 7 && j == 1) || (i == 7 && j == 6)) matrix[i][j] = new Knight('W', i, j);
                else if ((i == 0 && j == 2) || (i == 0 && j == 5)) matrix[i][j] = new Bishop('B', i, j);
                else if ((i == 7 && j == 2) || (i == 7 && j == 5)) matrix[i][j] = new Bishop('W', i, j);
                else if (i == 0 && j == 3) matrix[i][j] = new Queen('B', i, j);
                else if (i == 7 && j == 3) matrix[i][j] = new Queen('W', i, j);
                else if (i == 0 && j == 4) matrix[i][j] = new King('B', i, j);
                else if (i == 7 && j == 4) matrix[i][j] = new King('W', i, j);
                else matrix[i][j] = new Void(i, j);
            }
        }
    }

    public void play() throws IOException {
        printGame();
        do {
            turn('W');
            verifyPlayer('B');
            verifyDraw();
            if (end()) break;
            turn('B');
            verifyPlayer('W');
            verifyDraw();
        } while (!end());
    }

    public void turn(char color) {
        while (true) {
            try {
                movementInput(color);
                break;
            } catch (Exception except) {
                System.out.println(except.getMessage());
            }
        }
        printGame();
    }

    // interface Output
    public void printGame() {
        System.out.println("\n  A  B  C D  E F  G  H");

        for (int i = 0; i < matrix.length; i++) {
            System.out.printf("%d ", 8-i);

            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j]);
                System.out.print(' ');

            }
            System.out.println();
        }

    }

    // debug
    public void printPossibleMoves(int x, int y, Square[][] gameMatrix) {
        boolean[][] boolMatrix = ((Piece) gameMatrix[x][y]).availableMoves(gameMatrix);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (boolMatrix[i][j]) System.out.print("1 ");
                else System.out.print("0 ");
            }
            System.out.println();
        }
    }

    // interface InputConversion
    public int letterId(char letter) {
        for (int i = 0; i < lettersIndex.length; i++) {
            if (lettersIndex[i] == letter) return i;
        }
        return -1;
    }

    // interface InputConversion
    public int numberId(char number) {
        for (int i = 0; i < numberIndex.length; i++) {
            if (numberIndex[i] == number) return i;
        }
        return -1;
    }

    // interface InputConversion
    public int[] id(char[] input) {
        return new int[] { numberId(input[1]), letterId(input[0]) };
    }

    public void movementInput(char color) throws ChessboardException, PieceException {

        Scanner in = new Scanner(System.in);
        if (color == 'W') System.out.print("\nWHITE MOVEMENT: ");
        if (color == 'B') System.out.print("\nBLACK MOVEMENT: ");
        char[] line = in.nextLine().toUpperCase().strip().toCharArray();

        if (line.length < 4) throw new ChessboardException(9999);

        int[] select = id(new char[]{line[0], line[1]});
        int[] movement = id(new char[]{line[line.length - 2], line[line.length - 1]});
        int selectX = select[0];
        int selectY = select[1];
        int movementX = movement[0];
        int movementY = movement[1];

        if ( !(selectX >= 0 && selectX <= 7 && selectY >= 0 && selectY <= 7) // out of bounds (fora do tabuleiro)
            && !(movementX >= 0 && movementX <= 7 && movementY >= 0 && movementY <= 7) ) { // out of bounds (fora do tabuleiro)
            throw new ChessboardException(1000);
        }

        if ( !(matrix[selectX][selectY] instanceof Piece) ) throw new ChessboardException(2000); // void select

        if ( !(((Piece) matrix[selectX][selectY]).color == color) ) throw new PieceException(3000); // peça adversária select

        if ( !(((Piece) matrix[selectX][selectY]).canMove(movementX, movementY, matrix)) ) throw new PieceException(4000); // movimento inválido

        // rei em Xeque
        if (color == 'W' && whiteCheck) {
            if ( !(matrix[selectX][selectY] instanceof King) ) {
                if (!whiteValidMovements[movementX][movementY]) throw new PieceException(5000);
            } else {
                if ( !whiteCheckEscape[movementX][movementY] || check(color, movementX, movementY, matrix)) throw new PieceException(5000);
            }
        }
        if (color == 'B' && blackCheck) {
            if ( !(matrix[selectX][selectY] instanceof King) ) {
                if (!blackValidMovements[movementX][movementY]) throw new PieceException(5000);
            } else {
                if ( !blackCheckEscape[movementX][movementY] || check(color, movementX, movementY, matrix)) throw new PieceException(5000);
            }
        }

        // não estou em Xeque e não quero entrar em Xeque
        if ( (color == 'W' && !(whiteCheck)) || (color == 'B' && !(blackCheck)) ) {
            if ( !(matrix[selectX][selectY] instanceof King) ) {
                if (!(validNextMovement(color, selectX, selectY, movementX, movementY))) throw new PieceException(5000);
            } else {
                if (check(color, movementX, movementY, matrix)) throw new PieceException(5000);
            }
        }

        ((Piece) matrix[selectX][selectY]).move(movementX, movementY, matrix);

    }

    public boolean end() {
        return whiteCheckMate || blackCheckMate || draw;
    }

    public int[] getKingPosition(char color, Square[][] gameMatrix) {
        // localizando o Rei
        int[] kingPosition = new int[2];
        for (Square[] linha : gameMatrix) {
            for (Square e : linha) {
                if (e instanceof King) {
                    if (((King) e).color == color) {
                        kingPosition[0] = e.x;
                        kingPosition[1] = e.y;
                        return kingPosition;
                    }
                }
            }
        }

        return new int[0];
    }

    public void verifyPlayer(char color) {

        if (color == 'W') whiteCheckMate = false; whiteCheck = false;
        if (color == 'B') blackCheckMate = false; blackCheck = false;

        boolean checkMateCondition = true;

        int[] kingPosition = getKingPosition(color, matrix);

        ArrayList <int[]> piecesThatCheck = new ArrayList<>();

        // encontrar peças que podem dar Xeque
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {

                if ( matrix[i][j] instanceof Piece && !(matrix[i][j] instanceof King) ) {
                    if ( ((Piece) matrix[i][j]).color != color) {

                        if ( ((Piece) matrix[i][j]).canMove(kingPosition[0], kingPosition[1], matrix) ) {
                            piecesThatCheck.add(new int[] {i, j});
                        }

                    }
                }

            }
        }

        if (piecesThatCheck.size() == 1) {
            boolean[][] esc = new boolean[8][8];
            boolean[][] blocked = new boolean[8][8];

            for (int[] piecePos : piecesThatCheck) { // 1 iteração (ARRAYLIST... )
                esc = ((Piece) matrix[piecePos[0]][piecePos[1]]).getKingEscapePosition(kingPosition, matrix);
                blocked = ((Piece) matrix[piecePos[0]][piecePos[1]]).getBlockingPosition(kingPosition);

                // init _ValidMovements
                for (int i = 0; i <= 7 ; i++) {
                    for (int j = 0; j <= 7 ; j++) {
                        if (color == 'W') {
                            whiteValidMovements[i][j] = false;
                        }
                        if (color == 'B') {
                            blackValidMovements[i][j] = false;
                        }
                    }
                }

                for (int i = 0; i <= 7 ; i++) {
                    for (int j = 0; j <= 7 ; j++) {

                        // blocked
                        if (matrix[i][j] instanceof Piece && !(matrix[i][j] instanceof King) ) {
                            if ( ((Piece) matrix[i][j]).color == color ) {
                                boolean[][] pieceMovements = ((Piece) matrix[i][j]).availableMoves(matrix);

                                for (int k = 0; k <= 7 ; k++) {
                                    for (int l = 0; l <= 7 ; l++) {

                                        if ( pieceMovements[k][l] && blocked[k][l] ) {
                                            checkMateCondition = false;
                                            // break;
                                            if (color == 'W') whiteValidMovements[k][l] = true;
                                            if (color == 'B') blackValidMovements[k][l] = true;
                                        }

                                    }
                                }

                            }
                        }

                        esc[i][j] = esc[i][j] && !check(color, i, j, matrix);

                        if (esc[i][j]) {
                            checkMateCondition = false;
                        }
                    }
                }

            }

            // escape
            if (color == 'W') whiteCheckEscape = esc;
            if (color == 'B') blackCheckEscape = esc;

        }

        if (piecesThatCheck.size() == 2) {
            // esc only
            boolean[][] escMerge = new boolean[8][8];

            // init esc
            for (int i = 0; i <= 7 ; i++) {
                for (int j = 0; j <= 7 ; j++) {
                    escMerge[i][j] = true;
                }
            }

            for (int[] piecePos : piecesThatCheck) { // 2 iterações
                boolean[][] esc = ((Piece) matrix[piecePos[0]][piecePos[1]]).getKingEscapePosition(kingPosition, matrix);

                for (int i = 0; i <= 7 ; i++) {
                    for (int j = 0; j <= 7 ; j++) {
                        escMerge[i][j] = escMerge[i][j] && esc[i][j] && !(check(color, i, j, matrix));
                    }
                }

            }

            for (int i = 0; i <= 7 ; i++) {
                for (int j = 0; j <= 7 ; j++) {
                    escMerge[i][j] = escMerge[i][j] && !check(color, i, j, matrix);
                    if (escMerge[i][j]) {
                        checkMateCondition = false;
                        break;
                    }
                }
            }

            if (color == 'W') whiteCheckEscape = escMerge;
            if (color == 'B') blackCheckEscape = escMerge;

        }

        if (!piecesThatCheck.isEmpty() && checkMateCondition) {
            if (color == 'W') whiteCheckMate = true;
            if (color == 'B') blackCheckMate = true;
        }
        if (!piecesThatCheck.isEmpty() && !checkMateCondition) {
            if (color == 'W') whiteCheck = true;
            if (color == 'B') blackCheck = true;
        }
        if (piecesThatCheck.isEmpty()) {
            if (color == 'W') whiteCheck = false; whiteCheckMate = false;
            if (color == 'B') blackCheck = false; blackCheckMate = false;
        }

        if (whiteCheck) System.out.println("XEQUE REI BRANCO EM AMEAÇA");
        if (whiteCheckMate) System.out.println("XEQUE MATE VITÓRIA DO PRETO");
        if (blackCheck) System.out.println("XEQUE REI PRETO EM AMEAÇA");
        if (blackCheckMate) System.out.println("XEQUE MATE VITÓRIA DO BRANCO");
    }

    // [ EM DESENVOLVIMENTO ]
    public void verifyDraw() {
        int cont = 0;
        int bispo = 0;
        int cavalo = 0;
        for (int i = 0; i <= 7 ; i++) {
            for (int j = 0; j <= 7 ; j++) {
                if (matrix[i][j] instanceof Piece) cont++;
                if (matrix[i][j] instanceof Bishop) bispo++;
                if (matrix[i][j] instanceof Knight) cavalo++;
            }
        }
        if (cont == 2) draw = true;
        if (cont == 3 && bispo == 1) draw = true;
        if (cont == 3 && cavalo == 1) draw = true;
    }

    public boolean validNextMovement(char color, int selX, int selY, int movX, int movY) {
        Square[][] nextMatrix = new Square[8][8];
        for (int i = 0; i <= 7 ; i++) {
            for (int j = 0; j <= 7 ; j++) {
                nextMatrix[i][j] = matrix[i][j];
            }
        }
        ((Piece) nextMatrix[selX][selY]).move(movX, movY, nextMatrix);

        int[] kingPosition = getKingPosition(color, nextMatrix);

        // encontrar peças que podem dar Xeque
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {

                if ( nextMatrix[i][j] instanceof Piece && !(nextMatrix[i][j] instanceof King) ) {
                    if ( ((Piece) nextMatrix[i][j]).color != color) {

                        if ( ((Piece) nextMatrix[i][j]).canMove(kingPosition[0], kingPosition[1], nextMatrix) ) {
                            return false;
                        }

                    }
                }

            }
        }

        return true;
    }

    public boolean check(char color, int x, int y, Square[][] gameMatrix) {
        for (int i = 0; i <= 7 ; i++) {
            for (int j = 0; j <= 7 ; j++) {

                if (gameMatrix[i][j] instanceof Piece) {
                    if ( ((Piece) gameMatrix[i][j]).color != color ) {
                        if ( ((Piece) gameMatrix[i][j]).canMove(x, y, gameMatrix) ) return true;
                    }
                }

            }
        }

        return false;
    }

    /*
    debug tbm
    public void printBooleanMatrix(boolean[][] boolMatrix) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (boolMatrix[i][j]) System.out.print("1 ");
                else System.out.print("0 ");
            }
            System.out.println();
        }
        System.out.println();
    }
    */
}
