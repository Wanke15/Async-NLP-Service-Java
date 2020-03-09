package com.java.ml.controller;

import com.java.ml.model.SegmentSubmitData;
import com.java.ml.service.SegmentServiceImpl;
import com.java.ml.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class NlpController {
    @Autowired
    RedisUtil redisUtil = new RedisUtil();

    SegmentServiceImpl segmentService = new SegmentServiceImpl("202003081420", "301004F0DA14682B");

    ExecutorService pool = Executors.newFixedThreadPool(24);

    @RequestMapping(value = "/health", method = RequestMethod.GET)
    String health() {
        return "I am doing good!";
    }

    @ResponseBody
    @RequestMapping(value = "/api/v0.1/segment/submit", method = RequestMethod.POST)
    public Map<String, Object> submit(@RequestBody SegmentSubmitData segmentSubmitData)
    {
        Map<String,Object> segmentResponse = new HashMap<String, Object>();
        // segmentService.submit(segmentSubmitData.getRequestId(), segmentSubmitData.getText());
        pool.submit(new SegmentServiceImpl(segmentSubmitData.getRequestId(), segmentSubmitData.getText()));
        segmentResponse.put("code", "200");
        segmentResponse.put("requestId", segmentSubmitData.getRequestId());
        segmentResponse.put("msg", "提交成功");
        return segmentResponse;
    }

    @ResponseBody
    @RequestMapping("/api/v0.1/segment/fetchResult")
    public Map<String, Object> fetchResult(@RequestParam(value = "requestId", required = true) String requestId)
    {
        Map<String,Object> fetchResponse = new HashMap<String, Object>();
        List<String> words = redisUtil.lrange(requestId, 0, -1);

        fetchResponse.put("code", "200");
        fetchResponse.put("requestId", requestId);
        fetchResponse.put("data", words);
        fetchResponse.put("msg", "查询成功");
        return fetchResponse;
    }





}
