package fr.divineexodus.server.texture.lang.key.minecraft;

import fr.divineexodus.server.texture.lang.key.Key;

public enum Accessibility implements Key {
    ONBOARDING_SCREEN_NARRATOR("onboarding_screen.narrator"),
    ONBOARDING_SCREEN_TITLE("onboarding_screen.title");

    private final String key;

    Accessibility(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
