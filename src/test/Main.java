package test;


import model.map.Map;
import model.map.MapBuilder;

public class Main {

    public static void main (String[] args)
    {
        Map map = MapBuilder.createMap();
        System.out.println(map.toIDs());
    }

}
