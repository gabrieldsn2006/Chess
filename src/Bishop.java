public class Bishop extends Piece {
    public Bishop(char color, int x, int y) {
        super(color, x, y);
    }

    @Override
    public void move(int moveX, int moveY, Square[][] gameMatrix) {
        gameMatrix[x][y] = new Void(x, y);
        gameMatrix[moveX][moveY] = new Bishop(color, moveX, moveY);
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

                gameMatrixBin[i][j] = mainDiagonalMove(i, j, gameMatrix);
                if (secondaryDiagonalMove(i, j, gameMatrix)) gameMatrixBin[i][j] = true;
            }
        }

        return gameMatrixBin;
    }

    @Override
    public String toString() {
        if (color == 'W') return "♝";
        if (color == 'B') return "♗";
        else return "";
    }

    private boolean mainDiagonalMove(int moveX, int moveY, Square[][] gameMatrix) {
        // up
        for (int i = 1, j = 1; x - i >= 0 && y - j >= 0 ; i++, j++) { // - -
            // localizando peças no caminho
            if (gameMatrix[x-i][y-j] instanceof Piece) {
                // verificando movimento
                if (moveX == x-i && moveY == y-j) {
                    return ( ((Piece) gameMatrix[moveX][moveY]).color == target );
                } else break;
            }
            // verificando movimento

            if (moveX == x-i && moveY == y-j) {
                return true;
            }
        }

        // down
        for (int i = 1, j = 1; x + i <= 7 && y + j <= 7 ; i++, j++) { // + +
            // localizando peças no caminho
            if (gameMatrix[x+i][y+j] instanceof Piece) {
                // verificando movimento
                if (moveX == x+i && moveY == y+j) {
                    return ( ((Piece) gameMatrix[moveX][moveY]).color == target );
                } else break;
            }
            // verificando movimento
            if (moveX == x+i && moveY == y+j) {
                return true;
            }
        }

        return false;
    }

    private boolean secondaryDiagonalMove(int moveX, int moveY, Square[][] gameMatrix) {
        // up
        for (int i = 1, j = 1; x - i >= 0 && y + j <= 7 ; i++, j++) { // - +
            // localizando peças no caminho
            if (gameMatrix[x-i][y+j] instanceof Piece) {
                // verificando movimento
                if (moveX == x-i && moveY == y+j) {
                    return ( ((Piece) gameMatrix[moveX][moveY]).color == target );
                } else break;
            }
            // verificando movimento
            if (moveX == x-i && moveY == y+j) {
                return true;
            }
        }

        // down
        for (int i = 1, j = 1; x + i <= 7 && y - j >= 0 ; i++, j++) { // + -
            // localizando peças no caminho
            if (gameMatrix[x+i][y-j] instanceof Piece) {
                // verificando movimento
                if (moveX == x+i && moveY == y-j) {
                    return ( ((Piece) gameMatrix[moveX][moveY]).color == target );
                } else break;
            }
            // verificando movimento
            if (moveX == x+i && moveY == y-j) {
                return true;
            }
        }

        return false;
    }

    public boolean[][] getBlockingPosition(int[] kingPos) {
        boolean[][] blocked = new boolean[8][8];

        boolean mainUp = false, mainDown = false, secondaryUp = false, secondaryDown = false;

        // init false
        for (int i = 0; i <= 7 ; i++) {
            for (int j = 0; j <= 7 ; j++) {
                blocked[i][j] = false;
            }
        }

        // localizar direção do rei (para o bispo podem ser 4 diferentes)
        if (kingPos[0] < x && kingPos[1] < y) mainUp = true;
        if (kingPos[0] > x && kingPos[1] > y) mainDown = true;
        if (kingPos[0] < x && kingPos[1] > y) secondaryUp = true;
        if (kingPos[0] > x && kingPos[1] < y) secondaryDown = true;

        // preenchendo
        if (mainUp) { // - -
            for (int i = 0, j = 0; x - i > kingPos[0] && y - j > kingPos[1] ; i++, j++) {
                blocked[x-i][y-j] = true;
            }
        }
        if (mainDown) { // + +
            for (int i = 0, j = 0; x + i < kingPos[0] && y + j < kingPos[1] ; i++, j++) {
                blocked[x+i][y+j] = true;
            }
        }
        if (secondaryUp) { // - +
            for (int i = 0, j = 0; x - i > kingPos[0] && y + j < kingPos[1] ; i++, j++) {
                blocked[x-i][y+j] = true;
            }
        }
        if (secondaryDown) { // + -
            for (int i = 0, j = 0; x + i < kingPos[0] && y - j > kingPos[1] ; i++, j++) {
                blocked[x+i][y-j] = true;
            }
        }

        return blocked;
    }

    public boolean[][] getKingInCheckPosition(int[] kingPos) {
        boolean[][] stillInFireLine = new boolean[8][8];

        boolean main = false, secondary = false;

        // init false
        for (int i = 0; i <= 7 ; i++) {
            for (int j = 0; j <= 7 ; j++) {
                stillInFireLine[i][j] = false;
            }
        }

        // localizar direção do rei (para o bispo podem ser 4 diferentes)
        if (kingPos[0] < x && kingPos[1] < y || kingPos[0] > x && kingPos[1] > y) main = true;
        if (kingPos[0] < x && kingPos[1] > y || kingPos[0] > x && kingPos[1] < y) secondary = true;

        // preenchendo
        if (main) { // - - + +
            if (kingPos[0] > 0 && kingPos[1] > 0 && !(x == kingPos[0]-1 && y == kingPos[1]-1)) stillInFireLine[kingPos[0]-1][kingPos[1]-1] = true;
            if (kingPos[0] < 7 && kingPos[1] < 7 && !(x == kingPos[0]+1 && y == kingPos[1]+1)) stillInFireLine[kingPos[0]+1][kingPos[1]+1] = true;
        }
        if (secondary) { // - + + -
            if (kingPos[0] > 0 && kingPos[1] < 7 && !(x == kingPos[0]-1 && y == kingPos[1]+1)) stillInFireLine[kingPos[0]-1][kingPos[1]+1] = true;
            if (kingPos[0] < 7 && kingPos[1] > 0 && !(x == kingPos[0]+1 && y == kingPos[1]-1)) stillInFireLine[kingPos[0]+1][kingPos[1]-1] = true;
        }

        return stillInFireLine;
    }
}
