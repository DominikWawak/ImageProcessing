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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    DisjointSetNode<Integer> ds[];

    @FXML
    private Pane mainPane;

    @FXML
    private MenuItem openFile;

    @FXML
    private MenuItem rgbChannels,closeBTN,saveBTN,newTabBtn;

    @FXML
    private ImageView mainImage,redImg,blueImg,greenImg,ogImg,blackAndWhiteIMG;


    @FXML
    private ToggleButton grayScaleBTN, blackAndWhite;

    @FXML
    private Button applyZoomBTN;

    @FXML
    private Slider brightnessSlider;

    @FXML
    private Slider hueSlider;

    @FXML
    private Slider saturationSlider;

    @FXML
    private Slider zoomSlider;

    @FXML
    private Label fname;

    @FXML
    private Label fsize;

    @FXML
    private Label fdimensions, hueLab,satLab;
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


    private Image image;
    ColorAdjust colorAdjust = new ColorAdjust();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        template=tab1;

        colorChoice.getItems().add("red");
        colorChoice.getItems().add("orange");
        colorChoice.getItems().add("blue");

        zoomSlider.valueProperty().addListener((ov,old_val,new_val) -> {
            mainImage.setScaleX(new_val.doubleValue());

           mainImage.setScaleY(new_val.doubleValue());

         });


        brightnessSlider.valueProperty().addListener((ov,old_val,new_val) -> {

            colorAdjust.setBrightness(new_val.doubleValue());
            mainImage.setEffect(colorAdjust);

        });
       hueSlider.valueProperty().addListener((ov,old_val,new_val) -> {

            colorAdjust.setHue(new_val.doubleValue());
            mainImage.setEffect(colorAdjust);
           hueLab.setText(hueSlider.getValue()+"");

        });
        saturationSlider.valueProperty().addListener((ov,old_val,new_val) -> {

            colorAdjust.setSaturation(new_val.doubleValue());
            mainImage.setEffect(colorAdjust);
            satLab.setText(saturationSlider.getValue()+"");

        });



        saveBTN.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Image");

                File file = fileChooser.showSaveDialog(saveBTN.getParentPopup());
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
                System.out.println(after-before);


            }
        });

    }


    public void RGBColours(){
        PixelReader pixelReader = image.getPixelReader();
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage modImage = new WritableImage(width,height);
        WritableImage modImage1 = new WritableImage(width,height);
        WritableImage modImage2 = new WritableImage(width,height);
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
                Color col1 = pixelReader.getColor(x,y);
                pixelWriter.setColor(x,y,new Color(0,0, col1.getBlue(),1.0));
                blueImg.setImage(modImage);
                pixelWriter1.setColor(x,y,new Color(0,col1.getGreen(), 0,1.0));
                greenImg.setImage(modImage1);
                pixelWriter2.setColor(x,y,new Color(col1.getRed(),0, 0,1.0));
                redImg.setImage(modImage2);
                ogImg.setImage(image);
            }
        }
    }


    public void browse(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        Stage stage = (Stage)mainPane.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        System.out.println(selectedFile.toString());

       image = new Image(selectedFile.toURI().toString());

        mainImage.setImage(image);
       // mainImage.translateZProperty().bind(zoomSlider.valueProperty());
        width.setText(String.valueOf(image.getWidth()));
        height.setText(String.valueOf(image.getHeight()));
        fname.setText(selectedFile.toURI().toString());
        fsize.setText(selectedFile.length() + " Bytes");
        System.out.println(mainImage.getScaleZ());




    }




    public void toggleGrayScale(ActionEvent actionEvent) {
        if(grayScaleBTN.isSelected()){
            PixelReader pixelReader = image.getPixelReader();
            int width = (int) image.getWidth();
            int height = (int) image.getHeight();
            WritableImage grayImage = new WritableImage(width,height);
             PixelWriter pixelWriter = grayImage.getPixelWriter();

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int pixel = pixelReader.getArgb(x, y);

                    int a = (pixel>>24) & 0xff; //bitwise and
                    int red = ((pixel >> 16) & 0xff);
                    int green = ((pixel >> 8) & 0xff);
                    int blue = (pixel & 0xff);

                    int average = (red+green+blue)/3;
                    pixel = (a<<24) | (average<<16) | (average<<8) | average;

                    pixelWriter.setArgb(x, y, pixel); // AMENDED TO -gray here.
                }
            }
            mainImage.setImage(grayImage);
        }
        else {
            mainImage.setImage(image);
        }

    }

    public Image makeBlackAndWhite(Image original ,String color){


        PixelReader pixelReader = original.getPixelReader();

        int width = (int) original.getWidth();
        int height = (int) original.getHeight();
        WritableImage bwImage = new WritableImage(width,height);
        PixelWriter pixelWriter = bwImage.getPixelWriter();


        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = pixelReader.getArgb(x, y);

                Color og = pixelReader.getColor(x,y);
                double valueOfColour = og.getRed()+og.getBlue()+og.getGreen();

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
                pixelWriter.setColor(x,y,borW);

                blackAndWhiteIMG.setImage(bwImage);
               // System.out.println(pixel);

            }
        }


        createDisjointSets(bwImage);
        return original;

    }


    public DisjointSetNode<Integer>[]  createDisjointSets(Image blackAndWhite) {
        PixelReader pixelReader = blackAndWhite.getPixelReader();

        int width = (int) blackAndWhite.getWidth();
        int height = (int) blackAndWhite.getHeight();


         ds = new DisjointSetNode[(int) (width * height)];
        int i = 0;
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {


                    ds[i] = (pixelReader.getColor(y,x).equals(Color.color(0,0,0,1))) ? new DisjointSetNode<>(-1):new DisjointSetNode<>(i);
                    ds[i].parent = ds[i];
               // System.out.println(ds[i].data );
                i++;

            }
        }
        createSets(width,height);
        return ds;
    }

    public void createSets(int width,int height){
            System.out.println(width);

        for(int i = 0;i<ds.length;i++){
//            if(ds[i].data != -1 && ds[i+1].data!=-1 && ((i+1)%width)!=1 && (i/width>height-1)) {
//                ds[i + 1].parent = ds[i];
//                ds[i + width].parent = ds[i];
//            }
            if(i+width<width*height  && ds[i].data != -1) {
                if ( ds[i + width].data != -1) {
                    ds[i + width].parent = ds[i].parent;
                     // System.out.println(ds[i].data+" " + ds[i+width].data + " " + ds[i+width].parent.data);
                }
                if( ds[i + 1].data != -1 && (i)%width != 0 ){
                     ds[i + 1].parent = ds[i].parent;
                }
                //System.out.println(ds[i].data + " " + ds[i].parent.data );
            }
       }
        for(int j=0;j<ds.length;j++) System.out.print(ds[j].parent.data+((j+1)%width==0 ? "\n" : " "));
    }
//    ((I+1)%WIDTH)==0
//            (I/WIDTH)<HEIGHT
//(i/WIDTH)<(HEIGHT-1)


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

    public void blackAndWhiteToggle(ActionEvent actionEvent) {

        makeBlackAndWhite(image,colorChoice.getValue().toString());
    }

    public void getRGB(MouseEvent mouseEvent) {
        int x = new Double(mouseEvent.getX()).intValue();
        int y = new Double(mouseEvent.getY()).intValue();
        PixelReader r = mainImage.getImage().getPixelReader();
        System.out.println(r.getColor(x,y).getRed()*255 + " " + r.getColor(x,y).getGreen()*255 + " " +r.getColor(x,y).getBlue()*255 + "\n" + r.getColor(x,y).getHue() + " "+r.getColor(x,y).getSaturation());
    }
}


