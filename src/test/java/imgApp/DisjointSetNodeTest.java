package imgApp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisjointSetNodeTest {

    DisjointSetNode<Integer> t;

    @BeforeEach
    void setUp() {
         t = new DisjointSetNode<>(1);

    }

    @AfterEach
    void tearDown() {
        t=null;
    }

    @Test
    void setData() {
        assertEquals(1,t.getData());
       t.setData(2);
       assertEquals(2,t.getData());
    }

    @Test
    void getData() {
        assertEquals(1,t.getData());
    }


}