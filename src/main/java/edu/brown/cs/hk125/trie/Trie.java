package edu.brown.cs.hk125.trie;

public class Trie {
	
	private Node root;
	
	public Trie() {
		this.root = new Node(null, false);
	}
	
	public Node getRoot() {
		return root;
	}
	
	public Node getNode(String word) {
		return getNodeHelp(word, root);
	}
	
	public Node getNodeHelp(String word, Node curr) {
		if (word.length() == 0) {
			return curr;
		}
		Node child = curr.getChild(word.charAt(0));
		if (child == null) {
			return null;
		} else {
			return getNodeHelp(word.substring(1), child);
		}
	}
	
	public void insert(String word) {
		insertHelp(word, root);
	}
	
	public void insertHelp(String word, Node curr) {
		if (word.length() == 0) {
			curr.setTerminal(true);
		} else {
			Node child = curr.getChild(word.charAt(0));
			if (child == null) {
				child = curr.setChild(word.charAt(0), false);
			}
			insertHelp(word.substring(1), child);
		}
	}
}
