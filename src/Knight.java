public class Knight extends Piece {
    public Knight(char color, int x, int y) {
        super(color, x, y);
    }

    @Override
    public void move(int moveX, int moveY, Square[][] gameMatrix) {
        gameMatrix[x][y] = new Void(x, y);
        gameMatrix[moveX][moveY] = new Knight(color, moveX, moveY);
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

                gameMatrixBin[i][j] = upMove(i, j, gameMatrix);
                if (rightMove(i, j, gameMatrix)) gameMatrixBin[i][j] = true;
                if (downMove(i, j, gameMatrix)) gameMatrixBin[i][j] = true;
                if (leftMove(i, j, gameMatrix)) gameMatrixBin[i][j] = true;
            }
        }

        return gameMatrixBin;
    }

    @Override
    public String toString() {
        if (color == 'W') return "♞";
        if (color == 'B') return "♘";
        else return "";
    }

    private boolean upMove(int moveX, int moveY, Square[][] gameMatrix) {
        if ( (moveX == x - 2) && ((moveY == y - 1) || (moveY == y + 1)) ) {
            if (gameMatrix[moveX][moveY] instanceof Piece) {
                return ((Piece) gameMatrix[moveX][moveY]).color == target;
            } else return true;
        }
        return false;
    }
    private boolean rightMove(int moveX, int moveY, Square[][] gameMatrix) {
        if ( ((moveX == x - 1) || (moveX == x + 1)) && (moveY == y + 2 ) && (gameMatrix[x][y] instanceof Piece) ) {
            if (gameMatrix[moveX][moveY] instanceof Piece){
                return ((Piece) gameMatrix[moveX][moveY]).color == target;
            } else return true;
        }
        return false;
    }
    private boolean downMove(int moveX, int moveY, Square[][] gameMatrix) {
        if ( (moveX == x + 2) && ((moveY == y + 1) || (moveY == y - 1)) && (gameMatrix[x][y] instanceof Piece) ) {
            if (gameMatrix[moveX][moveY] instanceof Piece){
                return ((Piece) gameMatrix[moveX][moveY]).color == target;
            } else return true;
        }
        return false;
    }
    private boolean leftMove(int moveX, int moveY, Square[][] gameMatrix) {
        if ( ((moveX == x + 1) || (moveX == x - 1)) && (moveY == y - 2) && (gameMatrix[x][y] instanceof Piece) ) {
            if (gameMatrix[moveX][moveY] instanceof Piece){
                return ((Piece) gameMatrix[moveX][moveY]).color == target;
            } else return true;
        }
        return false;
    }

    public boolean[][] getKingInCheckPosition(int[] kingPos) {
        boolean[][] stillInFireLine = new boolean[8][8];

        for (int i = 0; i <= 7 ; i++) {
            for (int j = 0; j <= 7 ; j++) {
                stillInFireLine[i][j] = false;
            }
        }

        if ( (kingPos[0] == x-2 && kingPos[1] == y-1) || (kingPos[0] == x+1 && kingPos[1] == y+2) ) {
            // inf esq
            if (kingPos[0] < 7 && kingPos[1] > 0) stillInFireLine[kingPos[0]+1][kingPos[1]-1] = true;
        }
        if ( (kingPos[0] == x-2 && kingPos[1] == y+1) || (kingPos[0] == x+1 && kingPos[1] == y-2) ) {
            // inf dir
            if (kingPos[0] < 7 && kingPos[1] < 7) stillInFireLine[kingPos[0]+1][kingPos[1]+1] = true;
        }
        if ( (kingPos[0] == x-1 && kingPos[1] == y+2) || (kingPos[0] == x+2 && kingPos[1] == y-1) ) {
            // sup esq
            if (kingPos[0] > 0 && kingPos[1] > 0) stillInFireLine[kingPos[0]-1][kingPos[1]-1] = true;
        }
        if ( (kingPos[0] == x+2 && kingPos[1] == y+1) || (kingPos[0] == x-1 && kingPos[1] == y-2) ) {
            // sup dir
            if (kingPos[0] > 0 && kingPos[1] < 7) stillInFireLine[kingPos[0]-1][kingPos[1]+1] = true;
        }

        return stillInFireLine;
    }
    
}
