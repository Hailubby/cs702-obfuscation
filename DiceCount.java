package com.jjhhh.dice.Models;

// Stores a die type and how many of that die are to be rolled
public class DiceCount {

    // type of dice (sides)
    private int dice;

    // how many of that dice
    private int count = 0;

    public DiceCount(int dice, int count) {
        this.dice = dice;
        this.count = count;
    }

    public DiceCount(int dice) {
        this.dice = dice;
    }

    public int getDie() {
        return dice;
    }

    // get sides
    public int getCount() {
        return count;
    }

    // get how many of die
    public void increment() {
        this.count++;
    }

    // add die of this type
    public void reset() {
        this.count = 0;
    }
    // remove all dice of this type
}

