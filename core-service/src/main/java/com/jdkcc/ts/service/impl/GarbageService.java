package com.jdkcc.ts.service.impl;

import com.jdkcc.ts.common.util.ObjectConvertUtil;
import com.jdkcc.ts.dal.entity.Garbage;
import com.jdkcc.ts.dal.repository.IBaseDAO;
import com.jdkcc.ts.dal.repository.GarbageDAO;
import com.jdkcc.ts.service.dto.request.BasicSearchReq;
import com.jdkcc.ts.service.dto.request.GarbageReq;
import com.jdkcc.ts.service.dto.response.GarbageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly = true)
public class GarbageService extends ABaseService<Garbage, Long> {

    private final GarbageDAO garbageDAO;

    @Autowired
    public GarbageService(GarbageDAO garbageDAO) {
        this.garbageDAO = garbageDAO;
    }

    @Override
    protected IBaseDAO<Garbage, Long> getEntityDAO() {
        return garbageDAO;
    }

    public GarbageDTO findOne(Long id) {
        Garbage garbage = garbageDAO.findOne(id);
        return garbage == null ? null : ObjectConvertUtil.objectCopy(new GarbageDTO(), garbage);
    }

    @Transactional
    public void delete(Long id) {
        Garbage garbage = garbageDAO.findOne(id);
        if (garbage == null) return;
        super.delete(id);
    }

    public Page<GarbageDTO> findAll(BasicSearchReq basicSearchReq) {

        int pageNumber = basicSearchReq.getStartIndex() / basicSearchReq.getPageSize() + 1;
        Page<Garbage> garbagePage = this.findAll(pageNumber, basicSearchReq.getPageSize());

        return garbagePage.map(source -> {
            GarbageDTO garbageDTO = new GarbageDTO();
            ObjectConvertUtil.objectCopy(garbageDTO, source);
            return garbageDTO;
        });
    }

    @Transactional
    public void update(GarbageReq garbageReq) {
        Garbage garbage = this.findById(garbageReq.getId());
        if (garbage == null) return;

        ObjectConvertUtil.objectCopy(garbage, garbageReq);
        garbage.setModifiedTime(new Date());
        if (garbage.getReadSum() == null){
            garbage.setReadSum(0);
        }
        this.update(garbage);
    }

    @Transactional
    public Garbage save(GarbageReq garbageReq) {
        Garbage garbage = new Garbage();
        ObjectConvertUtil.objectCopy(garbage, garbageReq);
        garbage.setCreatedTime(new Date());
        garbage.setModifiedTime(new Date());
        if (garbage.getReadSum() == null){
            garbage.setReadSum(0);
        }
        return this.save(garbage);
    }


}
