package edu.brown.cs.hk125.kdtree;

import java.util.Comparator;

public class KDComparator implements Comparator<KDData> {
	
	private int currDim;
	
	public KDComparator(int currDim) {
		this.currDim = currDim;
	}

	@Override
	public int compare(KDData o1, KDData o2) {
		int result = 0;
		Double comparison = o1.compareDimension(currDim, o2);
		if (comparison > 0) {
			result = 1;
		} else if (comparison < 0) {
			result = -1;
		} else if (comparison == 0) {
			result = 0;
		}
		return result;
	}

}
