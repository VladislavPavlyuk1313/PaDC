import models.Matrix;
import models.Vector;
import services.AppConfig;
import services.DataService;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Lab6 {
    static Vector vectorB, vectorC;
    static Matrix matrixD, matrixT, matrixZ, matrixB;
    static double a;
    static long generalStartTime, matrixStartTime, vectorStartTime, generalEndTime, matrixEndTime, vectorEndTime,
            generalComputationTime, matrixComputationTime, vectorComputationTime;
    static BlockingQueue<Matrix> secondMatrixOperandResultQueue = new ArrayBlockingQueue<Matrix>(1);
    static BlockingQueue<Vector> secondVectorOperandResultQueue = new ArrayBlockingQueue<Vector>(1);
    static BlockingQueue<Matrix> matrixAQueue = new ArrayBlockingQueue<Matrix>(1);
    static BlockingQueue<Vector> vectorEQueue = new ArrayBlockingQueue<Vector>(1);
    static Thread matrixThread1 = new Thread(new Runnable() {
        @Override
        public void run() {

            matrixStartTime = System.currentTimeMillis();
            matrixThread2.start();

            Matrix firstMatrixOperandResult = matrixD
                    .multiply(
                            vectorB
                                    .add(vectorC)
                                    .max()
                    )
                    .multiply(matrixT);

            try {
                Matrix secondMatrixOperandResult = secondMatrixOperandResultQueue.take();
                matrixAQueue.put(firstMatrixOperandResult.add(secondMatrixOperandResult));

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
            try {
                secondMatrixOperandResultQueue.put(matrixZ.multiply(matrixB));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

    static Thread vectorThread1 = new Thread(new Runnable() {
        @Override
        public void run() {
            vectorStartTime = System.currentTimeMillis();

            vectorThread2.start();

            Vector firstVectorOperandResult = vectorB.multiply(matrixD);

            try {
                Vector secondVectorOperandResult = secondVectorOperandResultQueue.take();
                vectorEQueue.put(firstVectorOperandResult.add(secondVectorOperandResult));

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
            try {
                secondVectorOperandResultQueue.put(vectorC.multiply(matrixT).multiply(a));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
            Matrix matrixA = matrixAQueue.take();
            Vector vectorE = vectorEQueue.take();

            generalEndTime = System.currentTimeMillis();
            generalComputationTime = generalEndTime - generalStartTime;

            DataService.writeResults(6, matrixA, vectorE, generalComputationTime, matrixComputationTime, vectorComputationTime);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
