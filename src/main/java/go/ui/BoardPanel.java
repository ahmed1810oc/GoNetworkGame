package go.ui;

import go.model.Board;
import go.model.Stone;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Draws the Go board and handles mouse clicks.
 */
public class BoardPanel extends JPanel {

    private Stone[][] board;
    private BoardClickListener clickListener;

    private final int padding = 45;

    public BoardPanel() {
        setPreferredSize(new Dimension(620, 620));

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
        setStoneAt(row, col, stone);
        repaint();
    }

    public void setStoneAt(int row, int col, Stone stone) {
        if (row >= 0 && row < Board.SIZE && col >= 0 && col < Board.SIZE) {
            board[row][col] = stone;
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

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        drawWoodBackground(g2);
        drawGrid(g2);
        drawStarPoints(g2);
        drawStones(g2);
    }

    private void drawWoodBackground(Graphics2D g2) {
        GradientPaint gradient = new GradientPaint(
                0,
                0,
                new Color(230, 190, 120),
                getWidth(),
                getHeight(),
                new Color(200, 145, 75)
        );

        g2.setPaint(gradient);
        g2.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 25, 25);
    }

    private void drawGrid(Graphics2D g2) {
        int cellSize = getCellSize();

        g2.setColor(new Color(45, 30, 15));
        g2.setStroke(new BasicStroke(2));

        for (int i = 0; i < Board.SIZE; i++) {
            int position = padding + i * cellSize;

            g2.drawLine(
                    padding,
                    position,
                    padding + (Board.SIZE - 1) * cellSize,
                    position
            );

            g2.drawLine(
                    position,
                    padding,
                    position,
                    padding + (Board.SIZE - 1) * cellSize
            );
        }
    }

    private void drawStarPoints(Graphics2D g2) {
        int cellSize = getCellSize();

        int[][] starPoints = {
            {2, 2},
            {2, 6},
            {4, 4},
            {6, 2},
            {6, 6}
        };

        g2.setColor(new Color(45, 30, 15));

        for (int[] point : starPoints) {
            int row = point[0];
            int col = point[1];

            int x = padding + col * cellSize;
            int y = padding + row * cellSize;

            g2.fillOval(x - 5, y - 5, 10, 10);
        }
    }

    private void drawStones(Graphics2D g2) {
        int cellSize = getCellSize();
        int stoneSize = cellSize - 10;

        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                if (board[row][col] == Stone.EMPTY) {
                    continue;
                }

                int x = padding + col * cellSize - stoneSize / 2;
                int y = padding + row * cellSize - stoneSize / 2;

                // Shadow
                g2.setColor(new Color(0, 0, 0, 80));
                g2.fillOval(x + 3, y + 4, stoneSize, stoneSize);

                if (board[row][col] == Stone.BLACK) {
                    g2.setColor(Color.BLACK);
                    g2.fillOval(x, y, stoneSize, stoneSize);

                    g2.setColor(new Color(70, 70, 70));
                    g2.drawOval(x + 3, y + 3, stoneSize - 6, stoneSize - 6);

                } else if (board[row][col] == Stone.WHITE) {
                    g2.setColor(Color.WHITE);
                    g2.fillOval(x, y, stoneSize, stoneSize);

                    g2.setColor(Color.BLACK);
                    g2.drawOval(x, y, stoneSize, stoneSize);

                    g2.setColor(new Color(220, 220, 220));
                    g2.drawOval(x + 3, y + 3, stoneSize - 6, stoneSize - 6);
                }
            }
        }
    }

    public interface BoardClickListener {

        void onBoardClick(int row, int col);
    }
}
