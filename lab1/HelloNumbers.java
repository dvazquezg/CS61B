public class HelloNumbers{
	public static void main(String[] args){
		int x = 0;
		int currentSum = 0;
		while(x < 10){
			System.out.print(currentSum + " ");
			x = x + 1;
			currentSum = currentSum + x;
		}
		System.out.println();
	}
}