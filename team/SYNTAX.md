# Defined syntax to use
The present document specifies the syntax to use when code is written, with the aim of making the whole project coherent. It has not any intention to be discussed, only to make sure that everyone follows the same rules.

## Classes

### Class names
As it is normally already done,  class names should begin with an **upper case letter**. Simple names should be prefered, with compound words *not longer than the contraction of three words*, in **CamelCase**.

Examples of *valid* format: `Person`, `Project`, `EditorController`, `MainView`, `MainProjectView`, ...

Examples of *invalid* formats: `consoleController`, `animal_view`, `MainMangementProjectView`, ...

### Classes attributes
Attributes of class should be listed in the begin of the class. They have to be listed in a logical order. They should begin with a **lower case letter* and should not be compound of more than two words.

If they have to be compound (which has to be avoided), they should be written in **CamelCase**. They **HAVE** to have getters and setters. They cannot contain verbs in their name.

Example of valid class:

```java
public class Person {
    private name = "any name"
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
}

```

### Getters and Setters

The getters and setters are specific methods, so, they don't have to follow the rule (explained later) to introduce their first argument). They should be placed in the **begin** of the class, right after the constructors.

## Methods
This concern every methods other than getters, setters, and constructors. They have to be **fully documented** as soon as they exist.

### Method names
Method names should be as simple as possible, written in **CamelCase**. They have to be as complete as possible and really have to be **constructed around a verb**, and should begin with a **lower case letter**. 

They also should end by a word *introducing the first parameter* (if there is any). It can be helpful to put the parameters in a logical order.

Method names **cannot contain any abreviation**.

Boolean returning methods should be named in a questionning manner.

Examples of *valid* format: `getInputFrom(User user)`, `translateFrom(String text)`, `isButtonActive()`...

Examples of *invalid* format: `drawing(Shape shape)`, `getTxt(String txt)`, `getProjectWithDate(Integer id, Date date)`, `buttonIsActive()`...

Specifically, they don't have to contain the name of the first parameter. Thus, 
```java
private void splitText(String text){}
```
becomes
```java
private void split(String text){}
```

### Methods length
**Methods cannot exceed 20 lines**. If they do, they have to be splitted in private methods. *Hint*: body of loops can often become a separate method. Of course, it excludes comments and closing brackets are not included.

The lines themselves cannot exceed **130 characters** (to make files more readables), even in *if* or *while* statements.

### Methods parameters
Parameters have to be in a logical order, and the first one has to be introduced by the name of the method. They should have consistent names, not compound of **more than three words**, written in **CamelCase**. They should begin with a **lower case letter**.

Their name cannot contain any verb.

### Return statement
Methods can contain only **one** return statement, at the and of its body.

## Test Methods

### Test names
Test names have to match the tested feature, be written in **CamelCase** and should begin with a **lower case letter**. 

Tests names **cannot contain any abreviation**.

Examples of *valid* format: `projectIsCorrectlyDeleted()`, `archiveExistsAfterCompression()`, `getIDFromNonExistentUserThrowsException()`...

Examples of *invalid* format: `isProjectDeleted()`, `testArchiveExistsAfterCompression`, `getIDTest()`...


## Variables
Classes attributes and methods parameters have already been discussed earlier.

Every Java variable should be in Wrapper Class, never using the primitive type. The only exception to this rule is the iterating variable in loop.
Every variable has to be declared and defined with a default value in the begin of the function's body. They should appear in a logical order, *in the order of apparition in the code*.

Their name should begin with a **lower case letter*, and cannot contain any verb. They have to be the shortest and the most accurate possible. Their name should not use unuseful words such as "my" or numbers.

Examples of *valid* format: `String text = "Content"`, `Integer number = 8`, `Castle lordCastle = new Castle()`, ...

Example of *invalid* format: `String Text`, `double i = 0.0`, `Farm myFarm = new Farm()`, `Integer loop1 = 0`, ...

### Specific case of return value
The return variable should have a name linked with the method name.

Example of *valid* format:

```java
private char getFirstLetterOf(String word) {
    char firstLetter = '';
    firstLetter = word.charAt(0);
    return firstLetter;
}
```

### Iterating variables
The variables in defined for *looping* can be, and it's the only exception, one letter: 'i'. As soon as there is imbricated loops, those variables should have names according to the rules previously defined.

Example of *invalid* format:
```java
for (int i = 0; i < 10 ; i++) {
    for (int j = 0; j < 10 ; j++) {
        System.out.println(matrix[i][j]);
    }
}
```
This one should have been, as an example:
```java
for (int line = 0; line < 10; line++) {
    for (int column = 0; column < 10; column++) {
        System.out.println(matrix[line][columns]);
    }
}
```


## Comments
Comments can appear in only two situations:
1. Right after a line. In this case, the amount of characters is not counted in the length of the line.
2. Outside a class, to express any message to future programmers.

In both case, they have to be sentences (upper case letter and dot at the end). They should leave a blank space between the '//' and the comment.
In case of multiligned comments, they have to begin on the same column (4 character after the begin of the line). The end of multiligned comment should be placed on a new empty line.
In the first case, the comment should not exceed one line. If it does, then try to find a clearer way to write down the code.
In the second case, multiline comments have to avoid this kind of ugly things:
```java
// I have absolutely no idea of what I could
//write as an example, so I will have to try 
// to find a better idea to complete this 
// useless space.
```
This comment should have been multiligned:
```java
/*  I have absolutely no idea of what I could  
    write as an example, so I will have to try       
    to find a better idea to complete this 
    useless space. 
*/
```

## Package and Imports
Package should be placed on the first line of the file.

Imports should be placed right after package (with a blank line between), and should not contain any blank line.
They cannot include and star import.

## Others
This section is maybe the most important, as it makes the "non-verbal" part of the syntax. As there is no real structure to expose those ideas, let's proceed by points:

1. Brackets should have an accent with the around text: 
```java
public void getName() { return this.name; } 
```
2. Any mathematical sign should have spaces arount him:
```java
Integer sum = 3 + 4;
```
3. There should not be blank space before a semicolon (the following is thus **not** valid):
```java
System.out.println("Et le Coronavirus de dire: Bonjour, monde!)" ;
```
4. Every ponctuation sign should directly follow the previous character (the following is **not** valid):
```java
public void displayFirstNumberOf(Integer[] table , String illegalParameter) {
   System.out.println("Bonjour , ceci est le premier élément : "+table[0]+" .") ;
}
```
5. Multigned instructions of less than 30 characters per line is **forbidden**
6. No paragraph-styled code should be written in a method. If it is needed for clarity, then split the method. So,
```java
private void performDummyThings() {
    System.out.println("Ceci fait partie d'un premier bloc");
    System.out.println("En effet, ceci n'a rien à voir avec la suite.");

    System.out.println("Ceci est un bloc différent.");
    System.out.println("Mais je l'aime bien quand même.");
}
```
should become:
```java
private void performDummyThings() {
    displayFirstPart();
    displaySecondPart();
}
private void performFirstDummyThing() {
    System.out.println("Ceci fait partie d'un premier bloc");
    System.out.println("En effet, ceci n'a rien à voir avec la suite.");
}
private void performSecondDummyThing() {
    System.out.println("Ceci est un bloc différent.");
    System.out.println("Mais je l'aime bien quand même.");
}
```
