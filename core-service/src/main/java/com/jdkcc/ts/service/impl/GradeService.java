package com.jdkcc.ts.service.impl;

import com.jdkcc.ts.common.util.ObjectConvertUtil;
import com.jdkcc.ts.dal.entity.Grade;
import com.jdkcc.ts.dal.repository.GradeDAO;
import com.jdkcc.ts.dal.repository.IBaseDAO;
import com.jdkcc.ts.service.dto.request.BasicSearchReq;
import com.jdkcc.ts.service.dto.request.GradeReq;
import com.jdkcc.ts.service.dto.response.GradeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly = true)
public class GradeService extends ABaseService<Grade, Long> {

    private final GradeDAO gradeDAO;

    @Autowired
    public GradeService(GradeDAO gradeDAO) {
        this.gradeDAO = gradeDAO;
    }

    @Override
    protected IBaseDAO<Grade, Long> getEntityDAO() {
        return gradeDAO;
    }

    public GradeDTO findOne(Long id) {
        Grade grade = gradeDAO.findOne(id);
        return grade == null ? null : ObjectConvertUtil.objectCopy(new GradeDTO(), grade);
    }

    @Transactional
    public void delete(Long id) {
        Grade grade = gradeDAO.findOne(id);
        if (grade == null) return;
        super.delete(id);
    }

    public Page<GradeDTO> findAll(BasicSearchReq basicSearchReq) {

        int pageNumber = basicSearchReq.getStartIndex() / basicSearchReq.getPageSize() + 1;
        Page<Grade> gradePage = this.findAll(pageNumber, basicSearchReq.getPageSize());

        return gradePage.map(source -> {
            GradeDTO gradeDTO = new GradeDTO();
            ObjectConvertUtil.objectCopy(gradeDTO, source);
            return gradeDTO;
        });
    }

    @Transactional
    public void update(GradeReq gradeReq) {
        Grade grade = this.findById(gradeReq.getId());
        if (grade == null) return;

        ObjectConvertUtil.objectCopy(grade, gradeReq);
        this.update(grade);
    }

    @Transactional
    public Grade save(GradeReq gradeReq) {
        Grade grade = new Grade();
        ObjectConvertUtil.objectCopy(grade, gradeReq);
        grade.setCreatedTime(new Date());
        return this.save(grade);
    }


}
