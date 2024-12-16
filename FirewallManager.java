package networktopology;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import networktopology.FirewallRule;
import networktopology.FirewallLog;

public class FirewallManager {
    private List<FirewallRule> firewallRules;
    private List<FirewallLog> trafficLogs;

    public FirewallManager() {
        this.firewallRules = new ArrayList<>();
        this.trafficLogs = new ArrayList<>();
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Firewall Manager");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        // Rule Management Panel
        JPanel rulePanel = new JPanel(new GridLayout(6, 2, 10, 10));
        rulePanel.setBorder(BorderFactory.createTitledBorder("Add Firewall Rule"));

        JTextField sourceField = new JTextField();
        JTextField destinationField = new JTextField();
        JTextField portField = new JTextField();
        JTextField protocolField = new JTextField();
        JComboBox<String> actionCombo = new JComboBox<>(new String[]{"ALLOW", "BLOCK"});
        JButton addRuleButton = new JButton("Add Rule");

        rulePanel.add(new JLabel("Source IP:"));
        rulePanel.add(sourceField);
        rulePanel.add(new JLabel("Destination IP:"));
        rulePanel.add(destinationField);
        rulePanel.add(new JLabel("Port:"));
        rulePanel.add(portField);
        rulePanel.add(new JLabel("Protocol:"));
        rulePanel.add(protocolField);
        rulePanel.add(new JLabel("Action:"));
        rulePanel.add(actionCombo);
        rulePanel.add(new JLabel());
        rulePanel.add(addRuleButton);

        // Rule Table
        String[] ruleColumns = {"Source", "Destination", "Port", "Protocol", "Action"};
        DefaultTableModel ruleTableModel = new DefaultTableModel(ruleColumns, 0);
        JTable ruleTable = new JTable(ruleTableModel);
        JScrollPane ruleScrollPane = new JScrollPane(ruleTable);
        ruleScrollPane.setBorder(BorderFactory.createTitledBorder("Active Firewall Rules"));

        // Traffic Validation Panel
        JPanel validationPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        validationPanel.setBorder(BorderFactory.createTitledBorder("Validate Traffic"));

        JTextField validateSourceField = new JTextField();
        JTextField validateDestinationField = new JTextField();
        JTextField validatePortField = new JTextField();
        JTextField validateProtocolField = new JTextField();
        JButton validateButton = new JButton("Validate Traffic");
        JLabel validationResultLabel = new JLabel("Result: ");

        validationPanel.add(new JLabel("Source IP:"));
        validationPanel.add(validateSourceField);
        validationPanel.add(new JLabel("Destination IP:"));
        validationPanel.add(validateDestinationField);
        validationPanel.add(new JLabel("Port:"));
        validationPanel.add(validatePortField);
        validationPanel.add(new JLabel("Protocol:"));
        validationPanel.add(validateProtocolField);
        validationPanel.add(validateButton);
        validationPanel.add(validationResultLabel);

        // Log Table
        String[] logColumns = {"Timestamp", "Source", "Destination", "Port", "Protocol", "Status"};
        DefaultTableModel logTableModel = new DefaultTableModel(logColumns, 0);
        JTable logTable = new JTable(logTableModel);
        JScrollPane logScrollPane = new JScrollPane(logTable);
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Traffic Logs"));

        // Export Logs Button
        JButton exportLogsButton = new JButton("Export Logs");

        // Frame Layout
        frame.setLayout(new BorderLayout());
        frame.add(rulePanel, BorderLayout.NORTH);
        frame.add(ruleScrollPane, BorderLayout.CENTER);
        frame.add(validationPanel, BorderLayout.EAST);
        frame.add(logScrollPane, BorderLayout.SOUTH);
        frame.add(exportLogsButton, BorderLayout.WEST);

        // Button Actions
        addRuleButton.addActionListener(e -> {
            try {
                String source = sourceField.getText();
                String destination = destinationField.getText();
                int port = Integer.parseInt(portField.getText());
                String protocol = protocolField.getText();
                String action = (String) actionCombo.getSelectedItem();

                firewallRules.add(new FirewallRule(source, destination, port, protocol, action));
                ruleTableModel.addRow(new Object[]{source, destination, port, protocol, action});
                JOptionPane.showMessageDialog(frame, "Rule added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        validateButton.addActionListener(e -> {
            try {
                String source = validateSourceField.getText();
                String destination = validateDestinationField.getText();
                int port = Integer.parseInt(validatePortField.getText());
                String protocol = validateProtocolField.getText();

                String status = "Allowed";
                for (FirewallRule rule : firewallRules) {
                    if (rule.source.equals(source) && rule.destination.equals(destination)
                            && rule.port == port && rule.protocol.equalsIgnoreCase(protocol)) {
                        status = rule.action.equals("BLOCK") ? "Blocked" : "Allowed";
                        break;
                    }
                }

                trafficLogs.add(new FirewallLog(source, destination, port, protocol, status));
                logTableModel.addRow(new Object[]{java.time.LocalDateTime.now(), source, destination, port, protocol, status});
                validationResultLabel.setText("Result: " + status);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        exportLogsButton.addActionListener(e -> {
            try (FileWriter writer = new FileWriter("FirewallLogs.txt")) {
                for (FirewallLog log : trafficLogs) {
                    writer.write(log.toString() + "\n");
                }
                JOptionPane.showMessageDialog(frame, "Logs exported successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error exporting logs: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }
}
