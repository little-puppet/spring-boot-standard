package com.standard.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.standard.dao.QuestionMapper;
import com.standard.pojo.Question;
import com.standard.pojo.QuestionStudentVO;
import com.standard.service.QuestionService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author linhongcun
 * @since 2018-08-03
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Override
    public Page<QuestionStudentVO> getQuestionStudent(Page<QuestionStudentVO> page) {
        List<QuestionStudentVO> questionStudent = this.baseMapper.getQuestionStudent(page);
        return page.setRecords(questionStudent);
    }

}
