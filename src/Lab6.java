import models.Matrix;
import models.Vector;
import services.AppConfig;
import services.DataService;

import java.io.IOException;

public class Lab6 {
    static Vector vectorB, vectorC, vectorE, firstVectorOperandResult, secondVectorOperandResult;
    static Matrix matrixD, matrixT, matrixZ, matrixB, matrixA, firstMatrixOperandResult, secondMatrixOperandResult;
    static double a;
    static long generalStartTime, matrixStartTime, vectorStartTime, generalEndTime, matrixEndTime, vectorEndTime,
            generalComputationTime, matrixComputationTime, vectorComputationTime;

    static Thread matrixThread1 = new Thread(new Runnable() {
        @Override
        public void run() {

            matrixStartTime = System.currentTimeMillis();
            matrixThread2.start();

            firstMatrixOperandResult = matrixD
                    .multiply(
                            vectorB
                                    .add(vectorC)
                                    .max()
                    )
                    .multiply(matrixT);

            try {
                matrixThread2.join();
                matrixA = firstMatrixOperandResult.add(secondMatrixOperandResult);

                matrixEndTime = System.currentTimeMillis();
                matrixComputationTime = matrixEndTime - matrixStartTime;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

    static Thread matrixThread2 = new Thread(new Runnable() {
        @Override
        public void run() {
            secondMatrixOperandResult = matrixZ.multiply(matrixB);
        }
    });

    static Thread vectorThread1 = new Thread(new Runnable() {
        @Override
        public void run() {
            vectorStartTime = System.currentTimeMillis();

            vectorThread2.start();

            firstVectorOperandResult = vectorB.multiply(matrixD);

            try {
                vectorThread2.join();
                vectorE = firstVectorOperandResult.add(secondVectorOperandResult);

                vectorEndTime = System.currentTimeMillis();
                vectorComputationTime = vectorEndTime - vectorStartTime;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    });

    static Thread vectorThread2 = new Thread(new Runnable() {
        @Override
        public void run() {
            secondVectorOperandResult = vectorC.multiply(matrixT).multiply(a);
        }
    });

    public static void main(String[] args) throws IOException {
        vectorB = DataService.readVectorFromCsv(AppConfig.getVectorBFilePath());
        vectorC = DataService.readVectorFromCsv(AppConfig.getVectorCFilePath());

        matrixD = DataService.readMatrixFromCsv(AppConfig.getMatrixDFilePath());
        matrixT = DataService.readMatrixFromCsv(AppConfig.getMatrixTFilePath());
        matrixZ = DataService.readMatrixFromCsv(AppConfig.getMatrixZFilePath());
        matrixB = DataService.readMatrixFromCsv(AppConfig.getMatrixBFilePath());

        a = AppConfig.getA();


        generalStartTime = System.currentTimeMillis();

        matrixThread1.start();

        vectorThread1.start();

        try {
            matrixThread1.join();
            vectorThread1.join();

            generalEndTime = System.currentTimeMillis();
            generalComputationTime = generalEndTime - generalStartTime;

            DataService.writeResults(6, matrixA, vectorE, generalComputationTime, matrixComputationTime, vectorComputationTime);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
