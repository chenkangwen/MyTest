package com.example.testcommon.commom.collection;

import java.io.IOException;

/**
 * 2020/3/8
 * created by chenpp
 */
public class NioServerStarter {

    public static void main(String[] args) throws IOException {
        NioServer nioServer = new NioServer();
        nioServer.startServer(8999);
        nioServer.listen();
    }
}