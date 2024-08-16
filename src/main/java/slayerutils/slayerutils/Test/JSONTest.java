package slayerutils.slayerutils.Test;

import slayerutils.slayerutils.Slayerutils;
import slayerutils.slayerutils.SlayerJson.SlayerJson;

public class JSONTest {
    public static void run() {
        String jsonstring = "{\"place1\":{\"object1\":123,\"object2\":\"hey maine\",\"object3\":true},\"place2\":12341,\"place3\":{\"object1\":\"Noted\"}}";
        SlayerJson json = SlayerJson.loadFromString(jsonstring);
        System.out.println(json.stringify());
        SlayerJson place1 = json.getSub("place1");
        for (String key : place1.getKeys()) {
            System.out.println(place1.get(key).toString()+" "+place1.get(key).getClass());
        }
        int place2 = json.getInt("place2");
        System.out.println(place2);
        SlayerJson place3 = json.getSub("place3");
        for (String key : place3.getKeys()) {
            System.out.println(place1.get(key).toString()+" "+place1.get(key).getClass());
        }
        json.saveToFile(Slayerutils.getThe(),"test");
    }
}
