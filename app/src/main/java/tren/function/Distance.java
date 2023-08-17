package tren.function;

import java.util.function.ToDoubleFunction;
import tren.TrainBuilder;
import tren.state.PartialState;

/**
 * A distance function that is used to compare the utility of two partially built trains.
 *
 * @see TrainBuilder
 */
@FunctionalInterface
public interface Distance {

  static Distance byTrainValue() {
    return distance(PartialState::trainValue);
  }

  static Distance byTrainSize() {
    return distance(PartialState::trainSize);
  }

  private static Distance distance(ToDoubleFunction<PartialState> mapper) {
    return (start, end) -> mapper.applyAsDouble(end) - mapper.applyAsDouble(start);
  }

  double between(PartialState start, PartialState end);
}
