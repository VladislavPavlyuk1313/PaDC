package services;

public class Adder {
    double sum = 0;
    double c = 0;

    public Adder(){}

    public void add(double number) {
        if (AppConfig.isKahanAlgorithmMustBeUsed()) {
            double y = number - this.c;
            double t = sum + y;
            c = (t - sum) - y;
            sum = t;
        } else {
            sum += number;
        }
    }

    public double getSum() {
        return sum;
    }
}
