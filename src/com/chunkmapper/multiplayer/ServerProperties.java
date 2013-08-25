package com.chunkmapper.multiplayer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ServerProperties {
	public static final String GENERATOR_SETTINGS = "2;7,12,8,8";

	private static String makeStr(String levelName) {
		StringBuilder sb = new StringBuilder();
		sb.append("#Minecraft server properties");
		sb.append("#Fri Aug 23 18:57:31 EST 2013");
		sb.append("generator-settings=" + GENERATOR_SETTINGS);
		sb.append("allow-nether=true");
		sb.append("level-name=" + levelName);
		sb.append("enable-query=false");
		sb.append("allow-flight=false");
		sb.append("server-port=25565");
		sb.append("level-type=FLAT");
		sb.append("enable-rcon=false");
		sb.append("force-gamemode=false");
		sb.append("level-seed=");
		sb.append("server-ip=");
		sb.append("max-build-height=256");
		sb.append("spawn-npcs=true");
		sb.append("white-list=false");
		sb.append("spawn-animals=true");
		sb.append("texture-pack=");
		sb.append("snooper-enabled=true");
		sb.append("hardcore=false");
		sb.append("online-mode=true");
		sb.append("pvp=true");
		sb.append("difficulty=0");//peaceful, easy, normal, hard
		sb.append("gamemode=0");
		sb.append("max-players=20");
		sb.append("spawn-monsters=false"); //no monsters for now
		sb.append("view-distance=10");
		sb.append("generate-structures=true");
		sb.append("spawn-protection=16");
		sb.append("motd=" + levelName + " (Chunkmapper world)");
		return sb.toString();
	}
	public static void spitProperties(String levelName, File f) throws IOException {
		if (!f.exists()) {
			FileWriter writer = new FileWriter(f);
			writer.write(makeStr(levelName));
			writer.close();
		}
	}

}
