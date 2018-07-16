package com.example.passportdemo.sort;

import java.util.Arrays;

/**
 * 冒泡排序
 *
 * @author muxiaorui
 * @create 2018-07-16 10:40
 **/
public class BubbleSort {
    private static void sort(int array[]){
        System.out.println(Arrays.toString(array));
        int tmp=0;
        for(int i=0;i<array.length;i++){
            for(int j=0;j<array.length-i-1;j++){
                if(array[j]>array[j+1]){
                    tmp=array[j];
                    array[j]=array[j+1];
                    array[j+1]=tmp;
                    System.out.println(Arrays.toString(array));
                }
            }
        }
    }

    private static void sort2(int array[]){
        System.out.println(Arrays.toString(array));
        int tmp=0;

        for(int i=0;i<array.length;i++){
            boolean isSorted=true;
            for(int j=0;j<array.length-i-1;j++){
                if(array[j]>array[j+1]){
                    tmp=array[j];
                    array[j]=array[j+1];
                    array[j+1]=tmp;
                    isSorted=false;
                }
                System.out.println(Arrays.toString(array));
            }
            if(isSorted){
               break;
            }
        }
    }

    private static void sort3(int array[]){
        System.out.println(Arrays.toString(array));
        int tmp=0;
        int lastExchangeIndex=0;
        int sortBorder=array.length-1;
        for(int i=0;i<array.length;i++){
            boolean isSorted=true;
            for(int j=0;j<sortBorder;j++){
                if(array[j]>array[j+1]){
                    tmp=array[j];
                    array[j]=array[j+1];
                    array[j+1]=tmp;
                    isSorted=false;
                    lastExchangeIndex=j;
                }
                System.out.println(Arrays.toString(array));
            }
            sortBorder=lastExchangeIndex;
            if(isSorted){
                break;
            }
        }
    }
    public static void main(String[] args){
        int[] array=new int[]{5,8,6,3,9,2,1,7};
        int[] array2=array;
        sort(array);
        System.out.println(Arrays.toString(array));
        sort2(array2);
        System.out.println(Arrays.toString(array2));
    }
}
