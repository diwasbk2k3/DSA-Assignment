// Q.N. 5
// Optimizing a Network with Multiple Objectives 
// Problem: 
// Suppose you are hired as software developer for certain organization and you are tasked with creating a 
// GUI application that helps network administrators design a network topology that is both cost-effective 
// and efficient for data transmission. The application needs to visually represent servers and clients as 
// nodes in a graph, with potential network connections between them, each having associated costs and 
// bandwidths. The goal is to enable the user to find a network topology that minimizes both the total cost 
// and the latency of data transmission. 
// Approach: 
// 1. Visual Representation of the Network: 
// o Design the GUI to allow users to create and visualize a network graph where each node 
// represents a server or client, and each edge represents a potential network connection. The 
// edges should display associated costs and bandwidths. 
// 2. Interactive Optimization: 
// o Implement tools within the GUI that enable users to apply algorithms or heuristics to 
// optimize the network. The application should provide options to find the best combination 
// of connections that minimizes the total cost while ensuring all nodes are connected. 
// 3. Dynamic Path Calculation: 
// o Include a feature where the user can calculate the shortest path between any pair of nodes 
// within the selected network topology. The GUI should display these paths, taking into 
// account the bandwidths as weights. 
// 4. Real-time Evaluation: 
// o Provide real-time analysis within the GUI that displays the total cost and latency of the 
// current network topology. If the user is not satisfied with the results, they should be able 
// to adjust the topology and explore alternative solutions interactively. 
// Example: 
//  Input: The user inputs a graph in the application, representing servers, clients, potential 
// connections, their costs, and bandwidths. 
//  Output: The application displays the optimal network topology that balances cost and latency, 
// and shows the shortest paths between servers and clients on the GUI. 

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

class Node {
    int x, y;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class Edge {
    Node node1, node2;
    double cost;
    double bandwidth;

    public Edge(Node node1, Node node2, double cost, double bandwidth) {
        this.node1 = node1;
        this.node2 = node2;
        this.cost = cost;
        this.bandwidth = bandwidth;
    }
}

public class NetworkOptimizer extends JPanel {
    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;

    public NetworkOptimizer() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    nodes.add(new Node(e.getX(), e.getY()));
                    repaint();
                }
            }
        });

        JButton addEdgeButton = new JButton("Add Edge");
        addEdgeButton.addActionListener(e -> addEdge());
        add(addEdgeButton, BorderLayout.SOUTH);

        JFrame frame = new JFrame("Network Optimizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(this, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void addEdge() {
        if (nodes.size() < 2) {
            JOptionPane.showMessageDialog(this, "At least two nodes are required to create an edge.");
            return;
        }

        String input = JOptionPane.showInputDialog(this, "Enter Edge Details (Format: Node0,Node1,Cost,Bandwidth):");
        
        try {
            String[] details = input.split(",");
            int node1Index = Integer.parseInt(details[0]);
            int node2Index = Integer.parseInt(details[1]);
            double cost = Double.parseDouble(details[2]);
            double bandwidth = Double.parseDouble(details[3]);

            if (node1Index >= nodes.size() || node2Index >= nodes.size()) {
                throw new IndexOutOfBoundsException();
            }

            edges.add(new Edge(nodes.get(node1Index), nodes.get(node2Index), cost, bandwidth));
            repaint();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please try again.");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw edges
        for (Edge edge : edges) {
            g.drawLine(edge.node1.x, edge.node1.y, edge.node2.x, edge.node2.y);
            
            int midX = (edge.node1.x + edge.node2.x) / 2;
            int midY = (edge.node1.y + edge.node2.y) / 2;
            
            g.drawString("Cost: " + edge.cost + ", BW: " + edge.bandwidth, midX, midY);
        }

        // Draw nodes
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            
            g.fillOval(node.x - 10, node.y - 10, 20, 20);
            g.drawString("Node " + i, node.x - 15, node.y - 15);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NetworkOptimizer::new);
    }
}


/* Algorithm Explanation
1. Graph Representation 
   - The network is represented as a graph where nodes represent servers or clients, and edges represent network connections.
   - Nodes are stored in an `ArrayList<Node>`, and edges are stored in an `ArrayList<Edge>`.

2. Node Creation 
   - When the user clicks on the panel, a new node is created at the clicked position.
   - The `MouseAdapter` listens for mouse clicks, and if the left button is clicked, a `Node` is added to the list and the panel is repainted.

3. Edge Creation 
   - When the "Add Edge" button is clicked, the program prompts the user to input edge details in the format: `Node1,Node2,Cost,Bandwidth`.
   - It parses the input to get the indices of the nodes, cost, and bandwidth.
   - If valid, an `Edge` object is created and added to the edge list.

4. Drawing the Network 
   - The `paintComponent(Graphics g)` method is overridden to draw the network.
   - It first draws all edges as lines between connected nodes, displaying cost and bandwidth near the midpoint.
   - It then draws all nodes as circles and labels them.

5. User Interaction 
   - The user can add nodes by clicking anywhere on the panel.
   - The user can add edges by specifying node indices and edge details.
   - The interface dynamically updates to reflect the current network topology.

6. Potential Enhancements 
   - Implementing path-finding algorithms like Dijkstra’s Algorithm for shortest path calculation.
   - Applying Minimum Spanning Tree (MST) algorithms (e.g., Kruskal’s or Prim’s) to find an optimized network topology.
   - Providing real-time cost and latency calculations to assist in network optimization.
*/
