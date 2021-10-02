package com.yor42.projectazure.libs.utils;
//OpenJDK doesnt come with pair lol
public class Pair<T, U> {
    private final T first;
    private final U second;

    public Pair(T First, U Second) {
        this.first = First;
        this.second = Second;
    }

    public T getFirstValue(){
        return first;
    }
    public U getSecondValue(){
        return second;
    }
}
