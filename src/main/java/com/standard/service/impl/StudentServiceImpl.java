package com.standard.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.standard.dao.QuestionMapper;
import com.standard.dao.StudentMapper;
import com.standard.pojo.Question;
import com.standard.pojo.Student;
import com.standard.service.StudentService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author linhongcun
 * @since 2018-08-03
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student>  implements StudentService {


}