package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.ApplicationInMemory;
import service.ApplicationService;

@WebServlet("/RecipesServlet")
public class RecipesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final ApplicationService recipeService = new ApplicationInMemory();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Handle DELETE
        String deleteId = request.getParameter("delete");
        if (deleteId != null) {
            recipeService.deleteRecipe(deleteId);
            response.sendRedirect("RecipesServlet");
            return;
        }

        // Create New Recipe Form
        out.println("<html><head><title>Recipe Manager</title></head><body>");
        out.println("<h2>Create a New Recipe</h2>");
        out.println("<form method='POST'>");
        out.println("Title:<br><input type='text' name='title' required><br><br>");
        out.println("Description:<br><textarea name='description' required></textarea><br><br>");

        // Ingredients Section
        out.println("<h3>Ingredients</h3>");
        out.println("<div id='ingredientsContainer'></div>");
        out.println("<button type='button' onclick='addIngredientRow()'>+</button><br><br>");

        // Instructions Section
        out.println("<h3>Instructions</h3>");
        out.println("<div id='instructionsContainer'></div>");
        out.println("<button type='button' onclick='addInstructionRow()'>+</button><br><br>");

        out.println("<input type='submit' value='Submit'>");
        out.println("</form>");

        // Display Existing Recipes
        out.println("<h2>Existing Recipes</h2>");
        out.println("<table border='1'>");
        out.println("<tr><th>#</th><th>Title</th><th>Description</th><th>Ingredients</th><th>Instructions</th><th>Actions</th></tr>");

        int count = 1;
        for (Recipe r : recipeService.readRecipes().values()) {
            out.println("<tr>");
            out.println("<td>" + count + "<br><small>" + r.getRecipeId() + "</small></td>");
            out.println("<td>" + r.getTitle() + "</td>");
            out.println("<td>" + r.getDescription() + "</td>");

            // Display Ingredients
            out.println("<td><ul>");
            for (Ingredient ing : r.getIngredients()) {
                out.println("<li>" + ing.getQuantity() + " " + ing.getMeasure() + " " +
                            ing.getPreparation() + " " + ing.getName() + "</li>");
            }
            out.println("</ul></td>");

            // Display Instructions
            out.println("<td><ol>");
            for (String step : r.getInstructions()) {
                out.println("<li>" + step + "</li>");
            }
            out.println("</ol></td>");

            // Actions
            out.println("<td>");
            out.println("<a href='RecipesServlet?edit=" + r.getRecipeId() + "'>Edit</a> | ");
            out.println("<a href='RecipesServlet?delete=" + r.getRecipeId() + "'>Delete</a>");
            out.println("</td>");
            out.println("</tr>");

            count++;
        }
        out.println("</table>");

        out.println(getJavaScript());
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String editId = request.getParameter("editId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");

        // Process Ingredients
        String[] quantities = request.getParameterValues("quantity[]");
        String[] measures = request.getParameterValues("measure[]");
        String[] preparations = request.getParameterValues("preparation[]");
        String[] ingredientNames = request.getParameterValues("ingredient[]");

        // Process Instructions
        String[] instructionSteps = request.getParameterValues("instructions[]");

        List<Ingredient> ingredientsList = new ArrayList<>();
        if (quantities != null && measures != null && preparations != null && ingredientNames != null) {
            for (int i = 0; i < ingredientNames.length; i++) {
                if (!ingredientNames[i].trim().isEmpty()) {
                    ingredientsList.add(new Ingredient(
                        Integer.parseInt(quantities[i]), measures[i], preparations[i], ingredientNames[i]
                    ));
                }
            }
        }

        List<String> instructionsList = new ArrayList<>();
        if (instructionSteps != null) {
            for (String step : instructionSteps) {
                if (!step.trim().isEmpty()) {
                    instructionsList.add(step);
                }
            }
        }

        if (editId != null && !editId.isEmpty()) {
            Recipe existing = recipeService.readRecipe(editId);
            if (existing != null) {
                existing.setTitle(title);
                existing.setDescription(description);
                existing.setIngredients(ingredientsList);
                existing.setInstructions(instructionsList);
                existing.setCreateTimestamp(new Date());
                recipeService.updateRecipe(existing);
            }
        } else {
            if (title != null && !title.isEmpty() && description != null && !description.isEmpty()) {
                Recipe newRecipe = new Recipe(title, description);
                newRecipe.setIngredients(ingredientsList);
                newRecipe.setInstructions(instructionsList);
                recipeService.createRecipe(newRecipe);
            }
        }

        response.sendRedirect("RecipesServlet");
    }

    private String getJavaScript() {
        return "<script>"
                + "function addIngredientRow() {"
                + "    const container = document.getElementById('ingredientsContainer');"
                + "    const row = document.createElement('div');"
                + "    row.className = 'ingredient-row';"
                + "    row.innerHTML = \"<input type='number' name='quantity[]' min='1' max='999' value='1'>"
                + "<select name='measure[]'>"
                + "<option value='' selected></option>"
                + "<option value='Cup'>Cup</option>"
                + "<option value='Teaspoon'>Teaspoon</option>"
                + "<option value='Tablespoon'>Tablespoon</option>"
                + "<option value='Gram'>Gram</option>"
                + "<option value='Kilogram'>Kilogram</option>"
                + "</select>"
                + "<select name='preparation[]'>"
                + "<option value='' selected></option>"
                + "<option value='Chopped'>Chopped</option>"
                + "<option value='Grated'>Grated</option>"
                + "<option value='Sliced'>Sliced</option>"
                + "</select>"
                + "<input type='text' name='ingredient[]' placeholder='Ingredient Name'>"
                + "<button type='button' onclick='removeIngredientRow(this)'>X</button>\";"
                + "    container.appendChild(row);"
                + "}"
                + "function removeIngredientRow(button) {"
                + "    button.parentElement.remove();"
                + "}"
                + "function addInstructionRow() {"
                + "    const container = document.getElementById('instructionsContainer');"
                + "    const row = document.createElement('div');"
                + "    row.className = 'instruction-row';"
                + "    row.innerHTML = \"<input type='text' name='instructions[]' placeholder='Enter instruction...'>"
                + "<button type='button' onclick='removeInstructionRow(this)'>X</button>\";"
                + "    container.appendChild(row);"
                + "}"
                + "function removeInstructionRow(button) {"
                + "    button.parentElement.remove();"
                + "}"
                + "</script>";
    }


}