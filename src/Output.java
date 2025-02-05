import java.io.File;

public interface Output {
    File hist = new File(".\\hist.txt");

    void printGame();

    void printPossibleMoves(int x, int y, Square[][] gameMatrix);

}
