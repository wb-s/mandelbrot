import java.awt.Color;

public class ComputeThread implements Runnable {
    
	private  int n;
    private  double x0;
    private  double y0;
    private  int xLow;
    private  int xHigh;
    private  int yLow;
    private  int yHigh;
    private  int max;
    private  double scaleFactor;
    private  double zoom;
    private  Color[][] colors = new Color[1000][1000];
    private CalcListener listener;
    
    public ComputeThread(int n, double zoom, double x0, double y0, int xLow, int xHigh, int yLow, int yHigh, int max, double scaleFactor, Color[][] colors, CalcListener l) { //, CalcListener l
        this.n = n;
        this.zoom = zoom;
        this.x0 = x0;
        this.y0 = y0;
        this.xLow = xLow;
        this.xHigh = xHigh;
        this.yLow = yLow;
        this.yHigh = yHigh;
        this.max = max;
        this.scaleFactor = scaleFactor;
        this.colors = colors;
        this.listener = l;
    }

    @Override
    public void run() {
        for (int i = xLow; i < xHigh; i++) {
            for (int j = yLow; j < yHigh; j++) {
                double a = x0 + (i * scaleFactor);
                double b = y0 - (j * scaleFactor);
                Complex z0 = new Complex(a, b);
                int gray = max - mand(z0, max);
                Color color = new Color(gray, gray, gray);
                colors[j][i] = color;
            }
        }
        System.out.print("thread");
        listener.calcDoneEvent();
    }

    public  int mand(Complex z0, int max) {
        Complex z = z0;
        for (int t = 0; t < max; t++) {
            if (z.abs() > zoom) {
                return t;
            }
            z = Complex.ad(Complex.mult(z, z), z0);
        }
        return max;
    }
    
}
