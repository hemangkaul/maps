package edu.brown.cs.sl234.autocorrect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.TreeMultimap;

/**
 * Ranker ranks a list of words for the autocorrect program according to either
 * the Bigram Unigram algorithm or a smart algorithm.
 *
 * @author sl234
 *
 */
public class Ranker {

  private Boolean           isSmart;
  private Trie              trie;
  private ArrayList<String> wordList;
  private static final int  MAX_SIZE = 5;

  // the maxSize of the ranker we return. needed to prevent magic number error

  /**
   * Constructs our ranker. If smartOrNo is true, our ranker will use the smart
   * ranker. If not, it will use the Bigram / Unigram model.
   *
   * @param smartOrNo
   *          , true if smart ranking is desired; false if not
   * @param trie
   *          , the Trie for the Ranker to access information from
   * @param wordList
   *          , a list of words. Iterated through for Bigram search
   */
  public Ranker(Boolean smartOrNo, Trie trie, ArrayList<String> wordList) {
    isSmart = smartOrNo;
    this.trie = trie;
    this.wordList = wordList;
  }

  /**
   * Ranks all the generated suggestions for a phrase, and returns the top five
   * Knows when to use bigram, unigram, or smart search. Non alphabet characters
   * will be converted to whitespace prior to performing the ranking
   *
   * Returns the top five suggestions for completing the phrase
   *
   * @param phrase
   *          , any String
   * @param g
   *          , a generator to generate suggestions.
   * @return the top five suggestions for completing the phrase
   */
  public ArrayList<String> topRanked(String phrase, Generator g) {
    String cleanedPhrase = phrase.replaceAll("[^\\sa-zA-Z]", " ")// replace all
                                                                 // non-word,
                                                                 // non-space
                                                                 // characters
                                                                 // with a
                                                                 // white space
        .replaceAll("^\\s+", "") // remove leading white space
        .replaceAll("\\s+", " ")// replace long whitespaces with just a single
                                // whitespace
        .toLowerCase(); // Finally, convert all to lowercase
    String[] splitPhrase = phrase.split("");
    if (phrase.split("")[splitPhrase.length - 1].equals(" ")) {
      // if there is a trailing whitespace, there shall be no suggestions
      // We could have also done phrase.substring(word.length() - 1). But in the
      // case of an
      // empty string, this would have resulted in an out of bound exception.
      return new ArrayList<String>();
    } else {
      if (isSmart) {
        return smartRanker(cleanedPhrase, g);
      } else if (cleanedPhrase.split(" ").length == 1) { // if there is just one
                                                         // word...
        return unigram(cleanedPhrase, g);
      } else {
        return bigram(cleanedPhrase, g);
      }
    }
  }

  /**
   * Helper function to bigram. Sorts a Map of Strings and Integers according to
   * the Integer Value
   *
   * Ties are broken based on LeafCount value for each Key. Finally if there are
   * still ties, those are settled alphabetically
   *
   * Returns the top five values after sorting.
   *
   * @param map
   *          , a map of strings and integers to sort.
   * @param t
   *          , the Trie we will access infomration from whlie sorting
   * @return
   */
  private static ArrayList<String> bigramSorter(Map<String, Integer> map,
      Trie t) {
    List<Entry<String, Integer>> wordsToRank = new LinkedList<Map.Entry<String, Integer>>(
        map.entrySet());
    // We must call take a Trie as argument, instead of using our private field,
    // otherwise
    // we get a "cannot make static reference to non-static method" error when
    // sorting with respect
    // to leafCounts

    // We create a list of the Maps... to sort them using Comparator
    // Sort list with comparator, to compare the Map values according to size of
    // Collection<Integer>

    // We perform three sorts. We first sort by alphabetical order.
    Collections.sort(wordsToRank,
        new Comparator<Map.Entry<String, Integer>>() {
          @Override
          public int compare(Map.Entry<String, Integer> word1,
              Map.Entry<String, Integer> word2) {
            return word1.getKey().compareTo(word2.getKey());
          }
        });

    // Now we sort by leafCount. Words with the same leafCount will remain in
    // their
    // original sorted alphabetical order.

    Collections.sort(wordsToRank,
        new Comparator<Map.Entry<String, Integer>>() {
          @Override
          public int compare(Map.Entry<String, Integer> word1,
              Map.Entry<String, Integer> word2) {
            return Integer.compare(
                t.prefixTrie(word2.getKey().split(" ")[0]).getLeafCount(),
                t.prefixTrie(word1.getKey().split(" ")[0]).getLeafCount());
            // We switch the order of comparison, since we want large leafCounts
            // to
            // appear first
          }
        });

    // Finally we sort by value! Ties will already be sorted in their original
    // sorted order by leafCount and alphabet.

    Collections.sort(wordsToRank,
        new Comparator<Map.Entry<String, Integer>>() {
          @Override
          public int compare(Map.Entry<String, Integer> word1,
              Map.Entry<String, Integer> word2) {
            return (word2.getValue().compareTo(word1.getValue()));
            // We want large integers to appear first, so again we switch the
            // order
            // of the comparison
            // Also, we use elem.split(" ")[0] in case the suggestion is two
            // words
            // long,
          }
        });

    ArrayList<String> rankedWords = new ArrayList<String>();
    for (Map.Entry<String, Integer> entry : wordsToRank) {
      rankedWords.add(entry.getKey());
    }
    return rankedWords;
  }

  /**
   * Generates a list of suggestions for the phrase, and then ranks according to
   * bigram ranking algorithm.
   *
   * Ties are settled using the unigram ranking algorithm. Further ties are
   * settled by ranking tied suggestions alphabetically.
   *
   * @param phrase
   *          , any String
   * @param g
   *          , a generator to generate suggestions
   * @return the top five suggestions for the phrase given the generator, where
   *         the suggestions are ranked according to the bigram ranking
   *         algorithm.
   */
  private ArrayList<String> bigram(String phrase, Generator g) {

    String[] splitPhrase = phrase.split(" ");
    String lastWord = splitPhrase[splitPhrase.length - 1];
    String secondToLast = splitPhrase[splitPhrase.length - 2];
    String restOfPhrase = phrase.substring(0,
        phrase.length() - lastWord.length()); // everything except the last word

    Set<String> suggestions = g.generate(lastWord);

    Map<String, Integer> wordMap = new HashMap<String, Integer>();
    // We store how often our last word appears as a Bigram. Since we want
    // to perform sorting on the values in this map later, we use a HashMap
    // instead of an ArrayListMultiMap, which makes it difficult to include keys
    // with current Bigram counted values of 0.

    for (String elem : suggestions) {
      wordMap.put(elem, 0);
    } // we first add all the suggestions to the map
    for (int i = 0; i < wordList.size() - 1; i++) {
      String nextWord = wordList.get(i + 1); // the word following the current
                                             // word
      if (wordList.get(i).equals(secondToLast)) {
        // if our current word equals the second to last word in the input
        // phrase
        for (String sug : suggestions) { // iterate through suggestions
          if (sug.split(" ")[0].equals(nextWord)) {
            // We use elem.split(" ")[0] in case the suggestion is two words
            // long,
            // so we only select the first word.
            // the suggestions are unique, so we don't have to worry about
            // double counting
            int curVal = wordMap.get(sug); // get current value in the map for
                                           // 'sug'
            wordMap.put(sug, curVal + 1); // update its value
          }
        }

      }
      // finds every time the second to last word occurs in the wordList. Stores
      // the word after it
      // if that word is in suggestions.
    }
    ArrayList<String> rankedWords = bigramSorter(wordMap, trie);
    ArrayList<String> topRanked = new ArrayList<String>();
    if (suggestions.contains(lastWord)) {
      topRanked.add(restOfPhrase + lastWord);
    }
    while ((topRanked.size() < MAX_SIZE) && (rankedWords.size() >= 1)) {
      String topWord = rankedWords.get(0);
      if (!topWord.equals(lastWord)) {
        // if it equals the last word, it will already be in the list!
        topRanked.add(restOfPhrase + rankedWords.get(0));
      }
      rankedWords.remove(0);
    }

    return topRanked;
  }

  /**
   * Generates a list of suggestions for the phrase, and then ranks according to
   * unigram ranking algorithm (how many times the phrase appears in the Trie /
   * word list).
   *
   * @param phrase
   *          , any String
   * @param g
   *          , a generator to generate suggestions
   * @return the top five ranked suggestions
   */
  private ArrayList<String> unigram(String phrase, Generator g) {

    String[] splitPhrase = phrase.split(" ");
    String lastWord = splitPhrase[splitPhrase.length - 1];
    String restOfPhrase = phrase.substring(0,
        phrase.length() - lastWord.length()); // everything except the last word

    Set<String> suggestions = g.generate(lastWord);

    TreeMultimap<Integer, String> wordMap = TreeMultimap.create();
    for (String word : suggestions) {
      wordMap
          .put(trie.prefixTrie(word.split(" ")[0]).getLeafCount(), word);
      // word.split(" ")[0] returns the first word if there are two, and if
      // there is just one returns the first one
    }
    // the TreeMultimap we just made is of form <leafCount, String>
    // so it lists all the suggestions that appear once, all the suggestions
    // that appear twice, etc.
    ArrayList<String> topRanked = new ArrayList<String>();
    if (suggestions.contains(lastWord)) {
      topRanked.add(restOfPhrase + lastWord);
    }
    while ((topRanked.size() < MAX_SIZE) && (wordMap.size() > 0)) {
      int highestKey = wordMap.asMap().lastKey(); // we take the highest
                                                  // leafCount

      String match = wordMap.get(highestKey).first();
      // the words in wordMap.get(highestKey) are already in alphabetical order!
      if (!match.equals(lastWord)) { // the Last Word will already be in the
                                     // list...
        topRanked.add(restOfPhrase + match);
      } // we keep removing until the highest
        // key is empty, and it is removed,
        // and the loop continues.
      wordMap.remove(highestKey, match);
    }
    return topRanked;
  }

  /**
   * Smart Ranker.
   *
   * @param phrase
   *          , any String
   * @param g
   *          , a Generator to generate suggestions
   * @return a smarter ranking of said suggestions
   */
  private ArrayList<String> smartRanker(String phrase, Generator g) {
    String[] splitPhrase = phrase.split(" ");
    String lastWord = splitPhrase[splitPhrase.length - 1];

    Set<String> suggestions = g.generate(lastWord);

    String qwert = "qwertyuiop";
    String asdf = "asdfghjkl";
    String zxcv = "zxcvbnm";

    // Create a coordinate for every key on the keyboard, store it in an
    // arrayList.
    ArrayList<Map<Integer, Integer>> distanceMap = new ArrayList<Map<Integer, Integer>>();
    int i = 0;
    for (String let : qwert.split("")) {
      HashMap<Integer, Integer> temp = new HashMap<Integer, Integer>();
      temp.put(0, i);
      distanceMap.add(temp);
      i += 1;
    }
    int j = 0;
    for (String let : asdf.split("")) {
      HashMap<Integer, Integer> temp = new HashMap<Integer, Integer>();
      temp.put(1, j);
      distanceMap.add(temp);
      j += 1;
    }
    int k = 0;
    for (String let : zxcv.split("")) {
      HashMap<Integer, Integer> temp = new HashMap<Integer, Integer>();
      temp.put(0, k);
      distanceMap.add(temp);
      k += 1;
    }

    Map<String, Double> wordDistances = new HashMap<String, Double>();
    for (String word : suggestions) {
      wordDistances.put(word, 0.0);
    }

    return new ArrayList<String>();
  }
}
