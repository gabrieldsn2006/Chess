import java.io.IOException;

public class Main {
    // https://en.wikipedia.org/wiki/Chess

    // POSIÇÃO TABULEIRO
    /* A8 B8 C8 D8 E8 F8 G8 H8 *
     * A7 B7 C7 D7 E7 F7 G7 H7 *
     * A6 B6 C6 D6 E6 F6 G6 H6 *
     * A5 B5 C5 D5 E5 F5 G5 H5 *
     * A4 B4 C4 D4 E4 F4 G4 H4 *
     * A3 B3 C3 D3 E3 F3 G3 H3 *
     * A2 B2 C2 D2 E2 F2 G2 H2 *
     * A1 B1 C1 D1 E1 F1 G1 H1 */

    // POSIÇÃO MATRICIAL
    /* 00 01 02 03 04 05 06 07 *
     * 10 11 12 13 14 15 16 17 *
     * 20 21 22 23 24 25 26 27 *
     * 30 31 32 33 34 35 36 37 *
     * 40 41 42 43 44 45 46 47 *
     * 50 51 52 53 54 55 56 57 *
     * 60 61 62 63 64 65 66 67 *
     * 70 71 72 73 74 75 76 77 */

    static Game ChessMatch = new Game();

    public static void main(String[] args) throws IOException {
        ChessMatch.play();
    }
}
