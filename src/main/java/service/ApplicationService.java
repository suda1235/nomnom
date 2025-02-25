package service;



import java.util.Map;
import java.util.UUID;

import models.Recipe;


public interface ApplicationService {

    public Map<UUID, Recipe> readRecipes();

    public Recipe readRecipe(String id);

    public void createRecipe(Recipe recipe);

    public void updateRecipe(Recipe recipe);

    public void deleteRecipe(String id);

    public void createOrUpdateRecipe(Recipe recipe);
}
