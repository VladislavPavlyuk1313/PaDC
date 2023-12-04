import models.Matrix;
import models.Vector;
import services.AppConfig;
import services.DataService;

import java.io.IOException;

public class Lab0 {
    public static void main(String[] args) throws IOException {
        Vector vectorB = DataService.readVectorFromCsv(AppConfig.getVectorBFilePath());

        Vector vectorC = DataService.readVectorFromCsv(AppConfig.getVectorCFilePath());

        Matrix matrixD = DataService.readMatrixFromCsv(AppConfig.getMatrixDFilePath());

        Matrix matrixT = DataService.readMatrixFromCsv(AppConfig.getMatrixTFilePath());

        Matrix matrixZ = DataService.readMatrixFromCsv(AppConfig.getMatrixZFilePath());

        Matrix matrixB = DataService.readMatrixFromCsv(AppConfig.getMatrixBFilePath());

        double a = AppConfig.getA();


        long generalStartTime = System.currentTimeMillis();
        long matrixStartTime = System.currentTimeMillis();

        Matrix matrixA = matrixD
                            .multiply(
                                vectorB
                                    .add(vectorC)
                                    .max()
                            )
                            .multiply(matrixT)
                            .add(
                                matrixZ
                                    .multiply(matrixB));

        long vectorStartTime = System.currentTimeMillis();
        long matrixEndTime = System.currentTimeMillis();

        Vector vectorE = vectorB
                            .multiply(matrixD)
                            .add(
                                vectorC
                                    .multiply(matrixT)
                                    .multiply(a)
                            );

        long vectorEndTime = System.currentTimeMillis();
        long generalEndTime = System.currentTimeMillis();

        long generalComputationTime = generalEndTime - generalStartTime;
        long matrixComputationTime = matrixEndTime - matrixStartTime;
        long vectorComputationTime = vectorEndTime - vectorStartTime;

        DataService.writeResults(0, matrixA, vectorE, generalComputationTime, matrixComputationTime, vectorComputationTime);


    }
}
