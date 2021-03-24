package domain.user;

import java.util.HashMap;
import java.util.Map;

public enum Language {
    ENGLISH("en", "English"),
    FRENCH("fr", "Français");

    private static final Map<String, Language> lookup = new HashMap<>();

    static {
        for (Language language : Language.values()) {
            lookup.put(language.code, language);
        }
    }

    private final String code;
    private final String displayName;

    Language(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
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

    public String getDisplayName() {
        return displayName;
    }
}
