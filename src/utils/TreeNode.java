package utils;

class TreeNode {
    String value;
    TreeNode left;
    TreeNode right;

    TreeNode(String value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    @Override
    public String toString() {
        return "TreeNode [value=" + value + ", left=" + left + ", right=" + right + "]";
    }
}