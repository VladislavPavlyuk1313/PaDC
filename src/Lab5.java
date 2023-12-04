import models.Matrix;
import models.Vector;
import services.AppConfig;
import services.DataService;

import java.util.concurrent.*;
import java.io.IOException;

public class Lab5 {
    static Vector vectorB, vectorC;
    static Matrix matrixD, matrixT, matrixZ, matrixB;
    static double a;
    static long generalStartTime, matrixStartTime, vectorStartTime, generalEndTime, matrixEndTime, vectorEndTime,
            generalComputationTime, matrixComputationTime, vectorComputationTime;


    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        vectorB = DataService.readVectorFromCsv(AppConfig.getVectorBFilePath());
        vectorC = DataService.readVectorFromCsv(AppConfig.getVectorCFilePath());

        matrixD = DataService.readMatrixFromCsv(AppConfig.getMatrixDFilePath());
        matrixT = DataService.readMatrixFromCsv(AppConfig.getMatrixTFilePath());
        matrixZ = DataService.readMatrixFromCsv(AppConfig.getMatrixZFilePath());
        matrixB = DataService.readMatrixFromCsv(AppConfig.getMatrixBFilePath());

        a = AppConfig.getA();

        ExecutorService generalExecutor = Executors.newFixedThreadPool(2);

        generalStartTime = System.currentTimeMillis();

        Callable<Matrix> getMatrixA = getMatrixCallable();
        Callable<Vector> getVectorE = getVectorCallable();

        Future<Matrix> matrixA = generalExecutor.submit(getMatrixA);

        Future<Vector> vectorE = generalExecutor.submit(getVectorE);

        generalExecutor.shutdown();
        generalExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        generalEndTime = System.currentTimeMillis();
        generalComputationTime = generalEndTime - generalStartTime;

        DataService.writeResults(
                5, matrixA.get(),
                vectorE.get(),
                generalComputationTime,
                matrixComputationTime,
                vectorComputationTime
        );
    }

    private static Callable<Matrix> getMatrixCallable() {
        Callable<Matrix> getFirstMatrixOperandResult = () -> {
            return matrixD
                    .multiply(
                            vectorB
                                    .add(vectorC)
                                    .max()
                    )
                    .multiplyUsingFork(matrixT);
        };

        Callable<Matrix> getSecondMatrixOperandResult = () -> {
            return matrixZ.multiplyUsingFork(matrixB);
        };


        return () -> {
            ExecutorService matrixExecutor = Executors.newFixedThreadPool(2);

            matrixStartTime = System.currentTimeMillis();

            Future<Matrix> firstMatrixOperandResult = matrixExecutor.submit(getFirstMatrixOperandResult);

            Future<Matrix> secondMatrixOperandResult = matrixExecutor.submit(getSecondMatrixOperandResult);


            matrixExecutor.shutdown();
            matrixExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            matrixEndTime = System.currentTimeMillis();
            matrixComputationTime = matrixEndTime - matrixStartTime;

            return firstMatrixOperandResult.get().add(secondMatrixOperandResult.get());
        };
    }

    private static Callable<Vector> getVectorCallable() {
        Callable<Vector> getFirstVectorOperandResult = () -> {
            return vectorB.multiply(matrixD);
        };

        Callable<Vector> getSecondVectorOperandResult = () -> {
            return vectorC.multiply(matrixT).multiply(a);
        };


        return () -> {
            ExecutorService vectorExecutor = Executors.newFixedThreadPool(2);

            vectorStartTime = System.currentTimeMillis();

            Future<Vector> firstVectorOperandResult = vectorExecutor.submit(getFirstVectorOperandResult);

            Future<Vector> secondVectorOperandResult = vectorExecutor.submit(getSecondVectorOperandResult);


            vectorExecutor.shutdown();
            vectorExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            vectorEndTime = System.currentTimeMillis();
            vectorComputationTime = vectorEndTime - vectorStartTime;

            return firstVectorOperandResult.get().add(secondVectorOperandResult.get());
        };
    }
}
