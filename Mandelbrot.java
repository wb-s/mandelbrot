import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Graphics;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Stack;


public class Mandelbrot extends JPanel implements CalcListener {
    private static double size = 4.0; // complex plane is size units in width and length
    private static int n    = 1000; // window is n x n in pixels
    private static int max  = 255; // iterate through hex color values
    private static double x0 = -size/2; // set window origin
    private static double y0 = size/2; // set window origin
    private static double xC = 0; // set complex plane origin
    private static double yC = 0; // set complex plane origin
    private static double scaleFactor = size/n; // translate complex plane to pixels
    private static Stack<double[]> origins = new Stack<double[]>(); // track origins for zooming out
    private static double zoom = 2.0; // modify to alter brightness scheme on zoom
    private static Color[][] colors = new Color[n][n];
    static Mandelbrot me;

    public Mandelbrot() {
        this.me=this;
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                double[] center = new double[]{xC, yC}; // rescale
                origins.push(center);
                zoom = (zoom * 2) - (0.5 * zoom);
                xC = x0 + (scaleFactor * e.getX());
                yC = y0 - (scaleFactor * e.getY());
                size = size/2.0;
                scaleFactor = size/n;
                x0 = xC - (size/2);
                y0 = yC + (size/2);
                ComputeThread quadrant1 = new ComputeThread(n, zoom, x0, y0, 0, n/2, 0, n/2, max, scaleFactor, colors, me);
                ComputeThread quadrant2 = new ComputeThread(n, zoom, x0, y0, n/2, n, 0, n/2, max, scaleFactor, colors, me);
                ComputeThread quadrant3 = new ComputeThread(n, zoom, x0, y0, 0, n/2, n/2, n, max, scaleFactor, colors, me);
                ComputeThread quadrant4 = new ComputeThread(n, zoom, x0, y0, n/2, n, n/2, n, max, scaleFactor, colors, me);
                Thread thread1 = new Thread(quadrant1);
                Thread thread2 = new Thread(quadrant2);
                Thread thread3 = new Thread(quadrant3);
                Thread thread4 = new Thread(quadrant4);
                thread1.start();
                thread2.start();
                thread3.start();
                thread4.start();
                repaint(0, 0, getWidth(), getHeight());
            }
        });
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    double[] center = origins.pop(); // rescale
                    zoom = (zoom + (zoom / 3)) / 2;
                    xC = center[0];
                    yC = center[1];
                    size = size*2.0;
                    scaleFactor = size/n;
                    x0 = xC - (size/2);
                    y0 = yC + (size/2);
                    ComputeThread quadrant1 = new ComputeThread(n, zoom, x0, y0, 0, n/2, 0, n/2, max, scaleFactor, colors,me);
                    ComputeThread quadrant2 = new ComputeThread(n, zoom, x0, y0, n/2, n, 0, n/2, max, scaleFactor, colors,me);
                    ComputeThread quadrant3 = new ComputeThread(n, zoom, x0, y0, 0, n/2, n/2, n, max, scaleFactor, colors,me);
                    ComputeThread quadrant4 = new ComputeThread(n, zoom, x0, y0, n/2, n, n/2, n, max, scaleFactor, colors,me);
                    Thread thread1 = new Thread(quadrant1);
                    Thread thread2 = new Thread(quadrant2);
                    Thread thread3 = new Thread(quadrant3);
                    Thread thread4 = new Thread(quadrant4);
                    thread1.start();
                    thread2.start();
                    thread3.start();
                    thread4.start();
                    repaint(0, 0, getWidth(), getHeight());
                }
            }
        });
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {           
                g.setColor(colors[j][i]);
                g.drawLine(i, j, i, j);
            }
        }
    }
    

    public static int mand(Complex z0, int max) {
        Complex z = z0;
        for (int t = 0; t < max; t++) {
            if (z.abs() > zoom) {
                return t;
            }
            z = Complex.ad(Complex.mult(z, z), z0);
        }
        return max;
    }
    
    
    public static void main(String[] args) {

        
        for (int i = 0; i < n; i++) { // initialize array for initial drawing
            for (int j = 0; j < n; j++) {
                double a = x0 + (i * scaleFactor);
                double b = y0 - (j * scaleFactor);
                Complex z0 = new Complex(a, b);
                int gray = max - mand(z0, max);
                Color color = new Color(gray, gray, gray);
                colors[j][i] = color;
            }
        }

        
        SwingUtilities.invokeLater(() -> {
            var panel = new Mandelbrot();
            panel.setBackground(Color.WHITE);
            panel.setFocusable(true);
            var frame = new JFrame("Mandelbrot");
            frame.setSize(n, n);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(panel, BorderLayout.CENTER);
            frame.setVisible(true);
        });
        
    }
    
    public void calcDoneEvent()  {
    	repaint();
    }
}
