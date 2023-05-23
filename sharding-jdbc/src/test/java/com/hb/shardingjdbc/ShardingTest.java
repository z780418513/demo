package com.hb.shardingjdbc;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.google.common.collect.Lists;
import com.hb.shardingjdbc.mapper.StudentMapper;
import com.hb.shardingjdbc.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class ShardingTest {
    @Resource
    private StudentMapper studentMapper;

    @Test
    public void uuidTest() {
        for (int i = 0; i < 200; i++) {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            long mode = Math.abs((long) uuid.hashCode()) % 16;
            System.out.println("uuid = " + "-----" + mode);
        }
    }

    @Test
    public void addStudent() {
        for (int i = 0; i < 10; i++) {
            Student s = new Student();
            //主键就不需要了，因为主键是雪花算法
            //sdto.setId(1);
            s.setName("张三");
            s.setStatus("1");
            s.setTearchid(10);
            s.setClassName("E009");
            String uuid = UUID.randomUUID().toString().replace("-", "");
            s.setStNo(uuid);
            studentMapper.insert(s);
        }
    }


    @Test
    public void query() {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Student::getClassName, "E009")
                .eq(Student::getStNo, "ec3a1ef23b0941d88d9f8cc094619a7a");
        Student student = studentMapper.selectOne(wrapper);
        System.out.println("student = " + student);
    }

    @Test
    public void query2() {
        List<Student> students = studentMapper.selectBatchIds(Lists.newArrayList(1660969255527342082L, 1660969255686725634L));
        System.out.println("students = " + students);
    }

    @Test
    public void queryStudent() {
        QueryWrapper result = new QueryWrapper<Student>();
        // 精确查询
        //result.eq("id", 1535492214268665858L);

        result.in("id", 1535492214268665858L, 1535492218039345154L);
        result.between("tearchid", 1L, 6L);

        for (Object o : studentMapper.selectList(result)) {
            System.out.println("查询结果：" + o);
        }
    }
}
