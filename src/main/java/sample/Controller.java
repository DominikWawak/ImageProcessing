package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.*;
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

    @FXML
    private Pane mainPane;

    @FXML
    private MenuItem openFile;

    @FXML
    private MenuItem rgbChannels,closeBTN,saveBTN,newTabBtn;

    @FXML
    private ImageView mainImage,redImg,blueImg,greenImg,ogImg;


    @FXML
    private ToggleButton grayScaleBTN;

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
    private Label fdimensions;
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



    private Image image;
    ColorAdjust colorAdjust = new ColorAdjust();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        template=tab1;

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

        });
        saturationSlider.valueProperty().addListener((ov,old_val,new_val) -> {

            colorAdjust.setSaturation(new_val.doubleValue());
            mainImage.setEffect(colorAdjust);

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
}


