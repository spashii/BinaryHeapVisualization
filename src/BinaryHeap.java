import java.util.Arrays;

public class BinaryHeap {

    private BinaryHeapNode[] backingArray;
    private int nodes = 0;
    private int initialSize = 10;

    public BinaryHeap() {
        backingArray = new BinaryHeapNode[initialSize];
        backingArray[0] = null;
    }

    public void add(int data) {
        if (nodes + 1 >= backingArray.length) {
            backingArray = Arrays.copyOf(backingArray, backingArray.length * 2);
        }
        BinaryHeapNode node = new BinaryHeapNode(data);
        backingArray[nodes + 1] = node;
        int index = nodes + 1;
        while (index > 1 && getParent(index).getData() > data)  {
            swap(index, index /= 2);
        }
        nodes++;
    }

    public int remove() {
        if (size() == 0) {
            return -1;
        }
        int oldData = backingArray[1].getData();
        backingArray[1] = backingArray[nodes];
        backingArray[nodes] = null;
        int index = 1;
        while (index * 2 < nodes && getLesserChild(index).getData() < getNode(index).getData())  {
            int lesserChildIndex = getLesserChildIndex(index);
            swap(index, lesserChildIndex);
            index = lesserChildIndex;
        }
        nodes--;
        return oldData;
    }

    private BinaryHeapNode getParent(int index) {
        return backingArray[index / 2];
    }

    private BinaryHeapNode getLesserChild(int index) {
        return backingArray[getLesserChildIndex(index)];
    }

    private int getLesserChildIndex(int index) {
        if (nodes <= index * 2 + 1) {
            return index * 2;
        }
        if (backingArray[index * 2].getData() > backingArray[index * 2 + 1].getData()) {
            return index * 2 + 1;
        } else {
            return index * 2;
        }
    }

    public String toString() {
        return Arrays.toString(backingArray);
    }

    public BinaryHeapNode getNode(int index) {
        return backingArray[index];
    }

    public void swap(int index1, int index2) {
        BinaryHeapNode node = backingArray[index1];
        BinaryHeapNode otherNode = backingArray[index2];
        int swapNodeData = node.getData();
        node.setData(otherNode.getData());
        otherNode.setData(swapNodeData);
    }

    public int size() {
        return nodes;
    }

    public static void main(String[] args) {
        BinaryHeap heap = new BinaryHeap();
        heap.add(11);
        heap.add(17);
        heap.add(15);
        heap.add(20);
        heap.add(9);
        heap.add(55);
        heap.add(4);
        heap.add(16);
        heap.add(8);
        heap.remove();
        heap.remove();
        heap.remove();
        System.out.println(heap.toString());
    }
}
