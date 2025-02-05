// falta pouco eu acho para o En Passant
// vai dar trabalho
// falta condição para não pular peça caso possa andar duas casas.

import java.util.Scanner;

public class Pawn extends Piece {

    protected boolean firstMove;
    protected boolean enPassantCondition;
    protected int oneStep;
    protected int twoSteps;

    public Pawn(char color, int x, int y, boolean firstMove, boolean enPassantCondition) {
        super(color, x, y);
        if (color == 'W') {
            oneStep = -1;
            twoSteps = -2;
        } else if (color == 'B') {
            oneStep = +1;
            twoSteps = +2;
        }
        this.firstMove = firstMove;
        this.enPassantCondition = enPassantCondition;
    }

    public Pawn(char color, int x, int y) {
        super(color, x, y);
        if (color == 'W') {
            oneStep = -1;
            twoSteps = -2;
        } else if (color == 'B') {
            oneStep = +1;
            twoSteps = +2;
        }
        this.firstMove = false;
        this.enPassantCondition = false;
    }

    public Pawn(char color, int x, int y, boolean enPassantCondition) {
        super(color, x, y);
        if (color == 'W') {
            oneStep = -1;
            twoSteps = -2;
        } else if (color == 'B') {
            oneStep = +1;
            twoSteps = +2;
        }
        this.firstMove = false;
        this.enPassantCondition = enPassantCondition;
    }

    @Override
    public void move(int moveX, int moveY, Square[][] gameMatrix) {
        gameMatrix[x][y] = new Void(x, y);

        boolean didTwoSteps = twoStepsMove(moveX, moveY, gameMatrix);

        if (!verifyPromotion(moveX)) {
            if (didTwoSteps) {
                gameMatrix[moveX][moveY] = new Pawn(color, moveX, moveY, true);
            } else {
                gameMatrix[moveX][moveY] = new Pawn(color, moveX, moveY); // false false
            }
        } else {
            promotion(moveX, moveY, gameMatrix);
        }
    }

    @Override
    public boolean canMove(int moveX, int moveY, Square[][] gameMatrix) {
        return (availableMoves(gameMatrix))[moveX][moveY];
    }

    @Override
    public boolean[][] availableMoves(Square[][] gameMatrix) {

        boolean[][] gameMatrixBin = new boolean[8][8];

        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                gameMatrixBin[i][j] = false;

                gameMatrixBin[i][j] = oneStepMove(i, j, gameMatrix);
                if (twoStepsMove(i, j, gameMatrix)) gameMatrixBin[i][j] = true;
                if (diagonalStepMove(i, j, gameMatrix)) gameMatrixBin[i][j] = true;
                if (enPassantMove(i, j, gameMatrix)) gameMatrixBin[i][j] = true;
            }
        }

        return gameMatrixBin;
    }

    @Override
    public String toString() {
        if (color == 'W') return "♟";
        if (color == 'B') return "♙";
        else return "";
    }

    // aqui cabe mais uma Exception
    private void promotion(int moveX, int moveY, Square[][] gameMatrix) {
        while (true) {
            System.out.println("Promover para que peça?");
            System.out.println("QUEEN [Q]");
            System.out.println("BISHOP [B]");
            System.out.println("ROOK [R]");
            System.out.println("KNIGHT [K]");
            System.out.print("Digite: ");
            Scanner in = new Scanner(System.in);
            char[] piece = ((((in.nextLine()).toUpperCase()).strip()).toCharArray());
            if (piece.length == 1) {
                if (piece[0] == 'Q') {
                    gameMatrix[moveX][moveY] = new Queen(color, moveX, moveY);
                    break;
                }
                if (piece[0] == 'B') {
                    gameMatrix[moveX][moveY] = new Bishop(color, moveX, moveY);
                    break;
                }
                if (piece[0] == 'R') {
                    gameMatrix[moveX][moveY] = new Rook(color, moveX, moveY);
                    break;
                }
                if (piece[0] == 'K') {
                    gameMatrix[moveX][moveY] = new Knight(color, moveX, moveY);
                    break;
                }
            }
            System.out.println("INPUT INVALIDO4");
        }
    }

    private boolean oneStepMove(int moveX, int moveY, Square[][] gameMatrix) {
        return ( (moveX == x + oneStep) && (moveY == y) && (gameMatrix[moveX][moveY] instanceof Void) );
    }

    private boolean twoStepsMove(int moveX, int moveY, Square[][] gameMatrix) {
        return ( (moveX == x + twoSteps) && (moveY == y) && (gameMatrix[moveX][moveY] instanceof Void) && firstMove );
    }

    private boolean diagonalStepMove(int moveX, int moveY, Square[][] gameMatrix) {
        if ( (moveX == x + oneStep) && ((moveY == y + 1 || moveY == y - 1)) && (gameMatrix[moveX][moveY] instanceof Piece) ) {
            return ((Piece) gameMatrix[moveX][moveY]).color == target;
        } else return false;
    }

    private boolean enPassantMove(int moveX, int moveY, Square[][] gameMatrix) {
//        if ( (moveX == x + oneStep) && (moveY == y + 1) && (gameMatrix[moveX][moveY + 1] instanceof Piece) ) {
//            return ( ( (Piece) gameMatrix[x][moveY + 1] ).color == target );
//        } else if ( (moveX == x + oneStep) && (moveY == y - 1) && (gameMatrix[moveX][moveY - 1] instanceof Piece) ) {
//            return ( ( (Piece) gameMatrix[x][moveY - 1] ).color == target );
//        } else return false;
        return false;
    }

    private boolean verifyPromotion(int moveX) {
        if (color == 'W') {
            return moveX == 0;
        } else {
            return moveX == 7;
        }
    }
}