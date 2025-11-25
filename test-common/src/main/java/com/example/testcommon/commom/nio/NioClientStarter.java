package com.example.testcommon.commom.nio;

import java.io.IOException;

/**
 * 2020/3/8
 * created by chenpp
 */
public class NioClientStarter {
    public static void main(String[] args) throws IOException {
        NioClient client = new NioClient();
        client.start("localhost",8999);
        client.listen();
 
    }
}