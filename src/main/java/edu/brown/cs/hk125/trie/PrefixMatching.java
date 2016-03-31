package edu.brown.cs.hk125.trie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.brown.cs.hk125.trie.Node;
import edu.brown.cs.hk125.trie.Trie;

public class PrefixMatching {
	public static List<String> prefix(String word, Trie trie) {
		Node node = trie.getNode(word);
		if (node == null) {
			return new ArrayList<>();
		}
		List<String> words = new ArrayList<>();
		return prefixHelp(node, words, word);
	}
	
	public static List<String> prefixHelp(Node node, List<String> words, String word) {
		if (node.isTerminal()) {
			words.add(word);
		}
		if (node.hasChild()) {
			Iterator<Node> children = node.getChildren();
			while (children.hasNext()) {
				Node child = children.next();
				words = prefixHelp(child, words, word + child.getChar());
			}
		}
		return words;
	}
}
