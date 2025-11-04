## Refactor it to use iterator

re-implement the given bad design of ProductCatalog and its printer using Iterator

```java
class ProductCatalog<T> {
    private Node<T> productRoot; // This is a BST
    public Node<T> getElements() {
        return this.productRoot; //  DESIGN FLAW
    } //… methods to manage catalog item like add in BST
}
class Client {
    public <T> void printCatalog (ProductCatalog<T> catalog) {
        Node<T> root = catalog.getElements();
        this.printInOrder(root);
        //Client is forced to write its own logic, go left/right etc
    }
    private <T> void printInOrder(Node<T> node) { /* ... */ }
}
```

### Solution

```java
import java.util.Iterator;

class ProductCatalog<T> {
    private Node<T> productRoot; // This is a BST
    public Iterator<T> iterate() {
        return new Iterator<>(this.productRoot);
    }
}

class Client {
    public <T> void printCatalog (ProductCatalog<T> catalog) {

        Iterator<T> iter = catalog.iterate();

        while (iter.hasNext()){
            this.printInOrder(iter.next());
        }
    }
    private <T> void printInOrder(Node<T> node) { /* ... */ }
}
```
