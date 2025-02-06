package fr.leabar.iutbot.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Tuple<T,V> {
    private T firstElement;
    private V secondElement;
}
