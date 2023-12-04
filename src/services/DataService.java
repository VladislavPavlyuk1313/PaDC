package services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.Vector;
import models.Matrix;

public class DataService {

    // Запис вектора у CSV файл
    public static void writeToCsv(String filePath, Vector vector) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < vector.length; i++) {
                writer.write(String.valueOf(vector.get(i)));
                writer.newLine();
            }
        }
    }


    // Зчитування вектора з CSV файлу
    public static Vector readVectorFromCsv(String filePath) throws IOException {
        List<Double> values = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                values.add(Double.parseDouble(line.trim()));
            }
        }

        double[] data = new double[values.size()];
        for (int i = 0; i < values.size(); i++) {
            data[i] = values.get(i);
        }

        return new Vector(data);
    }

    // Запис матриці у CSV файл
    public static void writeToCsv(String filePath, Matrix matrix) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < matrix.getRows(); i++) {
                StringBuilder line = new StringBuilder();
                for (int j = 0; j < matrix.getColumns(); j++) {
                    line.append(matrix.get(i, j));
                    if (j < matrix.getColumns() - 1) {
                        line.append(",");
                    }
                }
                writer.write(line.toString());
                writer.newLine();
            }
        }
    }

    // Зчитування матриці з CSV файлу
    public static Matrix readMatrixFromCsv(String filePath) throws IOException {
        List<double[]> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                double[] row = new double[values.length];
                for (int i = 0; i < values.length; i++) {
                    row[i] = Double.parseDouble(values[i].trim());
                }
                rows.add(row);
            }
        }

        double[][] data = new double[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            data[i] = rows.get(i);
        }

        return new Matrix(data);
    }

    public static void writeResults(
            int labNum, Matrix matrix,
            Vector vector,
            long generalComputationTime,
            long matrixComputationTime,
            long vectorComputationTime
    ) throws IOException {
        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(
                                     AppConfig.getResultFilePath() + "lab-" + Integer.toString(labNum)
                             )
                     )
        ) {

            writer.write("General computation time = " + Long.toString(generalComputationTime) + "ms");
            writer.newLine();
            writer.write("Matrix computation time = " + Long.toString(matrixComputationTime) + "ms");
            writer.newLine();
            writer.write("Vector computation time = " + Long.toString(vectorComputationTime) + "ms");
            writer.newLine();
            writer.newLine();


            writer.write("MA = ");
            writer.newLine();

            for (int i = 0; i < matrix.getRows(); i++) {
                StringBuilder line = new StringBuilder();
                for (int j = 0; j < matrix.getColumns(); j++) {
                    line.append(matrix.get(i, j));
                    if (j < matrix.getColumns() - 1) {
                        line.append(",");
                    }
                }
                writer.write(line.toString());
                writer.newLine();
            }

            writer.newLine();
            writer.write("E = ");
            writer.newLine();

            for (int i = 0; i < vector.length; i++) {
                writer.write(String.valueOf(vector.get(i)));
                writer.newLine();
            }
        }

        DataService.writeResultsToConsole(matrix, vector, generalComputationTime, matrixComputationTime, vectorComputationTime);
    }


    public static void writeResultsToConsole(
            Matrix matrix,
            Vector vector,
            long generalComputationTime,
            long matrixComputationTime,
            long vectorComputationTime
    ) throws IOException {
        System.out.println("General computation time = " + Long.toString(generalComputationTime) + "ms");
        System.out.println("Matrix computation time = " + Long.toString(matrixComputationTime) + "ms");
        System.out.println("Vector computation time = " + Long.toString(vectorComputationTime) + "ms");

//        System.out.println();
//
//
//        System.out.println("MA = " + matrix.toString());
//        System.out.println("E = " + vector.toString());
    }
}
