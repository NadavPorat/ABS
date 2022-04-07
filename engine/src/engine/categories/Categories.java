package engine.categories;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Categories {

    Set<String> categories = new HashSet<>();

    public Categories(List<String> categories) {

        if (!(categories == null)) {
            this.categories.addAll(categories);
        }
    }


    @Override
    public String toString() {
        return "Categories{" +
                "categories=" + categories +
                '}';
    }
}
