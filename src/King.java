// VERIFICAR CHECK AO MOVIMENTAR (talvez não aqui mas na classe Game, para disparar exceção)

public class King extends Piece {
    public King(char color, int x, int y) {
        super(color, x, y);
    }

    @Override
    public void move(int moveX, int moveY, Square[][] gameMatrix) {
        gameMatrix[x][y] = new Void(x, y);
        gameMatrix[moveX][moveY] = new King(color, moveX, moveY);
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
                gameMatrixBin[i][j] = radialMove(i, j, gameMatrix);
            }
        }

        return gameMatrixBin;
    }

    @Override
    public String toString() {
        if (color == 'W') return "♚";
        if (color == 'B') return "♔";
        else return "";
    }

    boolean radialMove(int moveX, int moveY, Square[][] gameMatrix) {
        if (moveX != x || moveY != y) {
            if ((moveX <= x + 1 && moveX >= x - 1) && (moveY <= y + 1 && moveY >= y - 1)) {
                if (gameMatrix[moveX][moveY] instanceof Piece) {
                    return (((Piece) gameMatrix[moveX][moveY]).color == target);
                } else return true;
            }
        }
        return false;
    }



}