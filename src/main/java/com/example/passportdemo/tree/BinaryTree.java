package com.example.passportdemo.tree;

/**
 * 二叉树 实现
 *
 * @author muxiaorui
 * @create 2018-05-23 15:44
 **/
public class BinaryTree {
    private TreeNode root;
    public BinaryTree(){
        root=new TreeNode(1,"A");
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public void addTreeNode(int key,String data){
        TreeNode newNode=new TreeNode(key,data);
       if(root==null){
           root=newNode;
       }else{
           TreeNode current=root;
           TreeNode parent;
           while(true){
               parent=current;
               if(key<current.getKey()){
                   current=current.getLeftChild();
                   if(current==null){
                       parent.setLeftChild(newNode);
                       return;
                   }
               }else{
                   current=current.getRightChild();
                   if(current==null){
                       parent.setRightChild(newNode);
                       return;
                   }
               }
           }
       }
    }

    public void createBinTree(TreeNode root){
        TreeNode newNodeB = new TreeNode(2,"B");
        TreeNode newNodeC = new TreeNode(3,"C");
        TreeNode newNodeD = new TreeNode(4,"D");
        TreeNode newNodeE = new TreeNode(5,"E");
        TreeNode newNodeF = new TreeNode(6,"F");
        root.setLeftChild(newNodeB);
        root.setRightChild(newNodeC);
        newNodeB.setLeftChild(newNodeD);
        newNodeB.setRightChild(newNodeE);
        newNodeC.setRightChild(newNodeF);
    }
    public boolean isEmpty(){
        return root==null;
    }
    public int height(TreeNode root){
        if(root==null) return 0;
        int leftHeight=height(root.getLeftChild());
        int rightHeight=height(root.getRightChild());
         return leftHeight<rightHeight ? rightHeight+1:leftHeight+1;
    }
    public int height(){
        return  height(root);
    }
//    public int size(TreeNode root){
//        if(root==null) return 0;
//        TreeNode temp=root;
//        int leftSize=0;
//        while(temp!=null ){
//            leftSize++;
//            System.out.println("左边结点"+temp.getData());
//            temp=temp.getLeftChild();
//        }
//        int rightSize=0;
//        temp=root;
//        while(temp!=null ){
//            rightSize++;
//            System.out.println("右边结点"+temp.getData());
//            temp=temp.getRightChild();
//        }
//        return leftSize+rightSize;
//    }
    public int size(TreeNode root){
        if(root==null) return 0;
        System.out.println("递归查询tree size"+root.getData());
        return 1+size(root.getLeftChild())+size(root.getRightChild());
    }
    //返回父节点 (最好能用key 查 要不就重写equals 方法)
    public TreeNode getParent(TreeNode root,TreeNode element){
        if(root==null) return null;
        System.out.println("查询父节点："+root.getData());
        if(element==root.getLeftChild()||element==root.getRightChild()){
            return root;
        }
        TreeNode p;
        if((p=getParent(root.getLeftChild(),element))!=null){
            return p;
        }else {
            return getParent(root.getRightChild(),element);
        }
    }
    public TreeNode parent(TreeNode element){
        return root==null ?null:getParent(root,element);
    }

    //在释放某个结点时，该结点的左右子树都已经释放，
    //所以应该采用后续遍历，当访问某个结点时将该结点的存储空间释放
    public void destory(TreeNode root){
        if(root !=null){
            System.out.println("释放结点："+root.getData());
            destory(root.getLeftChild());
            destory(root.getRightChild());
            root =null;
        }
    }

    public void traverse(TreeNode subTree){
        if(subTree !=null) {
            System.out.println("key:" + subTree.getKey() + "--name:" + subTree.getData());
            traverse(subTree.getLeftChild());
            traverse(subTree.getRightChild());
        }
    }

    public void preOrder(TreeNode root){
        if(root!=null){
            System.out.println("前序遍历key:" + root.getKey() + "--name:" + root.getData());
            preOrder(root.getLeftChild());
            preOrder(root.getRightChild());
        }
    }

    public void middleOrder(TreeNode root){
        if(root!=null){
            System.out.println("前序遍历key:" + root.getKey() + "--name:" + root.getData());
            middleOrder(root.getLeftChild());
            middleOrder(root.getRightChild());
        }
    }
    public static void main(String args[]){
        BinaryTree tree=new BinaryTree();
        tree.createBinTree(tree.getRoot());
        System.out.println("判断tree是否为空"+tree.isEmpty());
        System.out.println("查看tree高度"+tree.height());

        System.out.println(tree.size(tree.getRoot()));

        System.out.println("查看A结点父节点："+tree.parent(new TreeNode(1,"A") ));
        System.out.println("查看D结点父节点："+tree.parent(new TreeNode(4,"D") ));
        System.out.println("查看F结点父节点："+tree.parent(new TreeNode(6,"F") ));
        System.out.println("查看traverse数据");
        tree.traverse(tree.getRoot());

        System.out.println("查看前序遍历数据");
       tree.preOrder(tree.getRoot());
    }
}
