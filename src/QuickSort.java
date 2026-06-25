import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;

public class QuickSort {
    private final static int pauseTime = 8000;
    private static void pause(int ms) {
        int p = ms;
        if (p < 1) { p = 50; }

        try {
            Thread.sleep(p);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static int partition(ArrayList<Integer> a, int low, int high, DrawingPanel panel, Graphics2D g2) {
        int pivot = a.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (a.get(j) <= pivot) {
                i++;

                int temp = a.get(j);
                a.set(j, a.get(i));
                a.set(i, temp);

                panel.drawQuickSortUpdates(g2, i, j, a);
                pause(pauseTime / a.size());
            }
        }

        int temp = a.get(high);
        a.set(high, a.get(i + 1));
        a.set(i + 1, temp);

        panel.drawQuickSortUpdates(g2, i + 1, high, a);
        pause(pauseTime / a.size());

        return i + 1;
    }

    private static void quickSort(ArrayList<Integer> a, int low, int high, DrawingPanel panel, Graphics2D g2) {
        if (low < high) {
            int i = partition(a, low, high, panel, g2);
            quickSort(a, low, i - 1, panel, g2);
            quickSort(a, i + 1, high, panel, g2);
        }
    }

    private static void printList(ArrayList<Integer> a) {
        System.out.print("PRINTING LIST:\n|");
        for (Integer i : a) {
            System.out.print(" " + i + " |");
        }
        System.out.println();
    }

    // Create list of random numbers and get it sorted via QuickSort
    public static ArrayList<Integer> createList(int val) {
        ArrayList<Integer> ls = new ArrayList<Integer>();
        for (int i = 0; i < val; i++) {
            int rnd = (int)(Math.random() * (val + 1));
            ls.add(rnd);
        }

        return ls;
    }

    public static void sortList(ArrayList<Integer> a, DrawingPanel panel, Graphics2D g2) {
        WindowHandler.enableButton(false);
        for (int i = 0; i < a.size(); i++) {
            int val1 = Math.min((int)(i + (a.size() / 5) % a.size()), a.size() - 1);
            panel.drawQuickSortUpdates(g2, val1, i, a);
            pause((pauseTime / a.size()) / 2);
        }

        printList(a);

        quickSort(a, 0, a.size() - 1, panel, g2);

        printList(a);

        for (int i = 0; i < a.size(); i++) {
            int val1 = Math.min((int)(i + (a.size() / 5) % a.size()), a.size() - 1);
            panel.drawQuickSortUpdates(g2, val1, i, a);
            pause((pauseTime / a.size()) / 2);
        }

        panel.drawQuickSortUpdates(g2, -1, -1, a);


        WindowHandler.enableButton(true);
    }
}
