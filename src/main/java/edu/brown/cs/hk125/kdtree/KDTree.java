package edu.brown.cs.hk125.kdtree;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.MinMaxPriorityQueue;

/**
 * KDTree is a class which represents the KDTree data structure.
 *
 * @author hk125
 *
 */
public class KDTree<T extends KDData> {

  /**
   * the root node.
   */
  private KDNode<T> root;

  /**
   * the number of dimensions.
   */
  private int dim;

  /**
   * This is the constructor for the class.
   *
   * @param elementList
   *          is a list of objects which extend KDData
   */
  public KDTree(List<T> elementList) {
    this.dim = elementList.get(0).dimensions();
    this.root = buildTree(0, elementList);
  }

  /**
   * buildTree recursively builds the tree.
   *
   * @param dimension
   *          is the number of dimensions to compare on
   * @param elementList
   *          is the list of elements
   * @return the root node, with the tree built
   */
  private KDNode<T> buildTree(int dimension, List<T> elementList) {
    if (elementList.isEmpty()) {
      return null;
    }

    int size = elementList.size();
    if (size == 1) {
      return new KDNode<T>(elementList.get(0), null, null);
    }
    elementList.sort(new KDComparator(dimension));

    int length;

    if ((size % 2) != 0) {
      length = Math.round(size / 2);
    } else {
      length = size / 2;
    }

    T rootVal = elementList.remove(length);
    List<T> left = new ArrayList<>();
    List<T> right = new ArrayList<>();
    for (int i = 0; i < length; i++) {
      left.add(elementList.remove(0));
    }

    right.addAll(elementList);

    return new KDNode<T>(rootVal, buildTree((dimension + 1) % dim, left),
        buildTree((dimension + 1) % dim, right));
  }

  /**
   * findNN finds the NN to a target.
   *
   * @param target
   *          is the target data
   * @return the nearest neighbor to target
   */
  public T findNN(T target) {
    T best = null;
    double bestDistance = Double.POSITIVE_INFINITY;
    best = findNNHelper(0, target, best, bestDistance, root);
    return best;
  }

  /**
   * find NNHelper is a helper function for the nearest neighbor search.
   *
   * @param dimension
   *          is the current dimension
   * @param target
   *          is the target data
   * @param best
   *          is the best data so far
   * @param bestDist
   *          is the best distance so far
   * @param curr
   *          is the current node of the tree being searched on
   * @return the nearest neighbor to target
   */
  private T findNNHelper(int dimension, T target, T best, double bestDist,
      KDNode<T> curr) {

    if (curr == null) {
      return best;
    }

    T current = curr.getDatum();
    double currDist = target.distance(current);

    if (currDist < bestDist) {
      bestDist = currDist;
      best = current;
    }

    boolean wentLeft;

    double dimDif = target.compareDimension(dimension, current);

    if (dimDif < 0) {
      best = findNNHelper((dimension + 1) % dim, target, best, bestDist,
          curr.getLeft());
      wentLeft = true;
    } else {
      best = findNNHelper((dimension + 1) % dim, target, best, bestDist,
          curr.getRight());
      wentLeft = false;
    }

    if (Math.abs(dimDif) < best.distance(target)) {
      if (wentLeft) {
        best = findNNHelper((dimension + 1) % dim, target, best, bestDist,
            curr.getRight());
      } else {
        best = findNNHelper((dimension + 1) % dim, target, best, bestDist,
            curr.getLeft());
      }
    }
    return best;
  }

  /**
   * findkNN returns a list of k nearest neighbors
   *
   * @param target
   *          the target you wish to compare to
   * @param k
   *          the number of neighbors you are calculating
   * @return the k nearest neighbors
   */
  public List<T> findkNN(T target, int k) {
    if (k == 0) {
      return null;
    }
    MinMaxPriorityQueue<T> best = MinMaxPriorityQueue
        .orderedBy(new KDDistanceComparator(target)).maximumSize(k + 1)
        .create();
    MinMaxPriorityQueue<T> bestQ = findkNNHelper(0, target, best, root, k + 1);
    List<T> bestList = new ArrayList<>();
    bestList.addAll(bestQ);
    bestList.remove(target);
    bestList.sort(new KDDistanceComparator(target));
    return bestList;
  }

  /**
   * helper function for findkNN
   *
   * @param dimension
   *          dimension to search on
   * @param target
   *          the target of the search
   * @param best
   *          the best so far
   * @param curr
   *          the current node searched on
   * @param k
   *          the number of results you want
   * @return a pq of the best k nearest neighbors
   */
  private MinMaxPriorityQueue<T> findkNNHelper(int dimension, T target,
      MinMaxPriorityQueue<T> best, KDNode<T> curr, int k) {

    if (curr == null) {
      return best;
    }

    T current = curr.getDatum();

    best.add(current);

    boolean wentLeft;

    if (target.compareDimension(dimension, current) < 0) {
      best = findkNNHelper((dimension + 1) % dim, target, best, curr.getLeft(),
          k);
      wentLeft = true;
    } else {
      best = findkNNHelper((dimension + 1) % dim, target, best,
          curr.getRight(), k);
      wentLeft = false;
    }

    boolean bestYet = (Math.abs(target.compareDimension(dimension, current)) < best
        .peekLast().distance(target));

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

  /**
   * finds all KDData within a radius.
   *
   * @param target
   *          the center of the search radius
   * @param radius
   *          the radius you want to search within
   * @return a list of the elements which are in the radius
   */
  public List<T> radiusSearch(T target, double radius) {
    List<T> inside = new ArrayList<>();
    List<T> insideList = rsHelper(0, inside, target, radius, root);
    insideList.remove(target);
    insideList.sort(new KDDistanceComparator(target));
    return inside;
  }

  /**
   * a helper function for the radius search.
   *
   * @param dimension
   *          the dimension you are comparing on
   * @param inside
   *          the list of elements inside the radius
   * @param target
   *          the target element
   * @param radius
   *          the radius you are searching within
   * @param curr
   *          the current node
   * @return a list of the elements within a certain radius from the target
   */
  private List<T> rsHelper(int dimension, List<T> inside, T target,
      double radius, KDNode<T> curr) {

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

  @SuppressWarnings("unchecked")
  @Override
  public boolean equals(Object o) {
    if (o.equals(null)) {
      return false;
    }
    if (!(o instanceof KDTree)) {
      return false;
    }
    KDTree<T> other = (KDTree<T>) o;
    return (this.root.equals(other));
  }

  @Override
  public String toString() {
    return toStringHelp(root).toString();
  }

  private StringBuilder toStringHelp(KDNode<T> root) {
    // if (root == null) {
    // return new StringBuilder("");
    // }
    // StringBuilder main = new StringBuilder(root.getDatum().toString());
    // main.append(toStringHelp(root.getLeft()).toString());
    // main.append(toStringHelp(root.getRight()).toString());
    // return main;
    StringBuilder main = new StringBuilder(root.getDatum().toString());
    main.append(root.getLeft().toString());
    main.append(root.getRight().toString());
    return main;

  }
}
