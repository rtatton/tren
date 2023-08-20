package tren;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public final class DominoTests {

  @ParameterizedTest
  @MethodSource("topsAndBottoms")
  public void dominoValueIsSumOfTopAndBottom(int top, int bottom) {
    var sum = top + bottom;
    assertEquals(sum, Domino.of(top, bottom).value());
    assertEquals(sum, Domino.of(bottom, top).value());
  }

  @ParameterizedTest
  @MethodSource("topsAndBottoms")
  public void dominoIsDoubleWhenTopAndBottomAreEqual(int top, int bottom) {
    assertEquals(top == bottom, Domino.of(top, bottom).isDouble());
  }

  @ParameterizedTest
  @MethodSource("topsAndBottoms")
  public void dominoReversedIsEqualToBottomAndTopOfOriginalDomino(int top, int bottom) {
    var original = Domino.of(top, bottom);
    var reversed = original.reversed();
    assertEquals(original.top(), reversed.bottom());
    assertEquals(original.bottom(), reversed.top());
  }

  @ParameterizedTest
  @MethodSource("containsDominos")
  public void dominoContainsValueIfEqualToTopOrBottom(int top, int bottom, int value) {
    var domino = Domino.of(top, bottom);
    assertEquals(domino.top() == value || domino.bottom() == value, domino.contains(value));
  }

  @ParameterizedTest
  @MethodSource("topsAndBottoms")
  @SuppressWarnings({"SimplifiableAssertion", "EqualsWithItself"})
  public void dominoEqualsOriginalOrReversed(int top, int bottom) {
    var domino = Domino.of(top, bottom);
    assertTrue(domino.equals(domino));
    assertTrue(domino.equals(domino.reversed()));
  }

  @ParameterizedTest
  @MethodSource("topsAndBottoms")
  public void dominoEqualsOnlyDominoTypes(int top, int bottom) {
    var notDomino = new Object();
    assertNotEquals(notDomino, Domino.of(top, bottom).equals(notDomino));
  }

  @ParameterizedTest
  @MethodSource("topsAndBottoms")
  public void dominoHashCodeIsSameForOriginalAndReversed(int top, int bottom) {
    var domino = Domino.of(top, bottom);
    assertEquals(domino.hashCode(), domino.reversed().hashCode());
  }

  @ParameterizedTest
  @MethodSource("orientedDominosWithDomino")
  public void
      dominoOrientedWithDomino_IsOriginalWhenDominoBottomIsBottom_IsReversedWhenDominoTopIsTop_IsNullWhenDominoIsNeitherTopOrBottom(
          Domino domino, Domino input, Domino expected) {
    assertEquals(expected, domino.oriented(input));
  }

  private static Stream<Arguments> topsAndBottoms() {
    return Stream.<Arguments>builder()
        .add(Arguments.of(4, 2))
        .add(Arguments.of(1, 1))
        .add(Arguments.of(-1, 0))
        .add(Arguments.of(-2, -3))
        .build();
  }

  private static Stream<Arguments> containsDominos() {
    return Stream.<Arguments>builder()
        .add(Arguments.of(4, 2, 2))
        .add(Arguments.of(1, 1, -1))
        .add(Arguments.of(-1, 0, -1))
        .add(Arguments.of(-2, -3, 5))
        .build();
  }

  private static Stream<Arguments> orientedDominosWithDomino() {
    return Stream.<Arguments>builder()
        .add(Arguments.of(Domino.of(1, 0), Domino.of(2, 0), Domino.of(0, 1)))
        .add(Arguments.of(Domino.of(0, 1), Domino.of(2, 0), Domino.of(0, 1)))
        .add(Arguments.of(Domino.of(1, 2), Domino.of(3, 4), null))
        .add(Arguments.of(Domino.of(2, 1), Domino.of(3, 4), null))
        .build();
  }
}
