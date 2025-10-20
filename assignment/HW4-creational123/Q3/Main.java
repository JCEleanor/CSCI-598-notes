
interface Product {
    String getName();

    void visualize();

    void generateReport();
}

interface InventoryFactory {
    Product createProduct();
}

class Book implements Product {
    private String name;

    public Book(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void visualize() {
        System.out.println("Visualizing a book product: " + name);
    }

    @Override
    public void generateReport() {
        System.out.println("Generating a sales report for the book: " + name);
    }
}

class Vinyl implements Product {
    private String name;

    public Vinyl(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void visualize() {
        System.out.println("Visualizing a vinyl product: " + name);
    }

    @Override
    public void generateReport() {
        System.out.println("Generating a streaming report for the vinyl: " + name);
    }
}

class PlushToy implements Product {
    private String name;

    public PlushToy(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void visualize() {
        System.out.println("Visualizing a plush toy product: " + name);
    }

    @Override
    public void generateReport() {
        System.out.println("Generating a sales report for the plush toy: " + name);
    }
}

class BookFactory implements InventoryFactory {
    private String name;

    public BookFactory(String name) {
        this.name = name;
    }

    @Override
    public Product createProduct() {
        return new Book(this.name);
    }
}

class VinylFactory implements InventoryFactory {
    private String name;

    public VinylFactory(String name) {
        this.name = name;
    }

    @Override
    public Product createProduct() {
        return new Vinyl(this.name);
    }
}

class PlushToyFactory implements InventoryFactory {
    private String name;

    public PlushToyFactory(String name) {
        this.name = name;
    }

    @Override
    public Product createProduct() {
        return new PlushToy(this.name);
    }
}

public class Main {
    public static void main(String[] args) {

        // Original inventory system
        InventoryFactory bookFactory = new BookFactory("The Lord of the Rings");
        Product LordoftheRing = bookFactory.createProduct();
        LordoftheRing.visualize();
        LordoftheRing.generateReport();

        // New vinyl inventory system
        InventoryFactory vinylFactory = new VinylFactory("Vinyl Album 1");
        Product vinylAlbum = vinylFactory.createProduct();
        vinylAlbum.visualize();
        vinylAlbum.generateReport();
    }
}
