package edu.brown.cs.hk125.trie;

import com.google.common.base.Objects;

public class Bigram {
	
	public String previous;
	public String word;
	
	public Bigram(String previous, String word) {
		this.previous = previous;
		this.word = word;
	}
	
	@Override
	public String toString() {
		return (previous + " " +  word);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(previous, word);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof Bigram)) {
			return false;
		}
		Bigram other = (Bigram) o;
		return ((this.previous.equals(other.previous)) 
				&& (this.word.equals(other.word)));
	}
}
