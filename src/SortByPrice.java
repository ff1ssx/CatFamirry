import java.util.Comparator;

/**
 * The SortByPrice class implements a comparator to sort items by their price.
 * It can sort in either ascending or descending order based on the specified parameter.
 */
public class SortByPrice implements Comparator<Item> {
    private boolean ascending;

    /**
     * Constructor.
     * Initializes the comparator with the specified sorting order.
     * @param ascending True for ascending order, false for descending order
     */
    public SortByPrice(boolean ascending) {
        this.ascending = ascending;
    }

    /**
     * Compares two items by their price.
     * @param item1 The first item to compare
     * @param item2 The second item to compare
     * @return A negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
     */
    @Override
    public int compare(Item item1, Item item2) {
        // Compare prices in ascending or descending order based on the ascending field
        return ascending ? Integer.compare(item1.getPrice(), item2.getPrice()) : Integer.compare(item2.getPrice(), item1.getPrice());
    }
}
