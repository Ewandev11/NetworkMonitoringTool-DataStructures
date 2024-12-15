package networktopology;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.swing.table.DefaultTableModel;


public class DeviceDiscoveryUI {
    private Graph graph;
    private JTextArea logArea; // Real-time log area
    private JTable outputTable; // Table for displaying discovered devices
    private DefaultTableModel tableModel; // Table model for managing table data
    private JLabel statusBar; // Status bar
    private JProgressBar progressBar; // Progress bar

    public DeviceDiscoveryUI(Graph graph) {
        this.graph = graph;
    }

    public void createAndShowGUI() {
        // Create JFrame
        JFrame frame = new JFrame("Device Discovery Tool");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        // Create Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JLabel startNodeLabel = new JLabel("Enter Starting Device:");
        JTextField startNodeField = new JTextField();
        JLabel depthLimitLabel = new JLabel("Enter Depth Limit:");
        JTextField depthLimitField = new JTextField();
        inputPanel.add(startNodeLabel);
        inputPanel.add(startNodeField);
        inputPanel.add(depthLimitLabel);
        inputPanel.add(depthLimitField);

        // Create Buttons Panel
        JPanel buttonPanel = new JPanel();
        JButton discoverButton = new JButton("Discover Devices");
        JButton reportButton = new JButton("Generate Report");
        JButton clearButton = new JButton("Clear");
        buttonPanel.add(discoverButton);
        buttonPanel.add(reportButton);
        buttonPanel.add(clearButton);

        // Create Output Table
        String[] columnNames = {"Device Name", "Connection Type", "Latency"};
        tableModel = new DefaultTableModel(columnNames, 0);
        outputTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(outputTable);

        // Create Log Area
        logArea = new JTextArea(8, 20);
        logArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logArea);

        // Create Status Bar and Progress Bar
        statusBar = new JLabel("Ready");
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);

        // Add Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Device Discovery Tool\nVersion 1.1\nDeveloped by Your Team"));
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);
        frame.setJMenuBar(menuBar);

        // Discover Button Action
        discoverButton.addActionListener((ActionEvent e) -> {
            try {
                String startNode = startNodeField.getText().trim();
                int depthLimit = Integer.parseInt(depthLimitField.getText().trim());

                // Validate Input
                if (startNode.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter a starting device.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (depthLimit < 0) {
                    JOptionPane.showMessageDialog(frame, "Depth limit must be a positive integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Perform Device Discovery
                log("Starting device discovery...");
                progressBar.setIndeterminate(true);
                List<String> discoveredDevices = graph.discoverDevices(startNode, depthLimit);
                tableModel.setRowCount(0); // Clear existing table data

                for (String device : discoveredDevices) {
                    // Example: Add device data to table
                    tableModel.addRow(new Object[]{device, "Ethernet", Math.random() * 100 + "ms"});
                }

                progressBar.setIndeterminate(false);
                statusBar.setText("Device discovery complete!");
                log("Device discovery complete!");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Depth limit must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Report Button Action
        reportButton.addActionListener((ActionEvent e) -> {
            try {
                String startNode = startNodeField.getText().trim();
                int depthLimit = Integer.parseInt(depthLimitField.getText().trim());

                // Validate Input
                if (startNode.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter a starting device.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (depthLimit < 0) {
                    JOptionPane.showMessageDialog(frame, "Depth limit must be a positive integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Perform Device Discovery and Generate Report
                log("Generating report...");
                List<String> discoveredDevices = graph.discoverDevices(startNode, depthLimit);
                generateReport(startNode, depthLimit, discoveredDevices);
                statusBar.setText("Report generated successfully!");
                log("Report generated successfully!");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Depth limit must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Clear Button Action
        clearButton.addActionListener(e -> {
            startNodeField.setText("");
            depthLimitField.setText("");
            tableModel.setRowCount(0);
            logArea.setText("");
            statusBar.setText("Ready");
            log("Fields and data cleared.");
        });

        // Layout Setup
        frame.setLayout(new BorderLayout(10, 10));
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(tableScrollPane, BorderLayout.WEST);
        frame.add(logScrollPane, BorderLayout.EAST);
        frame.add(progressBar, BorderLayout.SOUTH);
        frame.add(statusBar, BorderLayout.PAGE_END);

        // Show Frame
        frame.setVisible(true);
    }

    private void generateReport(String startNode, int depthLimit, List<String> discoveredDevices) {
        String fileName = "DeviceDiscoveryReport.txt";

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("Device Discovery Report\n");
            writer.write("=======================\n");
            writer.write("Start Node: " + startNode + "\n");
            writer.write("Depth Limit: " + depthLimit + "\n");
            writer.write("Timestamp: " + java.time.LocalDateTime.now() + "\n");
            writer.write("Discovered Devices:\n");

            for (String device : discoveredDevices) {
                writer.write("- " + device + "\n");
            }

            JOptionPane.showMessageDialog(null, "Report generated successfully: " + fileName);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error generating report: " + e.getMessage());
        }
    }

    private void log(String message) {
        logArea.append(message + "\n");
    }
}
