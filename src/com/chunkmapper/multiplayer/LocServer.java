package com.chunkmapper.multiplayer;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import com.chunkmapper.Point;

public class LocServer implements Container {
	private final Point rootPoint;
	private final File parentFolder;
	private static final String USER_NOT_FOUND = "<html><head></head>" +
			"<body>" +
			"User not found" +
			"<script>" +
			"setTimeout(function() {" +
			"location.reload()}, 10000)" +
			"</script>" +
			"</body></html>";

	private static String makeLink(String href, String text) {
		return String.format("<a href=\"%s\" target=\"_blank\">%s</a>", href, text);
	}
	private static String getBody(double lat, double lon) {
		String gm = String.format("https://maps.google.com.au/?q=loc:%s+%s", lat, lon);
		String osm = String.format("http://www.openstreetmap.org/index.html?lat=%s&lon=%s&zoom=15", lat, lon);
		return "<html>" +
		"<head></head>" +
		"<body>" +
		makeLink(gm, "Google maps") + "<br /><br />" +
		makeLink(osm, "Open Street Map") + "<br /><br />" + 
		"<script>" +
		"setTimeout(function() {" +
		"location.reload()}, 10000)" +
		"</script>" +
		"</body></html>";
	}

	private LocServer(Point rootPoint, File parentFolder) {
		this.rootPoint = rootPoint;
		this.parentFolder = parentFolder;
	}

	public void handle(Request request, Response response) {
		try {
			HashMap<String, Point> playerPositions = MPPointManager.readPositions(parentFolder);
			String user = request.getPath().toString().substring(1);
			PrintStream body = response.getPrintStream();
			if (playerPositions.containsKey(user)) {
				Point p = playerPositions.get(user);
				double lon = (p.x + rootPoint.x * 512) / 3600.;
				double lat = -(p.z + rootPoint.z * 512) / 3600.;
				body.print(getBody(lat, lon));
			} else {
				body.print(USER_NOT_FOUND);
			}
			body.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	} 

//	public static void main(String[] list) throws Exception {
//		Point rootPoint = new Point(611, -197);
//		File parentFolder = new File("game/world");
//	}
	public static void startLocServer(Point rootPoint, File parentFolder) throws IOException {
		Container container = new LocServer(rootPoint, parentFolder);
		Server server = new ContainerServer(container);
		Connection connection = new SocketConnection(server);
		SocketAddress address = new InetSocketAddress(1234);

		connection.connect(address);
		System.out.println("started LocServer");
	}
}