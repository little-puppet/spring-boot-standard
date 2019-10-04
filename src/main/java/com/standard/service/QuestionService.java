package com.standard.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.standard.pojo.Question;
import com.standard.pojo.QuestionStudentVO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author linhongcun
 * @since 2018-08-03
 */
public interface QuestionService extends IService<Question> {

    Page<QuestionStudentVO> getQuestionStudent(Page<QuestionStudentVO> page);

}
