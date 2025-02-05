public class Queen extends Piece {
    public Queen(char color, int x, int y) {
        super(color, x, y);
    }

    @Override
    public void move(int moveX, int moveY, Square[][] gameMatrix) {
        gameMatrix[x][y] = new Void(x, y);
        gameMatrix[moveX][moveY] = new Queen(color, moveX, moveY);
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

                gameMatrixBin[i][j] = verticalMove(i, j, gameMatrix);
                if (horizontalMove(i, j, gameMatrix)) gameMatrixBin[i][j] = true;
                if (mainDiagonalMove(i, j, gameMatrix)) gameMatrixBin[i][j] = true;
                if (secondaryDiagonalMove(i, j, gameMatrix)) gameMatrixBin[i][j] = true;
            }
        }
        return gameMatrixBin;
    }

    @Override
    public String toString() {
        if (color == 'W') return "♛";
        if (color == 'B') return "♕";
        else return "";
    }

    private boolean verticalMove(int moveX, int moveY, Square[][] gameMatrix) {
        if (moveY == y && moveX != x) {
            // localizando peças no caminho
            int upperBound = 0;
            int lowerBound = 7;

            for (int i = x-1; i >= 0; i--) { // UP
                if (gameMatrix[i][moveY] instanceof Piece) {
                    upperBound = i;
                    break;
                }
            }
            for (int i = x+1; i <= 7; i++) { // DOWN
                if (gameMatrix[i][moveY] instanceof Piece) {
                    lowerBound = i;
                    break;
                }
            }

            if (moveX <= lowerBound && moveX >= upperBound) {

                if (gameMatrix[moveX][moveY] instanceof Piece) {
                    return  ( ((Piece) gameMatrix[moveX][moveY]).color == target );
                } else return true;

            } else return false;
        } else return false;
    }

    private boolean horizontalMove(int moveX, int moveY, Square[][] gameMatrix) {
        if (moveX == x && moveY != y) {
            // localizando peças no caminho
            int leftBound = 0;
            int rightBound = 7;

            for (int i = y-1; i >= 0; i--) { // LEFT
                if (gameMatrix[moveX][i] instanceof Piece) {
                    leftBound = i+1;
                    break;
                }
            }
            for (int i = y+1; i <= 7; i++) { // RIGHT
                if (gameMatrix[moveX][i] instanceof Piece) {
                    rightBound = i-1;
                    break;
                }
            }

            if (moveY <= rightBound || moveY >= leftBound) {
                if (gameMatrix[moveX][moveY] instanceof Piece) {
                    return  ( ((Piece) gameMatrix[moveX][moveY]).color == target );
                } else return true;
            }
        }
        return false;
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

        boolean right = false, left = false, up = false, down = false;
        boolean mainUp = false, mainDown = false, secondaryUp = false, secondaryDown = false;

        // init false
        for (int i = 0; i <= 7 ; i++) {
            for (int j = 0; j <= 7 ; j++) {
                blocked[i][j] = false;
            }
        }

        // localizar direção do rei (para a rainha podem ser 8 diferentes)
        if (kingPos[0] > x) down = true; // rei em baixo da torre
        if (kingPos[0] < x) up = true; // rei acima da torre
        if (kingPos[1] > y) right = true; // rei a direita da torre
        if (kingPos[1] < y) left = true; // rei a esquerda da torre

        if (kingPos[0] < x && kingPos[1] < y) mainUp = true;
        if (kingPos[0] > x && kingPos[1] > y) mainDown = true;
        if (kingPos[0] < x && kingPos[1] > y) secondaryUp = true;
        if (kingPos[0] > x && kingPos[1] < y) secondaryDown = true;

        // preenchendo
        if (down) {
            for (int i = 0; i <= 7 ; i++) {
                for (int j = 0; j <= 7 ; j++) {
                    blocked[i][j] = ( j == y && i >= x && i < kingPos[0] );
                }
            }
        }
        if (up) {
            for (int i = 0; i <= 7 ; i++) {
                for (int j = 0; j <= 7 ; j++) {
                    blocked[i][j] = ( j == y && i <= x && i > kingPos[0]);
                }
            }
        }
        if (right) {
            for (int i = 0; i <= 7 ; i++) {
                for (int j = 0; j <= 7 ; j++) {
                    blocked[i][j] = ( j >= y && j < kingPos[1] && i == x );
                }
            }
        }
        if (left) {
            for (int i = 0; i <= 7 ; i++) {
                for (int j = 0; j <= 7 ; j++) {
                    blocked[i][j] = ( j <= y && j > kingPos[1] && i == x );
                }
            }
        }

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

        // preenchendo
        if (kingPos[1] == y) {
            for (int i = 0; i <= 7 ; i++) {
                for (int j = 0; j <= 7 ; j++) {
                    stillInFireLine[i][j] = ( !(x == kingPos[0]+1 || x == kingPos[0]-1) && j == y && (i == kingPos[0]+1 || i == kingPos[0]-1) );
                }
            }
        }
        if (kingPos[0] == x) {
            for (int i = 0; i <= 7 ; i++) {
                for (int j = 0; j <= 7 ; j++) {
                    stillInFireLine[i][j] = ( !(y == kingPos[0]+1 || y == kingPos[0]-1) && (j == kingPos[1]+1 || j == kingPos[1]-1) && i == x );
                }
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
