package Jpeg;

import Enums.SamplingTypes;
import Jama.Matrix;

public class Sampling {

    public static Matrix sampleDown(Matrix inputMatrix, SamplingTypes samplingType) {
        Matrix sampledMatrix = null;
        switch (samplingType) {
            case S_4_4_4:
                sampledMatrix = inputMatrix;
                break;
            case S_4_2_2:
                sampledMatrix = Sampling.sampleDown(inputMatrix);
                break;
            case S_4_2_0:
                sampledMatrix = Sampling.sampleDown(Sampling.sampleDown(inputMatrix).transpose()).transpose();
                break;
            case S_4_1_1:
                sampledMatrix = Sampling.sampleDown(Sampling.sampleDown(inputMatrix));
                break;
            default: System.out.println("Worng argument downSample");
                break;
        }
        return sampledMatrix;

    }

    public static Matrix sampleUp(Matrix inputMatrix, SamplingTypes samplingType) {
        Matrix sampledMatrix = null;
        switch (samplingType) {
            case S_4_4_4:
                sampledMatrix = inputMatrix;
                break;
            case S_4_2_2:
                sampledMatrix = Sampling.sampleUp(inputMatrix);
                break;
            case S_4_2_0:
                sampledMatrix = Sampling.sampleUp(Sampling.sampleUp(inputMatrix).transpose()).transpose();
                break;
            case S_4_1_1:
                sampledMatrix = Sampling.sampleUp(Sampling.sampleUp(inputMatrix));
                break;
            default: throw new IllegalArgumentException();
        }
        return sampledMatrix;

    }

    private static Matrix sampleDown(Matrix matrix) {

        long startTime = System.nanoTime();


        int width = (int) Math.ceil(matrix.getColumnDimension() >> 1);
        int height = matrix.getRowDimension();

        int[] indexesW = new int[width];
        int[] indexesH = new int[height];

        for (int i = 0; i < width; i++) {
            indexesW[i] = i*2;
        }
        for (int i = 0; i < height; i++) {
            indexesH[i] = i;
        }

        long stoptime = System.nanoTime();

        System.out.println("Down sample time: " + (stoptime-startTime));

        return matrix.getMatrix(indexesH,indexesW);
    }

    private static Matrix sampleUp(Matrix matrix) {
        long startTime = System.nanoTime();

        Matrix upSampled = new Matrix(matrix.getRowDimension(), matrix.getColumnDimension()*2);
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                for (int h = 0; h < 2; h++) {
                    upSampled.set(i, j+j+h, matrix.get(i,j));
                }
            }
        }

//        int height = matrix.getRowDimension();
//        int width = matrix.getColumnDimension();
//        Matrix upSampled = new Matrix(height, width * 2);
//        for (int i = 0; i < width; i++) {
//            Matrix subMatrix = matrix.getMatrix(0, height - 1, i, i);
//            upSampled.setMatrix(0, height - 1, i * 2, i * 2, subMatrix);
//            upSampled.setMatrix(0, height - 1, (i * 2) + 1, (i * 2) + 1, subMatrix);
//        }

        long stoptime = System.nanoTime();

        System.out.println("Up sample time: " + (stoptime-startTime));

        return upSampled;



    }

}