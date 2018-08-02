package com.example.passportdemo.Elasticsearch;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Elasticsearch
 *
 * @author muxiaorui
 * @create 2018-08-01 14:17
 **/
public class Elasticsearch {
    public final static String HOST = "127.0.0.1";

    public final static int PORT = 9300;//http请求的端口是9200，客户端是9300

    /**
     * 测试Elasticsearch客户端连接
     * @Title: test1
     * @author sunt
     * @date 2017年11月22日
     * @return void
     * @throws UnknownHostException
     */
    @SuppressWarnings("resource")

    public static void main(String args[]) throws UnknownHostException {
        //创建客户端
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddresses(
                new InetSocketTransportAddress(InetAddress.getByName(HOST),PORT));

        System.out.println("Elasticsearch connect info:" + client.toString());

        //关闭客户端
        client.close();
    }
}
