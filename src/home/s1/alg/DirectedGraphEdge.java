package home.s1.alg;
public class DirectedGraphEdge {
	private float val;
	DirectedGraphNode from;
	DirectedGraphNode to;
	public DirectedGraphEdge(DirectedGraphNode from, DirectedGraphNode to) {
		super();
		this.from = from;
		this.to = to;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
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
		DirectedGraphEdge other = (DirectedGraphEdge) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}
	public float getVal() {
		return val;
	}
	public void setVal(float val) {
		this.val = val;
	}
}
