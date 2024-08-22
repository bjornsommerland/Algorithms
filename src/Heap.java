public class Heap {

    Integer[] heapList;
    int currentIndex = 0;

    // Swap function
    private void swap(int i1, int i2) {
        Integer obj1 = heapList[i1];
        Integer obj2 = heapList[i2];
        heapList[i1] = obj2;
        heapList[i2] = obj1;
    }

    // Compare function
    private boolean compare(int i1, int i2) {
        Integer obj1 = heapList[i1];
        Integer obj2 = heapList[i2];
        return (obj1 < obj2);
    }

    // Check function (percolate up)
    private void check() {
        boolean permittedToBreak = false;
        int index = currentIndex - 1;
        while (!permittedToBreak && index > 0) {
            int currentIndexParent = (index - 1) / 2;
            if (compare(index, currentIndexParent)) {
                swap(currentIndexParent, index);
                index = currentIndexParent;
            } else {
                permittedToBreak = true;
            }
        }
    }

    // Heapify (percolate down)
    private void heapify() {
        boolean permittedToBreak = false;
        int index = 0;
        while (!permittedToBreak) {
            int leftChildIndex = 2 * index + 1;
            int rightChildIndex = 2 * index + 2;

            if (leftChildIndex >= currentIndex) {
                return;
            }

            int childToCheck = leftChildIndex;
            if (rightChildIndex < currentIndex && compare(rightChildIndex, leftChildIndex)) {
                childToCheck = rightChildIndex;
            }

            if (compare(childToCheck, index)) {
                swap(childToCheck, index);
                index = childToCheck;
            } else {
                permittedToBreak = true;
            }
        }
    }

    public Heap(int size) {
        this.heapList = new Integer[size];
    }

    public void add(Integer num) {
        if (currentIndex >= heapList.length) {
            throw new IndexOutOfBoundsException("Heap is full");
        }
        heapList[currentIndex] = num;
        currentIndex++;
        if (currentIndex > 1) {
            check();
        }
    }

    public Integer removeSmallest() {
        if (currentIndex == 0) {
            return null; // Or throw an exception
        }
        Integer smallest = heapList[0];
        heapList[0] = heapList[currentIndex - 1];
        currentIndex--;
        heapify();
        return smallest;
    }

    public void printAll() {
        int level = 0;
        int itemsAtCurrentLevel = 1;
        int itemsPrinted = 0;

        for (int i = 0; i < currentIndex; i++) {
            System.out.print(heapList[i] + " ");
            itemsPrinted++;
            if (itemsPrinted == itemsAtCurrentLevel) {
                System.out.println();
                itemsPrinted = 0;
                level++;
                itemsAtCurrentLevel = (int) Math.pow(2, level);
            }
        }
        if (itemsPrinted != 0) {
            System.out.println();
        }
    }
}
