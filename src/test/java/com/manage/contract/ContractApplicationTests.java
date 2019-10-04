package com.manage.contract;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.standard.ContractApplication;
import com.standard.pojo.QuestionStudentVO;
import com.standard.service.QuestionService;
import org.apache.commons.collections4.SplitMapUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ContractApplication.class)
public class ContractApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    QuestionService questionService;

    @Test
    public void testQuery(){
        Map<String, Object> map = new HashMap<>();
        Page<QuestionStudentVO> questionStudent = questionService.getQuestionStudent(new Page<>(1, 5));
        System.out.println(1);
    }

}
