package networktopology;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkTopologyVisualizer extends JPanel {
    private Graph graph;
    private Map<String, Point> nodePositions;
    private String hoveredNode;
    private int radius = 20;

    public NetworkTopologyVisualizer(Graph graph) {
        this.graph = graph;
        this.nodePositions = new HashMap<>();
        this.hoveredNode = null;
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);

        // Add mouse listener for hover detection
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point mousePos = e.getPoint();
                for (Map.Entry<String, Point> entry : nodePositions.entrySet()) {
                    Point nodePos = entry.getValue();
                    if (mousePos.distance(nodePos) <= radius) {
                        hoveredNode = entry.getKey();
                        repaint();
                        return;
                    }
                }
                hoveredNode = null; // Reset if no node is hovered
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        Map<String, List<Graph.Edge>> adjacencyList = graph.getGraphData();
        int totalNodes = adjacencyList.keySet().size();

        if (totalNodes == 0) {
            g.drawString("No nodes to display. Add some nodes and edges to visualize the topology.", getWidth() / 2 - 100, getHeight() / 2);
            return;
        }

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int circleRadius = Math.min(getWidth(), getHeight()) / 3;

        int angleStep = 360 / totalNodes;
        int currentAngle = 0;

        nodePositions.clear();

        // Draw Nodes
        for (String node : adjacencyList.keySet()) {
            int x = centerX + (int) (circleRadius * Math.cos(Math.toRadians(currentAngle)));
            int y = centerY + (int) (circleRadius * Math.sin(Math.toRadians(currentAngle)));

            nodePositions.put(node, new Point(x, y));

            // Draw node circle
            if (node.equals(hoveredNode)) {
                g2d.setColor(Color.ORANGE);
            } else {
                g2d.setColor(Color.CYAN);
            }
            g2d.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);

            // Draw node label
            g2d.drawString(node, x - radius / 2, y + radius + 10);

            currentAngle += angleStep;
        }

        // Draw Edges
        g2d.setColor(Color.BLACK);
        for (String node : adjacencyList.keySet()) {
            Point startPoint = nodePositions.get(node);
            for (Graph.Edge edge : adjacencyList.get(node)) {
                Point endPoint = nodePositions.get(edge.destination);
                drawArrow(g2d, startPoint.x, startPoint.y, endPoint.x, endPoint.y);

                // Draw edge weight
                int midX = (startPoint.x + endPoint.x) / 2 + 10; // Offset by 10 pixels
                int midY = (startPoint.y + endPoint.y) / 2 - 10; // Offset by -10 pixels
                g2d.drawString(String.valueOf(edge.weight), midX, midY);
            }
        }
    }

    private void drawArrow(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        int arrowSize = 10;
        double angle = Math.atan2(y2 - y1, x2 - x1);

        int xArrow1 = (int) (x2 - arrowSize * Math.cos(angle - Math.PI / 6));
        int yArrow1 = (int) (y2 - arrowSize * Math.sin(angle - Math.PI / 6));
        int xArrow2 = (int) (x2 - arrowSize * Math.cos(angle + Math.PI / 6));
        int yArrow2 = (int) (y2 - arrowSize * Math.sin(angle + Math.PI / 6));

        g2d.drawLine(x1, y1, x2, y2);
        g2d.drawLine(x2, y2, xArrow1, yArrow1);
        g2d.drawLine(x2, y2, xArrow2, yArrow2);
    }
}