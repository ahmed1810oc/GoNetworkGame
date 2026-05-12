package go.ui;

import go.model.Board;
import go.model.Stone;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Draws the Go board and handles mouse clicks.
 */
public class BoardPanel extends JPanel {

    private Stone[][] board;
    private BoardClickListener clickListener;

    private final int padding = 40;

    public BoardPanel() {
        board = new Stone[Board.SIZE][Board.SIZE];

        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                board[row][col] = Stone.EMPTY;
            }
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleBoardClick(e.getX(), e.getY());
            }
        });
    }

    public void setBoardClickListener(BoardClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void placeStone(int row, int col, Stone stone) {
        if (row >= 0 && row < Board.SIZE && col >= 0 && col < Board.SIZE) {
            board[row][col] = stone;
            repaint();
        }
    }

    public void clearBoard() {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                board[row][col] = Stone.EMPTY;
            }
        }

        repaint();
    }

    private void handleBoardClick(int mouseX, int mouseY) {
        int cellSize = getCellSize();

        int col = Math.round((float) (mouseX - padding) / cellSize);
        int row = Math.round((float) (mouseY - padding) / cellSize);

        if (row >= 0 && row < Board.SIZE && col >= 0 && col < Board.SIZE) {
            if (clickListener != null) {
                clickListener.onBoardClick(row, col);
            }
        }
    }

    private int getCellSize() {
        int availableWidth = getWidth() - 2 * padding;
        int availableHeight = getHeight() - 2 * padding;

        return Math.min(availableWidth, availableHeight) / (Board.SIZE - 1);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        setBackground(new Color(222, 184, 135));

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.BLACK);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cellSize = getCellSize();

        drawGrid(g2, cellSize);
        drawStones(g2, cellSize);
    }

    private void drawGrid(Graphics2D g2, int cellSize) {
        for (int i = 0; i < Board.SIZE; i++) {
            int position = padding + i * cellSize;

            g2.drawLine(padding, position, padding + (Board.SIZE - 1) * cellSize, position);
            g2.drawLine(position, padding, position, padding + (Board.SIZE - 1) * cellSize);
        }
    }

    private void drawStones(Graphics2D g2, int cellSize) {
        int stoneSize = cellSize - 8;

        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                if (board[row][col] == Stone.EMPTY) {
                    continue;
                }

                int x = padding + col * cellSize - stoneSize / 2;
                int y = padding + row * cellSize - stoneSize / 2;

                if (board[row][col] == Stone.BLACK) {
                    g2.setColor(Color.BLACK);
                    g2.fillOval(x, y, stoneSize, stoneSize);
                } else if (board[row][col] == Stone.WHITE) {
                    g2.setColor(Color.WHITE);
                    g2.fillOval(x, y, stoneSize, stoneSize);

                    g2.setColor(Color.BLACK);
                    g2.drawOval(x, y, stoneSize, stoneSize);
                }
            }
        }
    }

    public interface BoardClickListener {

        void onBoardClick(int row, int col);
    }

    public void setStoneAt(int row, int col, Stone stone) {
        if (row >= 0 && row < Board.SIZE && col >= 0 && col < Board.SIZE) {
            board[row][col] = stone;
        }
    }
}
