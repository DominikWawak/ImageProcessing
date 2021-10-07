package imgApp;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.font.ImageGraphicAttribute;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Controller class for CA 2 Image Processing.
 * This class is used to control the whole project that is a simple image colour picker and editor.
 *
 * @author Dominik Wawak
 * @version 3.0.0 (17.March.2021)
 */
public class Controller implements Initializable {


    DisjointSetNode<Integer> ds[];

    @FXML
    private Pane mainPane;

    @FXML
    private MenuItem openFile;
//
//    @FXML
//    private Canvas imgCanvas;

    @FXML
    private MenuItem rgbChannels, closeBTN, saveBTN, newTabBtn;

    @FXML
    private ImageView mainImage, redImg, blueImg, greenImg, ogImg, blackAndWhiteIMG;

    @FXML
    private AnchorPane imagePane;
    @FXML
    private ToggleButton grayScaleBTN, blackAndWhite;

    @FXML
    private Button applyZoomBTN, processBtn;

    @FXML
    private Slider brightnessSlider;

    @FXML
    private Slider hueSlider;

    @FXML
    private Slider saturationSlider, outlierCounter,sizeManager;

    @FXML
    private Slider zoomSlider;

    @FXML
    private Label fname, numDisjointSets;

    @FXML
    private Label fsize;

    @FXML
    private Label fdimensions, hueLab, satLab;
    @FXML
    private TextField width;

    @FXML
    private TextField height;

    @FXML
    private Button applyResizeBtn;

    @FXML
    private Tab tab1;
    private Tab template;

    @FXML
    private TabPane tabPane;

    @FXML
    private ChoiceBox<String> colorChoice;



    private File file;

    private int outlier;
    private Tooltip tl;

    private Image image;
    ColorAdjust colorAdjust = new ColorAdjust();


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        template = tab1;

        colorChoice.getItems().add("red");
        colorChoice.getItems().add("orange");
        colorChoice.getItems().add("blue");

        zoomSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            mainImage.setScaleX(new_val.doubleValue());

            mainImage.setScaleY(new_val.doubleValue());

        });


        brightnessSlider.valueProperty().addListener((ov, old_val, new_val) -> {

            colorAdjust.setBrightness(new_val.doubleValue());
            mainImage.setEffect(colorAdjust);

        });
        outlierCounter.valueProperty().addListener((ov, old_val, new_val) -> {

            outlier = new_val.intValue();

        });

        sizeManager.valueProperty().addListener((ov, old_val, new_val) -> {

            outlier = new_val.intValue();

        });
        hueSlider.valueProperty().addListener((ov, old_val, new_val) -> {

            colorAdjust.setHue(new_val.doubleValue());
            mainImage.setEffect(colorAdjust);
            hueLab.setText(hueSlider.getValue() + "");

        });
        saturationSlider.valueProperty().addListener((ov, old_val, new_val) -> {

            colorAdjust.setSaturation(new_val.doubleValue());
            mainImage.setEffect(colorAdjust);
            satLab.setText(saturationSlider.getValue() + "");

        });


        saveBTN.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Image");

                file = fileChooser.showSaveDialog(saveBTN.getParentPopup());
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(mainImage.getImage(),
                            null), "png", file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        rgbChannels.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                long before = System.currentTimeMillis();
                RGBColours();
                long after = System.currentTimeMillis();
                System.out.println(after - before);


            }
        });

    }


    public void RGBColours() {
        PixelReader pixelReader = image.getPixelReader();
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage modImage = new WritableImage(width, height);
        WritableImage modImage1 = new WritableImage(width, height);
        WritableImage modImage2 = new WritableImage(width, height);
        PixelWriter pixelWriter = modImage.getPixelWriter();
        PixelWriter pixelWriter1 = modImage1.getPixelWriter();
        PixelWriter pixelWriter2 = modImage2.getPixelWriter();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
//                        int pixel = pixelReader.getArgb(x, y);
//                        int alpha = ((pixel >> 24) & 0xff);
//                        int red = ((pixel >> 16) & 0xff);
//                        int green = ((pixel >> 8) & 0xff);
//                        int blue = (pixel & 0xff);
//
//                        int redLevel = (0 << 24) | (red << 16) | (0 << 8) | 0;
//                        pixelWriter.setArgb(x, y, -redLevel);
                Color col1 = pixelReader.getColor(x, y);
                pixelWriter.setColor(x, y, new Color(0, 0, col1.getBlue(), 1.0));
                blueImg.setImage(modImage);
                pixelWriter1.setColor(x, y, new Color(0, col1.getGreen(), 0, 1.0));
                greenImg.setImage(modImage1);
                pixelWriter2.setColor(x, y, new Color(col1.getRed(), 0, 0, 1.0));
                redImg.setImage(modImage2);
                ogImg.setImage(image);
            }
        }
    }


    /**
     * Browse method
     * used for looking for the image file from the computer disk.
     *
     * @param actionEvent
     */
    public void browse(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        Stage stage = (Stage) mainPane.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        System.out.println(selectedFile.toString());

        image = new Image(selectedFile.toURI().toString());

        mainImage.setImage(image);
        // mainImage.translateZProperty().bind(zoomSlider.valueProperty());
        width.setText(String.valueOf((int) image.getWidth()));
        height.setText(String.valueOf((int) image.getHeight()));
        fname.setText(selectedFile.toURI().toString());
        fsize.setText(selectedFile.length() + " Bytes");
        System.out.println(mainImage.getScaleZ());


    }


    public void toggleGrayScale(ActionEvent actionEvent) {
        if (grayScaleBTN.isSelected()) {
            PixelReader pixelReader = image.getPixelReader();
            int width = (int) image.getWidth();
            int height = (int) image.getHeight();
            WritableImage grayImage = new WritableImage(width, height);
            PixelWriter pixelWriter = grayImage.getPixelWriter();

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int pixel = pixelReader.getArgb(x, y);

                    int a = (pixel >> 24) & 0xff; //bitwise and
                    int red = ((pixel >> 16) & 0xff);
                    int green = ((pixel >> 8) & 0xff);
                    int blue = (pixel & 0xff);

                    int average = (red + green + blue) / 3;
                    pixel = (a << 24) | (average << 16) | (average << 8) | average;

                    pixelWriter.setArgb(x, y, pixel); // AMENDED TO -gray here.
                }
            }
            mainImage.setImage(grayImage);
        } else {
            mainImage.setImage(image);
        }

    }

    //===============================================================================================================================================================================//

    /**
     * makeBlackAndWhite method
     * The method takes an Image that is to be transformed to black and white,
     * the string value describes of that pixels are desired white,
     * the set boolean specifies whether the main image view must be set(used for tests),
     *
     * @param original
     * @param color
     * @param set
     * @return Image that is black and white.
     */
    public Image makeBlackAndWhite(Image original, String color, Boolean set) {


        PixelReader pixelReader = original.getPixelReader();

        int width = (int) original.getWidth();
        int height = (int) original.getHeight();
        WritableImage bwImage = new WritableImage(width, height);
        PixelWriter pixelWriter = bwImage.getPixelWriter();


        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = pixelReader.getArgb(x, y);

                Color og = pixelReader.getColor(x, y);
                double valueOfColour = og.getRed() + og.getBlue() + og.getGreen();

                Color borW;
                switch (color) {
                    // FOR ORANGE
                    case "orange":
                        borW = (og.getHue() <= 45 && og.getSaturation() > 0.95) ? Color.WHITE : Color.BLACK;
                        break;
                    //blue
                    case "blue":
                        borW = (og.getHue() <= 206 && og.getHue() >= 195 && og.getSaturation() <= 0.4) ? Color.WHITE : Color.BLACK;
                        break;
                    //cherries
                    case "red":
                        borW = (og.getHue() <= 360 && og.getHue() >= 345 && og.getSaturation() > 0.85) ? Color.WHITE : Color.BLACK;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + color);
                }
                pixelWriter.setColor(x, y, borW);

                if (set)
                    blackAndWhiteIMG.setImage(bwImage);
                // System.out.println(pixel);

            }
        }

        return bwImage;

    }

    public void blackAndWhiteToggle(ActionEvent actionEvent) {

        makeBlackAndWhite(image, colorChoice.getValue().toString(), true);
    }


    //=========================================================================================================================================================


    /**
     * setElementValues method
     * <p>
     * The method will give values form 0-area of the image based on the value of the pixel, the image is passed on from the
     * makeBlackAndWhiteMethod, each black pixel gets a value of -1, while the rest get a integer value
     *
     * @param blackAndWhite
     * @return an array of DisjointSet Nodes
     */
    public DisjointSetNode<Integer>[] setElementValues(Image blackAndWhite) {
        PixelReader pixelReader = blackAndWhite.getPixelReader();

        int width = (int) blackAndWhite.getWidth();
        int height = (int) blackAndWhite.getHeight();


        ds = new DisjointSetNode[(int) (width * height)];
        int i = 0;
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {


                ds[i] = (pixelReader.getColor(y, x).equals(Color.color(0, 0, 0, 1))) ? new DisjointSetNode<>(-1) : new DisjointSetNode<>(i);
                ds[i].parent = ds[i];
                // System.out.println(ds[i].data );
                i++;

            }
        }

        return ds;
    }


    /**
     * setParents method
     * <p>
     * used to iterate through the array and check boundaries for example if the next pixel and the pixel underneath are not -1 then,
     * make that pixel the parent.
     * This method essentially initializes the tree structure.
     *
     * @param disjointSetNodes
     * @param width
     * @param height
     */
    public void setParents(DisjointSetNode<Integer>[] disjointSetNodes, int width, int height) {
        for (int i = 0; i < disjointSetNodes.length; i++) {

            if (i + width < width * height && disjointSetNodes[i].data != -1) {
                if (disjointSetNodes[i + width].data != -1) {
                    disjointSetNodes[i + width].parent = ds[i]; //.parent
                    // System.out.println(ds[i].data+" " + ds[i+width].data + " " + ds[i+width].parent.data);
                }
                if (disjointSetNodes[i + 1].data != -1 && (i) % width != 0) {

                    disjointSetNodes[i + 1].parent = disjointSetNodes[i]; //.parent
                }
                //System.out.println(ds[i].data + " " + ds[i].parent.data );
            }
        }
        ds = disjointSetNodes;
    }


    /**
     * unionSets
     * <p>
     * this method uses union and find algorithms to check and union/ merge two disjoint sets.
     * <p>
     * The method is given a array the width of the height of the image needed for boundary checking,
     * and a seperation integer which is used to control how far apart to merge the sets in pixel units.
     *
     * @param disjointSetNodes
     * @param width
     * @param height
     * @param seperation
     */
    public void unionSets(DisjointSetNode<Integer>[] disjointSetNodes, int width, int height, int seperation) {
        for (int i = 0; i < disjointSetNodes.length; i++) {
            //Counter to check the distance between pixels to be unioned
            int counter = seperation;
            while (counter != 0) {
                if (i + width < width * height && disjointSetNodes[i].data != -1 && (i + width * counter) <= width * height) {
                    if (disjointSetNodes[i + width * counter].data != -1) {
                        union(disjointSetNodes, disjointSetNodes[i + width * counter].data, ds[i].data);
                    }
                    if (disjointSetNodes[i + counter].data != -1 && (i) % width != 0) {
                        union(disjointSetNodes, disjointSetNodes[i + counter].data, disjointSetNodes[i].data);
                    }
                    //System.out.println(ds[i].data + " " + ds[i].parent.data );
                }
                counter--;
            }
        }

        ds = disjointSetNodes;
        for (int j = 0; j < ds.length; j++)
            System.out.print(((ds[j].data != -1) ? find(ds, j).data + " " : "-1") + ((j + 1) % width == 0 ? "\n" : " "));
    }

    public void union(DisjointSetNode<Integer>[] disjointSetNodes, Integer chosen, Integer parent) {
        find(disjointSetNodes, chosen).parent = find(disjointSetNodes, parent);
    }


    public DisjointSetNode<Integer> find(DisjointSetNode<Integer>[] disjointSetNodes, Integer id) {
        while (!disjointSetNodes[id].parent.data.equals(id)) {
            id = disjointSetNodes[(Integer) disjointSetNodes[id].parent.data].data;
            id = (Integer) disjointSetNodes[id].parent.data;
        }
        return disjointSetNodes[id];
    }


    //=====================================================================================================================================

    /**
     * colorInDisjointSets
     * the method uses the mouse x and y position to get the set that will be coloured in,
     * using find it is possible to colour in every pixel with the same root as the chosen one.
     *
     * @param disjointSetNodes
     * @param chX
     * @param chY
     * @param image
     */
    public void colourInDisjointSets(DisjointSetNode<Integer>[] disjointSetNodes, int chX, int chY, Image image) {
        Random random = new Random();
        double r = 0 + (1 - 0) * random.nextDouble();
        double g = 0 + (1 - 0) * random.nextDouble();
        double b = 0 + (1 - 0) * random.nextDouble();


        PixelReader pixelReader = image.getPixelReader();

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage bwImage = new WritableImage(width, height);
        PixelWriter pixelWriter = bwImage.getPixelWriter();


        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (disjointSetNodes[y * width + x].data !=-1  ) {
                    int parent = find(disjointSetNodes, chY * width + chX).data;

                    Color cl = (find(disjointSetNodes, y * width + x).data.equals(parent)) ? Color.color(r, g, b, 1) : pixelReader.getColor(x, y);


                    pixelWriter.setColor(x, y, cl);
                } else {
                    pixelWriter.setColor(x, y, Color.BLACK);
                }
            }
        }
        blackAndWhiteIMG.setImage(bwImage);
    }



    public void colourInAllSets(Image blackAndWhite){

        PixelReader pixelReader = blackAndWhite.getPixelReader();

        int width = (int) blackAndWhite.getWidth();
        int height = (int) blackAndWhite.getHeight();




        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {



                if(pixelReader.getColor(y,x).equals(Color.WHITE))
                    colourInDisjointSets(ds,y,x,blackAndWhiteIMG.getImage());



            }
        }

    }
    public void colourInAllSets(Image blackAndWhite,Set<Integer> roots){
        Random random = new Random();
        double r = 0 + (1 - 0) * random.nextDouble();
        double g = 0 + (1 - 0) * random.nextDouble();
        double b = 0 + (1 - 0) * random.nextDouble();


        PixelReader pixelReader = blackAndWhite.getPixelReader();

        int width = (int) blackAndWhite.getWidth();
        int height = (int) blackAndWhite.getHeight();
        WritableImage bwImage = new WritableImage(width, height);
        PixelWriter pixelWriter = bwImage.getPixelWriter();

        for(int k : roots) {
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    if (y * width + x<ds.length-1) {
                        if (ds[y * width + x].data != -1) {


                            Color cl = (find(ds, y * width + x).data.equals(k)) ? Color.color(r, g, b, 1) : pixelReader.getColor(x, y);
                            pixelWriter.setColor(x, y, cl);
                        }
                        if (pixelReader.getColor(x, y).equals(Color.BLACK)) {
                            pixelWriter.setColor(x, y, Color.BLACK);
                        }
                    }
                }
            }
        }
    }

    public Set<Integer> getAllRoots() {
        Set<Integer> roots = new HashSet<>();

        //int previous=-100;
          for (int i = 0; i < ds.length; i++) {

            if (ds[i].data != -1) {
                // System.out.println(find(ds,i).data);
                roots.add(find(ds, i).data);

            }

        }

        return roots;
    }


    //===================================================================================================================
    /**
     * setUpRectangleMethod
     * <p>
     * this method takes in an image on which the rectangles will be drawn on and also the id of the root
     * to check what set is chosen.
     * The method returns the parameters for the box of the disjoint set with the specified root.
     *
     * @param img
     * @param rootID
     * @return array of the parameters of the box its coordinates and size.
     */
    public int[] setUpRectangle(Image img, int rootID) {
        int boxWidth;
        int boxHeight;
        int[] rectDetails = new int[4];
        PixelReader pixelReader = img.getPixelReader();

        int width = (int) img.getWidth();
        int height = (int) img.getHeight();
        int xCoordMax = 0;
        int xCoordMin = width;
        int yCoordMax = 0;
        int yCoordMin = height;
        WritableImage image = new WritableImage(width, height);
        PixelWriter pixelWriter = image.getPixelWriter();
        Set<Integer> allRoots = getAllRoots();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int currID = y * width + x;
                if (ds[currID].data != -1) {
                    if (allRoots.contains(ds[rootID].data) && find(ds, currID).data == rootID) {

                        xCoordMin = (x < xCoordMin) ? x : xCoordMin;
                        xCoordMax = (x > xCoordMax) ? x : xCoordMax;
                        yCoordMin = (y < yCoordMin) ? y : yCoordMin;
                        yCoordMax = (y > yCoordMax) ? y : yCoordMax;

                    }

                }
            }

        }
        boxHeight = yCoordMax - yCoordMin;
        boxWidth = xCoordMax - xCoordMin;
        rectDetails[0] = xCoordMin;
        rectDetails[1] = yCoordMin;
        rectDetails[2] = boxWidth;
        rectDetails[3] = boxHeight;

        return rectDetails;
    }

    /**
     * drawRectangle method
     * <p>
     * this method is used with the setUpRectangle method.
     * It loops through all the roots taken from the getAllRoots method and makes a box with pixelwriter,
     *
     * @param img
     */
    public Set<Integer> drawRectangle(Image img,int minSize) {
        Set<Integer> usedRoots = new HashSet<>();
        PixelReader pixelReader = image.getPixelReader();

        int width = (int) img.getWidth();
        int height = (int) img.getHeight();

        WritableImage image = new WritableImage(width, height);
        PixelWriter pixelWriter = image.getPixelWriter();
        //Set<Integer> allRoots = getAllRoots();
        for (int i = 0; i < getAllRoots().toArray().length; i++) {
            int[] boxDetails = setUpRectangle(img, (Integer) getAllRoots().toArray()[i]);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {


                    Color cl = pixelReader.getColor(x, y);



                    if ((((y == boxDetails[1] || y == boxDetails[1] + boxDetails[3] - 1) && (x >= boxDetails[0] && x <= boxDetails[0] + boxDetails[2] - 1)) || ((x == boxDetails[0] || x == boxDetails[0] + boxDetails[2] - 1) && (y >= boxDetails[1] && y <= boxDetails[3] + boxDetails[1] - 1)))&&(boxDetails[3]>=minSize || boxDetails[2]>=minSize)) {


                        pixelWriter.setColor(x, y, Color.BLUE);
                        usedRoots.add((Integer) getAllRoots().toArray()[i]);

                    } else if (pixelReader.getColor(x, y) != Color.BLUE) {
                        pixelWriter.setColor(x, y, cl);
                    }
                }
            }

            pixelReader = image.getPixelReader();

        }
        mainImage.setImage(image);
        return usedRoots;
    }




    public void setLabels(Image img,Set<Integer> roots){
            //int index = roots.size();
            List<Integer> sBySize = new ArrayList<>();
            for(int i :roots){
                sBySize.add(getSetSize(ds,i));
            }
          Collections.sort(sBySize);

        for(int root : roots){
            int[] boxDetails = setUpRectangle(img, root);

            Text t = new Text(boxDetails[0],boxDetails[1]+2,sBySize.indexOf(getSetSize(ds,root)) +"");
            imagePane.getChildren().add(t);
            //index--;
        }



    }


    //===================================================================================================================================

    //                        **************** UTIL METHODS **********************

    //==================================================================================================================================
    public int[] shellSort(Set<Integer> set) {
        int[] array = set.stream().mapToInt(i -> i).toArray();
        ArrayList<Integer> gaps = new ArrayList<>(); //
        int size = array.length;

        while (size > 1) {
            size = size / 2;

            gaps.add(size);
        }
        for (int g : gaps) {

            for (int e = g; e < array.length; e++) {
                int elem = array[e];
                int i;
                for (i = e; i >= g && array[i - g] < elem; i -= g)
                    array[i] = array[i - g];

                array[i] = elem;
            }
        }

        return array;
    }

    @FXML
    void applyResize(ActionEvent event) {
        mainImage.setFitWidth(Double.parseDouble(width.getText()));
        mainImage.setFitHeight(Double.parseDouble(height.getText()));

    }

    public void closeApp(ActionEvent actionEvent) {
        System.exit(0);
    }


    public void openNewTab(ActionEvent actionEvent) {

        mainImage.setImage(null);
        fname.setText(null);
        fsize.setText(null);
        width.setText(null);
        height.setText(null);


    }

    public int getSetSize(DisjointSetNode[] disjointSetNodes, int rootId) {
        int size = 0;
        for (int i = 0; i < disjointSetNodes.length; i++) {
            if (ds[i].data != -1) {
                if (find(disjointSetNodes, i).data.equals(rootId)) size = size + 1;
            }
        }
        return size;
    }

    public int getIndex(int[] a, int id) {
        int index = -1;
        for (int i = 0; i < a.length; i++) {
            if (a[i] == id) {
                index = i;
                break;
            }
        }
        return index;
    }


    public void getRGB(MouseEvent mouseEvent) {
        int x = new Double(mouseEvent.getX()).intValue();
        int y = new Double(mouseEvent.getY()).intValue();

        PixelReader r = mainImage.getImage().getPixelReader();
        System.out.print(x + " " + y);
        System.out.println(ds[x + y * Integer.parseInt(width.getText())].data);
        tl = new Tooltip("Disjoint Set " + " " + getIndex(shellSort(getAllRoots()), find(ds, x + y * Integer.parseInt(width.getText())).data) + "\n" + "Size = " + getSetSize(ds, find(ds, x + y * Integer.parseInt(width.getText())).data));
        //System.out.println(r.getColor(x,y).getRed()*255 + " " + r.getColor(x,y).getGreen()*255 + " " +r.getColor(x,y).getBlue()*255 + "\n" + r.getColor(x,y).getHue() + " "+r.getColor(x,y).getSaturation());

        Tooltip.install(mainImage, tl);
        colourInDisjointSets(ds, x, y, blackAndWhiteIMG.getImage());
    }


    //**************************************************************************************************************

    public void processImage(ActionEvent actionEvent) {
        setElementValues(blackAndWhiteIMG.getImage());
        setParents(ds, Integer.parseInt(width.getText()), Integer.parseInt(height.getText()));
        unionSets(ds, Integer.parseInt(width.getText()), Integer.parseInt(height.getText()), (int) outlierCounter.getValue());
        setLabels(mainImage.getImage(),drawRectangle(mainImage.getImage(), (int) sizeManager.getValue()));}).start();
        numDisjointSets.setText(numDisjointSets.getText() + " " + getAllRoots().size());


        //**************************************************************************************************************


    }

    public ImageView getBlackAndWhiteIMG() {
        return blackAndWhiteIMG;
    }

    public void setBlackAndWhiteIMG(ImageView blackAndWhiteIMG) {
        this.blackAndWhiteIMG = blackAndWhiteIMG;
    }

    public DisjointSetNode<Integer>[] getDs() {
        return ds;
    }

    public void colourInAll(ActionEvent actionEvent) {
        colourInAllSets(blackAndWhiteIMG.getImage());
    }
}



