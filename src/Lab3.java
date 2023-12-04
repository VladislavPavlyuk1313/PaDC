import models.Matrix;
import models.Vector;
import services.AppConfig;
import services.DataService;

import java.util.concurrent.*;
import java.io.IOException;

public class Lab3 {
    static Vector vectorB, vectorC;
    static Matrix matrixD, matrixT, matrixZ, matrixB;
    static double a;
    static long generalStartTime, matrixStartTime, vectorStartTime, generalEndTime, matrixEndTime, vectorEndTime,
            generalComputationTime, matrixComputationTime, vectorComputationTime;

    static ExecutorService generalExecutor = Executors.newFixedThreadPool(2);
    static ExecutorService matrixExecutor = Executors.newFixedThreadPool(2);
    static ExecutorService vectorExecutor = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        vectorB = DataService.readVectorFromCsv(AppConfig.getVectorBFilePath());
        vectorC = DataService.readVectorFromCsv(AppConfig.getVectorCFilePath());

        matrixD = DataService.readMatrixFromCsv(AppConfig.getMatrixDFilePath());
        matrixT = DataService.readMatrixFromCsv(AppConfig.getMatrixTFilePath());
        matrixZ = DataService.readMatrixFromCsv(AppConfig.getMatrixZFilePath());
        matrixB = DataService.readMatrixFromCsv(AppConfig.getMatrixBFilePath());

        a = AppConfig.getA();

        generalStartTime = System.currentTimeMillis();

        Future<Matrix> matrixA = generalExecutor.submit(() -> {
            matrixStartTime = System.currentTimeMillis();

            Future<Matrix> firstMatrixOperandResult = matrixExecutor.submit(() -> {
                return matrixD
                        .multiply(
                                vectorB
                                        .add(vectorC)
                                        .max()
                        )
                        .multiply(matrixT);
            });

            Future<Matrix> secondMatrixOperandResult = matrixExecutor.submit(() -> {
                return matrixZ.multiply(matrixB);
            });


            matrixExecutor.shutdown();
            matrixExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            matrixEndTime = System.currentTimeMillis();
            matrixComputationTime = matrixEndTime - matrixStartTime;

            return firstMatrixOperandResult.get().add(secondMatrixOperandResult.get());
        });

        Future<Vector> vectorE = generalExecutor.submit(() -> {
            vectorStartTime = System.currentTimeMillis();

            Future<Vector> firstVectorOperandResult = vectorExecutor.submit(() -> {
                return vectorB.multiply(matrixD);
            });

            Future<Vector> secondVectorOperandResult = vectorExecutor.submit(() -> {
                return vectorC.multiply(matrixT).multiply(a);
            });


            vectorExecutor.shutdown();
            vectorExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            vectorEndTime = System.currentTimeMillis();
            vectorComputationTime = vectorEndTime - vectorStartTime;

            return firstVectorOperandResult.get().add(secondVectorOperandResult.get());
        });

        generalExecutor.shutdown();
        generalExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        generalEndTime = System.currentTimeMillis();
        generalComputationTime = generalEndTime - generalStartTime;

        DataService.writeResults(
                3, matrixA.get(),
                vectorE.get(),
                generalComputationTime,
                matrixComputationTime,
                vectorComputationTime
        );
    }
}
