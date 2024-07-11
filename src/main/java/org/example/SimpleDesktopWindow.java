package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
public class SimpleDesktopWindow extends JFrame{
    private final JTextField[] textFields = new JTextField[10];
    private final JLabel[] labels = new JLabel[10];
    private final String[] labelNames = {
            "样品编号:", "报告编号:", "检测机构:", "检测项目:", "工程名称:",
            "检测部位:", "委托单位:", "试验状态:", "检测结果:", "报告日期:"
    };
    private JButton generateButton;
    private JPanel qrCodePanel;

    public SimpleDesktopWindow() {
        setTitle("QR Code Generator Form");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        generateButton = new JButton("生成二维码");
        qrCodePanel = new JPanel();

        generateButton.addActionListener(e -> generateQRCode());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(11, 1));

        for (int i = 0; i < 10; i++) {
            labels[i] = new JLabel(labelNames[i]);
            textFields[i] = new JTextField(20);
            inputPanel.add(labels[i]);
            inputPanel.add(textFields[i]);
        }
        inputPanel.add(generateButton);

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(qrCodePanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void generateQRCode() {
        StringBuilder dataBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            dataBuilder.append(labelNames[i]).append(textFields[i].getText()).append("\n");
        }
        String data = dataBuilder.toString();
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 200, 200);
            qrCodePanel.removeAll();
            qrCodePanel.add(new QRCodeImagePanel(bitMatrix));
            qrCodePanel.revalidate();
            qrCodePanel.repaint();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private class QRCodeImagePanel extends JPanel {
        private BitMatrix bitMatrix;

        public QRCodeImagePanel(BitMatrix bitMatrix) {
            this.bitMatrix = bitMatrix;
            setPreferredSize(new java.awt.Dimension(200, 200));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            for (int y = 0; y < bitMatrix.getHeight(); y++) {
                for (int x = 0; x < bitMatrix.getWidth(); x++) {
                    if (bitMatrix.get(x, y)) {
                        g2d.fillRect(x, y, 1, 1);
                    }
                }
            }
            g2d.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SimpleDesktopWindow::new);
    }

}
