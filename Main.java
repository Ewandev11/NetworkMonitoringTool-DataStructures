package networktopology;

import javax.swing.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Graph graph = new Graph(); // Create an instance of the Graph class

        while (true) {
            System.out.println("\n=== Network Topology Menu ===");
            System.out.println("1. Add Edge to Network Topology (Add a connection between devices)");
            System.out.println("2. Print Network Topology (View all devices and connections)");
            System.out.println("3. Network Topology Visualization (Display network graphically)");
            System.out.println("4. Find Shortest Path (Find the shortest route between two devices)");
            System.out.println("5. Detect Bottlenecks (Identify high-latency connections)");
            System.out.println("6. Exit (Close the application)");
            System.out.println("7. Launch Device Discovery (Discover devices in the network)");
            System.out.print("Choose an option: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        // Feature 1: Add Edge to Network Topology
                        System.out.print("Enter Source Node: ");
                        String source = scanner.nextLine();
                        System.out.print("Enter Destination Node: ");
                        String destination = scanner.nextLine();
                        System.out.print("Enter Weight (e.g., latency in ms): ");
                        int weight = scanner.nextInt();
                        graph.addEdge(source, destination, weight);
                        System.out.println("Edge added successfully! (" + source + " -> " + destination + " with weight " + weight + ")");
                        break;

                    case 2:
                        // Feature 2: Print Network Topology
                        System.out.println("Printing Network Topology...");
                        graph.printTopology();
                        break;
                        
                    case 3:
                       // Feature 3: Network Topology Visualization
                        System.out.println("Launching Network Topology Visualization...");
                        JFrame frame = new JFrame("Network Topology Visualization");
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.add(new NetworkTopologyVisualizer(graph));
                        frame.pack();
                        frame.setVisible(true);
                        break;

                    case 4:
                        // Feature 3: Find Shortest Path
                        System.out.print("Enter Start Node: ");
                        String start = scanner.nextLine();
                        System.out.print("Enter End Node: ");
                        String end = scanner.nextLine();
                        System.out.println("Calculating the shortest path from " + start + " to " + end + "...");
                        graph.findShortestPath(start, end);
                        break;

                    case 5:
                        // Feature 4: Detect Bottlenecks
                        System.out.print("Enter Weight Threshold: ");
                        int threshold = scanner.nextInt();
                        System.out.println("Detecting bottlenecks for connections with weight greater than " + threshold + "...");
                        graph.detectBottlenecks(threshold);
                        break;

                    case 6:
                        // Exit
                        System.out.println("Exiting... Thank you for using the Network Topology Tool!");
                        return;

                    case 7:
                        // Feature 6: Launch Device Discovery
                        System.out.println("Launching Device Discovery Tool...");
                        SwingUtilities.invokeLater(() -> new DeviceDiscoveryUI(graph).createAndShowGUI());
                        break;

                    default:
                        // Invalid Input
                        System.out.println("Invalid choice. Please enter a number between 1 and 7.");
                }
            } catch (Exception ex) {
                System.out.println("Error: Invalid input. Please enter a valid option.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
}
