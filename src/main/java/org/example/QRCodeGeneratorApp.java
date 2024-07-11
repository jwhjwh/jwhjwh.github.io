package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class QRCodeGeneratorApp extends Application {

    private TextField sampleIDField;
    private TextField reportIDField;
    private TextField testingOrgField;
    private TextField testingItemField;
    private TextField projectField;
    private TextField testingPartField;
    private TextField entrustUnitField;
    private TextField testStatusField;
    private TextField testingResultField;
    private TextField reportDateField;

    @Override
    public void start(Stage primaryStage) {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(5);

        sampleIDField = addLabelAndField(vbox, "样品编号:");
        reportIDField = addLabelAndField(vbox, "报告编号:");
        testingOrgField = addLabelAndField(vbox, "检测机构:");
        testingItemField = addLabelAndField(vbox, "检测项目:");
        projectField = addLabelAndField(vbox, "工程名称:");
        testingPartField = addLabelAndField(vbox, "检测部位:");
        entrustUnitField = addLabelAndField(vbox, "委托单位:");
        testStatusField = addLabelAndField(vbox, "试验状态:");
        testingResultField = addLabelAndField(vbox, "检测结果:");
        reportDateField = addLabelAndField(vbox, "报告日期:");

        Button generateQRButton = new Button("生成二维码");
        generateQRButton.setOnAction(event -> generateQRCode());

        vbox.getChildren().add(generateQRButton);

        primaryStage.setScene(new Scene(vbox, 400, 600));
        primaryStage.setTitle("QR Code Generator");
        primaryStage.show();
    }

    private TextField addLabelAndField(VBox vbox, String labelText) {
        Label label = new Label(labelText);
        TextField textField = new TextField();
        vbox.getChildren().addAll(label, textField);
        return textField;
    }

    private void generateQRCode() {
        Map<String, String> data = new HashMap<>();
        data.put("sampleID", sampleIDField.getText());
        data.put("reportID", reportIDField.getText());
        data.put("testingOrg", testingOrgField.getText());
        data.put("testingItem", testingItemField.getText());
        data.put("project", projectField.getText());
        data.put("testingPart", testingPartField.getText());
        data.put("entrustUnit", entrustUnitField.getText());
        data.put("testStatus", testStatusField.getText());
        data.put("testingResult", testingResultField.getText());
        data.put("reportDate", reportDateField.getText());

        try {
            String qrData = "http://your-web-server.com/inspection-report.html?" +
                    data.entrySet().stream()
                            .map(entry -> entry.getKey() + "=" + entry.getValue())
                            .collect(Collectors.joining("&"));
            BitMatrix bitMatrix = new QRCodeWriter().encode(qrData, BarcodeFormat.QR_CODE, 200, 200);
            Path path = File.createTempFile("tempQR", ".png").toPath();
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
            System.out.println("QR code generated at: " + path);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
