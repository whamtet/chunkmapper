package com.chunkmapper.ui;

import com.chunkmapper.Point;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Main {
    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> out = new HashMap<String, String>();
        for (int i = 0; i < args.length; i += 2) {
            out.put(args[i], args[i + 1]);
        }
        return out;
    }

    private static void logback(String s) {
        System.out.println("logback: " + s);
    }

    public static void logSteve(double lat, double lng) {
        JSONObject o = new JSONObject();
        o.put("lat", lat);
        o.put("lng", lng);
        o.put("evt", "steve");
        logback(o.toJSONString());
    }

    public static void logRegion(Point p1) {
        Point p2 = p1.plus(1, 1);
        JSONObject o = new JSONObject();
        o.put("lat1", p2.getRegionLat());
        o.put("lng1", p1.getRegionLng());
        o.put("lat2", p1.getRegionLat());
        o.put("lng2", p2.getRegionLng());
        o.put("evt", "region");
        logback(o.toJSONString());
    }

    public static void main(String[] args) {
        Map<String, String> parsed = parseArgs(args);
        double lat = Double.parseDouble(parsed.get("lat"));
        double lng = Double.parseDouble(parsed.get("lng"));
        logRegion(Point.getRegionPoint(lat, lng));
    }
}
