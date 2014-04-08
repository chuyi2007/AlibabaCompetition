package home.s1.alg;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DirectedGraphNode {
	String id;
	Set<DirectedGraphEdge> inEdges;
	Set<DirectedGraphEdge> outEdges;
	public DirectedGraphNode(String id) {
		super();
		this.id = id;
		inEdges = new HashSet<DirectedGraphEdge>();
		outEdges = new HashSet<DirectedGraphEdge>();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DirectedGraphNode other = (DirectedGraphNode) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public static DirectedGraphNode createChain(String[] tokens) {
		Map<String, DirectedGraphNode> map = new HashMap<String, DirectedGraphNode>();
		map.put(tokens[0], new DirectedGraphNode(tokens[0]));
		for (int i = 0; i < tokens.length - 1; ++i) {
			if (!map.containsKey(tokens[i + 1])) {
				map.put(tokens[i], new DirectedGraphNode(tokens[i]));
			}
			DirectedGraphEdge edge = new DirectedGraphEdge(
					map.get(tokens[i]), map.get(tokens[i + 1]));
			
		}
	}
	
	public static void add(String word1, String word2) {
		
	}
	
	public static void main(String[] args) {
		String inputData = "this is a great place and many people believe in me";
		
	}
}
