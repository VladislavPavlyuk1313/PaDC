package models;

import services.Adder;
import services.AppConfig;

import java.util.Arrays;
import java.util.Random;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class Matrix {
    private int rows;
    private int columns;
    private double[][] data;

    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.data = new double[rows][columns];
    }

    public Matrix(double[][] data) {
        this.data = data;
        rows = data.length;
        columns = data[0].length;
    }

    // Метод для встановлення значення елемента матриці
    public void set(int row, int col, double value) {
        data[row][col] = value;
    }

    // Метод для отримання значення елемента матриці
    public double get(int row, int col) {
        return data[row][col];
    }

    // Метод для отримання кількості рядків
    public int getRows() {
        return rows;
    }

    // Метод для отримання кількості стовпців
    public int getColumns() {
        return columns;
    }

    public Matrix add(Matrix matrix) {
        if (this.getRows() != matrix.getRows() || this.getColumns() != matrix.getColumns()) {
            throw new IllegalArgumentException("The number of columns and rows in the first matrix must be equal to the second matrix.");
        }

        Matrix result = new Matrix(this.getRows(), this.getColumns());

        for (int i = 0; i < result.getRows(); i++) {
            for (int j = 0; j < result.getColumns(); j++) {
                result.set(i, j, this.get(i, j) + matrix.get(i, j));
            }
        }

        return result;
    }

    // Метод для множення матриці на іншу матрицю
    public Matrix multiply(Matrix matrix) {
        if (this.getColumns() != matrix.getRows()) {
            throw new IllegalArgumentException("The number of columns in the first matrix must be equal to the number of rows in the second matrix.");
        }

        Matrix result = new Matrix(this.getRows(), matrix.getColumns());

        for (int i = 0; i < result.getRows(); i++) {
//            System.out.println(Thread.currentThread().getName());
            for (int j = 0; j < result.getColumns(); j++) {
                Adder adder = new Adder();

                for (int k = 0; k < result.columns; k++) {
                    adder.add(this.get(i, k) * matrix.get(k, j));
                }

                result.set(i, j, adder.getSum());
            }
        }

        return result;
    }

    // Метод для множення матриці на скаляр
    public Matrix multiply(double scalar) {
        Matrix result = new Matrix(this.rows, this.columns);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                result.set(i, j, this.data[i][j] * scalar);
            }
        }
        return result;
    }

    // Метод для множення матриці на вектор
    public Vector multiply(Vector vector) {
        if (vector.length != this.getColumns()) {
            throw new IllegalArgumentException("The number of columns in the matrix must be equal to the number of items in the column vector.");
        }

        Vector result = new Vector(vector.length);

        for (int i = 0; i < this.rows; i++) {
            Adder adder = new Adder();

            for (int j = 0; j < this.columns; j++) {
                adder.add(vector.get(j) * this.get(i, j));
            }

            result.set(i, adder.getSum());
        }

        return result;
    }

    public Matrix multiplyUsingFork(Matrix matrix) {
        if (this.getColumns() != matrix.getRows()) {
            throw new IllegalArgumentException("The number of columns in the first matrix must be equal to the number of rows in the second matrix.");
        }

        int rowsA = this.data.length;
        int colsB = matrix.data[0].length;

        double[][] resultData = new double[rowsA][colsB];

        // Створення пулу потоків ForkJoinPool
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        // Створення кореневої задачі
        MatrixMultiplyTask multiplyTask = new MatrixMultiplyTask(this.data, matrix.data, resultData, 0, rowsA, 0, colsB);

        // Запуск кореневої задачі в пулі потоків
        forkJoinPool.invoke(multiplyTask);

        // Завершення пулу потоків
        forkJoinPool.shutdown();

        // Повернення результату у вигляді нового об'єкту Matrix
        return new Matrix(resultData);
    }

    public static Matrix generate() {
        int dimension = AppConfig.getDimension();
        double minValue = AppConfig.getMinValue();
        double maxValue = AppConfig.getMaxValue();

        Matrix result = new Matrix(dimension, dimension);

        Random random = new Random();

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                double value = minValue + (maxValue - minValue) * random.nextDouble();

                result.set(i, j, value);
            }
        }

        return result;
    }

    private static class MatrixMultiplyTask extends RecursiveTask<Void> {
        private double[][] matrixA;
        private double[][] matrixB;
        private double[][] resultMatrix;
        private int startRow;
        private int endRow;
        private int startCol;
        private int endCol;

        public MatrixMultiplyTask(double[][] matrixA, double[][] matrixB, double[][] resultMatrix,
                                  int startRow, int endRow, int startCol, int endCol) {
            this.matrixA = matrixA;
            this.matrixB = matrixB;
            this.resultMatrix = resultMatrix;
            this.startRow = startRow;
            this.endRow = endRow;
            this.startCol = startCol;
            this.endCol = endCol;
        }

        @Override
        protected Void compute() {
            if (endRow - startRow <= 1 && endCol - startCol <= 1) {
                Adder adder = new Adder();

                for (int i = 0; i < matrixA.length; i++) {
                    adder.add(matrixA[startRow][i] * matrixB[i][startCol]);
                }
                // Простий випадок - викликаємо пряме множення для невеликих підматриць
                resultMatrix[startRow][startCol] = adder.getSum();
            } else {
                // Розділення завдання на підзадачі
                int midRow = (startRow + endRow) / 2;
                int midCol = (startCol + endCol) / 2;

                invokeAll(
                        new MatrixMultiplyTask(matrixA, matrixB, resultMatrix, startRow, midRow, startCol, midCol),
                        new MatrixMultiplyTask(matrixA, matrixB, resultMatrix, startRow, midRow, midCol, endCol),
                        new MatrixMultiplyTask(matrixA, matrixB, resultMatrix, midRow, endRow, startCol, midCol),
                        new MatrixMultiplyTask(matrixA, matrixB, resultMatrix, midRow, endRow, midCol, endCol)
                );
            }
            return null;
        }
    }

    // Перевизначений метод toString() для зручного виведення матриці
    @Override
    public String toString() {
        return Arrays.deepToString(data);
    }
}
