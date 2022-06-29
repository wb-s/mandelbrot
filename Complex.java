import java.lang.Math;

public class Complex {
	double real;
	double imag;
	public Complex(double real, double imag) {
		this.real = real;
		this.imag = imag;
	}
	public static Complex ad(Complex num1, Complex num2) {
		Complex num = new Complex(0.0, 0.0);
		num.real = num1.real + num2.real;
		num.imag = num1.imag + num2.imag;
		return(num);
	}
	
    public static Complex mult(Complex num1, Complex num2) {
    	Complex num = new Complex(0.0, 0.0);
        num.real = num1.real * num2.real - num1.imag * num2.imag;
        num.imag = num1.real * num2.imag + num1.imag * num2.real;
        return(num);
    }
	
    public double abs() {
		return Math.hypot(real, imag);
    }
}
