import java.util.ArrayList;

public class TreeRenderer<T> {

    private GUINode<T> rootNode;

    private SwizzledTree<T> tree;

    public TreeRenderer(Object treeImplementation) {
        tree = new SwizzledTree<>(treeImplementation);
    }

    public void add(T data) {
        tree.add(data);
    }

    public T remove(T data) {
        return tree.remove(data);
    }

    public int size() {
        return tree.size();
    }

    public GUINode<T> getNode(int i) {
        return tree.getNodes().get(i);
    }

    private class SwizzledTree<T> {

        private Object treeImplementation;
        private ArrayList<GUINode<T>> nodes;

        private SwizzledTree(Object treeImplementation) {
            this.treeImplementation = treeImplementation;
        }

        private void add(T data) {
            // Reflection stuff
        }

        private T remove(T data) {
            // Reflection stuff
            return data;
        }

        private int size() {
            // Reflection stuff
            return 0;
        }

        private ArrayList<GUINode<T>> getNodes() {
            return nodes;
        }


    }

}
