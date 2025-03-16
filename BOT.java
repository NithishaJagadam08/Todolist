import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class BOT extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;

    private static final String MEMORY_FILE = "chatbot_memory.txt";
    private static Map<String, String> knowledgeBase = new HashMap<>();

    public BOT() {
        // Window properties
        setTitle("AI Chatbot");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Chat display area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        // User input field
        inputField = new JTextField();
        sendButton = new JButton("Send");

        // Panel for input field and button
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Add components to the frame
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        // Load previous memory
        loadKnowledgeBase();

        // Action listener for sending messages
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = inputField.getText().trim().toLowerCase();
                if (!userInput.isEmpty()) {
                    chatArea.append("You: " + userInput + "\n");
                    String response = getResponse(userInput);
                    chatArea.append("Chatbot: " + response + "\n\n");
                    inputField.setText("");
                }
            }
        });

        // Pressing Enter should also send the message
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendButton.doClick();
            }
        });

        setVisible(true);
    }

    private static String getResponse(String input) {
        // Predefined greetings
        if (input.contains("hello") || input.contains("hi")||  input.contains("heyyy")) {
            return "Hello! How can I assist you today?";
        } else if (input.contains("how are you")) {
            return "I'm just a chatbot, but I'm doing great! Thanks for asking.";
        } else if (input.contains("what's your name") || input.contains("who are you")) {
            return "I'm an AI Chatbot powered by Wikipedia!";
        }

        // Check predefined responses
        for (String key : knowledgeBase.keySet()) {
            if (input.contains(key)) {
                return knowledgeBase.get(key);
            }
        }

        // If no match, search Wikipedia
        return getWikipediaSummary(input);
    }

    private static void loadKnowledgeBase() {
        try (BufferedReader reader = new BufferedReader(new FileReader(MEMORY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("::");
                if (parts.length == 2) {
                    knowledgeBase.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Chatbot: No previous memory found. Starting fresh.");
        }
    }

    private static String getWikipediaSummary(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String apiUrl = "https://en.wikipedia.org/api/rest_v1/page/summary/" + encodedQuery;

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Extract summary from JSON response
                String json = response.toString();
                int start = json.indexOf("\"extract\":\"") + 10;
                int end = json.indexOf("\",\"extract_html");

                if (start > 10 && end > start) {
                    return json.substring(start, end);
                }
            } else {
                return "I'm not sure about that. Try asking something else!";
            }
        } catch (Exception e) {
            return "Sorry, I couldn't retrieve information at this time.";
        }
        return "I'm not sure about that. Try asking something else!";
    }

    public static void main(String[] args) {
        new BOT();
    }
}
