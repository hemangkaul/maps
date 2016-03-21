package edu.brown.cs.sl234.dijkstra;

/**
 * A Link represents an edge connecting, or 'linking' two nodes. A Link contains
 * a source, an end, a distance, and a name.
 *
 * Notice a link can also represent a neighbor. All the links connected to a
 * source node represent its neighbors.
 *
 * @author sl234
 *
 */
public class Link implements Comparable<Link> {

	private String source;
	private String end;
	private Double distance;
	// distance refers to the distance from the end node to the start node, NOT
	// the distance from the end node to the source node.
	private String name;

	/**
	 * Constructs a link; sets source, end, distance, and link.
	 *
	 * @param source
	 *          , the name of the source of the link
	 * @param end
	 *          , the name of the end of the link
	 * @param distance
	 *          , the distance between the source and the end
	 * @param name
	 *          , the name of the link
	 */
	public Link(String source, String end, Double distance, String name) {
		this.setSource(source);
		this.setEnd(end);
		this.setDistance(distance);
		this.setName(name);
	}

	/**
	 * Another constructor; sometimes link isn't needed.
	 *
	 * @param source
	 *          , the name of the source of the link
	 * @param end
	 *          , the name of the end of the link
	 * @param distance
	 *          , the distance between the source and the end
	 */
	public Link(String source, String end, Double distance) {
		this.setSource(source);
		this.setEnd(end);
		this.setDistance(distance);
		this.setName("");
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source
	 *          the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the neighborName
	 */
	public String getEnd() {
		return end;
	}

	/**
	 * @param end
	 *          the neighborName to set
	 */
	public void setEnd(String end) {
		this.end = end;
	}

	/**
	 * @return the distance
	 */
	public Double getDistance() {
		return distance;
	}

	/**
	 * @param distance
	 *          the distance to set
	 */
	public void setDistance(Double distance) {
		this.distance = distance;
	}

	/**
	 * @return the name of the Link
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *          the name of the link to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Allows us to sort neighbors according to their distance values.
	 *
	 * @param l
	 *          , the link we are comparing to
	 * @return 1 if the compared link is has smaller distance, -1 if the compared
	 *         link has greater distance, and 0 if the distances are the same
	 */
	@Override
	public int compareTo(Link l) {
		// TODO Auto-generated method stub
		double distDiff = distance - l.getDistance();
		if (distDiff < 0) {
			return -1;
		} else if (distDiff > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	/**
	 * Simply checking for equality by checking each of the four components are equal.
	 */
	public boolean equals(Object other) {
		if (!(other instanceof Link)) {
			return false;
		}
		Link l = (Link) other;

		if (!(this.getSource().equals(l.getSource()))) {
			return false;
		}
		if (!(this.getEnd().equals(l.getEnd()))) {
			return false;
		}
		if (!(this.getDistance().equals(l.getDistance()))) {
			return false;
		}
		if (!(this.getName().equals(l.getName()))) {
			return false;
		}
		return true;
	}
}
