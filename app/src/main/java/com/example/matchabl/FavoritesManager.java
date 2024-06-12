package com.example.matchabl;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class FavoritesManager {

    private static final String PREFS_NAME = "FavoritesPrefs";
    private static final String FAVORITES_KEY = "favorites";

    private SharedPreferences sharedPreferences;

    public FavoritesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void addFavorite(int fieldId) {
        Set<String> favorites = getFavorites();
        favorites.add(String.valueOf(fieldId));
        saveFavorites(favorites);
    }

    public void removeFavorite(int fieldId) {
        Set<String> favorites = getFavorites();
        favorites.remove(String.valueOf(fieldId));
        saveFavorites(favorites);
    }

    public boolean isFavorite(int fieldId) {
        Set<String> favorites = getFavorites();
        return favorites.contains(String.valueOf(fieldId));
    }

    public Set<String> getFavorites() {
        return sharedPreferences.getStringSet(FAVORITES_KEY, new HashSet<>());
    }

    private void saveFavorites(Set<String> favorites) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(FAVORITES_KEY, favorites);
        editor.apply();
    }
}
