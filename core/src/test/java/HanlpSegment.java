import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.BaseSearcher;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.CRF.CRFSegment;
import com.hankcs.hanlp.seg.Dijkstra.DijkstraSegment;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.hankcs.hanlp.tokenizer.SpeedTokenizer;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class HanlpSegment {


    @Test
    public void demo01() {
        char[] chars = "你好，世界！".toCharArray();
        for (char aChar : chars) {
            System.out.println(aChar);
        }
    }

    @Test
    public void demo02() {
        List<Term> segment = HanLP.segment("你好，欢迎使用HanLP汉语处理包！");
        for (Term term : segment) {
            System.out.println(term.word);
        }
    }

    // 标准分词
    @Test
    public void demo03() {
        List<Term> termList = StandardTokenizer.segment("商品和服务");
        for (Term term : termList) {
            System.out.println(term.word);
        }
    }

    // NLP分词
    @Test
    public void demo04() {
        List<Term> termList = NLPTokenizer.segment("中国科学院计算技术研究所的宗成庆教授正在教授自然语言处理课程");
        for (Term term : termList) {
            System.out.println(term.word);
        }
    }

    // 索引分词
    @Test
    public void demo05() {
        List<Term> termList = IndexTokenizer.segment("主副食品");
        for (Term term : termList) {
            System.out.println(term + " [" + term.offset + ":" + (term.offset + term.word.length()) + "]");
        }
    }

    // N - 最短路径分词
    @Test
    public void demo06() {
        Segment nShortSegment = new NShortSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
        Segment shortestSegment = new DijkstraSegment().enableCustomDictionary(true).enableOrganizationRecognize(true);
        String[] testCase = {
                "今天，刘志军案的关键人物，山西女商人丁书苗在市二中院出庭受审。",
                "刘喜杰石国祥会见吴亚琴先进事迹报告团成员",
        };
        for (String sentence : testCase) {
            System.out.println("N-最短分词：" + nShortSegment.seg(sentence) + "\n最短路分词：" + shortestSegment.seg(sentence));
        }
    }

    // CRF 分词，使用 pom 引入的方式只能使用基本功能(除了CRF,依存句法分析外的全部功能)
    @Test
    public void demo07() {
        CRFSegment crfSegment = new CRFSegment();
        crfSegment.enablePartOfSpeechTagging(true);
        List<Term> termList = crfSegment.seg("你看过穆赫兰天吗");
        System.out.println(termList);
        for (Term term : termList) {
            if (term.nature == null) {
                System.out.println("识别到新词：" + term.word);
            }
        }
    }

    // 极速词典分词，基于 AhoCorasickDoubleArrayTrie 实现的词典分词，适用于“高吞吐”的场合
    @Test
    public void demo08() {
        String text = "江西鄱阳湖干枯，中国最大淡水湖编程大草原";
        System.out.println(SpeedTokenizer.segment(text));
        long start = System.currentTimeMillis();
        int pressure = 1000000;
        for (int i = 0; i < pressure; i++) {
            SpeedTokenizer.segment(text);
        }
        double costTime = (System.currentTimeMillis() - start) / (double) 1000;
        System.out.printf("分词速度：%.2f字每秒", text.length() * pressure / costTime);
    }

    @Test
    public void demo09() {
        String text = "攻城狮逆袭单身狗，迎娶白富美，走上人生巅峰";

        System.out.println(HanLP.segment(text));

        CustomDictionary.add("攻城狮");
        CustomDictionary.insert("白富美", "nz 1024");

        System.out.println(CustomDictionary.add("单身狗", "nz 1024 n 1"));
        System.out.println(CustomDictionary.get("单身狗"));


        final char[] charArray = text.toCharArray();
        CustomDictionary.parseText(charArray, (begin, end, value) -> System.out.printf("[%d:%d]=%s %s\n", begin, end, new String(charArray, begin, end - begin), value));

        BaseSearcher searcher = CustomDictionary.getSearcher(text);
        Map.Entry entry;
        while ((entry = searcher.next()) != null) {
            System.out.println(entry);
        }
        System.out.println(HanLP.segment(text));
    }
}
