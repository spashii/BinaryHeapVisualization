public class BinaryHeapNode<T> extends GUINode {

    private T data;

    public BinaryHeapNode(T data) {
        super();
        this.data = data;

    }

    public String toString() {
        return data.toString();
    }

}
