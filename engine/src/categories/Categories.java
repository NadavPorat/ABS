package categories;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Categories {

    public List<String> getCategories() {
        return categories;
    }

    List<String> categories = new ArrayList<>();

    public Categories(List<String> categories) {

        if (!(categories == null)) {
            this.categories= categories;
        }
    }

    @Override
    public String toString() {
        return "Categories{" +
                "categories=" + categories +
                '}';
    }
}
