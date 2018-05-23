package com.example.passportdemo.tree;

/**
 * tree node
 *
 * @author muxiaorui
 * @create 2018-05-23 15:23
 **/
public class TreeNode {
    private int key;
    private String data;
    private TreeNode leftChild=null;
    private TreeNode rightChild=null;

    public TreeNode() {

    }

    public TreeNode(int key, String data) {
        this.key = key;
        this.data = data;
        this.leftChild = null;
        this.rightChild = null;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public TreeNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(TreeNode leftChild) {
        this.leftChild = leftChild;
    }

    public TreeNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(TreeNode rightChild) {
        this.rightChild = rightChild;
    }
}
