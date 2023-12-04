import models.Matrix;
import models.Vector;
import services.AppConfig;
import services.DataService;

import java.io.IOException;

public class GenerateData {
    public static void main(String[] args) throws IOException {
        Vector vectorB = Vector.generate();
        DataService.writeToCsv(AppConfig.getVectorBFilePath(), vectorB);

        Vector vectorC = Vector.generate();
        DataService.writeToCsv(AppConfig.getVectorCFilePath(), vectorC);

        Matrix matrixD = Matrix.generate();
        DataService.writeToCsv(AppConfig.getMatrixDFilePath(), matrixD);

        Matrix matrixT = Matrix.generate();
        DataService.writeToCsv(AppConfig.getMatrixTFilePath(), matrixT);

        Matrix matrixZ = Matrix.generate();
        DataService.writeToCsv(AppConfig.getMatrixZFilePath(), matrixZ);

        Matrix matrixB = Matrix.generate();
        DataService.writeToCsv(AppConfig.getMatrixBFilePath(), matrixB);
    }
}