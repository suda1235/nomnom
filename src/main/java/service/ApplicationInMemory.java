package service;




import java.util.*;

import models.Recipe;

public class ApplicationInMemory implements ApplicationService {

    private final Map<UUID, Recipe> recipes = new HashMap<>();

    @Override
    public Map<UUID, Recipe> readRecipes() {
        return recipes;
    }

    @Override
    public Recipe readRecipe(String id) {
        return recipes.get(UUID.fromString(id));
    }

    @Override
    public void createRecipe(Recipe recipe) {
        recipes.put(recipe.getRecipeId(), recipe);
    }

    @Override
    public void updateRecipe(Recipe recipe) {
        recipes.put(recipe.getRecipeId(), recipe);
    }

    @Override
    public void deleteRecipe(String id) {
        recipes.remove(UUID.fromString(id));
    }

    @Override
    public void createOrUpdateRecipe(Recipe recipe) {
        recipes.put(recipe.getRecipeId(), recipe);
    }
}