```java
import java.util.ArrayList;
import java.util.List;

// The 'Visitor' interface
interface IVisitor {
    void visit(Section s);
    void visit(Paragraph p);
    void visit(Table t);
}

// The 'Element' base class
// commenting our 'public' so that we can put all code in one file to make the exercise fast
/*public*/ abstract class DocumentPart {
    protected String title;
    protected String text;

    public String getTitle() { return title; }
    public String getText() { return text; }

    // TODO: Add the abstract accept() method
    public abstract void accept(IVisitor v){

    };
}

// A 'ConcreteElement' (Leaf)
/*public*/ class Paragraph extends DocumentPart {
    public Paragraph(String text) {
        this.text = text;
    }

    // TODO: Implement accept()
    @override
    public void accept(IVisitor v){
        v.visit(this);
    }
}

// A 'ConcreteElement' (Leaf)
/*public*/ class Table extends DocumentPart {
    public Table(String title) {
        this.title = title;
    }

    // TODO: Implement accept()
        @override
    public void accept(IVisitor v){
        v.visit(this);
    }
}

// The 'Composite' (also a 'ConcreteElement')
/*public*/ class Section extends DocumentPart {
    private List<DocumentPart> children = new ArrayList<>();

    public Section(String title) {
        this.title = title;
    }

    public void add(DocumentPart part) {
        children.add(part);
    }

    public List<DocumentPart> getChildren() {
        return children;
    }

    // TODO: Implement accept()
    public void accept(IVisitor v){
        v.visit(this);
        for (DocumentPart child : children) {
            child.accept(v);
        }
    }
}

class ToCVisitor implements IVisitor {
    public void visit(Section s) {
        System.out.println("Section: " + s.getTitle());
    }
    public void visit(Table t) {
        System.out.println("Table: " + s.getTitle());
    }
    public void visit(Paragraph p) {
        // do nothing
    }
}

//make the file name VisitorExample.java
public class VisitorExample{
 public static void main(String[] args) {
   Section document = new Section("Document");
   Section intro = new Section("Introduction");
   intro.add(new Paragraph("This is the intro paragraph."));
   intro.add(new Table("Intro Table"));
   document.add(intro);
   Section body = new Section("Body");
   body.add(new Paragraph("This is the body paragraph."));
   document.add(body);

   IVisitor visitor = new ToCVisitor();
    //    no body call visit but the section itself
   document.accept(visitor)
   //TODO create the table of contents, i.e. start the process
 }
}
```

### Part 1: Traversal in Component

In this design, the Component Decides the traversal. The for loop will be inside the Section class.

#### Your Task:

1. Add `public abstract void accept(IVisitor v);` to the `DocumentPart` class.

2. Implement accept in `Paragraph` and `Table`.

Goal: These are simple elements. They just need to tell the visitor "Hi, I'm here." How do they do that?

3. Implement `accept` in Section. This is the important one! It must do two things:

Process this node (tell the visitor "Hi, I'm a Section").

Propagate the accept call to all its children. (similar to the composite pattern) This is the traversal logic.

Challenge: Try to write this method yourself first.

4. Create the `ToCVisitor` class.

It must implement the `IVisitor` interface. Make sure all the methods are public.

- Visiting a Section should print System.out.println("Section: " + s.getTitle());.

- Visiting a Table should print "Table: " . and its title.

- Visiting a Paragraph should do nothing.

5. Go to the main method that builds a document. Add one line of code to create the Table of Conents.

i.e. Write the last line to start the visiting process.

### Part 2: Refactor: Traversal in Visitor

Let's move the traversal logic so the Visitor Decides.

#### Your Task:

1. Modify `Section::accept`:

- Make it "dumb" just like Paragraph and Table.

- It must only call v.visit(this).

- Remove the traversal logic (the for loop).

```java
/*public*/ class Section extends DocumentPart {
    private List<DocumentPart> children = new ArrayList<>();

    public Section(String title) {
        this.title = title;
    }

    public void add(DocumentPart part) {
        children.add(part);
    }

    public List<DocumentPart> getChildren() {
        return children;
    }

    // TODO: Implement accept()
    @override
    public void accept(IVisitor v){
        v.visit(this);
    }
}
```

2. Modify ToCVisitor::visit(Section s):

This method is now responsible for both the operation and the traversal.

Challenge: Modify your visit(Section s) method to add the traversal logic you just removed from Section::accept.

```java

```

Run your main method again. The output should be identical!

### Discussion Questions

- Q1: What is the major advantage of the Part 2 ("Visitor Decides") design?

- Q2: In Part 2, how would you change visit(Section s) to print a post-order ToC (where children are printed before their parent section)?
