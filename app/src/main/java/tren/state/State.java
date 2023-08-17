package tren.state;

import com.google.common.collect.Sets;
import com.google.common.graph.Graph;
import com.google.common.graph.Graphs;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import tren.Domino;
import tren.TrainBuilder;
import tren.function.Distance;
import tren.function.Heuristic;

/**
 * A complete state of the "world" that specifies the currently built train, the set of {@link
 * Domino}s that remain to be added to the train, and the utility (value and fitness) of those
 * objects. The <b>value</b> of a particular state is determined by the {@link Distance} function
 * used by {@link TrainBuilder}. The <b>fitness</b> of a particular state is determined by the
 * {@link Heuristic} function used by {@link TrainBuilder} and the value of that state.
 */
@SuppressWarnings("UnstableApiUsage")
public final class State extends PartialState implements Comparable<State> {

  private final double value;
  private final double fitness;

  private State(Graph<Domino> train, Collection<Domino> remaining, double value, double fitness) {
    super(train, remaining);
    this.value = value;
    this.fitness = fitness;
  }

  public static State initial(Collection<Domino> remaining, Heuristic heuristic) {
    var state = PartialState.initial(remaining);
    return new State(state.train(), state.remaining(), 0, -heuristic.of(state));
  }

  public State withUtility(double value, Heuristic heuristic) {
    return new State(train(), remaining(), value, value - heuristic.of(this));
  }

  public List<State> successors(int seed) {
    var leaves = (value == 0) ? seedLeaves(seed) : leaves();
    return leaves.stream().flatMap(this::leafSuccessors).toList();
  }

  public double value() {
    return value;
  }

  public State max(State state) {
    return compareTo(state) >= 0 ? this : state;
  }

  @Override
  public int compareTo(State state) {
    return Double.compare(fitness, state.fitness);
  }

  @Override
  public String toString() {
    return "%s[value=%f, fitness=%f]".formatted(getClass().getSimpleName(), value, fitness);
  }

  private Stream<State> leafSuccessors(Domino leaf) {
    return compatible(leaf).map(domino -> successor(leaf, domino));
  }

  private Stream<Domino> compatible(Domino domino) {
    return remaining().stream().map(d -> d.oriented(domino)).filter(Objects::nonNull);
  }

  private State successor(Domino leaf, Domino domino) {
    var train = Graphs.copyOf(train());
    train.putEdge(leaf, domino);
    var remaining = Sets.difference(remaining(), train.nodes());
    return new State(train, remaining, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
  }

  private List<Domino> seedLeaves(int seed) {
    return remaining().stream()
        .map(domino -> domino.oriented(seed))
        .filter(Objects::nonNull)
        .toList();
  }

  private List<Domino> leaves() {
    return train().nodes().stream().filter(this::isLeaf).toList();
  }

  private boolean isLeaf(Domino domino) {
    return domino.isDouble() ? isDoubleLeaf(domino) : isNonDoubleLeaf(domino);
  }

  private boolean isDoubleLeaf(Domino domino) {
    // A double is only a valid leaf if there exists another domino to satisfy it.
    return train().outDegree(domino) < 3 && compatible(domino).findAny().isPresent();
  }

  private boolean isNonDoubleLeaf(Domino domino) {
    return train().outDegree(domino) == 0;
  }
}
