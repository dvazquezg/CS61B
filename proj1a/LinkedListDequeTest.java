import java.awt.desktop.SystemEventListener;

/** Performs some basic linked list tests. */
public class LinkedListDequeTest {
	
	/* Utility method for printing out empty checks. */
	public static boolean checkEmpty(boolean expected, boolean actual) {
		if (expected != actual) {
			System.out.println("isEmpty() returned " + actual + ", but expected: " + expected);
			return false;
		}
		return true;
	}

	/* Utility method for printing out empty checks. */
	public static boolean checkSize(int expected, int actual) {
		if (expected != actual) {
			System.out.println("size() returned " + actual + ", but expected: " + expected);
			return false;
		}
		return true;
	}

	/* Prints a nice message based on whether a test passed. 
	 * The \n means newline. */
	public static void printTestStatus(boolean passed) {
		if (passed) {
			System.out.println("Test passed!\n");
		} else {
			System.out.println("Test failed!\n");
		}
	}

	/** Adds a few things to the list, checking isEmpty() and size() are correct, 
	  * finally printing the results. 
	  *
	  * && is the "and" operation. */
	public static void addIsEmptySizeTest() {
		System.out.println("Running add/isEmpty/Size test.");

		LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

		boolean passed = checkEmpty(true, lld1.isEmpty());

		lld1.addFirst("front");
		/*
		lld1.addFirst("front 3");
		lld1.addFirst("front 2");
		lld1.addFirst("front 1");
		lld1.addLast("Last 4");*/

		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
		passed = checkSize(1, lld1.size()) && passed;
		passed = checkEmpty(false, lld1.isEmpty()) && passed;

		lld1.addLast("middle");
		passed = checkSize(2, lld1.size()) && passed;

		lld1.addLast("back");
		passed = checkSize(3, lld1.size()) && passed;


		System.out.println("Printing out deque: ");
		lld1.printDeque();
		lld1.printDequeBackwards();

		printTestStatus(passed);

	}

	public static void copyTest(){
		System.out.println("Running deep copy test.");

		LinkedListDeque<String> lld1 = new LinkedListDeque<String>();


		boolean passed = checkEmpty(true, lld1.isEmpty());

		lld1.addFirst("front 3");
		lld1.addFirst("front 2");
		lld1.addFirst("front 1");
		lld1.addLast("Last 4");
		lld1.addLast("Last 5");

		LinkedListDeque<String> lld1copy = new LinkedListDeque<String>(lld1);

		System.out.println("Printing out original deque: ");
		lld1.printDeque();
		System.out.println("Printing out copied deque: ");
		lld1copy.printDeque();

		lld1.addFirst("front 0"); //adding a new element in original

		System.out.println("Printing out original modified deque: ");
		lld1.printDeque();
		System.out.println("Printing out copied deque: ");
		lld1copy.printDeque();
	}

	/** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
	public static void addRemoveTest() {

		System.out.println("Running add/remove test.");

		System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

		LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
		// should be empty 
		boolean passed = checkEmpty(true, lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty 
		passed = checkEmpty(false, lld1.isEmpty()) && passed;

		lld1.removeFirst();
		// should be empty 
		passed = checkEmpty(true, lld1.isEmpty()) && passed;

		printTestStatus(passed);

	}

	/** Adds an item, then removes last item, and ensures that dll is empty afterwards. */
	public static void addRemoveLastTest() {

		System.out.println("Running add/remove test.");

		System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

		LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
		// should be empty
		boolean passed = checkEmpty(true, lld1.isEmpty());

		lld1.addLast(10);
		// should not be empty
		passed = checkEmpty(false, lld1.isEmpty()) && passed;

		lld1.removeLast();
		// should be empty
		passed = checkEmpty(true, lld1.isEmpty()) && passed;

		printTestStatus(passed);

	}

	public static void TestAddRemoveBothSides(){
		System.out.println("Running add/remove both sides test.");
		LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

		boolean passed = checkEmpty(true, lld1.isEmpty());


		lld1.addFirst("front 3");
		lld1.addFirst("front 2");
		lld1.addFirst("front 1");
		lld1.addLast("Last 4");
		lld1.addFirst("front 0");
		lld1.addLast("Last 5");


		System.out.println("Printing out deque after adding: ");
		lld1.printDeque();
		lld1.printDequeBackwards();

		lld1.removeFirst();
		lld1.removeLast();

		System.out.println("Printing out deque after removing: ");
		lld1.printDeque();
		lld1.printDequeBackwards();

		lld1.addLast("End");
		lld1.addFirst("Start");

		System.out.println("Printing out deque after re-adding new elements: ");
		lld1.printDeque();
		lld1.printDequeBackwards();
		System.out.println("Size: " + lld1.size());

		System.out.println("item -1 (expected null): " + lld1.get(-1));
		System.out.println("item 0 (expected: Start): " + lld1.get(0));
		System.out.println("item 3 (expected: 3): " + lld1.get(3));
		System.out.println("item 5 (expected: End): " + lld1.get(5));
		System.out.println("item 6 (expected null): " + lld1.get(6));

		System.out.println("Get recursive: ");
		System.out.println("item -1 (expected null): " + lld1.getRecursive(-1));
		System.out.println("item 0 (expected: Start): " + lld1.getRecursive(0));
		System.out.println("item 3 (expected: 3): " + lld1.getRecursive(3));
		System.out.println("item 5 (expected: End): " + lld1.getRecursive(5));
		System.out.println("item 6 (expected null): " + lld1.getRecursive(6));

	}

	public static void main(String[] args) {
		System.out.println("Running tests.\n");
		//addIsEmptySizeTest();
		copyTest();
		//addRemoveTest();
		//TestAddRemoveBothSides();
	}
} 