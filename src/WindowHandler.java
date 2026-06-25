import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

public class WindowHandler extends JPanel {
    private static int screenWidth;
    private static int screenHeight;
    private static float rectVal = 26f;
    static HashMap<Integer, Color> colorMap = new HashMap<>();
    private static JButton btn1;

    private static DrawingPanel panel;

    private static void setWindowSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        screenWidth = screenSize.width;
        screenHeight = screenSize.height;
    }

    public static void setColorMap(Integer x, Color color) {
        colorMap.put(x, color);
    }

    public static Color getColorMap(Integer x) {
        return colorMap.get(x);
    }

    public static void enableButton(Boolean b) { btn1.setEnabled(b); }

    private static void frameSetup(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        frame.setContentPane(content);

        // Drawing components
        // Class declarations
        panel = new DrawingPanel();
        panel.setMax(rectVal);
        frame.add(panel);

        // Set up text fields
        JTextField field = new JTextField(screenWidth / 100);
        field.setText("How many values to sort?");
        field.setBounds(
                screenWidth / 3,
                screenHeight / 2,
                screenWidth / 3,
                screenHeight / 10
        );

        // Set up a focus listener to clear text when selected
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals("How many values to sort?")) {
                    field.setText("");
                    field.setForeground(Color.black);
                }
            }
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText("How many values to sort?");
                    field.setForeground(Color.LIGHT_GRAY);
                }
            }
        });
        field.setForeground(Color.LIGHT_GRAY);
        field.setEnabled(false);
        frame.add(field);

        // Set up buttons
        btn1 = new JButton("SORT");
        btn1.setEnabled(false);
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fieldText = field.getText();
                String digits = fieldText.replaceAll("[^0-9-]", "");
                try {
                    rectVal = Float.parseFloat(digits);
                    panel.setNumOfSlices(rectVal);

                    // Clear the text field
                    field.setText("How many values to sort?");
                    field.setForeground(Color.LIGHT_GRAY);

                    panel.drawSortedList(panel);
                } catch (NumberFormatException en) {
                    System.out.println("ERROR: Number format exception");
                }
            }
        });
        frame.add(btn1, BorderLayout.SOUTH);
        btn1.requestFocusInWindow();

        JPanel controls = new JPanel();
        controls.add(new JLabel("Sort"));
        controls.add(field);
        controls.add(btn1);

        content.add(controls, BorderLayout.NORTH);
        content.add(panel, BorderLayout.CENTER);

        panel.setBackground(Color.DARK_GRAY);
        frame.getContentPane().setBackground(Color.DARK_GRAY);

        frame.pack();

        frame.setSize((int) (screenWidth * 0.8), (int) (screenHeight * 0.8));

        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
            SwingUtilities.invokeLater(() -> {
                btn1.setEnabled(true);
                btn1.requestFocusInWindow();

                field.setEnabled(true);
            });
        });
    }

    public static void createWindow() {
        setWindowSize();

        JFrame frame = new JFrame();
        frame.setMinimumSize(new Dimension(400, 400));
        frame.setMaximumSize(new Dimension(screenWidth, screenHeight));

        frameSetup(frame);
    }
}