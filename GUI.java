import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI implements ActionListener {

    private static JPanel panel;
    private static JFrame frame;
    private static JLabel label;
    private static JTextField userText;
    private static JButton button;

    public static void main(String[] args) {

        panel = new JPanel();
        frame = new JFrame();
        frame.setSize(500, 160);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        panel.setLayout(null);

        label = new JLabel("FEN (leave empty for default position)");
        label.setBounds(135, 5, 400, 25);
        panel.add(label);

        userText = new JTextField(20);
        userText.setBounds(20, 40, 445, 25);
        panel.add(userText);

        button = new JButton("ok bruh");
        button.setBounds(200, 80, 80, 25);
        button.addActionListener(new GUI());
        panel.add(button);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new Chess(userText.getText());
        frame.dispose();
    }
}
