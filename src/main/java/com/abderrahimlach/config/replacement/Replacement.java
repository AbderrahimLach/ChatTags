package com.abderrahimlach.config.replacement;

/**
 * @author AbderrahimLach
 */
public class Replacement {

    private final String key, replacement;
    private final ReplacementChar replacementChar;

    public Replacement(String key, String replacement, ReplacementChar replacementChar) {
        this.key = key;
        this.replacement = replacement;
        this.replacementChar = replacementChar;
    }

    public Replacement(String key, String replacement) {
        this(key, replacement, new PercentReplacement());
    }

    public String replace(String message) {
        return message.replace((replacementChar.start() + key + replacementChar.end()), replacement);
    }
}