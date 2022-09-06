package ku.cs.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CategoryList implements Iterable<Category> {
    private final List<Category> categories;

    public CategoryList() {
        this.categories = new ArrayList<>();
    }

    public void add(Category category) {
        this.categories.add(category);
    }

    public Category searchByName(String name) {
        for (Category category : categories)
            if (category.getName().equals(name))
                return category;

        return null;
    }

    public String toCsv() {
        String result = "";

        for (Category category : categories)
            result += category.toCsv() + "\n";

        return result;
    }

    public List<String> getNames() {
        List<String> temp = new ArrayList<>();

        for (Category category : categories)
            temp.add(category.getName());

        return temp;
    }

    @Override
    public Iterator<Category> iterator() {
        return categories.iterator();
    }
}
