package Jpeg;

import Enums.QualityTypes;
import Enums.SamplingTypes;
import Enums.TransformTypes;
import Enums.YcRcBTypes;
import Jama.Matrix;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Process {

    private BufferedImage originalImage;
    private int imageHeight;
    private int imageWidth;

    private int [][] originalRed, modifiedRed;
    private int [][] originalGreen, modifiedGreen;
    private int [][] originalBlue, modifiedBlue;

    private Matrix originalY, modifiedY;
    private Matrix originalCb, modifiedCb;
    private Matrix originalCr, modifiedCr;
    //public static final int ALL = 0;
    public static final int RED = 1;
    public static final int GREEN = 2;
    public static final int BLUE = 3;

    public static final int Y = 7;
    public static final int cR = 8;
    public static final int cB = 9;

    public Process(BufferedImage image) {

        //this.originalImage = imageControl(image);

        this.originalImage = image;
        this.imageHeight = image.getHeight();
        this.imageWidth = image.getWidth();

        this.originalRed = new int[this.imageHeight][this.imageWidth];
        this.originalGreen = new int[this.imageHeight][this.imageWidth];
        this.originalBlue = new int[this.imageHeight][this.imageWidth];

        this.modifiedRed = new int[this.imageHeight][this.imageWidth];
        this.modifiedGreen = new int[this.imageHeight][this.imageWidth];
        this.modifiedBlue = new int[this.imageHeight][this.imageWidth];

        this.originalY = new Matrix(this.imageHeight,this.imageWidth);
        this.originalCb = new Matrix(this.imageHeight,this.imageWidth);
        this.originalCr = new Matrix(this.imageHeight,this.imageWidth);

//        this.modifiedY = new Matrix(this.imageHeight,this.imageWidth);
//        this.modifiedCb = new Matrix(this.imageHeight,this.imageWidth);
//        this.modifiedCr = new Matrix(this.imageHeight,this.imageWidth);

        setRGB();
        //System.out.println(this.imageHeight);
    }

//    private BufferedImage imageControl(BufferedImage image) {
//
//        if(image.getHeight()%8 != 0 || image.getWidth()%8 != 0) {
//
//            int newHeight = (int) (Math.ceil(image.getHeight()/8) * 8);
//            int newWidth = (int) (Math.ceil(image.getWidth()/8) * 8);
//
//            System.out.println("New image: " + newHeight + ", " + newWidth);
//
//            //BufferedImage newImage = new BufferedImage(newHeight, newWidth);
//
//            return image;
//
//        } else {
//            return image;
//        }
//
//    }

    private void setRGB() {
        for(int h = 0; h < imageHeight; h++) {
            for(int w = 0; w < imageWidth; w++) {
                Color color = new Color(originalImage.getRGB(w,h));
                originalRed[h][w] = color.getRed();
                originalGreen[h][w] = color.getGreen();
                originalBlue[h][w] = color.getBlue();
            }
        }
    }

    public BufferedImage getOriginalImageFromRGB() {
        BufferedImage bfImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

        for(int h = 0; h < imageHeight; h++) {
            for(int w = 0; w < imageWidth; w++) {
                bfImage.setRGB(w,h,(new Color(originalRed[h][w],originalGreen[h][w],originalBlue[h][w]).getRGB()));
            }
        }
        return bfImage;
    }

    public BufferedImage getComponentOriginal(int component) {
        BufferedImage bfImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        switch (component) {
            case RED:
                for(int h = 0; h < imageHeight; h++) {
                    for(int w = 0; w < imageWidth; w++) {
                        bfImage.setRGB(w,h,(new Color(originalRed[h][w],0,0).getRGB()));
                        //bfImage.setRGB(w,h,(new Color(originalRed[h][w]).getRGB()));
                    }
                }
                break;
            case GREEN:
                for(int h = 0; h < imageHeight; h++) {
                    for(int w = 0; w < imageWidth; w++) {
                        bfImage.setRGB(w,h,(new Color(0,originalGreen[h][w],0).getRGB()));
                    }
                }
                break;
            case BLUE:
                for(int h = 0; h < imageHeight; h++) {
                    for(int w = 0; w < imageWidth; w++) {
                        bfImage.setRGB(w,h,(new Color(0,0,originalBlue[h][w]).getRGB()));
                    }
                }
                break;
            default: throw new IllegalArgumentException();
        }
        return bfImage;
    }

    public BufferedImage showOriginalY() {
        BufferedImage bfImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        for(int h = 0; h < imageHeight; h++) {
            for(int w = 0; w < imageWidth; w++) {
                bfImage.setRGB(w,h,(new Color(convertColor(originalY.get(h,w)), convertColor(originalY.get(h,w)), convertColor(originalY.get(h,w))).getRGB()));
            }
        }
        return bfImage;
    }

    public BufferedImage showOriginalCr() {
        BufferedImage bfImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        for(int h = 0; h < imageHeight; h++) {
            for(int w = 0; w < imageWidth; w++) {
                bfImage.setRGB(w,h,(new Color(convertColor(originalCr.get(h,w)), convertColor(originalCr.get(h,w)), convertColor(originalCr.get(h,w))).getRGB()));
            }
        }
        return bfImage;
    }

    public BufferedImage showOriginalCb() {
        BufferedImage bfImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        for(int h = 0; h < imageHeight; h++) {
            for(int w = 0; w < imageWidth; w++) {
                bfImage.setRGB(w,h,(new Color(convertColor(originalCb.get(h,w)), convertColor(originalCb.get(h,w)), convertColor(originalCb.get(h,w))).getRGB()));
            }
        }
        return bfImage;
    }

    public BufferedImage getModifiedImageFromRGB() {
        BufferedImage bfImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        for(int h = 0; h < imageHeight; h++) {
            for(int w = 0; w < imageWidth; w++) {
                bfImage.setRGB(w,h,(new Color(modifiedRed[h][w],modifiedGreen[h][w],modifiedBlue[h][w]).getRGB()));
            }
        }
        return bfImage;
    }

    public BufferedImage getComponentModified(int component) {
        BufferedImage bfImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        switch (component) {
            case RED:
                for(int h = 0; h < imageHeight; h++) {
                    for(int w = 0; w < imageWidth; w++) {
                        bfImage.setRGB(w,h,(new Color(modifiedRed[h][w],0,0).getRGB()));
                    }
                }
                break;
            case GREEN:
                for(int h = 0; h < imageHeight; h++) {
                    for(int w = 0; w < imageWidth; w++) {
                        bfImage.setRGB(w,h,(new Color(0,modifiedGreen[h][w],0).getRGB()));
                    }
                }
                break;
            case BLUE:
                for(int h = 0; h < imageHeight; h++) {
                    for(int w = 0; w < imageWidth; w++) {
                        bfImage.setRGB(w,h,(new Color(0,0,modifiedBlue[h][w]).getRGB()));
                    }
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
        return bfImage;
    }

    public BufferedImage showModifiedY() {
        BufferedImage bfImage = new BufferedImage(modifiedY.getColumnDimension(), modifiedY.getRowDimension(), BufferedImage.TYPE_INT_RGB);
        for(int h = 0; h < modifiedY.getRowDimension(); h++) {
            for(int w = 0; w < modifiedY.getColumnDimension(); w++) {
                bfImage.setRGB(w,h,(new Color(convertColor(modifiedY.get(h,w)), convertColor(modifiedY.get(h,w)), convertColor(modifiedY.get(h,w))).getRGB()));
            }
        }
        return bfImage;
    }

    public BufferedImage showModifiedCr() {
        BufferedImage bfImage = new BufferedImage(modifiedCr.getColumnDimension(), modifiedCr.getRowDimension(), BufferedImage.TYPE_INT_RGB);
        for(int h = 0; h < modifiedCr.getRowDimension(); h++) {
            for(int w = 0; w < modifiedCr.getColumnDimension(); w++) {
                bfImage.setRGB(w,h,(new Color(convertColor(modifiedCr.get(h,w)), convertColor(modifiedCr.get(h,w)), convertColor(modifiedCr.get(h,w))).getRGB()));
                //bfImage.setRGB(w,h,(new Color((int) modifiedCr.get(h,w), (int) modifiedCr.get(h,w), (int) modifiedCr.get(h,w)).getRGB()));
            }
        }
        return bfImage;
    }

    public BufferedImage showModifiedCb() {
        BufferedImage bfImage = new BufferedImage(modifiedCb.getColumnDimension(), modifiedCb.getRowDimension(), BufferedImage.TYPE_INT_RGB);
        for(int h = 0; h < modifiedCb.getRowDimension(); h++) {
            for(int w = 0; w < modifiedCb.getColumnDimension(); w++) {
                bfImage.setRGB(w,h,(new Color(convertColor(modifiedCb.get(h,w)), convertColor(modifiedCb.get(h,w)), convertColor(modifiedCb.get(h,w))).getRGB()));
                //bfImage.setRGB(w,h,(new Color((int) modifiedCb.get(h,w), (int) modifiedCb.get(h,w), (int) modifiedCb.get(h,w)).getRGB()));
            }
        }
        return bfImage;
    }

    private int convertColor(double input) {
        int color = (int) Math.round(input);
        if (color < 0) color = 0;
        if (color > 255) color = 255;
        return color;
    }

    public void convertToYcBcR() {
        Matrix[] matrices = ColorTransform.convertOriginalRGBtoYcBcR(originalRed,originalGreen,originalBlue);
        originalY = matrices[0];
        originalCb = matrices[1];
        originalCr = matrices[2];

//        int count = 0;
//
//        for(int i = 0; i < originalY.getRowDimension(); i++) {
//            for (int j = 0; j < originalY.getColumnDimension(); j++) {
//                //if(originalY.get(i,j) < 0) count++;
//                originalY.set(i,j, originalY.get(i,j)+50);
//            }
//            System.out.println();
//        }
//
//        System.out.println("Y part:\n");
//        for(int i = 0; i < 8; i++) {
//            for (int j = 0; j < 8; j++) {
//                //if(originalY.get(i,j) < 0) count++;
//                System.out.print(originalY.get(i,j) + " ");
//                //byte b = (byte) originalY.get(i,j);
//                //System.out.print(b + " ");
//            }
//            System.out.println();
//        }

        //BufferedImage i = new BufferedImage(100,100,BufferedImage.TYPE_BYTE_BINARY);

        //System.out.println("Negative: " + count);

    }

    public BufferedImage showWatermark() {

        BufferedImage bfImage = new BufferedImage(originalY.getColumnDimension(), originalY.getRowDimension(), BufferedImage.TYPE_BYTE_BINARY);

        for(int h = 0; h < imageHeight; h++) {
            for(int w = 0; w < imageWidth; w++) {
                bfImage.setRGB(w,h,(new Color((int) originalY.get(h,w), (int) originalY.get(h,w), (int) originalY.get(h,w)).getRGB()));
            }
        }
        return bfImage;
    }

    public void addWatermark() {

        modifiedY = Watermark.addWatermark(originalY, 20);

        //Matrix w = Watermark.extractWatermark(newY, originalY);

//        System.out.println("Extracted watermark");

//        for(int i = 0; i < w.getRowDimension(); i++) {
//            for (int j = 0; j < w.getColumnDimension(); j++) {
//                System.out.print(w.get(i,j) + " ");
//            }
//            System.out.println();
//        }

//        for(int i = 0; i < 8; i++) {
//            for (int j = 0; j < 8; j++) {
//                System.out.print(w.get(i,j) + " ");
//            }
//            System.out.println();
//        }
    }

    public BufferedImage extractWatermark() {

        Matrix watermark = Watermark.extractWatermark(modifiedY);

        BufferedImage bfImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        for(int h = 0; h < imageHeight; h++) {
            for(int w = 0; w < imageWidth; w++) {
                bfImage.setRGB(w,h,(new Color(convertColor(watermark.get(h,w)), convertColor(watermark.get(h,w)), convertColor(watermark.get(h,w))).getRGB()));
            }
        }
        return bfImage;

    }

    public void convertToRBG() {
        Object[] objects = ColorTransform.convertModifiedYcBcRtoRGB(modifiedY,modifiedCr,modifiedCb);
        modifiedRed = (int[][]) objects[0];
        modifiedGreen = (int[][]) objects[1];
        modifiedBlue = (int[][]) objects[2];
    }

    public void downSample(SamplingTypes samplingType) {
        modifiedY = originalY;
        //modifiedY = modifiedY;
        modifiedCr = Sampling.sampleDown(originalCr, samplingType);
        modifiedCb = Sampling.sampleDown(originalCb, samplingType);
    }

    public void upSample(SamplingTypes samplingType) {
        //this.modifiedY = this.modifiedY;
        modifiedCr = Sampling.sampleUp(modifiedCr, samplingType);
        modifiedCb = Sampling.sampleUp(modifiedCb, samplingType);
    }

    public double[] countQuality(QualityTypes qualityType) {
        double [] params = new double[4];
        switch (qualityType) {
            case RED:
                params[0] = Quality.countMSE(Quality.convertIntToDouble(originalRed), Quality.convertIntToDouble(modifiedRed));
                params[1] = Quality.countMAE(Quality.convertIntToDouble(originalRed), Quality.convertIntToDouble(modifiedRed));
                params[2] = Quality.countPSNR(params[0]);
                params[3] = Quality.countSAE(Quality.convertIntToDouble(originalRed), Quality.convertIntToDouble(modifiedRed));
                break;
            case GREEN:
                params[0] = Quality.countMSE(Quality.convertIntToDouble(originalGreen), Quality.convertIntToDouble(modifiedGreen));
                params[1] = Quality.countMAE(Quality.convertIntToDouble(originalGreen), Quality.convertIntToDouble(modifiedGreen));
                params[2] = Quality.countPSNR(params[0]);
                params[3] = Quality.countSAE(Quality.convertIntToDouble(originalGreen), Quality.convertIntToDouble(modifiedGreen));
                break;
            case BLUE:
                params[0] = Quality.countMSE(Quality.convertIntToDouble(originalBlue), Quality.convertIntToDouble(modifiedBlue));
                params[1] = Quality.countMAE(Quality.convertIntToDouble(originalBlue), Quality.convertIntToDouble(modifiedBlue));
                params[2] = Quality.countPSNR(params[0]);
                params[3] = Quality.countSAE(Quality.convertIntToDouble(originalBlue), Quality.convertIntToDouble(modifiedBlue));
                break;
            case Y:
                params[0] = Quality.countMSE(originalY.getArray(), modifiedY.getArray());
                params[1] = Quality.countMAE(originalY.getArray(), modifiedY.getArray());
                params[2] = Quality.countPSNR(params[0]);
                params[3] = Quality.countSAE(originalY.getArray(), modifiedY.getArray());
                break;
            case cR:
                params[0] = Quality.countMSE(originalCr.getArray(), modifiedCr.getArray());
                params[1] = Quality.countMAE(originalCr.getArray(), modifiedCr.getArray());
                params[2] = Quality.countPSNR(params[0]);
                params[3] = Quality.countSAE(originalCr.getArray(), modifiedCr.getArray());
                break;
            case cB:
                params[0] = Quality.countMSE(originalCb.getArray(), modifiedCb.getArray());
                params[1] = Quality.countMAE(originalCb.getArray(), modifiedCb.getArray());
                params[2] = Quality.countPSNR(params[0]);
                params[3] = Quality.countSAE(originalCb.getArray(), modifiedCb.getArray());
                break;
            case R_G_B:
                double [] red = countQuality(QualityTypes.RED);
                double [] green = countQuality(QualityTypes.GREEN);
                double [] blue = countQuality(QualityTypes.BLUE);
                params[0] = (red[0] + green[0] + blue[0]) / 3.0;
                params[1] = (red[1] + green[1] + blue[1]) / 3.0;
                params[2] = (red[2] + green[2] + blue[2]) / 3.0;
                params[3] = (red[3] + green[3] + blue[3]) / 3.0;
                break;
            case YcRcB:
                double [] y = countQuality(QualityTypes.Y);
                double [] cR = countQuality(QualityTypes.cR);
                double [] cB = countQuality(QualityTypes.cB);
                params[0] = (y[0] + cR[0] + cB[0]) / 3.0;
                params[1] = (y[1] + cR[1] + cB[1]) / 3.0;
                params[2] = (y[2] + cR[2] + cB[2]) / 3.0;
                params[3] = (y[3] + cR[3] + cB[3]) / 3.0;
                break;
            default:
                System.out.println("Wrong argument: " + countQuality(null).toString());
                break;
        }
        return params;
    }

    public double[] countSSIM(YcRcBTypes ycRcBType) {
        double[] params = new double[2];

        int blockSize = 8;

        Matrix original = null, modified = null;

        List<Double> ssim = new ArrayList<>();

        switch (ycRcBType) {
            case Y:
                params[0] = Quality.countSSIM(originalY, modifiedY);
                original = originalY.copy();
                modified = modifiedY.copy();
                break;
            case cR:
                params[0] = Quality.countSSIM(originalCr, modifiedCr);
                original = originalCr.copy();
                modified = modifiedCr.copy();
                break;
            case cB:
                params[0] = Quality.countSSIM(originalCb, modifiedCb);
                original = originalCb.copy();
                modified = modifiedCb.copy();
                break;
            default: break;
        }

        for (int i = 0; i < original.getRowDimension(); i += blockSize) {
            for (int j = 0; j < original.getColumnDimension(); j += blockSize) {
                Matrix subMatrixOriginal = original.getMatrix(i, i+blockSize-1, j, j+blockSize-1);
                Matrix subMatrixModified = modified.getMatrix(i, i+blockSize-1, j, j+blockSize-1);
                ssim.add(Quality.countSSIM(subMatrixOriginal, subMatrixModified));
            }
        }
        double sum = ssim.stream().mapToDouble(a -> a).sum();
        params[1] = (1.0/ssim.size())*sum;
        return params;
    }

    public void transform(TransformTypes transformType, int size) {
        modifiedCr = Transform.transform(modifiedCr, transformType, size);
        modifiedY = Transform.transform(modifiedY, transformType, size);
        modifiedCb = Transform.transform(modifiedCb, transformType, size);
    }

    public void inverseTransform(TransformTypes transformType, int size) {
        modifiedY = Transform.inverseTransform(modifiedY, transformType, size);
        modifiedCr = Transform.inverseTransform(modifiedCr, transformType, size);
        modifiedCb = Transform.inverseTransform(modifiedCb, transformType, size);
    }

    public void quantize(int blockSize, double quality) {
        modifiedY = Quantization.quantize(modifiedY, blockSize, quality, true);
        modifiedCr = Quantization.quantize(modifiedCr, blockSize, quality, false);
        modifiedCb = Quantization.quantize(modifiedCb, blockSize, quality, false);
    }

    public void inverseQuantize(int blockSize, double quality) {
        modifiedY = Quantization.inverseQuantize(modifiedY, blockSize, quality, true);
        modifiedCr = Quantization.inverseQuantize(modifiedCr, blockSize, quality, false);
        modifiedCb = Quantization.inverseQuantize(modifiedCb, blockSize, quality, false);
    }

//    public void generateExcelFile() {
//        try
//        {
//            String filename = "Balance.xls";
//            HSSFWorkbook workbook = new HSSFWorkbook();
//            HSSFSheet sheet = workbook.createSheet("January");
//
//            HSSFRow rowhead = sheet.createRow((short)0);
//            rowhead.createCell(0).setCellValue("S.No.");
//            rowhead.createCell(1).setCellValue("Customer Name");
//            rowhead.createCell(2).setCellValue("Account Number");
//            rowhead.createCell(3).setCellValue("e-mail");
//            rowhead.createCell(4).setCellValue("Balance");
//
//            HSSFRow row = sheet.createRow((short)1);
//            row.createCell(0).setCellValue("1");
//            row.createCell(1).setCellValue("John William");
//            row.createCell(2).setCellValue("9999999");
//            row.createCell(3).setCellValue("william.john@gmail.com");
//            row.createCell(4).setCellValue("700000.00");
//
//            HSSFRow row1 = sheet.createRow((short)2);
//            row1.createCell(0).setCellValue("2");
//            row1.createCell(1).setCellValue("Mathew Parker");
//            row1.createCell(2).setCellValue("22222222");
//            row1.createCell(3).setCellValue("parker.mathew@gmail.com");
//            row1.createCell(4).setCellValue("200000.00");
//
//            FileOutputStream fileOut = new FileOutputStream(filename);
//            workbook.write(fileOut);
//            fileOut.close();
//            workbook.close();
//            System.out.println("Excel file has been generated successfully.");
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
}