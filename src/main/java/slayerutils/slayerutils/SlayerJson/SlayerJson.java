package slayerutils.slayerutils.SlayerJson;

import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SlayerJson {
    private JSONObject json;
    private String path;
    public SlayerJson(JSONObject jo,String path){
        json=jo;
        this.path=path;
    }
    public SlayerJson(JSONObject jo){
        json=jo;
        path="";
    }
    public SlayerJson(){
        json=new JSONObject();
        path="";
    }

    public String stringify(){
        String st = json.toJSONString();
        if(st==null)
            return "{}";
        return st;
    }

    public SlayerJson getSub(String key){
        try {
            JSONObject object = (JSONObject) json.get(key);
            return new SlayerJson(object);
        }catch (NullPointerException ignored){
            return new SlayerJson();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String key){
        try {
            List<T> object = (List<T>) json.get(key);
            return object;
        }catch (NullPointerException ignored){
            return null;
        }
    }

    public List<SlayerJson> getJsonList(String key){
        List<JSONObject> jsons = (List<JSONObject>) json.get(key);
        System.out.println("I am alive "+key);
        return jsons.stream().map(SlayerJson::new)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key){
        if(json==null) return null;
        Object o = json.get(key);
        if(o==null) return null;
        return (T) o;
    }

    public int getInt(String key){
        if(json==null) return -1;
        if(!json.containsKey(key)) return -1;
        int o = Integer.parseInt(json.get(key).toString());
        return o;
    }

    public double getDouble(String key){
        if(json==null) return -1;
        if(!json.containsKey(key)) return -1;
        double o = Double.parseDouble(json.get(key).toString());
        return o;
    }

    public long getLong(String key){
        if(json==null) return -1;
        if(!json.containsKey(key)) return -1;
        long o = Long.parseLong(json.get(key).toString());
        return o;
    }

    public List<String> getKeys(){
        List<String> strings = new ArrayList<>();
        json.keySet().forEach((o) ->{
            strings.add(o.toString());
        });
        return strings;
    }

    public boolean hasHey(String key){
        return getKeys().contains(key);
    }

    public JSONObject getRawJSON(){
        return json;
    }

    public SlayerJson set(String key,Object o){
        if(o==null) {
            json.put(key, null);
            return this;
        }
        if(o.getClass()== SlayerJson.class){
            json.put(key,((SlayerJson)o).getRawJSON());
            return this;
        }
        json.put(key,o);
        return this;
    }

    public SlayerJson setJsonList(String key,List<SlayerJson> list){
        json.put(key,list.stream()
                .map(SlayerJson::getRawJSON)
                .collect(Collectors.toList()));
        return this;
    }

    public boolean saveToFile(Plugin p,String location){
        String pathlocation = p.getDataFolder().toString()+"/"+location+".json";
        File dir = new File(p.getDataFolder().toString());
        if(!dir.exists()) dir.mkdir();
        try {
            FileWriter fl = new FileWriter(pathlocation);
            fl.write(this.stringify());
            fl.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveToFile(){
        try {
            FileWriter fl = new FileWriter(path);
            fl.write(this.stringify());
            fl.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static SlayerJson loadFromFile(Plugin p, String file){
        String pathlocation = p.getDataFolder().toString()+"/"+file+".json";
        Path pt = Paths.get(pathlocation);
        try {
            String content = new String(Files.readAllBytes(pt), StandardCharsets.UTF_8);
            JSONParser parser = new JSONParser();
            JSONObject json;
            json=(JSONObject)parser.parse(content);
            return new SlayerJson(json,pathlocation);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SlayerJson loadFromString(String st){
        try {
            JSONParser parser = new JSONParser();
            JSONObject json;
            System.out.println(st);
            json=(JSONObject)parser.parse(st);
            return new SlayerJson(json);
        } catch (ParseException e) {
            e.printStackTrace();
            return new SlayerJson();
        }
    }

    private int getNumberType(Object o){
        if(o instanceof Integer)
            return 0;
        if(o instanceof Long)
            return 1;
        if(o instanceof Double)
            return 2;
        return -1;
    }
}
