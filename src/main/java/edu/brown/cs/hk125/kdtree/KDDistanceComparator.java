package edu.brown.cs.hk125.kdtree;

import java.util.Comparator;

public class KDDistanceComparator implements Comparator<KDData> {
	
	private KDData target;
	
	public KDDistanceComparator(KDData target) {
		this.target = target;
	}
	
	@Override
	public int compare(KDData o1, KDData o2) {
		return Double.compare(o1.distance(target), o2.distance(target));
	}
	
}
