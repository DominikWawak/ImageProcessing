package imgApp;

import javafx.embed.swing.JFXPanel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    Controller controller = new Controller();
    Image original,blackAndWhite;
    ImageView blackAndWhiteIMG;
    String color;
    JFXPanel jfxPanel = new JFXPanel();

    DisjointSetNode<Integer> dsE[],dsT[];
    int width,height;
     DisjointSetNode t1,t2,t3;

    @BeforeEach
    void setUp() {
        controller = new Controller();
        original = new Image(new File("src/indexogs.jpg").toURI().toString());

        this.blackAndWhiteIMG= controller.getBlackAndWhiteIMG();
        color = "orange";
        width= (int) original.getWidth();
        height= (int) original.getHeight();
        //createDisjointSets(blackAndWhite);
        controller.setElementValues(controller.makeBlackAndWhite(original,color,false));
        controller.setParents(controller.getDs(),width,height);
        DisjointSetNode<Integer> t1 = new DisjointSetNode<Integer>(-1);
        DisjointSetNode<Integer> t2 = new DisjointSetNode<Integer>(2);
        DisjointSetNode<Integer> t3 = new DisjointSetNode<Integer>(3);
        t1.parent=t1;
        t2.parent=t2;
        t3.parent=t3;
        dsT = new DisjointSetNode[3];
        dsT[0]=t1;
        dsT[1]=t2;
        dsT[2]=t3;


    }

    @AfterEach
    void tearDown() {
        original= null;
        color=null;
        blackAndWhite=null;
        dsE= null;
    }





    @Test
    void createDisjointSets() {

        assertEquals(-1,controller.getDs()[1].data);
        assertEquals(31,controller.getDs()[31].data);
    }

    @Test
    void setParents() {

       assertEquals(31,controller.getDs()[31].parent.data);
        assertEquals(31,controller.getDs()[81].parent.data);
    }

    @Test
    void unionSets() {
        dsE= controller.setElementValues(controller.makeBlackAndWhite(original,color,false));
        controller.unionSets(controller.getDs(),width,height,1);
        assertEquals(1213,controller.find(controller.getDs(),509).data);

        controller.setParents(dsE,width,height);
        controller.unionSets(dsE,width,height,3);
        assertEquals(406,controller.find(dsE,509).data);
    }

    @Test
    void find() {
        dsE= controller.setElementValues(controller.makeBlackAndWhite(original,color,false));
        assertEquals(509,controller.find(controller.getDs(),509).data);
    }

//
//    @Test
//    void union() {
//        System.out.println(dsT[1].parent.data);
//        controller.union(dsT,1,2);
//
//        assertEquals(t3,t2.parent.data);
//    }
//    @Test
//    void makeBlackAndWhite() {
//        Color black = Color.BLACK;
//        Color white = Color.WHITE;
//        PixelReader pr = controller.makeBlackAndWhite(original,color,false).getPixelReader();
//        javafx.scene.paint.Color ch = pr.getColor(38,4);
//
//        assertEquals(white.getRed(),ch.getRed());
//    }


}