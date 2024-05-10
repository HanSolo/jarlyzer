package eu.hansolo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TreeNode<T> {
    private T                 item;
    private TreeNode<T>       parent;
    private TreeNode<T>       myRoot;
    private List<TreeNode<T>> children;


    // ******************** Constructors **************************************
    public TreeNode(final T item) {
        this(item, null);
    }
    public TreeNode(final T item, final TreeNode<T> parent) {
        this.item   = item;
        this.parent = parent;
        children    = new ArrayList<>();
        init();
    }


    // ******************** Methods *******************************************
    private void init() {
        // Add this node to parents children
        if (null != parent) { parent.getChildren().add(this); }
    }

    public boolean isRoot() { return null == parent; }
    public boolean isLeaf() { return (null == children || children.isEmpty()); }
    public boolean hasParent() { return null != parent; }
    public void removeParent() {
        parent = null;
        myRoot = null;
    }

    public TreeNode<T> getParent() { return parent; }
    public void setParent(final TreeNode<T> parent) {
        if (null != parent) { parent.addNode(TreeNode.this); }
        this.parent = parent;
        myRoot      = null;
    }

    public T getItem() { return item; }
    public void setItem(final T item) { this.item = item; }

    public List<TreeNode<T>> getChildrenUnmodifiable() { return Collections.unmodifiableList(children); }
    public List<TreeNode<T>> getChildren() { return children; }
    public void setChildren(final List<TreeNode<T>> children) {
        this.children.clear();
        this.children.addAll(new LinkedHashSet<>(children));
    }

    public void addNode(final T item) {
        TreeNode<T> child = new TreeNode<T>(item);
        child.setParent(this);
        children.add(child);
    }
    public void addNode(final TreeNode<T> node) {
        if (children.contains(node)) { return; }
        node.setParent(this);
        children.add(node);
    }
    public void removeNode(final TreeNode<T> node) { if (node.getChildren().contains(node)) { node.getChildren().remove(node); } }

    public void addNodes(final TreeNode<T>... nodes) { addNodes(Arrays.asList(nodes)); }
    public void addNodes(final List<TreeNode<T>> nodes) { nodes.forEach(node -> addNode(node)); }

    public void removeNodes(final TreeNode<T>... nodes) { removeNodes(Arrays.asList(nodes)); }
    public void removeNodes(final List<TreeNode<T>> nodes) { nodes.forEach(node -> removeNode(node)); }

    public void removeAllNodes() { children.clear(); }

    public boolean isEmpty() { return children.isEmpty(); }

    public Stream<TreeNode<T>> stream() {
        if (isLeaf()) {
            return Stream.of(this);
        } else {
            return getChildren().stream()
                                .map(child -> child.stream())
                                .reduce(Stream.of(this), (s1, s2) -> Stream.concat(s1, s2));
        }
    }
    public Stream<TreeNode<T>> lazyStream() {
        if (isLeaf()) {
            return Stream.of(this);
        } else {
            return Stream.concat(Stream.of(this), getChildren().stream().flatMap(TreeNode::stream));
        }
    }

    public Stream<TreeNode<T>> flattened() { return Stream.concat(Stream.of(this), children.stream().flatMap(TreeNode::flattened)); }
    public List<TreeNode<T>> getAll() { return flattened().toList(); }
    public List<T> getAllItems() { return flattened().map(TreeNode::getItem).toList(); }

    public int getNoOfNodes() { return flattened().map(TreeNode::getItem).toList().size(); }
    public int getNoOfLeafNodes() { return flattened().filter(node -> node.isLeaf()).map(TreeNode::getItem).toList().size(); }

    public boolean contains(final TreeNode<T> node) { return flattened().anyMatch(n -> n.equals(node)); }
    public boolean containsItem(final T item) { return flattened().anyMatch(n -> n.item.equals(item)); }

    public TreeNode<T> getMyRoot() {
        if (null == myRoot) {
            if (getParent().isRoot()) {
                myRoot = this;
            } else {
                myRoot = findMyRoot(getParent());
            }
        }
        return myRoot;
    }
    private TreeNode<T> findMyRoot(final TreeNode<T> node) {
        if (node.getParent().isRoot()) { return node; }
        return findMyRoot(node.getParent());
    }

    public TreeNode<T> getTreeRoot() {
        if (isRoot()) { return this; }
        return findTreeRoot(getParent());
    }
    private TreeNode<T> findTreeRoot(final TreeNode<T> node) {
        if (node.isRoot()) { return node; }
        return findTreeRoot(node.getParent());
    }

    public int getLevel() {
        if (isRoot()) { return 0; }
        return getLevel(getParent(), 0);
    }
    private int getLevel(final TreeNode<T> node, int level) {
        level++;
        if (node.isRoot()) { return level; }
        return getLevel(node.getParent(), level);
    }

    public int getMaxLevel() { return getTreeRoot().stream().map(TreeNode::getLevel).max(Comparator.naturalOrder()).orElse(0); }

    public List<TreeNode<T>> getSiblings() { return null == getParent() ? new ArrayList<>() : getParent().getChildren(); }

    public List<TreeNode<T>> nodesAtSameLevel() {
        final int LEVEL = getLevel();
        return getTreeRoot().stream().filter(node -> node.getLevel() == LEVEL).collect(Collectors.toList());
    }

    public List<TreeNode<T>> getHierarchicalNodeList() {
        List<TreeNode<T>> nodeList = new ArrayList<>();
        collectNodes(TreeNode.this, nodeList);
        Collections.reverse(nodeList);
        return nodeList;
    }
    private void collectNodes(final TreeNode<T> node, final List<TreeNode<T>> nodeList) {
        node.getChildren().forEach(n -> collectNodes(n, nodeList));
        nodeList.add(node);
    }

    public static final void removeEmptyPackages(TreeNode<Item> node) {
        List<TreeNode<Item>> emptyItems = node.getHierarchicalNodeList()
                                              .stream()
                                              .filter(n -> n.item.getType() != Type.METHOD)
                                              .filter(n -> n.getChildren().isEmpty()).toList();
        emptyItems.forEach(n -> System.out.println(n.getItem().getName()));
    }

    public static final TreeNode<Item> deepCopy(TreeNode<Item> node) {
        TreeNode<Item> copy = new TreeNode<Item>(copyItem(node.getItem()));
        for (TreeNode<Item> child : node.getChildren()) {
            copy.getChildren().add(deepCopy(child));
        }
        return copy;
    }
    private static Item copyItem(final Item item) {
        switch(item.getType()) {
            case JAR -> {
                JarItem jarItem = (JarItem) item;
                return new JarItem(jarItem.getName(), jarItem.getNumberOfClasses());
            }
            case PACKAGE -> {
                PackageItem pkgItem = (PackageItem) item;
                return new PackageItem(pkgItem.getName(), pkgItem.getFullyQualifiedName());
            }
            case CLASS -> {
                ClassItem classItem = (ClassItem) item;
                return new ClassItem(classItem.getName(), classItem.getNumberOfMethods(), classItem.getPercentageUsed());
            }
            case METHOD -> {
                MethodItem methodItem = (MethodItem) item;
                return new MethodItem(methodItem.getName(), methodItem.isUsed());
            }
            default -> { return null; }
        }
    }

    public static <T> Optional<TreeNode<T>> getTreeNodeByItem(final TreeNode<T> treeNode, final Item item) {
        return treeNode.getAll().stream().filter(n -> n.getItem().equals(item)).findFirst();
    }
}
