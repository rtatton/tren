# tren

Optimal Mexican train builder using the A* search algorithm.

## Usage

```java

import tren.TrainBuilder;

public class Example {

    public static void main(String[] args) {
        // Use a pre-configured builder to minimize the number of remaining pips.  
        // Use TrainBuilder.byRemainingSize() to minimize the number of remaining dominos.
        var state = TrainBuilder.byRemainingValue()
                .setSeed(12)
                .addDomino(1, 2)
                ...
                .build();
        System.out.println(state.train());
        System.out.println(state.remaining());
        System.out.println(state.trainValue());
        System.out.println(state.trainSize());
        System.out.println(state.remainingValue());
        System.out.println(state.remainingSize());
    }
    
} 

```