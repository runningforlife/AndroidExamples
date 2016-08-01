package com.github.jason.webdataretrieve;

import com.github.jason.webdataretrieve.parser.XmlParser;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by JasonWang on 2016/7/31.
 */
public class XmlParserUnitTest {

    @Test
    public void xmlParser_isCorrect(){
        try {
            File file = new File("../data/employee.xml");
            InputStream in = new FileInputStream(file);
            XmlParser parser = XmlParser.getInstance();
            List<XmlParser.Employee> employees = parser.parserByXMLPull(in);

            assertNotEquals(employees,null);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }


    }

}
