package com.github.jason.webdataretrieve;

import com.github.jason.webdataretrieve.parser.HtmlParser;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class HtmlParserUnitTest {
    @Test
    public void htmlParser_isCorrect() throws Exception {
        String html = "<div class=\"info\" id=\"info\"><dl><dt><img src=\"http://www.liantu.com/tiaoma/eantitle.php?title=U3FlV1I4aGdObXkvRGJiYmphOUVFT0VvVFFnRmc1WWg=\" alt=\"商品名称\"></dt><dd><span>参考价格：</span><i>2.5</i>元</dd><dd><span>厂商代码：</span>69475037</dd><dd><span>商品国别：</span>中国</dd><dd><span>厂商名称：</span>上海晨光文具股份有限公司</dd></dl></div>";

        HtmlParser parser = HtmlParser.getInstance();
        parser.setHtml(html);
        List<String> result = new ArrayList<>();
        result = parser.parse(true);

        assertEquals(result.equals(null),false);
        assertNotEquals(result.size(), 0);
    }
}