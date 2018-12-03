package com.kcm;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义词性
 */
public class SegmentWords {

    public static void main(String[] args) {
        System.out.println(isName("肖植金"));
    }


    /**
     * 判断添加新词典之前 是否是名字
     * 是 true
     * 否 false
     */
    public static Boolean isName(String word) {
        Segment segment = getSegment();
        List<TermInput> result = segment.seg(word).stream().map(eachWorld -> new TermInput(eachWorld.word, eachWorld.nature.toString(), eachWorld.offset)).collect(Collectors.toList());
        return result.size() == 1 && result.get(0).nature.equals("nr");
    }


    public static String getCoreName(String name) {
        List<Term> seg = getSegment().seg(name);
        StringBuilder coreBuilder = new StringBuilder();

        for (Term s : seg) {
            System.out.print(s.word + s.nature + " ");
            if (!s.nature.toString().equals("nt") && !s.nature.toString().equals("ns") && !s.nature.toString().equals("w") && !s.nature.toString().equals("nis")) {
                coreBuilder.append(s.word);
            }
        }

        System.out.println(coreBuilder);
        return coreBuilder.toString();
    }


    public static List<TermInput> segment(String word) {
        Segment segment = getSegment();
        List<TermInput> result = segment.seg(word).stream().map(eachWorld -> new TermInput(eachWorld.word, eachWorld.nature.toString(), eachWorld.offset)).collect(Collectors.toList());
        result.forEach(i -> {
            System.out.print(i.getWord() + " ");
            System.out.print(i.getNature() + " ");
        });
        System.out.println();
        return result;
    }

    private static Segment getSegment() {
        return HanLP.newSegment()
                .enableCustomDictionary(true)
                .enableOrganizationRecognize(true)
                .enablePlaceRecognize(true)
                .enableCustomDictionaryForcing(true);
    }
}

