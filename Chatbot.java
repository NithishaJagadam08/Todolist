import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Chatbot extends JFrame implements ActionListener {
    private JTextArea chatArea;
    private JTextField userInput;
    private JButton sendButton;
    private HashMap<String, String> responses;

    public Chatbot() {
        setTitle("AI Chatbot");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 16));
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        userInput = new JTextField();
        userInput.setFont(new Font("Arial", Font.PLAIN, 16));
        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Arial", Font.BOLD, 16));

        sendButton.addActionListener(this);
        userInput.addActionListener(this);

        panel.add(userInput, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);

        add(panel, BorderLayout.SOUTH);

        setupResponses();

        setVisible(true);
    }

    private void setupResponses() {
        responses = new HashMap<>();
        responses.put("hello", "Hello! How can I help you today?");
        responses.put("hi", "Hi there! What can I do for you?");
        responses.put("how are you", "I'm just a chatbot, but I'm doing great!");
        responses.put("your name", "I'm ChatBot 1.0, your friendly assistant!");
        responses.put("bye", "Goodbye! Have a great day!");
        responses.put("who created you", "I was created by a Java enthusiast!");
    }

    private String getDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
        return now.format(formatter);
    }

    private String getTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return now.format(formatter);
    }

    public void actionPerformed(ActionEvent e) {
        String input = userInput.getText().trim().toLowerCase();
        userInput.setText("");

        if (input.isEmpty()) return;

        chatArea.append("You: " + input + "\n");

        String response;
        if (input.contains("date")) {
            response = "Today's date is: " + getDate();
        } else if (input.contains("time")) {
            response = "The current time is: " + getTime();
        } else {
            response = responses.getOrDefault(input, "I'm not sure how to respond to that.");
        }

        chatArea.append("Bot: " + response + "\n\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Chatbot::new);
    }
}
