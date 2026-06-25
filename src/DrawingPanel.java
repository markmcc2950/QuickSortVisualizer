import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DrawingPanel extends JPanel {
    private float maxBlocks = 1f;
    private boolean drawColor = false;
    private ArrayList<Integer> myList;
    private int swapVal1 = -1;
    private int swapVal2 = -1;
    Graphics2D g2;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        drawGradient(g2);

        if (drawColor) {
            drawQuickSort(g2);
        }
    }

    public void setMax(float val) {
        maxBlocks = val;
        repaint();
    }

    private void drawGradient(Graphics2D g2) {
        int width = getWidth();
        int height = getHeight();

        for (float i = 0f; i < maxBlocks; i += 1f) {
            float hue = i / maxBlocks;
            float saturation = 1f;
            float brightness = 1f;

            Color rgb = Color.getHSBColor(hue, saturation, brightness);

            WindowHandler.setColorMap((int)(i + 1), rgb);

            int xVal = (int)((width * 0.01) + i * (0.98 * width) / maxBlocks);
            int yVal = (int)((height * 0.01));
            int rectW = (int)(0.95f * (width) / maxBlocks);
            int rectH = (int)(height * 0.3);

            if (rectW < 1) { rectW = 1; }

            g2.setColor(rgb);
            g2.fillRect(
                    xVal,
                    yVal,
                    rectW,
                    rectH
            );
        }
    }

    private void drawQuickSort(Graphics2D g2) {
        drawColor = false;

        int width = getWidth();
        int height = getHeight();

        for (int i = 0; i < myList.size(); i++) {
            int xVal = (int)((width * 0.01) + i * (0.98 * width) / maxBlocks);
            int yVal = (int)((height * 0.5));
            int rectW = (int)(0.95f * (width) / maxBlocks);
            int rectH = (int)(height * 0.3);

            Color thisColor = WindowHandler.getColorMap((int)(myList.get(i)));
            g2.setColor(thisColor);

            if (rectW < 1) { rectW = 1; }

            if (i == swapVal1) {
                rectH = (int)(height * 0.5);
                yVal = (int)(height * 0.4);
                swapVal1 = -1;
            }
            else if (i == swapVal2) {
                rectH = (int)(height * 0.5);
                yVal = (int)(height * 0.4);
                swapVal2 = -1;
            }

            g2.fillRect(
                xVal,
                yVal,
                rectW,
                rectH
            );
        }
    }

    private void redrawQuickSort() {
        drawColor = true;
        repaint();
    }

    public void drawQuickSortUpdates(Graphics2D g2, int i, int j, ArrayList<Integer> a) {
        if (i == -1 && j == -1) {
            swapVal1 = i;
            swapVal2 = j;
            redrawQuickSort();
            return;
        }
        Color thisColor = WindowHandler.getColorMap(a.get(i));

        // Map value (0 to maxBlocks) to piano frequency range (27.5 Hz to 4186 Hz)
        // Piano MIDI notes: 21 (A0) to 108 (C8), we'll use 21-88 for a comfortable range
        float normalizedValue = a.get(i) / maxBlocks; // 0.0 to 1.0
        float normalizedValue2 = a.get(j) / maxBlocks; // 0.0 to 1.0
        int pianoNote = 21 + (int)(normalizedValue * 67); // Maps to MIDI notes 21-87
        int pianoNote2 = 21 + (int)(normalizedValue2 * 67); // Maps to MIDI notes 21-87
        int frequency = (int)(27.5 * Math.pow(2, (pianoNote - 21) / 12.0));
        int frequency2 = (int)(27.5 * Math.pow(2, (pianoNote2 - 21) / 12.0));

        // Play both notes as a chord
        SoundHandler.playChord(frequency, frequency2, 50);

        g2.setColor(thisColor);
        g2.fillRect(
                (int)((getWidth() * 0.01) + i * (0.98 * getWidth()) / maxBlocks),
                (int)((getHeight() * 0.5)),
                (int)(0.95f * (getWidth()) / maxBlocks),
                (int)(getHeight() * 0.2)
        );

        swapVal1 = i;
        swapVal2 = j;

        myList = a;
        redrawQuickSort();
    }

    public void setNumOfSlices(float numOfSlices) {
        maxBlocks = numOfSlices;
        repaint();
    }

    public void drawSortedList(DrawingPanel panel) {
        myList = QuickSort.createList((int)(maxBlocks));
        drawColor = true;
        repaint();

        new Thread(() -> {
            drawColor = false;
            QuickSort.sortList(myList, panel, g2);
        }).start();

    }
}
