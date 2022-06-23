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


public class Mandlebrot extends JPanel {
	private static final long serialVersionUID = 7148504528835036003L;
	private static double size = 4.0;
	private static int n	= 1000;
	private static int max	= 255;
	private static double x0 = -size/2;
	private static double y0 = size/2;
	private static double xC = 0;
	private static double yC = 0;
	private static double scaleFactor = size/n;
	private static Stack<double[]> origins = new Stack<double[]>();

	public Mandlebrot() {
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				double[] center = new double[]{xC, yC};
				origins.push(center);
				xC = x0 + (scaleFactor * e.getX());
				yC = y0 - (scaleFactor * e.getY());
				size = size/2.0;
				scaleFactor = size/n;
				x0 = xC - (size/2);
				y0 = yC + (size/2);
				repaint(0, 0, getWidth(), getHeight());
			}
		});
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					double[] center = origins.pop();
					xC = center[0];
					yC = center[1];
					size = size*2.0;
					scaleFactor = size/n;
					x0 = xC - (size/2);
					y0 = yC + (size/2);
					repaint(0, 0, getWidth(), getHeight());
				}
			}
		});
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		System.out.printf("");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				double a = x0 + (i * scaleFactor);
				double b = y0 - (j * scaleFactor);
				Complex z0 = new Complex(a, b);
				int gray = max - mand(z0, max);
				Color color = new Color(gray, gray, gray);
				g.setColor(color);
				g.drawLine(i, j, i, j);
			}
		}
	}
	
	public static int mand(Complex z0, int max) {
		Complex z = z0;
		for (int t = 0; t < max; t++) {
			if (z.abs() > 2.0) {
				return t;
			}
			z = Complex.ad(Complex.mult(z, z), z0);
		}
		return max;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			var panel = new Mandlebrot();
			panel.setBackground(Color.WHITE);
			panel.setFocusable(true);
			var frame = new JFrame("Mandelbrot");
			frame.setSize(n, n);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().add(panel, BorderLayout.CENTER);
			frame.setVisible(true);
		});
	}
}