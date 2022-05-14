package com.abderrahimlach.internal.config.replacement.characters;

import com.abderrahimlach.internal.config.replacement.ReplacementChar;

/**
 * @author DirectPlan
 */
public class PercentReplacementChar implements ReplacementChar {

    @Override
    public char start() {
        return '%';
    }

    @Override
    public char end() {
        return '%';
    }
}
