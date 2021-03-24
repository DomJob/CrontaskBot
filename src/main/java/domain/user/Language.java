package domain.user;

import bot.command.Command;

import java.util.HashMap;
import java.util.Map;

public enum Language {
    ENGLISH("en"),
    FRENCH("fr");

    private static final Map<String, Language> lookup = new HashMap<>();

    static {
        for (Language language : Language.values()) {
            lookup.put(language.code, language);
        }
    }

    private String code;

    Language(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Language lookup(String code) {
        if(!lookup.containsKey(code)) {
            throw new IllegalArgumentException("Invalid language code " + code);
        }

        return lookup.get(code);
    }
}
