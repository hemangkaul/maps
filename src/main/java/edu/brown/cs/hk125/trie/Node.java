package edu.brown.cs.hk125.trie;

import java.util.HashMap;
import java.util.Iterator;

public class Node {
	
	private Character c;
	private boolean terminal;
	private HashMap<Character, Node> children;
	
	public Node(Character c, boolean terminal) {
		this.c = c;
		this.terminal = terminal;
		this.children = new HashMap<Character, Node>();
	}
	
	public Character getChar() {
		return c;	
	}
	
	public Iterator<Node> getChildren() {
		return children.values().iterator();
	}
	
	public boolean hasChild() {
		return (!children.isEmpty());
	}
	
	public Node getChild(Character c) {
		return children.get(c);
	}
	
	public Node setChild(Character c, boolean terminal) {
		Node child = new Node(c, terminal);
		children.put(c, child);
		return child;
	}
	
	public boolean isTerminal() {
		return terminal;
	}
	
	public void setTerminal(boolean terminal) {
		this.terminal = terminal;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o.equals(null)) {
			return false;
		}
		if (!(o instanceof Node)) {
			return false;
		}
		Node other = (Node) o;
		return ((this.c.equals(other.c)) 
				&& (this.terminal == other.terminal)
				&& (this.children.equals(other.children)));
	}
	
	@Override
	public String toString() {
		return c.toString();
	}
 }
