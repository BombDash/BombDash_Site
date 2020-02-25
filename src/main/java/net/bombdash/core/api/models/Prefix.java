package net.bombdash.core.api.models;

import lombok.Value;

import java.util.List;

@Value
public class Prefix {
    private String text;
    private int speed;
    private List<Integer> animation;

}
