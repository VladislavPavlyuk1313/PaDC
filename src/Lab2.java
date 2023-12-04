import models.Matrix;
import models.Vector;
import services.AppConfig;
import services.DataService;

import java.util.concurrent.CountDownLatch;
import java.io.IOException;

public class Lab2 {
    static Vector vectorB, vectorC, vectorE, firstVectorOperandResult, secondVectorOperandResult;
    static Matrix matrixD, matrixT, matrixZ, matrixB, matrixA, firstMatrixOperandResult, secondMatrixOperandResult;
    static double a;
    static long generalStartTime, matrixStartTime, vectorStartTime, generalEndTime, matrixEndTime, vectorEndTime,
            generalComputationTime, matrixComputationTime, vectorComputationTime;
    static CountDownLatch matrixLatch = new CountDownLatch(1);
    static CountDownLatch vectorLatch = new CountDownLatch(1);
    static CountDownLatch generalLatch = new CountDownLatch(2);

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
                matrixLatch.await();
                matrixA = firstMatrixOperandResult.add(secondMatrixOperandResult);

                matrixEndTime = System.currentTimeMillis();
                matrixComputationTime = matrixEndTime - matrixStartTime;

                generalLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

    static Thread matrixThread2 = new Thread(new Runnable() {
        @Override
        public void run() {
            secondMatrixOperandResult = matrixZ.multiply(matrixB);
            matrixLatch.countDown();
        }
    });

    static Thread vectorThread1 = new Thread(new Runnable() {
        @Override
        public void run() {
            vectorStartTime = System.currentTimeMillis();

            vectorThread2.start();

            firstVectorOperandResult = vectorB.multiply(matrixD);

            try {
                vectorLatch.await();
                vectorE = firstVectorOperandResult.add(secondVectorOperandResult);

                vectorEndTime = System.currentTimeMillis();
                vectorComputationTime = vectorEndTime - vectorStartTime;

                generalLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    });

    static Thread vectorThread2 = new Thread(new Runnable() {
        @Override
        public void run() {
            secondVectorOperandResult = vectorC.multiply(matrixT).multiply(a);
            vectorLatch.countDown();
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
            generalLatch.await();

            generalEndTime = System.currentTimeMillis();
            generalComputationTime = generalEndTime - generalStartTime;

            DataService.writeResults(2, matrixA, vectorE, generalComputationTime, matrixComputationTime, vectorComputationTime);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
