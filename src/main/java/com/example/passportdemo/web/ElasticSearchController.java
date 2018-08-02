package com.example.passportdemo.web;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;

/**
 * ElasticSearchController
 *
 * @author muxiaorui
 * @create 2018-08-02 14:34
 **/
@Controller
@RequestMapping("/es")
public class ElasticSearchController {
    @Autowired
    TransportClient esClient;

    @RequestMapping(value={"/home"},method= RequestMethod.GET)
    @ResponseBody
    public String elasticSearch(){
        System.out.println("elasticSearch 请求");
        System.out.println(esClient.transportAddresses().toArray());
        return  "成功";
    }

    @PostMapping("/add")
    public ResponseEntity add(
            @RequestParam(name = "title") String title, @RequestParam(name = "author") String author,
            @RequestParam(name = "word_count") int wordCount,
            @RequestParam(name = "publish_date") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date publishDate
    )
    {
        try {
            XContentBuilder content = XContentFactory.jsonBuilder().startObject()
                    .field("title", title)
                    .field("author", author)
                    .field("word_count", wordCount)
                    .field("publish_date", publishDate.getTime())
                    .endObject();

            IndexResponse result = this.esClient.prepareIndex("book", "novel").setSource(content).get();
            return new ResponseEntity(result.getId(), HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/get")
    public ResponseEntity get(@RequestParam(name = "id", defaultValue="") String id)
    {
        if (id.isEmpty())
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        GetResponse result = this.esClient.prepareGet("book", "novel", id).get();
        if (!result.isExists())
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        System.out.println(result.getSource());
        return new ResponseEntity(result.getSource(), HttpStatus.OK);
    }
}
