package com.jdkcc.ts.service.impl;

import com.jdkcc.ts.common.util.ObjectConvertUtil;
import com.jdkcc.ts.dal.entity.Warm;
import com.jdkcc.ts.dal.repository.WarmDAO;
import com.jdkcc.ts.dal.repository.IBaseDAO;
import com.jdkcc.ts.service.dto.request.WarmReq;
import com.jdkcc.ts.service.dto.request.BasicSearchReq;
import com.jdkcc.ts.service.dto.response.WarmDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly = true)
public class WarmService extends ABaseService<Warm, Long> {

    private final WarmDAO warmDAO;

    @Autowired
    public WarmService(WarmDAO warmDAO) {
        this.warmDAO = warmDAO;
    }

    @Override
    protected IBaseDAO<Warm, Long> getEntityDAO() {
        return warmDAO;
    }

    public WarmDTO findOne(Long id) {
        Warm warm = warmDAO.findOne(id);
        return warm == null ? null : ObjectConvertUtil.objectCopy(new WarmDTO(), warm);
    }

    @Transactional
    public void delete(Long id) {
        Warm warm = warmDAO.findOne(id);
        if (warm == null) return;
        super.delete(id);
    }

    public Page<WarmDTO> findAll(BasicSearchReq basicSearchReq) {

        int pageNumber = basicSearchReq.getStartIndex() / basicSearchReq.getPageSize() + 1;
        Page<Warm> warmPage = this.findAll(pageNumber, basicSearchReq.getPageSize());

        return warmPage.map(source -> {
            WarmDTO warmDTO = new WarmDTO();
            ObjectConvertUtil.objectCopy(warmDTO, source);
            return warmDTO;
        });
    }

    @Transactional
    public void update(WarmReq warmReq) {
        Warm warm = this.findById(warmReq.getId());
        if (warm == null) return;

        ObjectConvertUtil.objectCopy(warm, warmReq);
        warm.setModifiedTime(new Date());
        if (warm.getReadSum() == null){
            warm.setReadSum(0);
        }
        this.update(warm);
    }

    @Transactional
    public Warm save(WarmReq warmReq) {
        Warm warm = new Warm();
        ObjectConvertUtil.objectCopy(warm, warmReq);
        warm.setCreatedTime(new Date());
        warm.setModifiedTime(new Date());
        if (warm.getReadSum() == null){
            warm.setReadSum(0);
        }
        return this.save(warm);
    }


}
