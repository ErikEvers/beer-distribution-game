package org.han.ica.asd.c.leadermigration.testutil;

import org.han.ica.asd.c.leadermigration.IpHandler;

import java.net.UnknownHostException;

public class IpHandlerStub extends IpHandler {
	private static String ipString;

	public IpHandlerStub() {
		IpHandlerStub.ipString = "111";
	}

	@Override
	public String getLocalIp() throws UnknownHostException {
		return ipString;
	}

	public static void setIpString(String ipString) {
		IpHandlerStub.ipString = ipString;
	}
}
