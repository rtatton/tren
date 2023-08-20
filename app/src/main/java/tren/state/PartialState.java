package tren.state;

import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import tren.Domino;

/**
 * A partial state of the "world" that specifies the currently built train and the set of {@link
 * Domino}s that remain to be added to the train. This class exists to decouple the given train and
 * remaining dominos from their utility.
 *
 * @see State
 */
@SuppressWarnings("UnstableApiUsage")
public class PartialState {

  private final Graph<Domino> train;
  private final Set<Domino> remaining;

  protected PartialState(Graph<Domino> train, Collection<Domino> remaining) {
    this.train = ImmutableGraph.copyOf(train);
    this.remaining = Set.copyOf(remaining);
  }

  protected static PartialState initial(Collection<Domino> remaining) {
    return new PartialState(GraphBuilder.directed().build(), remaining);
  }

  public int trainSize() {
    return train.nodes().size();
  }

  public int trainValue() {
    return value(train.nodes());
  }

  public int remainingValue() {
    return value(remaining);
  }

  public int remainingSize() {
    return remaining.size();
  }

  public Graph<Domino> train() {
    return train;
  }

  public Set<Domino> remaining() {
    return remaining;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof PartialState state)
      return train.equals(state.train) && remaining.equals(state.remaining);
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(train, remaining);
  }

  private int value(Collection<Domino> dominos) {
    return dominos.stream().mapToInt(Domino::value).sum();
  }
}
