public abstract class Piece extends Square {
    protected char color;
    protected char target;

    public Piece(char color, int x, int y) {
        this.color = color;
        if (color == 'W') target = 'B';
        if (color == 'B') target = 'W';
        this.x = x;
        this.y = y;
    }
    
    public abstract void move(int moveX, int moveY, Square[][] gameMatrix);

    public abstract boolean canMove(int moveX, int moveY, Square[][] gameMatrix);

    public abstract boolean[][] availableMoves(Square[][] gameMatrix);

    public boolean[][] getBlockingPosition(int[] kingPos) {
        boolean[][] blocked = new boolean[8][8];

        for (int i = 0; i <= 7 ; i++) {
            for (int j = 0; j <= 7 ; j++) {
                blocked[i][j] = false;
            }
        }
        blocked[x][y] = true;
        return blocked;
    }

    public boolean[][] getKingInCheckPosition(int[] kingPos) {
        boolean[][] stillInFireLine = new boolean[8][8];

        for (int i = 0; i <= 7 ; i++) {
            for (int j = 0; j <= 7 ; j++) {
                stillInFireLine[i][j] = false;
            }
        }
        stillInFireLine[kingPos[0]][kingPos[1]] = true;

        return stillInFireLine;
    }

    public boolean[][] getKingEscapePosition(int[] kingPos, Square[][] gameMatrix) {
        boolean[][] escape = ( (Piece) gameMatrix[kingPos[0]][kingPos[1]]).availableMoves(gameMatrix);
        boolean[][] doNotMakeThoseMovements = getKingInCheckPosition(kingPos);

        for (int i = 0; i <= 7 ; i++) {
            for (int j = 0; j <= 7 ; j++) {
                if (doNotMakeThoseMovements[i][j]) escape[i][j] = false;
            }
        }

        return escape;
    }
}