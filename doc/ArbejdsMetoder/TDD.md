# Test driven develpment

Vi har klassen '*Foo*' og vil lave metoden '*bar*' hvor vi skriver tæsten til '*bar*' inden den bliver implementeret

## Inden '*bar*' bliver oprettet

```Java
public class Foo{
    
}
```

## Lav '*bar*' uden en body

```Java
public class Foo{
    public int bar(int a, int b){

    }
}
```

## Lav tæst metode

```Java
public class FooTest{
    private Foo foo

    @BeforeEach
    public void setup(){
        foo = new Foo();
    }
    
    @AfterEach
    public void teardown(){
        foo = null;
    }

    @Test
    public void barPositiveTest{
        // Tests the happy path
        // Arrange
        int expected = 5;
        int actual = -1;

        // Act 
        actual = foo.bar(2,3);
        
        // Assert
        assertEquals(expected,actual); // vi forvænter at 2 + 3 = 5
    }

    @Test
    public void barNegativeTest{
        // Tests the unhappy path
        int expected = 4;
        int actual = -1;

        // Act 
        actual = foo.bar(2,3);
        
        // Assert
        assertNotEquals(expected,actual); // vi forvænter ikke at 2 + 3 = 4
    }

}
```

Efter man har lavet tæstne burte man kørre dem for at se om de kompiler og **fejler**

## Lav '*bar*' body

```Java
public class Foo{
    public int bar(int a, int b){
        return a + b;
    }
}
```

Efter metoden er blevet implementeret burde tæstne ikke længere fejle.  
**Husk: hvis man kun laver en tæst er det relativt lige til at skrive metoden til at suksesfult komme i gennem tæsten men metoden gør ikke det man gærne vil have metoden gør men andet input**
