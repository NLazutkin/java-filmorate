package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

@Data
public class Pair<U, V> {
    private U first;
    private V second;

    public Pair(U first, V second) {
        this.first = first;
        this.second = second;
    }
}
