package tren;

public class App {

  public static void main(String[] args) {
    var state =
        TrainBuilder.byRemainingValue()
            .setSeed(7)
            .addDomino(3, 9)
            .addDomino(9, 1)
            .addDomino(3, 7)
            .addDomino(3, 3)
            .addDomino(4, 10)
            .addDomino(4, 5)
            .addDomino(7, 1)
            .addDomino(2, 7)
            .addDomino(4, 2)
            .addDomino(25, 5)
            .addDomino(8, 1)
            .addDomino(25, 7)
            .build();
    state.train().edges().forEach(System.out::println);
    System.out.println(state.remainingValue());
    System.out.println(state.remainingSize());
    System.out.println(state);
  }
}
