import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

class Graph {
    private Node[] nodes;
    
    public Graph(Node[] nodes) {
	this.nodes = nodes;
    }
    
    private static Graph buildExampleGraph() {
	Node[] nodes = new Node[7];
	for (int i = 0; i < 7; i++) {
	    nodes[i] = new Node(i);
	}
	nodes[0].addNeighbor(nodes[1]);
	nodes[0].addNeighbor(nodes[2]);
	nodes[1].addNeighbor(nodes[2]);
	nodes[2].addNeighbor(nodes[3]);
	nodes[2].addNeighbor(nodes[5]);
	nodes[3].addNeighbor(nodes[4]);
	nodes[4].addNeighbor(nodes[5]);
	nodes[5].addNeighbor(nodes[6]);
	return new Graph(nodes);
    }
    
    private static Graph buildRandomSparseGraph(int numberofV, long seed) {
	java.util.Random tilf = new java.util.Random(seed);
	int tilfeldig1 = 0, tilfeldig2 = 0;
	Node[] nodes = new Node[numberofV];
	
	for (int i = 0; i < numberofV; i++) {
	    nodes[i] = new Node(i);
	}
	
	for (int i = 0; i < numberofV; i++) {
	    tilfeldig1 = tilf.nextInt(numberofV);
	    tilfeldig2 = tilf.nextInt(numberofV);
	    if (tilfeldig1 != tilfeldig2)
		nodes[tilfeldig1].addNeighbor(nodes[tilfeldig2]);
	}
	return new Graph(nodes);
    }
    
    private static Graph buildRandomDenseGraph(int numberofV, long seed) {
	java.util.Random tilf = new java.util.Random(seed);
	int tilfeldig1 = 0, tilfeldig2 = 0;
	Node[] nodes = new Node[numberofV];
	
	for (int i = 0; i < numberofV; i++) {
	    nodes[i] = new Node(i);
	}
	
	for (int i = 0; i < numberofV * numberofV; i++) {
	    tilfeldig1 = tilf.nextInt(numberofV);
	    tilfeldig2 = tilf.nextInt(numberofV);
	    if (tilfeldig1 != tilfeldig2)
		nodes[tilfeldig1].addNeighbor(nodes[tilfeldig2]);
	}
	return new Graph(nodes);
    }
    
    private static Graph buildRandomDirGraph(int numberofV, long seed) {
	java.util.Random tilf = new java.util.Random(seed);
	int tilfeldig1 = 0, tilfeldig2 = 0;
	Node[] nodes = new Node[numberofV];
	
	for (int i = 0; i < numberofV; i++) {
	    nodes[i] = new Node(i);
	}
	
	for (int i = 0; i < 2 * numberofV; i++) {
	    tilfeldig1 = tilf.nextInt(numberofV);
	    tilfeldig2 = tilf.nextInt(numberofV);
	    if (tilfeldig1 != tilfeldig2)
		nodes[tilfeldig1].addSuccessor(nodes[tilfeldig2]);
	}
	return new Graph(nodes);
    }
    
    public void printNeighbors() {
	for (Node n1 : nodes) {
	    String s = n1.toString() + ": ";
	    for (Node n2 : n1.getNeighbors()) {
		s += n2.toString() + " ";
	    }
	    System.out.println(s.substring(0, s.length() - 1));
	}
    }
    
    public void resetVisited(){
	for(int i=0; i<nodes.length; i++){
	    nodes[i].unvisit();
	}
    }
    
    private void DFS(int n) {
	nodes[n].visit();
	for(int i=0; i<nodes[n].getNeighbors().size(); i++){
	    if(!nodes[n].getNeighbors().get(i).isVisited()){
		DFS(nodes[n].getNeighbors().get(i).getLabel());
	    }
	}
    }

    private void DFS2(int n, ArrayList<Node> noder){
	nodes[n].visit();
	noder.add(nodes[n]);
	for(int i=0; i<nodes[n].getNeighbors().size(); i++){
	    if(!nodes[n].getNeighbors().get(i).isVisited()){
		DFS2(nodes[n].getNeighbors().get(i).getLabel(), noder);
	    }
	}
    }

      public Graph biggestComponent() {
	if(this.numberOfComponents() <= 1){
	    return this;
	}
	ArrayList<Node> noder = new ArrayList<Node>();
	int h = nodes.length+1;

	for(int i=0; i<nodes.length; i++){
	    if(!nodes[i].isVisited()){
		noder.add(new Node(h));
		DFS2(i, noder);
	    }
	}

	int teller=0;
	int storst=0;
	int start=1;
	int slutt=0;
	for(int i=0; i<noder.size(); i++){
	    if(noder.get(i).getLabel() != h){
		teller++;
	    }
	    if(noder.get(i).getLabel() == h){
		if(teller > storst){
		    storst = teller;
		    slutt = teller;
		    teller = 0;
		}
	    }
	}

	noder.subList(slutt+1, noder.size()).clear();
	noder.subList(0,start).clear();

	Node[] node_array = new Node[noder.size()];
	for(int i=0; i<noder.size(); i++){
	    node_array[i] = noder.get(i);
	}
	Graph retur_graf = new Graph(node_array);
	return retur_graf;
    }


    public int numberOfComponents() {
	int antall_Komponenter = 0;
	for(int i=0; i<nodes.length; i++){
	    if(!nodes[i].isVisited()){
		DFS(i);
		antall_Komponenter++;
	    }
	}
	this.resetVisited();
	return antall_Komponenter;
    }
    
    public Graph transformDirToUndir() {
	Node[] noder = new Node[nodes.length];
	for(int i=0; i<nodes.length; i++){
	    noder[i] = nodes[i];
	}
	Graph retur_graf = new Graph(noder);
	
	for(int i=0; i<noder.length; i++){
	    for(int j=0; j<noder.length; j++){
		if(harNabo(noder[i].getLabel(), noder[j].getLabel()) && !harNabo(noder[j].getLabel(), noder[i].getLabel())){
		    noder[j].addNeighbor(noder[i]);
		}
	    }
	}
	return retur_graf;
    }

    public boolean isConnected(){
	Graph ny_graf = this.transformDirToUndir();
	if(ny_graf.numberOfComponents() <= 1){
	    return true;
	}
	
	return false;
    }

    
    private boolean harNabo(int en, int to){
	for(int i=0; i<nodes.length; i++){
	    if(nodes[i].getLabel() == en){
		for(int j=0; j<nodes[i].getNeighbors().size(); j++){
		    if(nodes[i].getNeighbors().get(j).getLabel() == to){
			return true;
		    }
		}
	    }
	}
	return false;
    }
    
    public int[][] buildAdjacencyMatrix() {
	int[][] nabomatrise = new int[nodes.length][nodes.length];
	for(int i=0; i<nodes.length; i++){
	    for(int j=0; j<nodes.length; j++){
		if(harNabo(nodes[i].getLabel(), nodes[j].getLabel())){
		    nabomatrise[i][j] = 1;
		}
		else {
		    nabomatrise[i][j] = 0;
		}
	    }
	}
	return nabomatrise;
    }
    
    public static void main(String[] args) {
	Graph graph = buildExampleGraph();
	graph = buildRandomSparseGraph(11, 20190902359L);
	graph.printNeighbors();
	System.out.println();
	graph = buildRandomDenseGraph(15, 201909202359L);
	graph.printNeighbors();
	Graph ny_graf = buildRandomSparseGraph(11, 201909202359L);
	System.out.println(ny_graf.numberOfComponents());
	System.out.println(ny_graf.isConnected());
    }
}

