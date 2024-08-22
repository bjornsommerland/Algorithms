import java.sql.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Graph {

    Integer globalIndex = 0;
    ArrayList<Point> Points = new ArrayList<Point>();

    class Point {
        Integer index;
        ArrayList<Integer[]> destinations;


        public Point() {
            index = globalIndex;
            globalIndex++;
            destinations = new ArrayList<Integer[]>();
            Points.add(this);
        }

        public void connect(Point point, int wage) {
            Integer[] connection = new Integer[]{point.index, wage};
            destinations.add(connection);
        }

        public void disconnect(Point point) {
            for (Integer[] connection : destinations) {
                if (connection[0] == point.index) {
                    destinations.remove(connection);
                    break;
                }
            }
        }
    }


    // Breadth First Search (Przeszukiwanie wszerz)
    public void BFS() {
        boolean[] visited = new boolean[Points.size()];
        visited[0] = true;
        LinkedList<Point> queue = new LinkedList<Point>();
        if (Points.getFirst() == null) {
            return;
        }
        queue.add(Points.getFirst());
        while (!queue.isEmpty()) {
            Point root = queue.removeFirst();
            for (Integer[] connection : root.destinations) {
                if (!visited[connection[0]]) {
                    queue.add(Points.get(connection[0]));
                    visited[connection[0]] = true;
                }
            }
            System.out.println(root.index);
        }
    }

    // Breadth First Search Elements
    public int[] BFSElements(int finishedProductValue) {

        boolean[] visited = new boolean[Points.size()];
        int[] elementsValue = new int[Points.size()];

        elementsValue[0] = finishedProductValue;
        visited[0] = true;

        LinkedList<Point> queue = new LinkedList<Point>();

        if (Points.getFirst() == null) {
            return null;
        }

        queue.add(Points.getFirst());
        // ------ Loop ------ \\
        while (!queue.isEmpty()) {
            Point root = queue.removeFirst();
            for (Integer[] connection : root.destinations) {
                if (!visited[connection[0]]) {
                    queue.add(Points.get(connection[0]));
                    elementsValue[connection[0]] = connection[1] * elementsValue[root.index];
                    visited[connection[0]] = true;
                }
            }
            System.out.println(root.index);
        }

        return elementsValue;
    }

    public Graph() {
        Point pancake = new Point();
        Point milk = new Point();
        Point flour = new Point();

        pancake.connect(milk, 2);
        pancake.connect(flour, 3);
    }


    public void presentGraph() {

    }

    public static void main(String[] StringArray) {
        Graph x = new Graph();
        int[] b = x.BFSElements(2);
        for (int element : b) {
            System.out.println(element);
        }
    }

}
