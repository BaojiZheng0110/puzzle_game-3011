import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {

    private int size;
    private Tile[] [] tiles;
    private int emptyRow;
    private int emptyCol;
    private Tile[] [] initialTiles;

    public Board(int size) {
        this.size = size;
        this.tiles = new Tile[size] [size];
    }

    public Tile getTile(int row, int col) {
        return tiles[row] [col];
    }

    public int getSize() {
        return size;
    }

    public void initializeSolvedBoard() {
        int id = 1;

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {

                if (row == size - 1 && col == size - 1) {
                    tiles[row][col] = new Tile(0, row, col, null, true);
                    emptyRow = row;
                    emptyCol = col;
                } else {
                    tiles[row][col] = new Tile(id, row, col, "tile_" + id, false);
                    id++;
                }
            }
        }
    }

    public boolean canMove(int row, int col) {
        return Math.abs(row - emptyRow) + Math.abs(col - emptyCol) == 1;
        //this formula will determine whether the tile can move to the current chosed place
    }

    public void swapTiles(int r1, int c1, int r2, int c2) { //this will swap the tiles

        Tile temp = tiles[r1][c1];

        tiles[r1][c1] = tiles[r2][c2];
        tiles[r2][c2] = temp;

        tiles[r1][c1].setPosition(r1, c1);
        tiles[r2][c2].setPosition(r2, c2);
    }

    public boolean moveTile(int row, int col) {

        if (!canMove(row, col)) {   //if this tile is next to the empty space, do nothing
            return false;
        }

        swapTiles(row, col, emptyRow, emptyCol);

        emptyRow = row;     //the empty space have alreadly move to the tile position
        emptyCol = col;     //the row and col of the empty space is changed

        return true;
    }

    public boolean isSolved() {

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {

                Tile tile = tiles[row][col];
                if (!tile.isInCorrectPosition()) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<int[]> getMovablePositions() {

        List<int[]> result = new ArrayList<>();

        int[][] directions = {
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };

        for (int[] d : directions) {

            int newRow = emptyRow + d[0];
            int newCol = emptyCol + d[1];

            if (newRow >= 0 && newRow < size &&
                newCol >= 0 && newCol < size) {

                    result.add(new int[]{newRow, newCol});
            }
        }
        return result;
    }

    public void shuffle() {
        Random random = new Random();

        int shuffleSteps = size * size * 20;

        for (int i = 0; i < shuffleSteps; i++) {
            List<int[]> movablePositions = getMovablePositions();

            int[] chosen = movablePositions.get(random.nextInt(movablePositions.size()));
            int row = chosen[0];
            int col = chosen[1];

            moveTile(row, col);
        }
        initialTiles = copyTiles();
    }

    private Tile[][] copyTiles() {
        Tile[][] copy = new Tile[size][size];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Tile original = tiles[row][col];

                Tile newTile = new Tile(
                    original.getId(),
                    original.getCorrectRow(),
                    original.getCorrectCol(),
                    original.getImagePiece(),
                    original.isEmpty()
                );

                newTile.setPosition(original.getCurrentRow(), original.getCurrentCol());
                copy[row][col] = newTile;

            }
        }
        return copy;
    }

    public void reset() {       //reset the puzzle to original state
        if (initialTiles == null) {
            return;
        }

        tiles = new Tile[size][size];

        for (int row = 0; row < size; row++) {      //copy initialTiles to tiles
            for (int col = 0; col < size; col++) {
                Tile original = initialTiles[row][col];

                Tile newTile = new Tile(
                    original.getId(),
                    original.getCorrectRow(),
                    original.getCorrectCol(),
                    original.getImagePiece(),
                    original.isEmpty()
                );

                newTile.setPosition(original.getCurrentRow(), original.getCurrentCol());
                tiles[row][col] = newTile;

                if (newTile.isEmpty()) {        //reset the empty tile position
                    emptyRow = row;
                    emptyCol = col;
                }
            }
        }
    }


}