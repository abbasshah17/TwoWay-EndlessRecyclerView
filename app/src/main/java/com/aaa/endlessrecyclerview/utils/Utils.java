package com.aaa.endlessrecyclerview.utils;

import com.aaa.endlessrecyclerview.ValueItem;

import java.util.ArrayList;

/**
 * Created by Abbas on 3/15/2018.
 */

public class Utils {

    private static int numOfItemsCreated = 0;

    public static ArrayList<ValueItem> generateDummyItemsList(int numOfItems)
    {
        if (numOfItems <= 0) {
            throw new IndexOutOfBoundsException("numOfItems is " + numOfItems + ", could not make any items");
        }

        ArrayList<ValueItem> list = new ArrayList<>();

        for (int i = 0; i < numOfItems; i++) {
            ValueItem item = new ValueItem();
            item.data = "This is the no. " + String.valueOf(numOfItemsCreated++) + " item.";
            list.add(item);
        }

        return list;
    }
}
