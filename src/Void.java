public class Void extends Square {
    public char squareColor;

    public Void(int x, int y) {
        if ((x + y) % 2 == 0) squareColor = 'W';
        if ((x + y) % 2 == 1) squareColor = 'B';
    }

    @Override
    public String toString() {
        if (squareColor == 'W') return "▬";
        if (squareColor == 'B') return "▭";
        else return "";
    }
}
