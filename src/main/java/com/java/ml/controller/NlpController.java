package com.java.ml.controller;

import com.java.ml.model.SegmentSubmitData;
import com.java.ml.service.NlpBasicImpl;
import com.java.ml.service.SegmentServiceImpl;
import com.java.ml.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class NlpController {
    private NlpBasicImpl nlpBasic = new NlpBasicImpl();

    @RequestMapping(value = "/health", method = RequestMethod.GET)
    String health() {
        return "I am doing great!";
    }

    @ResponseBody
    @RequestMapping(value = "/api/v0.1/nlp/segment", method = RequestMethod.GET)
    public Map<String, Object> segment(@RequestParam(value = "text", required = true) String text)
    {
        Map<String,Object> segmentResponse = new HashMap<String, Object>();
        List<String> result = nlpBasic.segment(text);
        segmentResponse.put("code", "200");
        segmentResponse.put("data", result);
        segmentResponse.put("msg", "success");
        return segmentResponse;
    }

    @ResponseBody
    @RequestMapping(value = "/api/v0.1/nlp/segmentHanlp", method = RequestMethod.GET)
    public Map<String, Object> segmentHanlp(@RequestParam(value = "text", required = true) String text)
    {
        Map<String,Object> segmentResponse = new HashMap<String, Object>();
//        List<Map<String, String>> result = nlpBasic.segmentHanlp(text);
        List<Map<String, String>> result = nlpBasic.segmentNlpTokenizer(text);
        segmentResponse.put("code", "200");
        segmentResponse.put("data", result);
        segmentResponse.put("msg", "success");
        return segmentResponse;
    }

    @ResponseBody
    @RequestMapping(value = "/api/v0.1/nlp/depParse", method = RequestMethod.GET)
//    @Cacheable(value = "depCache", key = "#text")
    public Map<String, Object> depParse(@RequestParam(value = "text", required = true) String text)
    {
        Map<String,Object> segmentResponse = new HashMap<String, Object>();
        List<Map<String, String>> result = nlpBasic.depParse(text);
        segmentResponse.put("code", "200");
        segmentResponse.put("data", result);
        segmentResponse.put("msg", "success");
        return segmentResponse;
    }

    @ResponseBody
    @RequestMapping(value = "/api/v0.2/nlp/depParse", method = RequestMethod.GET)
    @Cacheable(value = "depCache", key = "#text")
    public Map<String, Object> depParse2(@RequestParam(value = "text", required = true) String text)
    {
        Map<String,Object> segmentResponse = new HashMap<String, Object>();
        List<Map<String, String>> result = nlpBasic.depParse(text);
        segmentResponse.put("code", "200");
        segmentResponse.put("data", result);
        segmentResponse.put("msg", "success");
        return segmentResponse;
    }





}
