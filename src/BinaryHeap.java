import java.util.Arrays;

public class BinaryHeap<T extends Comparable<? super T>> {

    private BinaryHeapNode<T>[] backingArray;
    private int nodes = 0;
    private int initialSize = 10;

    public BinaryHeap() {
        backingArray = (BinaryHeapNode<T>[])(new BinaryHeapNode[initialSize]);
        backingArray[0] = null;
    }

    public void add(T data) {
        if (nodes + 1 >= backingArray.length) {
            backingArray = Arrays.copyOf(backingArray, backingArray.length * 2);
        }
        BinaryHeapNode node = new BinaryHeapNode<T>(data);
        backingArray[nodes + 1] = node;
        int index = nodes + 1;
        while (index > 1 && getParent(index).getData().compareTo(data) > 0)  {
            swap(index, index /= 2);
        }
        nodes++;
    }

    public T remove() {
        if (size() == 0) {
            return null;
        }
        T oldData = (T) backingArray[1].getData();
        backingArray[1] = backingArray[nodes];
        backingArray[nodes] = null;
        int index = 1;
        while (index * 2 < nodes && getLesserChild(index).getData().compareTo(getNode(index).getData()) < 0)  {
            int lesserChildIndex = getLesserChildIndex(index);
            swap(index, lesserChildIndex);
            index = lesserChildIndex;
        }
        nodes--;
        return oldData;
    }

    private BinaryHeapNode<T> getParent(int index) {
        return backingArray[index / 2];
    }

    private BinaryHeapNode<T> getLesserChild(int index) {
        return backingArray[getLesserChildIndex(index)];
    }

    private int getLesserChildIndex(int index) {
        if (nodes <= index * 2 + 1) {
            return index * 2;
        }
        if (backingArray[index * 2].getData().compareTo(backingArray[index * 2 + 1].getData()) > 0) {
            return index * 2 + 1;
        } else {
            return index * 2;
        }
    }

    public String toString() {
        return Arrays.toString(backingArray);
    }

    public BinaryHeapNode<T> getNode(int index) {
        return backingArray[index];
    }

    public void swap(int index1, int index2) {
        BinaryHeapNode<T> node = backingArray[index1];
        BinaryHeapNode<T> otherNode = backingArray[index2];
        T swapNodeData = node.getData();
        node.setData(otherNode.getData());
        otherNode.setData(swapNodeData);
    }

    public int size() {
        return nodes;
    }

}
