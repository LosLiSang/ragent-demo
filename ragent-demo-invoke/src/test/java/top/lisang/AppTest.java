package top.lisang;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    
    @Test
    void testGreet() {
        App app = new App();
        assertEquals("Hello, Maven!", app.greet("Maven"));
    }
    
    @Test
    void testGreetWithEmptyName() {
        App app = new App();
        assertEquals("Hello, !", app.greet(""));
    }
}
