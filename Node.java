import java.util.List;
import java.util.LinkedList;

class Node {
    private int label;
    private boolean visited = false;
    private List<Node> neighbors = new LinkedList<Node>();
    
    public Node(int label) {
	this.label = label;
    }
    
    public int getLabel() {
	return label;
    }
    
    public List<Node> getNeighbors() {
	return neighbors;
    }
    
    public boolean isVisited() {
	return visited;
    }
    
    public void visit() {
	visited = true;
    }

    public void unvisit(){
	visited = false;
    }
    
    
    public void addNeighbor(Node n) {
	if (!neighbors.contains(n)) {
	    neighbors.add(n);
	    n.addNeighbor(this);
	}
    }
    
    public void addSuccessor(Node n) {
	if (!neighbors.contains(n)) {
	    neighbors.add(n);
	}
    }
    
    public String toString() {
	return Integer.toString(label);
    }
}

