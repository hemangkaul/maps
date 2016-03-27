# maps
CS32 Maps

Which partner's Stars, Autocomplete, and Bacon were used?

Stars (KDTree) - Hemang (hk125)
Autocorrect (Trie) 
Bacon (Dijkstra) - Steven (sl234)

Known Bugs - none that we know of.

Design Details Specific to Code:

- We faced a decision implementing the A* Search. 
	
	The dilemma was as follows:

	1) The Dijkstra class was implemented generally by requiring a custom infoGetter for each graph / dataset. The primary function of the infoGetter was to get a list of neighbors, stored as 'Links' (containing among other attributes, the distance from the neighbor to the start node).

	2) infoGetter had a "getNearestNeighbor" method, which returned a list of all the neighbors of a node, including their distances from the very first node in the Dijkstra algorithm.

	3) infoGetter.getNearestNeighbor did not take in the end node, which is need in A* Search.

	We considered three solutions:

	1) Simply add an 'endNode' parameter to infoGetter.getNearestNeighbor. Then, when getting the list of neighbors, simultaneously, for each neighbor, get the distance from the neighbor to the end point, and adding that to the distance value. While this solution would have been super quick, we found it suboptimal from a style perspective, since the 'endNode' parameter would basically be ignored except when A* Search is being used.

	2) Perform Dijkstra as usual. Upon receiving the list of neighbors, update each of their 'distance' values by adding the heuristic distance. However, since the neighbors upon being returned did not contain latitude and longitude values, we would have to perform an additional query. This solution required an additional query, and would have been difficult / burdensome to make general (i.e. not specific to Maps), so we dismissed it.

	3) This was the solution we ended up going with. It took some work but it stylistically made the most sense, and it didn't require extra querying. We made a new class called AStar whose superclass was Dijkstra. We added a new method to infoGetter called getNearestNeighborAStar, which took the end node as a parameter. Then, in the AStar class, we overrode a few methods / made a new method so that infoGetter.getNearestNeighborAStar would be called instead of infoGetter.getNearestNeighbor.

	We think a further improvement would be to combine Dijkstra and AStar into a single class called GraphSearch. Then, when creating a new GraphSearch, we can specify whether we wnat to use Dijkstra or AStar. Time did not avail for us to implement this.