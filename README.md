# dart-simulator
Simulates a game of 501 darts based on player stats

## Simulation of areas on the dartboard

### Introduction

Calculation of where the dart lands, is based on Polar Coordinates :

![specifications-of-points-on-dartboard](/img/specification-of-points-on-dartboard.png?raw=true "specification-of-points-on-dartboard")

**When is a point inside a circle?**

Given dart `(A,B)` and circle with center point `(X, Y)` and radius `R`:

`(A - X)^2 + (B - Y)^2 = D^2`

If `D^2 < R^2` then the dart is inside the circle;
If `D^2 = R^2` then the dart is on the edge of the circle;
If `D^2 > R^2` then the dart is outside the circle;


### Trebles

A dart hits the treble if the range of the polar coordinates is between **99** and **107** where the center of the treble bed is **103**.
This means the range from the center of the treble bed to the outer wire is **16**.

#### Simulation algorithm

1. Draw a default circle starting from the center of the treble bed with radius 16+4
2. The first 9 average of the player determines how much the radius can shrink
3. Draw a number
4. Find the position of the number:
   - if inside the circle : counts
   - if outside the circle: draw a second number
     - if number is lower than the treble% : draw a new number
     - else the throw counts
5. If a dart lands in the treble bed, the standard deviation of the gaussian function is reduced 
so that the chance is much higher that the next dart also lands in the treble bed

This should make sure that heavy hitters score more trebles than other players, taking into account 2 player characteristics:
- the first 9 average
- treble 20 hit percentage

### Doubles

A dart hits the double if the range of the polar coordinates is between **162** and **170** where the center of the treble bed is **166**.

#### Simulation algorithm

1. Draw a default circle starting from the center of the double bed with radius 16
2. Draw a number
3. Find the position of the number:
    - if inside the circle : counts
    - if outside the circle: draw a second number
        - if number is lower than the checkout% : draw a new number
        - else the throw counts
4. If a dart lands close to the double (area 170 until 180), chances are higher that the next dart will hit the double, 
so the standard deviation of the gaussian function is reduced.

This should make sure that good finishers have more advantage, taking into account 1 player characteristic:
- Checkout percentage

### Bull

* A dart hits the bullseye if the range of the polar coordinates is between **0** and **6.35** where the center of the bullseye bed is **0**.
* A darts hits the signle bull if the range of the polar coordinates is between **6.35** and **15.9**. 
For the center, the center of the top green is chosen, meaning **11**

#### Simulation algorithm

1. Draw a default circle starting from the center of the single bull/bulls eye with radius 16
2. Draw a number
3. Find the position of the number:
    - if inside the circle : counts
    - if outside the circle: draw a second number
        - if number is lower than the bull% : draw a new number
        - else the throw counts

This should make sure that good bull throwers have more advantage, taking into account 1 player characteristic:
- bull accuracy

## Technical Setup

### Local DynamoDB setup

To locally browse the DynamoDB database instance, use 