import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class  Calculator extends JFrame implements ActionListener {
    private JTextField display;
    private StringBuilder currentInput;
    private double num1 = 0, num2 = 0;
    private String operator = "";
    private boolean newCalculation = false;

    public  Calculator() {
        setTitle("Calculator");
        setSize(350, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        display = new JTextField();
        display.setFont(new Font("Arial", Font.BOLD, 28));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        add(display, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 5, 5));

        String[] buttons = {
            "7", "8", "9", "/", 
            "4", "5", "6", "*", 
            "1", "2", "3", "-", 
            "0", ".", "=", "+",
            "C", "⌫", "±", "%"
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 22));
            button.addActionListener(this);
            panel.add(button);
        }

        add(panel, BorderLayout.CENTER);

        currentInput = new StringBuilder();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String input = e.getActionCommand();

        if (input.matches("[0-9]")) {  // Number clicked
            if (newCalculation) {
                currentInput.setLength(0);
                newCalculation = false;
            }
            currentInput.append(input);
            display.setText(currentInput.toString());
        } 
        else if (input.equals(".")) {  // Decimal point
            if (!currentInput.toString().contains(".")) {
                currentInput.append(".");
                display.setText(currentInput.toString());
            }
        } 
        else if (input.matches("[/*\\-+]")) {  // Operator clicked
            if (!currentInput.toString().isEmpty()) {
                num1 = Double.parseDouble(currentInput.toString());
                operator = input;
                currentInput.setLength(0);
            }
        } 
        else if (input.equals("=")) {  // Equals clicked
            if (!currentInput.toString().isEmpty() && !operator.isEmpty()) {
                num2 = Double.parseDouble(currentInput.toString());
                double result = calculate(num1, num2, operator);
                display.setText(String.valueOf(result));
                currentInput.setLength(0);
                currentInput.append(result);
                newCalculation = true;
                operator = "";
            }
        } 
        else if (input.equals("C")) {  // Clear button
            num1 = num2 = 0;
            operator = "";
            currentInput.setLength(0);
            display.setText("");
        } 
        else if (input.equals("BackSpace")) {  // Backspace button
            if (currentInput.length() > 0) {
                currentInput.setLength(currentInput.length() - 1);
                display.setText(currentInput.toString());
            }
        } 
        else if (input.equals("±")) {  // Toggle negative/positive
            if (currentInput.length() > 0) {
                if (currentInput.charAt(0) == '-') {
                    currentInput.deleteCharAt(0);
                } else {
                    currentInput.insert(0, '-');
                }
                display.setText(currentInput.toString());
            }
        } 
        else if (input.equals("%")) {  // Percentage
            if (!currentInput.toString().isEmpty()) {
                double value = Double.parseDouble(currentInput.toString()) / 100;
                display.setText(String.valueOf(value));
                currentInput.setLength(0);
                currentInput.append(value);
            }
        }
    }

    private double calculate(double a, double b, String op) {
        return switch (op) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> (b != 0) ? a / b : Double.NaN;
            default -> 0;
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Calculator::new);
    }
}
