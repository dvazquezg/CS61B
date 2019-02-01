/**
 * NBody is the abstract class for a universe of celestial bodies
 *
 * @author   Daniel Vazquez Guevara
 */
public class NBody{

	/**
	* Returns a double corresponding to the radius of the
	* universe in the specified file
	*
	* @param filename The full path to file
	* @return         The radius of the universe
	*/
	public static double readRadius(String filename) {
		// start reading the given file
		In in = new In(filename); 
		// skip the first line (the number of bodies)
		if (in.hasNextLine())
			in.readLine(); 
		return in.hasNextLine() ? in.readDouble() : 0; 
	}

	/**
	* Returns an array of Bodys corresponding to the 
	* bodies in the specified file
	*
	* @param filename The full path to file
	* @return         The array of bodies
	*/
	public static Body[] readBodies(String filename) {
		// start reading the given file
		In in = new In(filename); 
		// read number of planet in the file
		int num_planets = in.readInt(); 
		// array of planets to be created
		Body[] bodies = new Body[num_planets]; 
		// skip the second line (the radius)
		in.readLine(); // reads the end of first line
		in.readLine();  // reads the the second line
		int counter = 0; 
		while (!in.isEmpty() && counter < num_planets) {
			bodies[counter] = new Body(in.readDouble(), in.readDouble(),
				                       in.readDouble(), in.readDouble(),
		                               in.readDouble(), in.readString()); 
			counter++; 
		}
		return bodies; 
	}

	/**
	* Main method: assembles the universe
	* @param args The initial parameters
	*/
	public static void main(String[] args) {
		// setting up the universe
		double T = Double.parseDouble(args[0]); 
		double dt = Double.parseDouble(args[1]); 
		String filename = args[2]; 
		double radius = readRadius(filename); 
		Body[] bodies = readBodies(filename); 
		double time = 0; 

		// prevent flickering
		StdDraw.enableDoubleBuffering(); 
		// Sets up the universe scale
		StdDraw.setScale(-radius, radius); 
		// the force arrays
		double[] xForces;
		double[] yForces;

		// animation loop
		while (time <= T) {
			xForces = new double[bodies.length]; 
			yForces = new double[bodies.length]; 

			// Store the net x and y forces for each Body into arrays
			for (int i=0; i < bodies.length; i++) {
				xForces[i] = bodies[i].calcNetForceExertedByX(bodies); 
				yForces[i] = bodies[i].calcNetForceExertedByY(bodies); 
			}

			// update location of each object
			for (int i=0; i < bodies.length; i++) {
				bodies[i].update(dt, xForces[i], yForces[i]); 
			}

			// draw background
			StdDraw.picture(0, 0, "images/starfield.jpg"); 

			// draw all bodies
			for (Body b: bodies) {
				b.draw(); 
			}

			// Shows the drawing to the screen, and waits 10 milliseconds
			StdDraw.show(); 
			StdDraw.pause(10); 

			// increase time variable
			time += dt;

		}

		// printing the universe (final state)
		StdOut.printf("%d\n", bodies.length);
		StdOut.printf("%.2e\n", radius);
		for (int i = 0; i < bodies.length; i++) {
		    StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
		                  bodies[i].xxPos, bodies[i].yyPos, bodies[i].xxVel,
		                  bodies[i].yyVel, bodies[i].mass, bodies[i].imgFileName);   
		}
	}
}