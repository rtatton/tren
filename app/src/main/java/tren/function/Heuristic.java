package tren.function;

import tren.TrainBuilder;
import tren.state.PartialState;

/**
 * A heuristic function that is used to estimate the proximity of a partially built train to the
 * optimal train.
 *
 * @see TrainBuilder
 */
@FunctionalInterface
public interface Heuristic {

  static Heuristic byRemainingValue() {
    return PartialState::remainingValue;
  }

  static Heuristic byRemainingSize() {
    return PartialState::remainingSize;
  }

  double of(PartialState state);
}
