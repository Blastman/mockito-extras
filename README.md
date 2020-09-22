# mockito-extras
This project contains utilities for Mockito that allow for some extra functionality.

## MockSpotter
The main purpose of this project is to add a `MockSpotter`. Going with the mantra of:
"If you mock with and use a when statement, then you should verify it to ensure it was called".
Normally you would run:

 ```java
    // Given
    Foo foo = mock(Foo.class);

    // When
    when(foo.getSibling())
        .thenReturn("Bar");
    when(mock1.getFoo())
        .thenReturn(foo);
    
    // Then
    someObject.performOpearion())
    verify(mock1).getFoo();
    verify(foo).getSibling();    

    Mockito.verifyNoMoreInteractions(foo, mock1);       
```

To ensure that all mocks have been verified. This project makes that process similar to knowing what
mocks are created so you don't need to list them all.

```java
class TestClass { 
    @Mock
    private MockClass1 someMock1;

    @Mock
    private MockClass2 someMock2;
    
    @Unmonitored
    @Spy
    private RealClass realObject;
    
    @FindMocks
    private MockSpotter mocks;
 
    @InjectMocks
    private SomeService someService;
    
    @BeforeEach
    void before() {
        realObject = new RealClass();
        // This line replaces MockitoAnnotations.initMocks(this) and performs those operations too,
        // including injecting real objects marked with @Spy
        MockitoExtrasAnnotations.init(this);
    }

    @AfterEach
    void after() {
        mocks.verifyNoMoreInteractions();  // Verify no other mock interactions happened
    }
    
    @Test
    void testDoSomething() {
        someService.doSomething();
        verify(someMock1).method1();
    }
}   
```

# Annotations
There are two main annotations to use for this project. `@FindMocks` and `@Unmonitored`.

## FindMocks
`@FindMocks` must be used on a `MockSpotter`. Specifying this will tell the annotation processor
you want to inject a new instance there. This actually isn't required and you can do:

```java
@BeforeEach
void setup(){
    mockSpotter = new MockSpotter();
    MockitoAnnotations.initMocks(this);
}
```

If you need more fine grain control of items.

## Unmonitored
This annotation is used for class variables that you don't want to monitor. This is useful when using
`@Spy` to inject a real object into an `@InjectMock` object. Normally you don't care verifing the Spy in
this case  since they call real internal methods. Note at anytime you can also remove a mock or spy
 from being tracked with:
 
```java
    mockSpotter.removeMock(someMockOrSpy);
```

#$ Junit 5
This project contains a Junit 5 extension which allows for all the setup needed without the need
of a before() method, unless special processing is required. You can use use this feature by using
the `MockSpotter` extension:

```java
@ExtendWith(MockFinderExtension.class)
public class SomeTestClass {

    @InjectMocks
    private TestObject testObject;

    @Mock
    private TestObject1 testObject1;

    @Unmonitored
    @Mock
    private TestObject2 testObject3;
    
    @Mock
    private TestObject3 testObject3;

    @FindMocks
    private MockSpotter mocks;
    
    void testSomething() {
        when(testObject1.getString())
            .thenReturn("Test String");
        
        testObject.doOperation();
    
        vierfy(testObject1).getString();
        mocks.verifyNoMoreInteractions();
    }
}
```

## Junit 4
Junit 4 is currently supported only via setting up manually from a `@Before` method as such:

```java
public class SomeTestClass {
    @Mock
    private MockClass1 someMock1;
   
    @Mock
    private MockClass2 someMock2;
   
    @FindMocks
    private MockSpotter mocks;
   
    @InjectMocks
    private SomeService someService;
   
    @Before
    public void setup(){
        MockitoExtrasAnnotations.initMocks(this);
    }
   
    @Test
    public void testDoSomething(){
        someService.doSomething();
        
        verify(someMock1).method1();
        
        mocks.verifyNoMoreInteractions();  // Verify no other mock interactions happened
    }
}
``` 

Support for a Junit 4 specific `Runner` is planned for a future release.