package com.abderrahimlach.config.replacement;

/**
 * @author AbderrahimLach
 */
public class PercentReplacement implements ReplacementChar {

    @Override
    public char start() {
        return '%';
    }

    @Override
    public char end() {
        return '%';
    }
}
