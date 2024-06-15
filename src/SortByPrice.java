import java.util.Comparator;

public class SortByPrice implements Comparator<Item> {
    private boolean ascending;

    public SortByPrice(boolean ascending) {
        this.ascending = ascending;
    }

    @Override
    public int compare(Item item1, Item item2) {
        return ascending ? Integer.compare(item1.getPrice(), item2.getPrice()) : Integer.compare(item2.getPrice(), item1.getPrice());
    }
}
