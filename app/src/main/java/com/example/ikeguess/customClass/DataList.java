package com.example.ikeguess.customClass;

import java.util.ArrayList;

public class DataList {

    public final ArrayList<Data> furnitureSimple;

    public final ArrayList<Data> furnitureMedium;

    public final ArrayList<DataMaps> furnitureMaps;


    public DataList(ArrayList<Data> furnitureSimple, ArrayList<Data> furnitureMedium, ArrayList<DataMaps> furnitureMaps) {
        this.furnitureSimple = furnitureSimple;
        this.furnitureMedium = furnitureMedium;
        this.furnitureMaps = furnitureMaps;
    }
    public int size() {
        return furnitureSimple.size();
    }
}
