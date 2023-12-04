package models;

import services.Adder;
import services.AppConfig;

import java.util.Arrays;
import java.util.Random;

public class Vector {
    public int length;
    private double[] data;

    // Конструктор для створення вектора заданої довжини
    public Vector(int length) {
        this.length = length;
        this.data = new double[length];
    }

    // Конструктор для створення вектора на основі масиву даних
    public Vector(double[] data) {
        this.data = data;
        this.length = data.length;
    }

    // Отримання значення вектора за індексом
    public double get(int index) {
        return this.data[index];
    }

    // Встановлення значення вектора за індексом
    public void set(int index, double value) {
        this.data[index] = value;
    }

    // Отримання значення вектора за індексом
    public double max() {
        double max = data[0];

        for (int i = 1; i < data.length; i++) {
            if (data[i] > max) {
                max = data[i];
            }
        }

        return max;
    }

    // Додавання векторів
    public Vector add(Vector otherVector) {
        if (this.length != otherVector.length) {
            throw new IllegalArgumentException("Vector addition is not allowed for vectors of different lengths.");
        }

        Vector result = new Vector(this.length);

        for (int i = 0; i < this.length; i++) {
            result.set(i, this.get(i) + otherVector.get(i));
        }

        return result;
    }

    // Множення вектора на скаляр
    public Vector multiply(double scalar) {
        Vector result = new Vector(this.length);

        for (int i = 0; i < this.length; i++) {
            result.set(i, this.get(i) * scalar);
        }

        return result;
    }

    // Скалярний добуток векторів
    public double multiply(Vector otherVector) {
        if (this.length != otherVector.length) {
            throw new IllegalArgumentException("Vector multiplying is not allowed for vectors of different lengths.");
        }

        Adder adder = new Adder();

        for (int i = 0; i < this.length; i++) {
            adder.add(this.get(i) * otherVector.get(i));
        }

        return adder.getSum();
    }

    // Множення вектора на матрицю
    public Vector multiply(Matrix matrix) {
        if (this.length != matrix.getRows()) {
            throw new IllegalArgumentException("The number of items in the row vector must be equal to the number of rows in the matrix.");
        }

        Vector result = new Vector(this.length);

        for (int i = 0; i < this.length; i++) {
            Adder adder = new Adder();

            for (int j = 0; j < this.length; j++) {
                adder.add(this.get(j) * matrix.get(j, i));
            }

            result.set(i, adder.getSum());
        }

        return result;
    }

    public static Vector generate() {
        int dimension = AppConfig.getDimension();
        double minValue = AppConfig.getMinValue();
        double maxValue = AppConfig.getMaxValue();

        Vector result = new Vector(dimension);

        Random random = new Random();

        for (int i = 0; i < dimension; i++) {
            double value = minValue + (maxValue - minValue) * random.nextDouble();

            result.set(i, value);
        }

        return result;
    }

    // Перевизначений метод toString() для зручного виведення вектора
    @Override
    public String toString() {
        return Arrays.toString(data);
    }
}
