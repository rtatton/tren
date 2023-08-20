package tren;

/**
 * The basic unit of a Mexican train.
 *
 * <p>While a domino has a "top" and a "bottom", the ordering does not affect equality. That is,
 * dominos (1, 2) and (2, 1) are equal.
 *
 * @param top Number of pips on the "top" half of the domino
 * @param bottom Number of pips on the "bottom" half of a domino
 */
public record Domino(int top, int bottom) {

  public static Domino of(int top, int bottom) {
    return new Domino(top, bottom);
  }

  public static Domino ofDouble(int value) {
    return of(value, value);
  }

  public int value() {
    return top + bottom;
  }

  public boolean isDouble() {
    return top == bottom;
  }

  public Domino oriented(Domino domino) {
    if (equals(domino) || !contains(domino.bottom)) return null;
    return top == domino.bottom ? this : reversed();
  }

  public Domino reversed() {
    return of(bottom, top);
  }

  public boolean contains(int value) {
    return top == value || bottom == value;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Domino d)
      return (top == d.top && bottom == d.bottom) || (top == d.bottom && bottom == d.top);
    return false;
  }

  @Override
  public int hashCode() {
    return value();
  }
}
