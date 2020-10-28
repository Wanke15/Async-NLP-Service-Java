package com.java.ml.service;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import com.huaban.analysis.jieba.JiebaSegmenter;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

@Service
public class NlpBasicImpl implements Serializable {
    private final static JiebaSegmenter segmenter = new JiebaSegmenter();

    public NlpBasicImpl()
    {
        String initText = "你好";
        segmenter.sentenceProcess(initText);
        HanLP.parseDependency(initText);

    }

    public List<String> segment(String text)
    {
        return segmenter.sentenceProcess(text);
    }

    public List<Map<String, String>> segmentHanlp(String text)
    {
//        List<Term> termList = StandardTokenizer.segment(text);
        List<Term> termList = StandardTokenizer.segment(text);
        System.out.println(termList);
        List<Map<String, String>> result = new ArrayList<>();
        for (Term term : termList)
        {
            Map<String, String> termResult = new HashMap<>();
            termResult.put(term.word, term.nature.toString());
            result.add(termResult);
        }
        return result;
    }

    public List<Map<String, String>> segmentNlpTokenizer(String text)
    {
        List<Term> termList = NLPTokenizer.segment(text);
        System.out.println(termList);
        List<Map<String, String>> result = new ArrayList<>();
        for (Term term : termList)
        {
            Map<String, String> termResult = new HashMap<>();
            termResult.put(term.word, term.nature.toString());
            result.add(termResult);
        }
        return result;
    }

    public List<Map<String, String>> depParse(String text)
    {
        CoNLLSentence sentence = HanLP.parseDependency(text);

        CoNLLWord[] wordArray = sentence.getWordArray();
        String rootWord = "";
        Map<String, String> rootWordRes = new HashMap<>();

        Map<String, Map<String, String>> depDict = new HashMap<>();

        List<String> nouns = new ArrayList<>();

        // 构建定中关系词典、确定核心词、名词
        for (int i = 0; i <= wordArray.length - 1; i++)
        {
            CoNLLWord word = wordArray[i];

//            System.err.println(word);
            if (word.DEPREL.equals("定中关系")) {
                depDict.put(word.HEAD.LEMMA, new HashMap<String, String>() {{
                    put("rel", word.DEPREL);
                    put("lemma", word.LEMMA);
                }});
            }

            if (word.DEPREL.equals("核心关系") && !word.POSTAG.equals("v"))
            {
                rootWord = word.LEMMA;
            }

            if (word.POSTAG.equals("n"))
            {
                nouns.add(word.LEMMA);
            }
        }
//         System.err.println("依存词典: " + depDict);
        // 最终结果
        List<Map<String, String>> result = new ArrayList<>();

        // 遍历句法分析结果
        for (int i = 0; i <= wordArray.length - 1; i++)
        {
            CoNLLWord word = wordArray[i];

//            System.out.printf("%s --(%s)--> %s\n", word.LEMMA, word.DEPREL, word.HEAD.LEMMA);
            // 与核心词存在定中关系
            if (word.HEAD.LEMMA.equals(rootWord) && word.DEPREL.equals("定中关系"))
            {
//                System.err.println("################# rootWord: " + rootWord + ", modifier: " + " &&&&&&&&&&&&" + word.LEMMA + depDict);
                rootWordRes.put("rootWord", rootWord);
                String completeModifier = word.LEMMA;
                if (depDict.containsKey(completeModifier)) {
                    String rel = depDict.get(completeModifier).get("rel");
                    if (rel != null && rel.equals("定中关系")) {
                        completeModifier = depDict.get(completeModifier).get("lemma") + completeModifier;
                    }
                }
                rootWordRes.put("modifier", completeModifier);
            }

            // 存在并列关系的核心词
            if (word.HEAD.LEMMA.equals(rootWord) && word.DEPREL.equals("并列关系"))
            {

                String modifier = "";
                if (depDict.containsKey(word.LEMMA)) {
                    String rel = depDict.get(word.LEMMA).get("rel");
                    if (rel != null && rel.equals("定中关系")) {
                        modifier = depDict.get(word.LEMMA).get("lemma");
                    }
                }

                if (!modifier.equals("")) {
                    String finalModifier = modifier;
                    result.add(new HashMap<String, String>() {{
                        put("rootWord", word.LEMMA);
                        put("modifier", finalModifier);
                    }});

                }
                else {
                    result.add(new HashMap<String, String>() {{
                        put("rootWord", word.LEMMA);
                    }});
                }
            }
        }

        if (!rootWordRes.isEmpty())
        {
            result.add(rootWordRes);
        }
        else
        {
            if (!rootWord.equals(""))
            {
                String finalRootWord = rootWord;
                result.add(new HashMap<String, String>(){{ put("rootWord", finalRootWord);}});
            }
            else
            {
                for (String noun : nouns)
                {
                    result.add(new HashMap<String, String>(){{ put("rootWord", noun);}});
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
//        NlpBasicImpl nlpBasic = new NlpBasicImpl();
//        nlpBasic.segment("我爱你中国");
//        nlpBasic.segmentHanlp("我爱你中国");
//        nlpBasic.segmentNlpTokenizer("我爱你中国");
//
//        System.out.println(NLPTokenizer.analyze("我的希望是希望张晚霞的背影被晚霞映红").translateLabels());

//        CoNLLSentence sentence = HanLP.parseDependency("香蕉味薯片");
//        CoNLLSentence sentence = HanLP.parseDependency("新鲜的芒果");
        CoNLLSentence sentence = HanLP.parseDependency("新鲜的三文鱼和芥末");
//        CoNLLSentence sentence = HanLP.parseDependency("大码女装");
        System.out.println(sentence);
        // 可以方便地遍历它
//        for (CoNLLWord word : sentence)
//        {
//            System.out.printf("%s --(%s)--> %s\n", word.LEMMA, word.DEPREL, word.HEAD.LEMMA);
//        }
        // 也可以直接拿到数组，任意顺序或逆序遍历
        CoNLLWord[] wordArray = sentence.getWordArray();
//        for (int i = wordArray.length - 1; i >= 0; i--)
        String rootWord = "";
        for (int i = 0; i <= wordArray.length - 1; i++)
        {
            CoNLLWord word = wordArray[i];
            if (word.DEPREL.equals("核心关系"))
            {
                rootWord = word.LEMMA;
            }
        }
        List<Map<String, String>> result = new ArrayList<>();
        for (int i = 0; i <= wordArray.length - 1; i++)
        {
            CoNLLWord word = wordArray[i];
            System.out.printf("%s --(%s)--> %s\n", word.LEMMA, word.DEPREL, word.HEAD.LEMMA);
            if (word.HEAD.LEMMA.equals(rootWord) && word.DEPREL.equals("定中关系"))
            {
                String finalRootWord = rootWord;
                result.add(new HashMap<String, String>(){{ put("rootWord", finalRootWord);}});
                result.add(new HashMap<String, String>(){{ put("modifier", word.LEMMA);}});
            }
        }
        // 还可以直接遍历子树，从某棵子树的某个节点一路遍历到虚根
//        CoNLLWord head = wordArray[12];
//        while ((head = head.HEAD) != null)
//        {
//            if (head == CoNLLWord.ROOT) System.out.println(head.LEMMA);
//            else System.out.printf("%s --(%s)--> ", head.LEMMA, head.DEPREL);
//        }
    }
}
