package com.ciotc.teemo.splines;

import javax.swing.*;

import java.awt.*;

public class Test extends JFrame {

	private static final long serialVersionUID = 1L;

	protected Polygon pts;

	public Test( ) {
		pts = new Polygon( );
		pts.addPoint( 200, 100 );
		pts.addPoint( 300, 100 );
		pts.addPoint( 300, 200 );
		pts.addPoint( 400, 200 );
		pts.addPoint( 500, 200 );
		
		pts.addPoint( 500, 100 );
		pts.addPoint( 600, 100 );
		pts.addPoint( 600, 200 );
		pts.addPoint( 500, 200 );
		
		
		
		
		
		pts.addPoint( 500, 300 );
		pts.addPoint( 400, 300 );
		pts.addPoint( 400, 400 );
		pts.addPoint( 300, 400 );
		pts.addPoint( 300, 500 );
		pts.addPoint( 400, 500 );
		pts.addPoint( 500, 500 );
		pts.addPoint( 600, 500 );
		pts.addPoint( 600, 600 );
		pts.addPoint( 600, 700 );
		pts.addPoint( 500, 700 );
		pts.addPoint( 400, 700 );
		pts.addPoint( 300, 700 );
		pts.addPoint( 300, 600 );
		pts.addPoint( 200, 600 );
		pts.addPoint( 200, 500 );
		pts.addPoint( 200, 400 );
		pts.addPoint( 200, 300 );
		pts.addPoint( 200, 200 );
		pts.addPoint( 100, 200 );
		pts.addPoint( 100, 100 );
		//pts.addPoint( 100, 100 );
	}

	public static void main( String args[] ) {
		Test p = new Test( );
		p.setSize( 800, 740 );
		p.setLocation( 200, 20 );
		p.setDefaultCloseOperation( EXIT_ON_CLOSE );
		p.setVisible( true );
	}

	final int STEPS = 12;

	public void paint( Graphics g ) {
		// g.drawLine( 0, 0, 800, 500 );

		super.paint( g );
		if ( pts.npoints > 2 ) {
			Cubic[] X = calcNaturalCubic( pts.npoints - 1, pts.xpoints );
			Cubic[] Y = calcNaturalCubic( pts.npoints - 1, pts.ypoints );

			/*
			 * very crude technique - just break each segment up into steps
			 * lines
			 */
			Polygon p = new Polygon( );
			p.addPoint( ( int ) Math.round( X[0].eval( 0 ) ), ( int ) Math.round( Y[0].eval( 0 ) ) );
			for ( int i = 0; i < X.length; i++ ) {
				for ( int j = 1; j <= STEPS; j++ ) {
					float u = j / ( float ) STEPS;
					p.addPoint( Math.round( X[i].eval( u ) ), Math.round( Y[i].eval( u ) ) );
				}
			}
			g.fillPolygon( p.xpoints, p.ypoints, p.npoints );

		}
	}

	Cubic[] calcNaturalCubic( int n, int[] x ) {
		float[] w = new float[n + 1];
		float[] v = new float[n + 1];
		float[] y = new float[n + 1];
		float[] D = new float[n + 1];
		float z, F, G, H;
		int k;
		/*
		 * We solve the equation [4 1 1] [D[0]] [3(x[1] - x[n]) ] |1 4 1 |
		 * |D[1]| |3(x[2] - x[0]) | | 1 4 1 | | . | = | . | | ..... | | . | | .
		 * | | 1 4 1| | . | |3(x[n] - x[n-2])| [1 1 4] [D[n]] [3(x[0] - x[n-1])]
		 * by decomposing the matrix into upper triangular and lower matrices
		 * and then back sustitution. See Spath "Spline Algorithms for Curves
		 * and Surfaces" pp 19--21. The D[i] are the derivatives at the knots.
		 */
		w[1] = v[1] = z = 1.0f / 4.0f;
		y[0] = z * 3 * ( x[1] - x[n] );
		H = 4;
		F = 3 * ( x[0] - x[n - 1] );
		G = 1;
		for ( k = 1; k < n; k++ ) {
			v[k + 1] = z = 1 / ( 4 - v[k] );
			w[k + 1] = -z * w[k];
			y[k] = z * ( 3 * ( x[k + 1] - x[k - 1] ) - y[k - 1] );
			H = H - G * w[k];
			F = F - G * y[k - 1];
			G = -v[k] * G;
		}
		H = H - ( G + 1 ) * ( v[n] + w[n] );
		y[n] = F - ( G + 1 ) * y[n - 1];

		D[n] = y[n] / H;
		D[n - 1] = y[n - 1] - ( v[n] + w[n] ) * D[n]; /*
													 * This equation is WRONG!
													 * in my copy of Spath
													 */
		for ( k = n - 2; k >= 0; k-- ) {
			D[k] = y[k] - v[k + 1] * D[k + 1] - w[k + 1] * D[n];
		}

		/* now compute the coefficients of the cubics */
		Cubic[] C = new Cubic[n + 1];
		for ( k = 0; k < n; k++ ) {
			C[k] = new Cubic( ( float ) x[k], D[k], 3 * ( x[k + 1] - x[k] ) - 2 * D[k] - D[k + 1], 2 * ( x[k] - x[k + 1] ) + D[k]
					+ D[k + 1] );
		}
		C[n] = new Cubic( ( float ) x[n], D[n], 3 * ( x[0] - x[n] ) - 2 * D[n] - D[0], 2 * ( x[n] - x[0] ) + D[n] + D[0] );
		return C;
	}
}
