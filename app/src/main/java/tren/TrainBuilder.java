package tren;

import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Queue;
import java.util.Set;
import tren.function.Distance;
import tren.function.Heuristic;
import tren.state.State;

/**
 * A builder of Mexican trains.
 *
 * <p>A* search is used to find the optimal Mexican train, given the seed {@link Domino} and the
 * {@link Domino} set available to build the train. The "optimality" of a particular train is
 * determined by the specified {@link Distance} function and {@link Heuristic} function.
 */
public final class TrainBuilder {

  private final Set<Domino> dominos;

  private Distance distance;
  private Heuristic heuristic;
  private Integer seed;

  private TrainBuilder() {
    this.dominos = Sets.newHashSet();
  }

  /** Returns an empty train builder. */
  public static TrainBuilder create() {
    return new TrainBuilder();
  }

  /**
   * Returns a Mexican train builder that minimizes the number of pips not contained in the train.
   * Conversely, this builder maximizes the number of pips contained in the train.
   */
  public static TrainBuilder byRemainingValue() {
    return create().setDistance(Distance.byTrainValue()).setHeuristic(Heuristic.byRemainingValue());
  }

  /**
   * Returns a Mexican train builder that minimizes the number of dominos not contained in the
   * train. Conversely, this builder maximizes the number of dominos contained in the train.
   */
  public static TrainBuilder byRemainingSize() {
    return create().setDistance(Distance.byTrainSize()).setHeuristic(Heuristic.byRemainingSize());
  }

  /**
   * Adds a {@link Domino} with the specified top and bottom pips to the set of dominos that can be
   * used to build the train.
   */
  public TrainBuilder addDomino(int top, int bottom) {
    dominos.add(Domino.of(top, bottom));
    return this;
  }

  /** Sets the seed domino that is required to be matched by the first domino in the built train. */
  public TrainBuilder setSeed(int seed) {
    this.seed = seed;
    return this;
  }

  /**
   * Sets the distance function that is used to compare the utility of two partially built trains.
   */
  public TrainBuilder setDistance(Distance distance) {
    this.distance = distance;
    return this;
  }

  /**
   * Sets the heuristic function that is used to estimate the proximity of a partially built train
   * to the optimal train.
   */
  public TrainBuilder setHeuristic(Heuristic heuristic) {
    this.heuristic = heuristic;
    return this;
  }

  /** Returns an optimal Mexican train by applying the A* search algorithm. */
  public State build() {
    var frontier = newFrontier(dominos);
    var current = frontier.peek();
    var best = current;
    while (!frontier.isEmpty()) {
      current = frontier.remove();
      best = best.max(current);
      for (var successor : current.successors(seed)) {
        var value = current.value() + distance.between(current, successor);
        if (value > successor.value()) {
          frontier.add(successor.withUtility(value, heuristic));
        }
      }
    }
    return best;
  }

  private Queue<State> newFrontier(Collection<Domino> dominos) {
    var frontier = Queues.<State>newPriorityQueue();
    frontier.add(State.initial(dominos, heuristic));
    return frontier;
  }
}
