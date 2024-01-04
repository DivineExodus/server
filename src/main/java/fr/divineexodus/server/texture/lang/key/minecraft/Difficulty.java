package fr.divineexodus.server.texture.lang.key.minecraft;

import fr.divineexodus.server.texture.lang.key.Key;

public enum Difficulty implements Key {
    LOCK_QUESTION("difficulty.lock.question"),
    LOCK_TITLE("difficulty.lock.title");

    private final String key;

    Difficulty(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
