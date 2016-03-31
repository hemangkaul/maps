package edu.brown.cs.sl234.autocorrect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * A Trie data structure.
 *
 * @author sl234
 *
 */
public class Trie {

  /**
   * Tries are leaves if they are the last letter of a word in the trie. If
   * leafCount == 0, then the Trie is not a leaf. If leafCount >= 0, then the
   * Trie is a leaf. that appears n times.
   */
  private int               leafCount;
  /**
   * The name of the Trie. The root node must have name "";
   */
  private String            name;         // String, not character, since
  // with prefix tree, the name of the String may have length greater than one!
  private ArrayList<Trie>   trieChildren;
  private ArrayList<String> childrenNames;

  /**
   * Creates an empty, top Trie. To create a Trie users should use this, or
   * makeTrie, which takes a list of words and makes a Trie.
   */
  public Trie() {
    leafCount = 0;
    name = "";
    trieChildren = new ArrayList<Trie>();
    childrenNames = new ArrayList<String>();
  }

  /**
   * Constructs a Trie that cannot be the top Trie, since it has a letter. Set
   * to private to prevent confusion / mis-use.
   *
   * @param name
   *          - This should be a letter from A-Z, unless we are dealing with the
   *          root node.
   *
   */
  private Trie(String letter, boolean isLeaf) {
    if (isLeaf) {
      leafCount = 1;
    } else {
      leafCount = 0;
    }
    name = letter;
    trieChildren = new ArrayList<Trie>();
    childrenNames = new ArrayList<String>();
  }

  /**
   * Gets the leafCount.
   *
   * @return leafCount of the Trie.
   */
  public int getLeafCount() {
    return leafCount;
  }

  /**
   * Tries are leaves if they are the last letter of a word in the trie. If
   * leafCount == 0, then the Trie is not a leaf. If leafCount is greater than
   * 0, then the Trie is a leaf. that appears n times.
   *
   * @param leafCount
   *          , the leafCount you want to set for the Trie. In essence, you are
   *          setting up how many times the word the Trie represents appears in
   *          the Trie!
   */
  public void setLeafCount(int leafCount) {
    this.leafCount = leafCount;
  }

  /**
   * Gets the name of the Trie. Should be one character or nothing.
   *
   * @return the name of the Trie.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the Trie. Input should be a String
   *
   * @param name
   *          - name should one letter long
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets a list of the children of the Tries (which are Tries themselves).
   *
   * @return an Array List of the children of the Tries
   */
  public ArrayList<Trie> getTrieChildren() {
    return trieChildren;
  }

  /**
   * Sets the children of the Trie.
   *
   * @param trieChildren
   *          an arrayList of children, which will be the new children of the
   *          Trie
   */
  public void setTrieChildren(ArrayList<Trie> trieChildren) {
    this.trieChildren = trieChildren;
  }

  /**
   * Gets the names of the children in the Trie.
   *
   * @return a list of the names (Strings) of the children of the Trie
   */
  public ArrayList<String> getChildrenNames() {
    return childrenNames;
  }

  /**
   * Sets the names of the children in the Trie. Please note you are not
   * actually changing the names of the children, just the corresponding list of
   * names.
   *
   * @param childrenNames
   *          , an ArrayList of names of the children in the Trie.
   */
  public void setChildrenNames(ArrayList<String> childrenNames) {
    this.childrenNames = childrenNames;
  }

  /**
   * Adds a word to the Trie.
   *
   * @param word
   *          must contain only letters of the alphabet.
   */
  private void addWord(String word) {
    if (word.length() > 1) { // if the word is at least one letter long...
      String firstLetter = word.substring(0, 1); // first letter in the word
      int index = childrenNames.indexOf(firstLetter);
      if (index != -1) { // if the first letter of the word is in the current
        // list of leaf names
        Trie subjTrie = trieChildren.get(index); // the corresponding trie
        subjTrie.addWord(word.substring(1)); // recursion
        trieChildren.set(index, subjTrie);
      } else { // currently leafnames does not contain the first letter
        childrenNames.add(firstLetter);
        Trie newTrie = new Trie(firstLetter, false);
        // not a leaf since there are letters remaining!
        newTrie.addWord(word.substring(1));
        trieChildren.add(newTrie);
      }
    } else if (word.length() == 1) {
      String firstLetter = word.substring(0, 1);
      int index = childrenNames.indexOf(firstLetter);
      if (index != -1) {
        Trie subjTrie = trieChildren.get(index);
        subjTrie.setLeafCount(subjTrie.getLeafCount() + 1);
        trieChildren.set(index, subjTrie);
      } else {
        childrenNames.add(firstLetter);
        Trie newTrie = new Trie(firstLetter, true);
        // it is a leaf, since it's the last letter!
        trieChildren.add(newTrie);
      }
    }
    // finally, if word.length() is zero, we do nothing, so no code for it
  }

  /**
   * Makes a Trie given a list of words.
   *
   * @param wList
   *          a list of words
   * @return A Trie which contains every word in the list of words.
   */
  public static Trie makeTrie(ArrayList<String> wList) {
    Trie newTrie = new Trie();
    for (String word : wList) {
      newTrie.addWord(word);
    }
    return newTrie;
  }

  /**
   * Condenses the Trie, removing any redundant nodes. This creates a prefix
   * tree. It's possible to add new words to a condensed tree, but won't
   * necessarily result in the optimally configured tree. While this is
   * subideal, please note the condense function is intended only to be used
   * after all words have been added, so this shouldn't be an issue barring
   * atypical situations.
   *
   */
  public void condense() {
    for (Trie child : trieChildren) {
      child.condense(); // recursion
    }
    ArrayList<String> newChildrenNames = new ArrayList<String>();
    for (Trie child : trieChildren) {
      // update the names of the children!
      newChildrenNames.add(child.getName());
    }
    childrenNames = newChildrenNames;
    if ((trieChildren.size() == 1) && (leafCount == 0)
        && (!name.equals(""))) {
      // Three criteria for condensing. First, it must only have one children.
      // Second it can't be a leaf by itself. For example, make a trie with only
      // "a"
      // and "as". If you combine "a" and "as" into one node, you lose
      // the "a" word.
      // Thirdly, we don't want to condense the very top level. In other words,
      // we want to keep our root Trie as having name "", even
      // if it only has one child. This consistency
      // makes searching for words in the Trie much easier.
      Trie child = trieChildren.get(0);
      name += child.getName();
      leafCount = child.getLeafCount();
      trieChildren = child.getTrieChildren();
      childrenNames = child.getChildrenNames();
    }
  }

  /**
   * Checks whether a word appears in a Trie.
   *
   * @param word
   *          - any word
   * @return true if the word appears in the Trie, false if not
   */
  public Boolean inTrie(String word) {
    for (Trie child : trieChildren) {
      String cName = child.getName(); // name of the child
      if (cName.equals(word)) {
        if (child.getLeafCount() != 0) { // the child must be a leaf!
          // we nest another if statement instead of writing:
          // if ((cName.equals(word)) && (child.getLeafCount() != 0)
          // because if we wrote the above, and cName does indeed equal the
          // word, but
          // leafCount also equals 0, the else if statement would still be run.
          // In short, nesting helps reduce potential computation
          return true;
        }
      } else if (word.startsWith(cName)) {
        String restOfWord = word.substring(cName.length());
        // cuts off the first part of the word which matches 'name'
        return child.inTrie(restOfWord);
      }
    }
    return false;
  }

  /**
   * Takes in a 'prefix' (any string). Finds if the prefix exists in the Trie.
   * If so, returns the Trie of the last letter of the prefix. For example, if
   * the prefix is 'compl', the function will return the corresponding Trie
   * named 'l'. If the prefix does not exist in the Trie, returns an empty Trie.
   * Used as a helper to prefixSuggestions method
   *
   * @param word
   *          - any string
   * @return the Trie of the last letter of the prefix ("word"), if it exists.
   *         If the prefix doesn't exist in the Trie, then it returns an empty
   *         Trie.
   */
  protected Trie prefixTrie(String word) {
    for (Trie child : trieChildren) {
      String cName = child.getName(); // name of the child
      if (cName.equals(word)) {
        return child;
      } else if (word.startsWith(cName)) {
        String restOfWord = word.substring(cName.length());
        // cuts off the first part of the word which matches 'name'
        return child.prefixTrie(restOfWord); // recursion
      } else if (cName.startsWith(word)) {
        // when the Trie is condensed, we have cases where
        // the last letter of a prefix is no longer represented in an individual
        // trie.
        // for example, "hemisphere" might be split into "h-e-misphere".
        // So if you typed "hemis", you would eventually be searching for "mis"
        // in the children of the "e" Trie, and you wouldn't find it.
        return child;
      }
    }
    return new Trie();
  }

  /**
   * Returns all the words in a Trie. Adds the prefix to all the words. From a
   * programming perspective, the prefix helps with recursion.
   *
   * @param prefix
   *          - a String, which will be added to all the words returned
   * @return all the words in a Trie
   */
  public Set<String> wordsIn(String prefix) {
    Set<String> wordSet = new HashSet<String>();
    if (leafCount != 0) {
      wordSet.add(prefix + name);
    }
    for (Trie child : trieChildren) {
      wordSet.addAll(child.wordsIn(prefix + name));
    }
    return wordSet;
  }

  /**
   * Generates a list of suggestions using prefix matching. Finds all the words
   * in the Trie which start with the inputed string. (The inputed string is the
   * prefix)
   *
   * @param word
   *          any String
   * @return an ArrayList of words in the Trie which start with the inputed
   *         prefix
   */

  public Set<String> prefixSuggestions(String word) {
    if (word.equals("")) { // the word doesn't exist!
      return new HashSet<String>();
    } else {
      Trie prefixTrie = prefixTrie(word);
      String trieName = prefixTrie.getName();
      if (word.contains(trieName)) {
        return prefixTrie.wordsIn(word.substring(0, word.length()
            - trieName.length()));
      } else {
        String antiPrefix = word;
        while (!trieName.contains(antiPrefix)) {
          antiPrefix = antiPrefix.substring(1);
          // keep removing first letter of antiPrefix until antiPrefix
          // is contained within trieName
        }
        return prefixTrie.wordsIn(word.substring(0, word.length()
            - antiPrefix.length()));
      }
      // Things are trickier since we have condensed tries. Sometimes the name
      // of the prefix trie
      // is longer than our word. For example, if "hemisphere" is in a Trie, and
      // "hemis" is not
      // the prefixTrie of "hemis" might be "hemisphere".

      // So we break it up into cases: When our word contains the name of the
      // prefix Trie, and
      // when it doesn't
    }
    // We perform the wordsIn function... but the input will be the 'word'
    // with the [last letter]* removed.
    // This is necessary since otherwise, the wordsIn method will slightly
    // malfunction, repeating the removed last letter twice in all the words
    // returned.
    //
    // For example, if the prefix is "he" and "hear" is in the tree, it will
    // return "heear" (the "e" is repeated twice).
    // * Sometimes in a condensed tree, we need to remove more than the last
    // letter; we need
    // to remove the entire name of the prefix Trie. Thus instead of writing
    // "word.length() - 1", we write
    // "word.length() - prefixTrie.getName().length()"
  }

  /**
   * Given a word, returns all versions of the word with one substitution.
   *
   * @param word
   *          , any String
   * @return an ArrayList of all versions of 'word' with one substitution
   */
  private ArrayList<String> substitutionHelper(String word) {
    ArrayList<String> wordList = new ArrayList<String>();
    // an empty list to which we will add words later
    String alphabet = "abcdefghijklmnopqrstuvwxyz";
    for (int i = 0; i < word.length(); i++) {
      for (int j = 0; j < alphabet.length(); j++) {
        String subbedWord = word.substring(0, i) + alphabet.charAt(j)
            + word.substring(i + 1);

        // replaces each letter in the word with each letter of the alphabet
        wordList.add(subbedWord);
      }
    }
    return wordList;
  }

  /**
   * Given a word, returns all versions of the word with one inserted letter.
   *
   * @param word
   *          , any String
   * @return an ArrayList of all versions of 'word' with one inserted letter
   *
   */
  private ArrayList<String> insertionHelper(String word) {
    ArrayList<String> wordList = new ArrayList<String>();
    // an empty list to which we will add words later
    String alphabet = "abcdefghijklmnopqrstuvwxyz";
    for (int i = 0; i < word.length() + 1; i++) {
      for (int j = 0; j < alphabet.length(); j++) {
        String insWord = word.substring(0, i) + alphabet.charAt(j)
            + word.substring(i);
        // at each spot in the word inserts each letter of the alphabet
        wordList.add(insWord);
      }
    }
    return wordList;
  }

  /**
   * Given a word, returns all versions of the word with one deleted letter.
   *
   * @param word
   *          , any String
   * @return an ArrayList of all versions of 'word' with one deleted letter
   */
  private ArrayList<String> deletionHelper(String word) {
    ArrayList<String> wordList = new ArrayList<String>();
    // an empty list to which we will add words later
    for (int i = 0; i < word.length(); i++) {
      String delWord = word.substring(0, i) + word.substring(i + 1);
      wordList.add(delWord);
    }
    return wordList;
  }

  /**
   * Generates a list of suggestions which appear in the Trie using levenshtein
   * edit distance, with the given string and given maximum distance. "Maximum"
   * means words with smaller levenshtein distance which appear in the Trie will
   * also be in the list of suggestions
   *
   * @param word
   *          any String
   * @param distance
   *          the maximum Levenshtein edit distance
   *
   * @return a list of words which appear in the Trie which are within the
   *         maximum Levenshtein edit distance from "word"
   */
  public Set<String> levSuggestions(String word, int distance) {
    Set<String> wordSet = new HashSet<String>();
    // We plan on adding words to the set, since order doesn't matter
    // and we want to avoid duplicates
    if (distance <= 0) {
      if (inTrie(word)) {
        // if the distance is 0 or negative, we assume it's zero, and only the
        // word itself can be returned if
        // it appears in the Trie
        wordSet.add(word);
      }
      return wordSet;
    } else {
      ArrayList<String> tempList = new ArrayList<String>();
      // there will be no duplicates in tempList, since substitutionHelper,
      // insertionHelper, and deletionHelper
      // will each produce a list of unique words of different lengths
      tempList.addAll(substitutionHelper(word));
      tempList.addAll(insertionHelper(word));
      tempList.addAll(deletionHelper(word));
      for (String word2 : tempList) {
        if (inTrie(word2)) {
          wordSet.add(word2); // words with smaller levenshtein distance than
                              // the max are to be added as well;
        }
        wordSet.addAll(levSuggestions(word2, distance - 1));
      }
    }
    return wordSet;
  }

  /**
   * Generates a list of suggested words under the presumption that the user
   * forgot to type a space. Returns all the possible splits of the word such
   * that both of the split output words are in the Trie
   *
   * @param word
   *          , any String
   * @return a set of words which appear in the Trie which are edited versions
   *         of the inputed word where a space is added
   */
  public Set<String> whiteSpaceSuggestions(String word) {
    Set<String> wordSet = new HashSet<String>();
    // an empty list to which we will add words later
    for (int i = 1; i < word.length(); i++) {
      String output1 = word.substring(0, i);
      String output2 = word.substring(i);
      if ((inTrie(output1)) && (inTrie(output2))) {
        wordSet.add(output1 + " " + output2);
      }
    }
    return wordSet;
  }

  // Don't forget to write a testing function for the run method...

  // Break the parsing part into a separate method and test that. Have it return
  // options
}
