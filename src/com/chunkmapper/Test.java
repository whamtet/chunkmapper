package com.chunkmapper;

import java.io.IOException;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;

public class Test {
	public static void main(String[] args) throws Exception {
		
		MemcachedClient mc = null;
		String username = "63bcf0", password = "0d40bd8aab";
		String host = "mc4.dev.ec2.memcachier.com:11211";
		AuthDescriptor ad = new AuthDescriptor(new String[]{"PLAIN"},
				new PlainCallbackHandler(username, password));

		//Then connect using the ConnectionFactoryBuilder.  Binary is required.
		try {
			if (mc == null) {
				mc = new MemcachedClient(
						new ConnectionFactoryBuilder().setProtocol(Protocol.BINARY)
						.setAuthDescriptor(ad)
						.build(),
						AddrUtil.getAddresses(host));
			}
		} catch (IOException ex) {
			System.err.println("Couldn't create a connection, bailing out: \nIOException " + ex.getMessage());
		}
		mc.set("hi", 300, "there");
		System.out.println(mc.get("hi"));
		mc.shutdown();
		System.out.println("done");
	}
}
