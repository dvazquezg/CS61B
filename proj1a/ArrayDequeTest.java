public class ArrayDequeTest {
    public static void ArrayAddEmptySizeTest() {
        System.out.println("Running add/isEmpty/Size test.");
        ArrayDeque<String> ad1 = new ArrayDeque<String>();
        ad1.addFirst("3");
        ad1.addLast("4");
        ad1.addLast("5");
        ad1.addFirst("2");
        ad1.addLast("6");
        ad1.addFirst("1");
        ad1.addLast("7");
        ad1.addLast("8");
        ad1.addFirst("0");

        ad1.printDeque();

        System.out.println("Removed from back (8): " + ad1.removeLast());
        System.out.println("Removed from back (7): " + ad1.removeLast());
        System.out.println("Removed from front (0): " + ad1.removeFirst());
        System.out.println("Removed from front (1): " + ad1.removeFirst());
        System.out.println("Removed from back (6): " + ad1.removeLast());

        ad1.printDeque();

        System.out.println("Removed from front (2): " + ad1.removeFirst());

        ad1.printDeque();
        System.out.println("Final size (3): " + ad1.size());

    }

    public static void deepCopyAdeque() {
        System.out.println("Running DeepCopy test.");
        ArrayDeque<String> ad1 = new ArrayDeque<String>();
        ad1.addFirst("3");
        ad1.addLast("4");
        ad1.addLast("5");
        ad1.addFirst("2");
        ad1.addLast("6");
        ad1.addFirst("1");
        ad1.addLast("7");
        ad1.addLast("8");
        ad1.addFirst("0");

        System.out.print("Original: ");
        ad1.printDeque();

        ArrayDeque<String> ad2 = new ArrayDeque<String>(ad1);
        System.out.print("Copy: ");

        ad2.printDeque();
    }

    public static void getTest() {
        System.out.println("Running get test.");
        ArrayDeque<String> ad1 = new ArrayDeque<String>();
        ad1.addFirst("3");
        ad1.addLast("4");
        ad1.addLast("5");
        ad1.addFirst("2");
        ad1.addLast("6");
        ad1.addFirst("1");
        ad1.addLast("7");
        ad1.addLast("8");
        ad1.addFirst("0");

        ad1.printDeque();

        System.out.println("get(0) (0): " + ad1.get(0));
        System.out.println("get(5) (5): " + ad1.get(5));
        System.out.println("get(-1) (null): " + ad1.get(-1));
        System.out.println("get(10) (null): " + ad1.get(10));
        System.out.println("get(8) (8): " + ad1.get(8));
        System.out.println("get(3) (3): " + ad1.get(3));

    }

    public static void savageTest() {
        System.out.println("Running savage test");

        ArrayDeque<String> theDeque = new ArrayDeque<String>();
        for (int i = 0; i < 10000; i++) {
            theDeque.addLast("" + i);
        }

        System.out.println("Initial Size: " + theDeque.size());

        for (int i = 0; i < 9999; i++) {
            theDeque.removeLast();
        }

        theDeque.printDeque();
        System.out.println("Final Size: " + theDeque.size());
    }

    public static void main(String[] args) {
        //ArrayAddEmptySizeTest();
        //deepCopyAdeque();
        //getTest();
        savageTest();
    }
}
