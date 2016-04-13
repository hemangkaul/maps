package edu.brown.cs.hk125.autocorrect;

import java.util.List;

/**
 * auto corrector for auto correcting.
 *
 * @author sl234
 *
 */
public interface AutoCorrector {
  /**
   * generate suggestions based on an input.
   *
   * @param input
   *          the string to be corrected
   * @return the list of suggestions based on the input
   */
  List<String> suggestions(String input);
}
