package networktopology;

import java.util.*;

public class Graph {
    protected Map<String, List<Edge>> adjacencyList = new HashMap<>();

    // Feature 1: Build Network Topology

    // Method to add an edge to the graph
    public void addEdge(String source, String destination, int weight) {
        // Ensure both source and destination nodes exist in the adjacency list
        adjacencyList.putIfAbsent(source, new ArrayList<>());
        adjacencyList.putIfAbsent(destination, new ArrayList<>());
        // Add the edge to the source node's list
        adjacencyList.get(source).add(new Edge(destination, weight));
    }

    // Method to print the current topology
    public void printTopology() {
        System.out.println("Network Topology:");
        for (String node : adjacencyList.keySet()) {
            System.out.print(node + " -> ");
            for (Edge edge : adjacencyList.get(node)) {
                System.out.print("(" + edge.destination + ", " + edge.weight + ") ");
            }
            System.out.println();
        }
    }

    // Feature 2: Find Shortest Path (Dijkstra's Algorithm)

    public List<String> findShortestPath(String start, String end) {
        // Validation for start and end nodes
        if (!adjacencyList.containsKey(start)) {
            System.out.println("Error: Start node \"" + start + "\" does not exist in the graph.");
            return Collections.emptyList();
        }
        if (!adjacencyList.containsKey(end)) {
            System.out.println("Error: End node \"" + end + "\" does not exist in the graph.");
            return Collections.emptyList();
        }

        // Priority Queue for nodes to visit, sorted by distance
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.distance));
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        List<String> path = new ArrayList<>();

        // Initialize distances to infinity and set up the priority queue
        for (String node : adjacencyList.keySet()) {
            distances.put(node, Integer.MAX_VALUE);
        }
        distances.put(start, 0); // Distance to start node is 0
        queue.add(new Node(start, 0));

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            // Stop if we reached the destination
            if (current.name.equals(end)) break;

            // Process neighbors
            for (Edge edge : adjacencyList.getOrDefault(current.name, new ArrayList<>())) {
                int newDist = distances.get(current.name) + edge.weight;
                if (newDist < distances.get(edge.destination)) {
                    distances.put(edge.destination, newDist);
                    previous.put(edge.destination, current.name);
                    queue.add(new Node(edge.destination, newDist));
                }
            }
        }

        // Build the path by tracing back from the end node
        for (String at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path); // Reverse the path to start->end order

        // Print the result
        if (distances.get(end) == Integer.MAX_VALUE) {
            System.out.println("No path found between " + start + " and " + end);
        } else {
            System.out.println("Shortest Path: " + path);
            System.out.println("Total Weight: " + distances.get(end));
        }

        return path;
    }

    // Feature 3: Detect Bottleneck

    public void detectBottlenecks(int threshold) {
        System.out.println("Bottleneck Connections:");
        boolean found = false;
        for (String node : adjacencyList.keySet()) {
            for (Edge edge : adjacencyList.get(node)) {
                if (edge.weight > threshold) {
                    System.out.println(node + " -> " + edge.destination + " (Weight: " + edge.weight + ")");
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("No bottleneck connections found exceeding the threshold of " + threshold + ".");
        }
    }

    // Helper Classes

    // Inner class to represent an edge in the graph
    public static class Edge {
        String destination;
        int weight;

        Edge(String destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    // Inner class to represent nodes in the priority queue
    public static class Node {
        String name;
        int distance;

        Node(String name, int distance) {
            this.name = name;
            this.distance = distance;
        }
    }
     public Map<String, List<Edge>> getGraphData() {
       return adjacencyList;
   }  
     
    public List<String> discoverDevices(String startNode, int depthLimit) {
    List<String> discoveredDevices = new ArrayList<>();
    Set<String> visited = new HashSet<>();
    Queue<String> queue = new LinkedList<>();
    queue.add(startNode);
    visited.add(startNode);

    int currentDepth = 0;

    while (!queue.isEmpty() && currentDepth <= depthLimit) {
        int size = queue.size(); // Nodes at the current depth
        for (int i = 0; i < size; i++) {
            String current = queue.poll();
            discoveredDevices.add(current);

            // Explore neighbors
            for (Edge edge : adjacencyList.getOrDefault(current, new ArrayList<>())) {
                if (!visited.contains(edge.destination)) {
                    visited.add(edge.destination);
                    queue.add(edge.destination);
                }
            }
        }
        currentDepth++;
    }

    return discoveredDevices;
  }
}
