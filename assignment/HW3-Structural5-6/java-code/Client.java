// Implementer Interface 
interface Format {
    void format(String itemType);
}

// Concrete Implementer 1 (Web Platform)
class HTMLFormatter implements Format {
    @Override
    public void format(String itemType) {
        // Platform-specific logic
        System.out.println("HTMLFormatter");
        System.out.println(itemType);
    }
}

// Concrete Implementer 2 (Paper Platform)
class PaperFormatter implements Format {
    @Override
    public void format(String itemType) {
        // Platform-specific logic
        System.out.println("PaperFormatter");
        System.out.println(itemType);

    }
}

// Abstraction
abstract class MediaType {
    protected Format formatter;

    protected MediaType(Format formatter) {
        this.formatter = formatter;
    }

    // delegates to the Implementer
    public void print(String type) {
        formatter.format(type);
    }
}

// Refined Abstraction 1
class Book extends MediaType {

    public Book(Format formatter) {
        super(formatter);
    }
}

// Refined Abstraction 2
class Movie extends MediaType {
    public Movie(Format formatter) {
        super(formatter);
    }
}

class Client {
    public static void main(String[] args) {
        Format webFormat = new HTMLFormatter();
        Format paperFormat = new PaperFormatter();

        MediaType book = new Book(webFormat);
        book.print("Book");

        System.out.println();

        book = new Book(paperFormat);
        book.print("book");

        System.out.println();

        MediaType movie = new Movie(paperFormat);
        movie.print("movie");

        System.out.println();

        movie = new Movie(webFormat);
        movie.print("movie");
    }
}