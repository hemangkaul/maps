package edu.brown.cs.hk125.kdtree;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.MinMaxPriorityQueue;

public class KDTree {

  KDNode root;
  int dim;

  public KDTree(List<? extends KDData> elementList) {
    this.dim = elementList.get(0).dimensions();
    this.root = buildTree(0, elementList);
  }

  private KDNode buildTree(int dimension, List<? extends KDData> elementList) {
    if (elementList.isEmpty()) {
      return null;
    }

    int length = elementList.size();
    if (length == 1) {
      return new KDNode(elementList.get(0), null, null);
    }
    elementList.sort(new KDComparator(dimension));

    KDData rootVal = elementList.get(length / 2);
    elementList.remove(length / 2);
    ArrayList<KDData> left = new ArrayList<>();
    ArrayList<KDData> right = new ArrayList<>();
    for (int i = 0; i < length / 2; i++) {
      left.add(elementList.get(i));
      elementList.remove(i);
    }

    right.addAll(elementList);

    return new KDNode(rootVal, buildTree((dimension + 1) % dim, left),
        buildTree((dimension + 1) % dim, right));
  }

  public KDData findNN(KDData target) {
    return findNNHelper(0, target, null, Double.MAX_VALUE, root);
  }

  public KDData findNNHelper(int dimension, KDData target, KDData best,
      double bestDist, KDNode curr) {

    if (curr == null) {
      return best;
    }

    KDData current = curr.getDatum();
    double currDist = target.distance(current);

    if (currDist < bestDist) {
      bestDist = currDist;
      best = current;
    }

    boolean wentLeft;

    double dimDif = target.compareDimension(dimension, current);

    if (dimDif < 0) {
      findNNHelper((dimension + 1) % dim, target, best, bestDist,
          curr.getLeft());
      wentLeft = true;
    } else {
      findNNHelper((dimension + 1) % dim, target, best, bestDist,
          curr.getRight());
      wentLeft = false;
    }

    if (Math.abs(dimDif) < bestDist) {
      if (wentLeft) {
        findNNHelper((dimension + 1) % dim, target, best, bestDist,
            curr.getRight());
      } else {
        findNNHelper((dimension + 1) % dim, target, best, bestDist,
            curr.getLeft());
      }
    }

    return null;
  }

  public ArrayList<KDData> findkNN(KDData target, int k) {
    if (k == 0) {
      return null;
    }
    MinMaxPriorityQueue<KDData> best = MinMaxPriorityQueue
        .orderedBy(new KDDistanceComparator(target)).maximumSize(k + 1)
        .create();
    MinMaxPriorityQueue<KDData> bestQ = findkNNHelper(0, target, best, root,
        k + 1);
    ArrayList<KDData> bestList = new ArrayList<>();
    bestList.addAll(bestQ);
    bestList.remove(target);
    bestList.sort(new KDDistanceComparator(target));
    return bestList;
  }

  private MinMaxPriorityQueue<KDData> findkNNHelper(int dimension,
      KDData target, MinMaxPriorityQueue<KDData> best, KDNode curr, int k) {

    if (curr == null) {
      return best;
    }

    best.add(curr.getDatum());

    boolean wentLeft;

    if (target.compareDimension(dimension, curr.getDatum()) < 0) {
      best = findkNNHelper((dimension + 1) % dim, target, best, curr.getLeft(),
          k);
      wentLeft = true;
    } else {
      best = findkNNHelper((dimension + 1) % dim, target, best,
          curr.getRight(), k);
      wentLeft = false;
    }

    boolean bestYet = (Math.abs(target.compareDimension(dimension,
        curr.getDatum())) < best.peekLast().distance(target));

    if (bestYet || !(best.size() == k)) {
      if (wentLeft) {
        best = findkNNHelper((dimension + 1) % dim, target, best,
            curr.getRight(), k);
      } else {
        best = findkNNHelper((dimension + 1) % dim, target, best,
            curr.getLeft(), k);
      }
    }

    return best;

  }

  public ArrayList<KDData> radiusSearch(KDData target, double radius) {
    ArrayList<KDData> inside = new ArrayList<>();
    ArrayList<KDData> insideList = rsHelper(0, inside, target, radius, root);
    insideList.remove(target);
    insideList.sort(new KDDistanceComparator(target));
    return inside;
  }

  private ArrayList<KDData> rsHelper(int dimension, ArrayList<KDData> inside,
      KDData target, double radius, KDNode curr) {

    if (curr == null) {
      return inside;
    }

    if (target.distance(curr.getDatum()) < radius) {
      inside.add(curr.getDatum());
    }

    boolean wentLeft;

    if (target.compareDimension(dimension, curr.getDatum()) < 0) {
      rsHelper((dimension + 1) % dim, inside, target, radius, curr.getLeft());
      wentLeft = true;
    } else {
      rsHelper((dimension + 1) % dim, inside, target, radius, curr.getRight());
      wentLeft = false;
    }

    if (Math.abs(target.compareDimension(dimension, curr.getDatum())) < radius) {
      if (wentLeft) {
        rsHelper((dimension + 1) % dim, inside, target, radius, curr.getRight());
      } else {
        rsHelper((dimension + 1) % dim, inside, target, radius, curr.getLeft());
      }
    }

    return inside;
  }

  @Override
  public boolean equals(Object o) {
    if (o.equals(null)) {
      return false;
    }
    if (!(o instanceof KDTree)) {
      return false;
    }
    KDTree other = (KDTree) o;
    return (this.root.equals(other));
  }

  @Override
  public String toString() {
    String main = this.root.toString();
    String right = this.root.getRight().toString();
    String left = this.root.getLeft().toString();
    return (main + "\n" + left + "\n" + right);
  }

}
