package com.hb.test;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import lombok.Data;
import org.junit.Test;

import java.util.List;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/4/28
 */
public class CsvUtilTest {
    @Test
    public void readCsv() {
        CsvReader reader = CsvUtil.getReader();
        CsvData csvData = reader.read(FileUtil.file("test_bean.csv"));
        csvData.getRows().forEach(Console::log);
    }

    @Test
    public void readCsvBean() {
        CsvReader reader = CsvUtil.getReader();
        List<TestBean> result = reader.read(
                ResourceUtil.getUtf8Reader("test_bean.csv"), TestBean.class);
        result.forEach(Console::log);
    }
}


@Data
class TestBean {
    // 如果csv中标题与字段不对应，可以使用alias注解设置别名
    @Alias("姓名")
    private String name;
    private String gender;
    private String focus;
    private Integer age;
}
