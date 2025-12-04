Based on the assignment PDF visible on your screen, here is the text content converted for you:

### HW 6 CSCI 498B/598B

**Formatting Instructions:**
Please use this provided LaTeX template to complete your homework. When including figures such as UMLs, it is highly encouraged to use tools (graphviz, drawio, tikz, etc.). Figures may be hand drawn; however, these may not receive credit if the grader cannot read them. For ease of grading, when including code please have it as part of the same pdf as the question while also including correct formatting/indent, preferably syntax highlighting. Latex includes the minted, or lstlisting package as a helpful tool. For this assignment all code must be full code (no pseudo-code) and be written in either Java or C++. However, implements may ignore all logic not relevant to the design pattern with simple print out statements of "[BLANK] logic done here".

**Question Instructions:**
In this homework assignment, you will apply one or more Behavioral patterns discussed in the lectures. This is a group assignment that requires two students per group. For each question:

1.  Give the name of the design pattern(s) you are applying to the problem.
2.  Present your reasons why this pattern will solve the problem. Please be specific to the problem and do not give general applicability statements. If there is an alternative pattern, explain why you preferred this one.
3.  Show your design with a UML class diagram. If the pattern collaborations would be more visible with another diagram (e.g. sequence diagram), give that diagram as well.
    - (a) Your diagram should show every participant in the pattern including the pattern related methods.
    - (b) In pattern related classes, give the member (method and attribute) names that play a role in the pattern and affected by the pattern. Optionally, include the member names mentioned in the question. You are encouraged to omit the other methods and fields.
    - (c) For the non-pattern related classes, you are not expected to give detailed class names etc. You may give a high-level component, like “UserInterface” or “DBManagement”.
4.  Give Java or C++ code for your design showing how you have implemented the pattern.
    - (a) Pattern related methods and attributes should appear in the code.
    - (b) Client usage of the pattern should appear in the code.
    - (c) Non-pattern related parts of the methods could be a simple print. (e.g. "System.out.println()", "cout").
5.  Explain how this design solves the problem.
6.  Evaluate your design with respect to SOLID principles. Each principle should be addressed, if a principle is not applicable to the current pattern, say so.

---

**1. (18 points)**
We have one Profile instance for each user of the application. The Profile class contains personal information such as name, picture, etc. In this application, users post their online meetings announcement, and they also post links to their online meetings. Users may follow other users. Implement the **observer** pattern with **pull** method for this purpose. When a user posts an announcement or meeting link, we want all the followers to get informed.

**New functionality:**
The application maintains a collection of notifications. There is a notification bar for each user's page. This notification bar displays all the notifications it received from all of its followees. We want the user to iterate over the notifications independent of how the collection is represented. Suggest a design pattern to support the new functionality. (parts 1 and 2 on the cover page)

Give code and UML for both patterns. You may give them separately or combined. (rest of the parts on the cover page)

---

**2. (18 points)**
We are implementing a digital painting application where the artist creates artwork using various digital brushes. Each brush has different painting techniques implemented as methods. Here is an example brush class: `DigitalBrush` with 3 techniques.

```java
public class DigitalBrush {
    public void smoothStroke() {.....} // painting technique
    public void splattering() {.....} // painting technique
    public void gradientBlend() {.....} // painting technique
}
```

Another example is `TextureBrush` with 4 techniques:

```java
public class TextureBrush {
    public void stampPattern() {.....} // applies a repeating pattern
    public void roughSurface() {.....} // creates a textured, rough appearance
    public void smoothBlend() {.....} // smoothly blends colors with existing artwork
    public void mosaicEffect() {.....} // creates a tile-like mosaic effect
}
```

At the beginning of the session, the artist selects a brush and does a setup for mouse keys. First, the artist sets a primary painting technique as one of the methods of the selected brush. The primary technique is activated with the left click on the mouse. Similarly, a secondary painting technique out of the remaining methods of the brush is set. The secondary technique is activated with the right click on the mouse.

After this setup, the artist starts painting. For example: The artist selects the digital brush, sets smooth stroke as primary technique and splattering as secondary. During the session, when the artist clicks on the left mouse button, we see a smooth stroke with the digital brush. When the artist changes the brush, for example, to a `TextureBrush`, the setup must be performed again.

Suggest a design pattern to realize the setting up and painting with the selected brush.

---

**3. (16 points)**
In Question 2, you designed a system to map brush techniques (like `smoothStroke()` or `stampPattern()`) to mouse clicks. Every brush stroke modifies a central `Canvas` object, which holds the entire state of the artwork in a single `byte[]` (a byte array). When `smoothStroke()` is executed, it modifies this `byte[]`.

Extend your design to support multi-level undo and redo operations (e.g., for the last 10 strokes). Your solution must be able to restore the Canvas's `byte[]` to the exact state it was in before an operation was executed.

**Note:** A "prototype-like" solution, such as simply cloning the entire Canvas object, is not an acceptable design. The Canvas object itself is complex and may contain many other elements (like caches or UI references). Your solution should focus on explicitly managing the `byte[]` state.
