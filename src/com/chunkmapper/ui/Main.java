package com.chunkmapper.ui;

import com.chunkmapper.GameMetaInfo;
import com.chunkmapper.ManagingThread;
import com.chunkmapper.Point;
import com.chunkmapper.admin.BucketInfo;
import com.chunkmapper.admin.GlobalSettings;
import com.chunkmapper.gui.dialog.NewMapDialog;
import com.chunkmapper.interfaces.GeneratingLayer;
import com.chunkmapper.interfaces.MappedSquareManager;
import com.chunkmapper.interfaces.PlayerIconManager;
import com.chunkmapper.multiplayer.LocServer;
import com.chunkmapper.multiplayer.MPLevelDat;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final MappedSquareManager mappedSquareManager = new MappedSquareManager() {
        @Override
        public void addFinishedPoint(Point p) {
            logRegion(p);
        }

        @Override
        public void addUnfinishedPoint(Point p) {
            // TODO
        }
    };

    private static final PlayerIconManager playerIconManager = new PlayerIconManager() {
        @Override
        public void setLocation(double lat, double lon) {
            logSteve(lat, lon);
        }
    };

    private static final GeneratingLayer generatingLayer = new GeneratingLayer() {
        @Override
        public void zoomTo() {

        }

        @Override
        public void zoomTo(double lat, double lon) {

        }

        @Override
        public void cancel(boolean selfCalled) {

        }

        @Override
        public void cancel() {

        }
    };

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

    private static File prepareDir(File f, boolean delete) {
        if (delete && f.exists()) {
            try {
                FileUtils.deleteDirectory(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        f.mkdir();
        return f;
    }

    public static void main(String[] args) throws IOException {
        Map<String, String> parsed = parseArgs(args);
        double lat = Double.parseDouble(parsed.get("lat"));
        double lng = Double.parseDouble(parsed.get("lng"));
        String name = parsed.get("name");

//        double lat = 13.7563;
//        double lng = 100.5018;
//        String name = "asdf";
        int verticalExaggeration = 1;

        GlobalSettings globalSettings = new GlobalSettings();
        NewMapDialog.NewGameInfo newGameInfo = new NewMapDialog.NewGameInfo(name);
        File saves = new File("/Users/matthew/Library/Application Support/minecraft/saves");
        File gameFolder = new File(saves, name);
        if (!gameFolder.exists()) {
            gameFolder.mkdirs();
        }

        prepareDir(new File(gameFolder, "chunkmapper"), false);
        prepareDir(new File(gameFolder, "region"), false);
        prepareDir(new File(gameFolder, "players"), false);

        BucketInfo.initMap();

        ManagingThread t = new ManagingThread(
                lat,
                lng,
                gameFolder,
                mappedSquareManager,
                playerIconManager,
                globalSettings,
                generatingLayer,
                newGameInfo);
        t.start();
    }
}
