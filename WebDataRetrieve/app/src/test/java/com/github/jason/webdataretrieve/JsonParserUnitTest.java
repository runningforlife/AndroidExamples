package com.github.jason.webdataretrieve;

import android.util.JsonReader;

import com.github.jason.webdataretrieve.parser.JsonParser;

import org.junit.Test;
import org.mockito.Mock;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by JasonWang on 2016/7/31.
 */
public class JsonParserUnitTest {

    @Mock
    private JsonReader mReader;

    public static final String JsonString = "{" +
        " * [\n" +
        " *   {\n" +
        " *     \"id\": 912345678901,\n" +
        " *     \"text\": \"How do I read JSON on Android?\",\n" +
        " *     \"geo\": null,\n" +
        " *     \"user\": {\n" +
        " *       \"name\": \"android_newb\",\n" +
        " *       \"followers_count\": 41\n" +
        " *      }\n" +
        " *   },\n" +
        " *   {\n" +
        " *     \"id\": 912345678902,\n" +
        " *     \"text\": \"@android_newb just use android.util.JsonReader!\",\n" +
        " *     \"geo\": [50.454722, -104.606667],\n" +
        " *     \"user\": {\n" +
        " *       \"name\": \"jesse\",\n" +
        " *       \"followers_count\": 2\n" +
        " *     }\n" +
        " *   }\n" +
        " * ]}";

    @Test
    public void jsonParser_isCorrect() throws Exception{
        JsonParser jsonParser = new JsonParser();

        byte[] bytes = JsonString.getBytes();
        ByteArrayInputStream byteIs = new ByteArrayInputStream(bytes);
        mReader = new JsonReader(new InputStreamReader(byteIs,"UTF-8"));

        List<JsonParser.Message> messages = jsonParser.readJsonStream(mReader);
        assertEquals(messages.size(), 2);
    }
}
