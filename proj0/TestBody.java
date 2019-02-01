public class TestBody{
	public static void main(String[] args){
		NBody test = new NBody();
		In in = new In("/data/planets.txt"); 
		// read number of planet in the file
		int num_planets = in.readInt();
		System.out.println(num_planets);
		// array of planets to be created
		Body[] bodies = new Body[num_planets];
		// skip the second line (the radius)
		System.out.println("something: " + in.readLine()); 
		System.out.println("something2: " + in.readLine()); 
		int counter = 0;
		while (!in.isEmpty() && counter < num_planets) {
			bodies[counter] = new Body(in.readDouble(), in.readDouble(),
				                       in.readDouble(), in.readDouble(),
		                               in.readDouble(), in.readString()); 
			
			System.out.println("x: "  + bodies[counter].xxPos + 
				             ", y: "  + bodies[counter].yyPos + 
				             ", vx: " + bodies[counter].xxVel +
				             ", vy: " + bodies[counter].xxVel +
				             ", mass: " + bodies[counter].mass +
				             ", img: " + bodies[counter].imgFileName);

			counter++;
		}
	}
}