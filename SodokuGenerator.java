import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class SudokuGenerator {

    private int[][] board = new int[9][9];
    private int[][] solution = new int[9][9];
    private boolean[][] original = new boolean[9][9];
    private Random rand = new Random();

    public static void main(String[] args) {
        SudokuGenerator gen = new SudokuGenerator();
        gen.generate();
        gen.removeNumbers(40);
        System.out.println("Sudoku Puzzle - fill in the blanks!");
        System.out.println("Enter moves as: row col number (1-9)");
        System.out.println("Type 0 0 0 to quit.");
        System.out.println();
        gen.printBoard(true);
        gen.playGame();
    }

    // ─── PART 1 ───────────────────────────────────

    public void generate() {
        for (int r = 0; r < 9; r++) {
            boolean success = false;
            while (!success) {
                clearRow(r);
                success = fillRow(r);
            }
        }
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                solution[r][c] = board[r][c];
            }
        }
    }

    private void clearRow(int r) {
        for (int c = 0; c < 9; c++) {
            board[r][c] = 0;
        }
    }

    private boolean fillRow(int r) {
        for (int c = 0; c < 9; c++) {
            ArrayList<Integer> pool = buildPool(r, c);
            if (pool.size() == 0) {
                return false;
            }
            int idx = rand.nextInt(pool.size());
            board[r][c] = pool.get(idx);
        }
        return true;
    }

    private ArrayList<Integer> buildPool(int r, int c) {
        ArrayList<Integer> pool = new ArrayList<Integer>();
        for (int n = 1; n <= 9; n++) {
            pool.add(n);
        }

        shuffleList(pool);

        // Remove numbers already in this row
        for (int col = 0; col < c; col++) {
            removeFromList(pool, board[r][col]);
        }

        // Remove numbers already in this column
        for (int row = 0; row < r; row++) {
            removeFromList(pool, board[row][c]);
        }

        // Remove numbers already in this 3x3 box
        int boxRowStart = (r / 3) * 3;
        int boxColStart = (c / 3) * 3;
        for (int i = boxRowStart; i < boxRowStart + 3; i++) {
            for (int j = boxColStart; j < boxColStart + 3; j++) {
                if (i != r || j != c) {
                    removeFromList(pool, board[i][j]);
                }
            }
        }

        return pool;
    }

    private void shuffleList(ArrayList<Integer> list) {
        for (int i = list.size() - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, temp);
        }
    }

    private void removeFromList(ArrayList<Integer> list, int value) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == value) {
                list.remove(i);
                return;
            }
        }
    }

    public boolean validateBoard() {
        for (int r = 0; r < 9; r++) {
            int[] row = new int[9];
            for (int c = 0; c < 9; c++) {
                row[c] = board[r][c];
            }
            if (!isValidSet(row)) return false;
        }
        for (int c = 0; c < 9; c++) {
            int[] col = new int[9];
            for (int r = 0; r < 9; r++) {
                col[r] = board[r][c];
            }
            if (!isValidSet(col)) return false;
        }
        for (int boxRow = 0; boxRow < 3; boxRow++) {
            for (int boxCol = 0; boxCol < 3; boxCol++) {
                int[] box = new int[9];
                int idx = 0;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        box[idx] = board[boxRow * 3 + i][boxCol * 3 + j];
                        idx++;
                    }
                }
                if (!isValidSet(box)) return false;
            }
        }
        return true;
    }

    private boolean isValidSet(int[] nums) {
        boolean[] seen = new boolean[10];
        for (int i = 0; i < nums.length; i++) {
            int n = nums[i];
            if (n < 1 || n > 9 || seen[n]) return false;
            seen[n] = true;
        }
        return true;
    }

    // ─── PART 2 ───────────────────────────────────

    public void removeNumbers(int count) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                original[r][c] = true;
            }
        }

        int removed = 0;
        while (removed < count) {
            int r = rand.nextInt(9);
            int c = rand.nextInt(9);
            if (board[r][c] != 0) {
                board[r][c] = 0;
                original[r][c] = false;
                removed++;
            }
        }
    }

    public void playGame() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Your move (row col number): ");
            int r = scanner.nextInt() - 1;
            int c = scanner.nextInt() - 1;
            int num = scanner.nextInt();

            // Quit condition
            if (r == -1 && c == -1 && num == 0) {
                System.out.println("Thanks for playing!");
                break;
            }

            // Validate input range
            if (r < 0 || r > 8 || c < 0 || c > 8 || num < 1 || num > 9) {
                System.out.println("Invalid input. Row and column must be 1-9, number must be 1-9.");
                continue;
            }

            // Prevent overwriting original clue cells
            if (original[r][c]) {
                System.out.println("That cell is a clue and cannot be changed.");
                continue;
            }

            // Place the number
            board[r][c] = num;
            printBoard(true);

            // Check if puzzle is complete
            if (isBoardFull()) {
                if (validateBoard()) {
                    System.out.println("Congratulations! You solved the puzzle!");
                } else {
                    System.out.println("Board is full but has errors. Keep trying!");
                    board[r][c] = 0;
                }
                break;
            }
        }

        scanner.close();
    }

    private boolean isBoardFull() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == 0) return false;
            }
        }
        return true;
    }

    public void printBoard(boolean showBlanks) {
        System.out.println("+-------+-------+-------+");
        for (int r = 0; r < 9; r++) {
            System.out.print("| ");
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == 0 && showBlanks) {
                    System.out.print(".");
                } else {
                    System.out.print(board[r][c]);
                }
                if (c == 2 || c == 5) {
                    System.out.print(" | ");
                } else if (c < 8) {
                    System.out.print(" ");
                }
            }
            System.out.println(" |");
            if (r == 2 || r == 5) {
                System.out.println("+-------+-------+-------+");
            }
        }
        System.out.println("+-------+-------+-------+");
        System.out.println();
    }
}
