# Visitor

- Adding new concrete class is hard:
  Visitor usually used in a set strcuture. For example, if we were to add a new DocumentPart, you'll have to implement `visit` in that class again.

### iterator vs. visitor

iterator only traverse, while visitor making traverse and operations.
