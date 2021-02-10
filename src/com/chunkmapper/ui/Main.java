package com.chunkmapper.ui;

import com.chunkmapper.GameMetaInfo;
import com.chunkmapper.ManagingThread;
import com.chunkmapper.Point;
import com.chunkmapper.admin.BucketInfo;
import com.chunkmapper.gui.dialog.NewMapDialog;
import com.chunkmapper.interfaces.MappedSquareManager;
import com.chunkmapper.interfaces.PlayerIconManager;
import com.chunkmapper.writer.LevelDat;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final MappedSquareManager mappedSquareManager = new MappedSquareManager() {
        @Override
        public void addFinishedPoint(Point p) {
            logRegion(p, 2);
        }

        @Override
        public void addUnfinishedPoint(Point p) {
            logRegion(p, 1);
        }
    };

    private static final PlayerIconManager playerIconManager = (lat, lon) -> logSteve(lat, lon);

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

    private static void logSteve(double lat, double lng) {
        JSONObject o = new JSONObject();
        o.put("lat", lat);
        o.put("lng", lng);
        o.put("evt", "steve");
        logback(o.toJSONString());
    }

    private static void logRegion(Point p1, int stage) {
        Point p2 = p1.plus(1, 1);
        JSONObject o = new JSONObject();
        o.put("lat1", p2.getRegionLat());
        o.put("lng1", p1.getRegionLng());
        o.put("lat2", p1.getRegionLat());
        o.put("lng2", p2.getRegionLng());
        o.put("evt", "region" + stage);
        logback(o.toJSONString());
    }

    private static void logGoto(double lat, double lng) {
        JSONObject o = new JSONObject();
        o.put("lat1", lat);
        o.put("lng1", lng);
        o.put("evt", "goto");
        logback(o.toJSONString());
    }

    public static void main(String[] args) throws IOException {
        Map<String, String> parsed = parseArgs(args);
        File gameFolder = new File(parsed.get("name"));
        double lat, lng;
        if (parsed.containsKey("lat")) {
            lat = Double.parseDouble(parsed.get("lat"));
            lng = Double.parseDouble(parsed.get("lng"));
        } else {
            GameMetaInfo info = new GameMetaInfo(gameFolder, 0, 0, 0, false);
            LevelDat lLevelDat = LevelDat.getFromGameFolder(gameFolder);
            Point relativePlayerPoint = lLevelDat.getPlayerPosition();
            lat = - (relativePlayerPoint.z + info.rootPoint.z * 512) / 3600.;
            lng = (relativePlayerPoint.x + info.rootPoint.x * 512) / 3600.;
            logGoto(lat, lng);
        }

        if (!gameFolder.exists()) {
            gameFolder.mkdirs();
        }

        (new File(gameFolder, "chunkmapper")).mkdir();
        (new File(gameFolder, "region")).mkdir();
        (new File(gameFolder, "players")).mkdir();

        BucketInfo.initMap();

        ManagingThread t = new ManagingThread(
                lat,
                lng,
                gameFolder,
                mappedSquareManager,
                playerIconManager,
                null,
                null);
        t.start();
    }
}
