# maps
CS32 Maps

Which partner's Stars, Autocomplete, and Bacon were used?

Stars (KDTree) - Hemang (hk125)
Autocorrect (Trie) 
Bacon (Dijkstra) - Steven (sl234)

Known Bugs - none that we know of.

Design Details Specific to Code:

- We faced a decision implementing the A* Search. From a style perspective, it'd be optimal to create a separate class called A* (or AStar) which was similar to Dijkstra. The alternative was to incorporate the A* element into the querying. The specific considerations were as follows:

	The Dijkstra class was implemented generally by requiring a custom infoGetter for each graph / dataset. The primary function of the infoGetter was to get a list of neighbors, stored as 'Links' (containing among other attributes, the distance from the neighbor to the start node). The easiest way to incorporate A* search was, in the infoGetter, when getting the list of neighbors, simultaneously, for each neighbor, getting the distance from the neighbor to the end point, and adding that to the distance value.

	The alternative was to create a separate class called AStar. Under this method, the list of neighbors returned by the InfoGetter would NOT include the additional distance from the neighbor to the end point. Thus, we would have to perform an additional iteration on this list of neighbors, adding the distance from the neighbor to the end point. Since in the current Dijkstra implementation, we use only the id's to identify the nodes (and thus don't store the latitude and longitude), we would either have to perform an additional query to get the latitudes and longitudes of each of the neighbors, or we would have to increase our storage and create an additional node class.

	Though styllistically suboptimal, we decided to go with the former method, since it was much easier and saved storage.