package com.java.ml.service;

import com.huaban.analysis.jieba.JiebaSegmenter;

import com.java.ml.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Callable;

public class SegmentServiceImpl implements SegmentService, Callable {
    private final static JiebaSegmenter segmenter = new JiebaSegmenter();
    static List<String> test_words = segmenter.sentenceProcess("你好");
    private final static RedisUtil redisUtil = new RedisUtil();

    private String requestId;
    private String text;

    public SegmentServiceImpl(String requestId, String text) {
        this.requestId = requestId;
        this.text = text;
    }

    public Object call() {
        double d = Math.random();
        int sleep_secs = (int)(d*10);
        //System.out.println("requestId: " + requestId + " will sleep: " + sleep_secs + " seconds!");
        try {
            Thread.sleep(sleep_secs * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<String> words = segmenter.sentenceProcess(text);

        redisUtil.lpush(requestId, words);
        return null;
    }
}
