package fr.divineexodus.server.texture.lang.key.minecraft;

import fr.divineexodus.server.texture.lang.key.Key; 

public enum Lectern implements Key {
    TAKE_BOOK("lectern.take_book");

    private final String key;

    Lectern(final String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }
}
