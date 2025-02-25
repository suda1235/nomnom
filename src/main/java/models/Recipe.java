package models;

import java.util.*;

public class Recipe {
    private UUID recipeId;
    private String title;
    private String description;
    private Date createTimestamp;
    private List<String> instructions;
    private List<Ingredient> ingredients;

    public Recipe(String title, String description) {
        this.recipeId = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.createTimestamp = new Date();
        this.instructions = new ArrayList<>();
        this.ingredients = new ArrayList<>();
    }

    public UUID getRecipeId() { return recipeId; }
    public void setRecipeId(UUID recipeId) { this.recipeId = recipeId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getCreateTimestamp() { return createTimestamp; }
    public void setCreateTimestamp(Date createTimestamp) { this.createTimestamp = createTimestamp; }

    public List<String> getInstructions() { return instructions; }
    public void setInstructions(List<String> instructions) { this.instructions = instructions; }

    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }
}
