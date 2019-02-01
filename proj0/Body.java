/**
 * Body is the abstract class for celestial bodies
 *
 * @author   Daniel Vazquez Guevara
 */

public class Body{
	public double xxPos; 
	public double yyPos; 
	public double xxVel; 
	public double yyVel; 
	public double mass; 
	public String imgFileName; 
	private final double G_CONSTANT = 6.67e-11; 

	public Body(double xP, double yP, double xV,
		        double yV, double m, String img) {
		this.xxPos = xP; 
		this.yyPos = yP; 
		this.xxVel = xV; 
		this.yyVel = yV; 
		this.mass = m; 
		this.imgFileName = img; 
	}

	public Body(Body b) {
		this.xxPos = b.xxPos; 
		this.yyPos = b.yyPos; 
		this.xxVel = b.xxVel; 
		this.yyVel = b.yyVel; 
		this.mass = b.mass; 
		this.imgFileName = b.imgFileName; 
	}

	/**
	* Calculates the distance between the supplied 
	* body and the body that is doing the calculation
	*
	* @param b The supplied body
	* @return  The distance
	*/
	public double calcDistance(Body b) {
		return Math.sqrt(Math.pow((this.xxPos - b.xxPos),2) +
			             Math.pow((this.yyPos - b.yyPos),2)); 
	}

	/**
	* Calculates the force exerted on this body by the given body.
	*
	* @param b The supplied body
	* @return  The exerted force
	*/
	public double calcForceExertedBy(Body b) {
		double squared_distance = Math.pow(this.calcDistance(b), 2); 
		return (G_CONSTANT * this.mass * b.mass) / squared_distance; 
	}

	/**
	* Calculates the force exerted on this body in the X direction.
	* Note: The dx component is defined as the supplied body's x-position.
	* minus this body's x-position.
	*
	* @param b The supplied body
	* @return  The exerted force in the X direction
	*/
	public double calcForceExertedByX(Body b) {
		double dx = b.xxPos - this.xxPos; 
		return (this.calcForceExertedBy(b) * dx) / this.calcDistance(b); 
	}

	/**
	* Calculates the force exerted on this body in the Y direction.
	* Note: The dy component is defined as the supplied body's y-position.
	* minus this body's y-position.
	*
	* @param b The supplied body
	* @return  The exerted force in the Y direction
	*/
	public double calcForceExertedByY(Body b) {
		double dy = b.yyPos - this.yyPos; 
		return (this.calcForceExertedBy(b) * dy) / this.calcDistance(b); 
	}

	/**
	* Calculates the net X force exerted by all bodies in the
	* supplied array upon the current Body.
	* 
	* @param bodies The array of bodies
	* @return       The exerted net force in the X direction
	*/
	public double calcNetForceExertedByX(Body[] bodies) {
		double netForceX = 0;
		for (Body b: bodies) {
			if (!this.equals(b)) { // make sure curret body is not itself
				netForceX += this.calcForceExertedByX(b); 
			}
		}
		return netForceX; 
	}

	/**
	* Calculates the net Y force exerted by all bodies in the
	* supplied array upon the current Body.
	* 
	* @param bodies The array of bodies
	* @return       The exerted net force in the Y direction
	*/
	public double calcNetForceExertedByY(Body[] bodies) {
		double netForceY = 0;
		for (Body b: bodies) {
			if (!this.equals(b)) {
				netForceY += this.calcForceExertedByY(b); 
			}
		}
		return netForceY; 
	}

	/**
	* Updates the body’s position and velocity instance variables
	* as a force in both direction is applied in a given timeframe.
	* 
	* @param dt The timeframe (small period of time)
	* @param fX Applied force in the x-direction
	* @param fY Applied force in the y-direction
	*/
	public void update(double dt, double fX, double fY) {
		// calculating accelerations
		double aX = fX / this.mass; 
		double aY = fY / this.mass; 
		// updating velocities
		this.xxVel = this.xxVel + (dt * aX); 
		this.yyVel = this.yyVel + (dt * aY); 
		//updating position
		this.xxPos = xxPos + (dt * this.xxVel); 
		this.yyPos = yyPos + (dt * this.yyVel); 
	}

	/**
	* Draws a body in the current canvas
	*/
	public void draw(){
		StdDraw.picture(this.xxPos, this.yyPos, "images/" + this.imgFileName);
	}

}