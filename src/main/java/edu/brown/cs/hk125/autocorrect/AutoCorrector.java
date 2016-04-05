package edu.brown.cs.hk125.autocorrect;

import java.util.List;

public interface AutoCorrector {
  public List<String> suggestions(String input);
}
