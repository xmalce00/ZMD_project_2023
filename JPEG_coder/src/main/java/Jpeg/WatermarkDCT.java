package Jpeg;

import Jama.Matrix;

public class WatermarkDCT {

    public static void addWatermark(Matrix input, int blockSize) {



        Matrix output = new Matrix(input.getRowDimension(), input.getColumnDimension());
        for (int i = 0; i < input.getRowDimension(); i += blockSize) {
            for (int j = 0; j < input.getColumnDimension(); j += blockSize) {
                Matrix subMatrix = input.getMatrix(i, i+blockSize-1, j, j+blockSize-1);


                //output.setMatrix(i, i+blockSize-1, j, j+blockSize-1, transformMatrix.times(subMatrix).times(transformMatrix.transpose()));
            }
        }
    }

    public static Matrix generate(Matrix input, int blockSize) {

        Matrix coef = new Matrix(blockSize, blockSize);

        for (int i = 0; i < blockSize; i++) {
            for (int j = 0; j < blockSize; j++) {
                //coef.set(i,j,);
            }
        }

        for (int k = 0; k < blockSize; k++) {
            for (int h = 0; h < blockSize; h++) {

            }
        }


        return null;
    }

    public static double calculateCoef(int u, int v) {

        double result = 0;



        return result;
    }

}
