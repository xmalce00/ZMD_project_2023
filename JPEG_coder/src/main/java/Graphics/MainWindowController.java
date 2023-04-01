package Graphics;

import Enums.*;
import Jpeg.Process;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Pair;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class MainWindowController implements Initializable {

    public Button showOriginalImageBtn;

    public Button showOriginalRedBtn;
    public Button showOriginalGreenBtn;
    public Button showOriginalBlueBtn;

    public Button showOriginalYBtn;
    public Button showOriginalCrBtn;
    public Button showOriginalCbBtn;

    public Button convertBtn;
    public Button downSampleBtn;
    public Button transformBtn;
    public Button quantizeBtn;

    public Button iQuantizeBtn;
    public Button iTransformBtn;
    public Button overSampleBtn;
    public Button iConvertBtn;

    public Button countParamsBtn;
    public Button countSSIMparamsBtn;

    public Button showModifiedImageBtn;

    public Button showModifiedRedBtn;
    public Button showModifiedGreenBtn;
    public Button showModifiedBlueBtn;

    public Button showModifiedYBtn;
    public Button showModifiedCrBtn;
    public Button showModifiedCbBtn;
    public Button extractWatermarkBtn;

    private Process process;
    private BufferedImage originalImage;
    private final ArrayList<Button> buttons = new ArrayList<>();
//    public MainWindowController() {
    //  }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        samplingtype.getItems().setAll(SamplingTypes.values());
        transformtype.getItems().setAll(TransformTypes.values());

        samplingtype.getSelectionModel().select(SamplingTypes.S_4_4_4);
        transformtype.getSelectionModel().select(TransformTypes.DCT);

        qualityType.getItems().setAll(QualityTypes.values());
        qualityTypeSSIM.getItems().setAll(YcRcBTypes.values());

        qualityType.getSelectionModel().select(QualityTypes.RED);
        qualityTypeSSIM.getSelectionModel().select(YcRcBTypes.Y);

        waterMarkType.getItems().setAll(WatermarkTypes.values());
        waterMarkElement.getItems().setAll(YcRcBTypes.values());

        waterMarkType.getSelectionModel().select(WatermarkTypes.Space);
        waterMarkElement.getSelectionModel().select(YcRcBTypes.Y);




        ObservableList<Integer> blocks = FXCollections.observableArrayList(2,4,8,16,32,64,128,256,512);
        SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(blocks);
        spinnerValueFactory.setValue(8);
        transformblocks.setValueFactory(spinnerValueFactory);

        quantizeQuality.setValue(50);
        quantizeQualityField.textProperty().bindBidirectional(quantizeQuality.valueProperty(), NumberFormat.getIntegerInstance());
        quantizeQualityField.setTextFormatter(new TextFormatter<>(filter));

        showsteps.setSelected(true);

        buttons.add(showOriginalYBtn);
        buttons.add(showOriginalCrBtn);
        buttons.add(showOriginalCbBtn);

        buttons.add(downSampleBtn);
        buttons.add(transformBtn);
        buttons.add(quantizeBtn);

        buttons.add(iQuantizeBtn);
        buttons.add(iTransformBtn);
        buttons.add(overSampleBtn);
        buttons.add(iConvertBtn);

        buttons.add(countParamsBtn);
        buttons.add(countSSIMparamsBtn);

        buttons.add(showModifiedImageBtn);

        buttons.add(showModifiedRedBtn);
        buttons.add(showModifiedGreenBtn);
        buttons.add(showModifiedBlueBtn);

        buttons.add(showModifiedYBtn);
        buttons.add(showModifiedCrBtn);
        buttons.add(showModifiedCbBtn);

        importImage();
        buttonInit();
    }

    private void importImage() {
        try {
            this.originalImage = Dialogs.loadImageFromPath(Dialogs.openFile().getPath());
            this.process = new Process(originalImage);
            System.out.println("Image: w: " + originalImage.getWidth() + " h: " + originalImage.getHeight());
            Dialogs.showImageInWindow(originalImage, "Original");
        } catch (NullPointerException e) {
            System.out.println("No selected image");
        }
    }
    private void buttonInit() {
        for(Button b : buttons) {
            b.setDisable(true);
        }
    }

    @FXML
    private void Closewindows(ActionEvent event) { Dialogs.closeAllWindows(); }

    @FXML
    private void Changeimage(ActionEvent event) {
        importImage();
        Dialogs.closeAllWindows();
        buttonInit();
    }

    @FXML
    private void Showimage(/*ActionEvent event*/) { Dialogs.showImageInWindow(process.getOriginalImageFromRGB(), "Original RGB");}

    @FXML
    private void showOriginalRed(ActionEvent event) { Dialogs.showImageInWindow(process.getComponentOriginal(Process.RED),"Original RED"); }

    @FXML
    private void showOriginalGreen(ActionEvent event) { Dialogs.showImageInWindow(process.getComponentOriginal(Process.GREEN),"Original GREEN"); }

    @FXML
    private void showOriginalBlue(ActionEvent event) { Dialogs.showImageInWindow(process.getComponentOriginal(Process.BLUE),"Original BLUE"); }

    @FXML
    private void showModifiedRGB(ActionEvent event) { Dialogs.showImageInWindow(process.getModifiedImageFromRGB(),"Modified RBG"); }

    @FXML
    private void showModifiedRed(ActionEvent event) { Dialogs.showImageInWindow(process.getComponentModified(Process.RED), "Modified RED"); }

    @FXML
    private void showModifiedGreen(ActionEvent event) { Dialogs.showImageInWindow(process.getComponentModified(Process.GREEN), "Modified GREEN"); }

    @FXML
    private void showModifiedBlue(ActionEvent event) { Dialogs.showImageInWindow(process.getComponentModified(Process.BLUE), "Modified BLUE"); }

    @FXML
    private void showOriginalY(ActionEvent event) { Dialogs.showImageInWindow(process.showOriginalY(), "Original Y"); }

    @FXML
    private void showOriginalCr(ActionEvent event) { Dialogs.showImageInWindow(process.showOriginalCr(), "Original Cr"); }

    @FXML
    private void showOriginalCb(ActionEvent event) { Dialogs.showImageInWindow(process.showOriginalCb(), "Original cB"); }

    @FXML
    private void showModifiedY(ActionEvent event) { Dialogs.showImageInWindow(process.showOriginalY(), "Modified Y"); }

    @FXML
    private void showModifiedCr(ActionEvent event) { Dialogs.showImageInWindow(process.showModifiedCr(), "Modified cR"); }

    @FXML
    private void showModifiedCb(ActionEvent event) { Dialogs.showImageInWindow(process.showModifiedCb(), "Modified cB"); }

    @FXML
    private void ConvertToYCbCr(ActionEvent event) {
        process.convertToYcBcR();
        showOriginalYBtn.setDisable(false);
        showOriginalCrBtn.setDisable(false);
        showOriginalCbBtn.setDisable(false);
        downSampleBtn.setDisable(false);
    }

    @FXML
    private void convertToRGB(ActionEvent event) {
        process.convertToRBG();
        countParamsBtn.setDisable(false);
        countSSIMparamsBtn.setDisable(false);
        showModifiedImageBtn.setDisable(false);
        showModifiedRedBtn.setDisable(false);
        showModifiedGreenBtn.setDisable(false);
        showModifiedBlueBtn.setDisable(false);
    }




    @FXML
    private ComboBox<SamplingTypes> samplingtype;

    @FXML
    private CheckBox showsteps;
    @FXML
    private void downSample(ActionEvent event) {
        System.out.println("Down " + samplingtype.getValue());
        process.downSample(samplingtype.getValue());
        samplingtype.setDisable(true);
        if(showsteps.isSelected()) {
            Dialogs.showImageInWindow(
                    "Down " + samplingtype.getValue(),
                    new Pair<>(process.showModifiedY(),"Down sample Y"),
                    new Pair<>(process.showModifiedCr(), "Down sample cR"),
                    new Pair<>(process.showModifiedCb(), "Down sample cB")
                    );
        }

        ObservableList<Integer> blocks = null;
        if(samplingtype.getValue() == SamplingTypes.S_4_2_2 || samplingtype.getValue() == SamplingTypes.S_4_2_0) {
            blocks = FXCollections.observableArrayList(2,4,8,16,32,64,128,256);
        } else if (samplingtype.getValue() == SamplingTypes.S_4_1_1) {
            blocks = FXCollections.observableArrayList(2,4,8,16,32,64,128);
        } else if (samplingtype.getValue() == SamplingTypes.S_4_4_4) {
            blocks = FXCollections.observableArrayList(2,4,8,16,32,64,128,256,512);
        }
        SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(blocks);
        spinnerValueFactory.setValue(8);
        transformblocks.setValueFactory(spinnerValueFactory);

        transformBtn.setDisable(false);
    }
    @FXML
    private void oversample(ActionEvent event) {
        System.out.println("Up " + samplingtype.getValue());
        process.upSample(samplingtype.getValue());
        samplingtype.setDisable(false);
        if(showsteps.isSelected()) {
            Dialogs.showImageInWindow(
                    "Up " + samplingtype.getValue(),
                    new Pair<>(process.showModifiedY(),"Up sample Y"),
                    new Pair<>(process.showModifiedCr(), "Up sample cR"),
                    new Pair<>(process.showModifiedCb(), "Up sample cB")
            );
        }
        ObservableList<Integer> blocks = FXCollections.observableArrayList(2,4,8,16,32,64,128,256,512);
        SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(blocks);
        spinnerValueFactory.setValue(8);
        transformblocks.setValueFactory(spinnerValueFactory);

        //transformblocks.getValueFactory().ListSpinnerValueFactory<>(blocks);

        showModifiedYBtn.setDisable(false);
        showModifiedCrBtn.setDisable(false);
        showModifiedCbBtn.setDisable(false);

        iConvertBtn.setDisable(false);
    }



    @FXML
    public ComboBox<QualityTypes> qualityType;
    @FXML
    private void countquality(ActionEvent event) {
        double [] params = process.countQuality(qualityType.getValue());
        qualityMSF.setText(String.format("%.3f",params[0]));
        qualityMAE.setText(String.format("%.3f",params[1]));
        qualityPSNR.setText(String.format("%.3f",params[2]));
        qualitySAE.setText(String.format("%.3f",params[3]));
    }


    @FXML
    private TextField qualityMSF;
    @FXML
    public TextField qualityMAE;
    @FXML
    public TextField qualityPSNR;
    @FXML
    public TextField qualitySAE;

    @FXML
    public ComboBox<YcRcBTypes> qualityTypeSSIM;
    @FXML
    public void countSSIM(ActionEvent actionEvent) {
        double[] params = process.countSSIM(qualityTypeSSIM.getValue());
        qualitySSIM.setText(String.format("%.5f",params[0]));
        qualityMSSIM.setText(String.format("%.5f",params[1]));
    }


    @FXML
    public TextField qualitySSIM;
    @FXML
    public TextField qualityMSSIM;
    @FXML
    void Transform(ActionEvent event) {
        System.out.println(transformtype.getValue().toString() + ", blocks: " + transformblocks.getValue() + " x " + transformblocks.getValue());
        process.transform(transformtype.getValue(), transformblocks.getValue());
        if(showsteps.isSelected()) {
            Dialogs.showImageInWindow(
                    transformtype.getValue().toString() + ", blocks: " + transformblocks.getValue() + " x " + transformblocks.getValue(),
                    new Pair<>(process.showModifiedY(),"Transform Y"),
                    new Pair<>(process.showModifiedCr(), "Transform cR"),
                    new Pair<>(process.showModifiedCb(), "Transform cB")
            );
        }
        transformtype.setDisable(true);
        transformblocks.setDisable(true);

        quantizeBtn.setDisable(false);
    }
    @FXML
    void itransform(ActionEvent event) {
        System.out.println("Inverse " + transformtype.getValue().toString() + ", blocks: " + transformblocks.getValue() + " x " + transformblocks.getValue());
        process.inverseTransform(transformtype.getValue(), transformblocks.getValue());
        if(showsteps.isSelected()) {
            Dialogs.showImageInWindow(
                    "Inverse " + transformtype.getValue().toString() + ", blocks: " + transformblocks.getValue() + " x " + transformblocks.getValue(),
                    new Pair<>(process.showModifiedY(),"Inverse transform Y"),
                    new Pair<>(process.showModifiedCr(), "Inverse transform cR"),
                    new Pair<>(process.showModifiedCb(), "Inverse transform cB")
            );
        }
        transformtype.setDisable(false);
        transformblocks.setDisable(false);

        overSampleBtn.setDisable(false);
    }



    @FXML
    private ComboBox<TransformTypes> transformtype;
    @FXML
    private Spinner<Integer> transformblocks;
    @FXML
    void Quantize(ActionEvent event) {
        System.out.println("Quantize - quality: " + quantizeQuality.getValue());
        process.quantize(transformblocks.getValue(), quantizeQuality.getValue());
        if(showsteps.isSelected()) {
            Dialogs.showImageInWindow(
                    "Quantize",
                    new Pair<>(process.showModifiedY(),"Quantize Y"),
                    new Pair<>(process.showModifiedCr(), "Quantize cR"),
                    new Pair<>(process.showModifiedCb(), "Quantize cB")
            );
        }
        quantizeQuality.setDisable(true);
        quantizeQualityField.setDisable(true);
        iQuantizeBtn.setDisable(false);
    }
    @FXML
    void iquantize(ActionEvent event) {
        System.out.println("Inverse quantize - quality: " + quantizeQuality.getValue());
        process.inverseQuantize(transformblocks.getValue(), quantizeQuality.getValue());
        if(showsteps.isSelected()) {
            Dialogs.showImageInWindow(
                    "Inverse quantize",
                    new Pair<>(process.showModifiedY(),"Inverse quantize Y"),
                    new Pair<>(process.showModifiedCr(), "Inverse quantize cR"),
                    new Pair<>(process.showModifiedCb(), "Inverse quantize cB")
            );
        }
        quantizeQuality.setDisable(false);
        quantizeQualityField.setDisable(false);
        iTransformBtn.setDisable(false);
    }



    @FXML
    public Slider quantizeQuality;
    @FXML
    public TextField quantizeQualityField;

    @FXML
    private CheckBox shadesofgrey;
    @FXML
    void Close(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
    @FXML
    void Reset(ActionEvent event) {
        process = new Process(originalImage);
        buttonInit();
        samplingtype.setDisable(false);
        transformtype.setDisable(false);
        samplingtype.setDisable(false);
        quantizeQuality.setDisable(false);
        quantizeQualityField.setDisable(false);
        Dialogs.closeAllWindows();
    }

    @FXML
    public ComboBox<WatermarkTypes> waterMarkType;
    @FXML
    public ComboBox<YcRcBTypes> waterMarkElement;
    @FXML
    public ComboBox depthOfWatermark;
    @FXML
    public Button addWatermarkBtn;

    public void addWatermark(ActionEvent actionEvent) {
        System.out.println("Adding watermark");
        process.addWatermark();
        Dialogs.showImageInWindow(process.showModifiedY(), "Modified Y");
    }

    public void extractWatermark(ActionEvent actionEvent) {
        System.out.println("Extract watermark");
        BufferedImage image = process.extractWatermark();
        Dialogs.showImageInWindow(image, "Watermark");

    }

    private UnaryOperator<TextFormatter.Change> filter = change -> {
        String text = change.getText();
        if (text.matches("[0-9]*"))  return change;
        return null;
    };
}