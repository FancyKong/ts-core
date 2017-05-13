package com.jdkcc.ts.service.impl;

import com.jdkcc.ts.common.util.ObjectConvertUtil;
import com.jdkcc.ts.dal.entity.WarmSweet;
import com.jdkcc.ts.dal.repository.WarmSweetDAO;
import com.jdkcc.ts.dal.repository.IBaseDAO;
import com.jdkcc.ts.service.dto.request.WarmSweetReq;
import com.jdkcc.ts.service.dto.request.BasicSearchReq;
import com.jdkcc.ts.service.dto.response.WarmSweetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly = true)
public class WarmSweetService extends ABaseService<WarmSweet, Long> {

    private final WarmSweetDAO warmSweetDAO;

    @Autowired
    public WarmSweetService(WarmSweetDAO warmSweetDAO) {
        this.warmSweetDAO = warmSweetDAO;
    }

    @Override
    protected IBaseDAO<WarmSweet, Long> getEntityDAO() {
        return warmSweetDAO;
    }

    public WarmSweetDTO findOne(Long warmSweetId) {
        WarmSweet warmSweet = warmSweetDAO.findOne(warmSweetId);
        return warmSweet == null ? null : ObjectConvertUtil.objectCopy(new WarmSweetDTO(), warmSweet);
    }

    @Transactional
    public void delete(Long warmSweetId) {
        WarmSweet warmSweet = warmSweetDAO.findOne(warmSweetId);
        if (warmSweet == null) return;
        super.delete(warmSweetId);
    }

    public Page<WarmSweetDTO> findAll(BasicSearchReq basicSearchReq) {

        int pageNumber = basicSearchReq.getStartIndex() / basicSearchReq.getPageSize() + 1;
        Page<WarmSweet> warmSweetPage = this.findAll(pageNumber, basicSearchReq.getPageSize());

        return warmSweetPage.map(source -> {
            WarmSweetDTO warmSweetDTO = new WarmSweetDTO();
            ObjectConvertUtil.objectCopy(warmSweetDTO, source);
            return warmSweetDTO;
        });
    }

    @Transactional
    public void update(WarmSweetReq warmSweetReq) {
        WarmSweet warmSweet = this.findById(warmSweetReq.getId());
        if (warmSweet == null) return;

        ObjectConvertUtil.objectCopy(warmSweet, warmSweetReq);
        warmSweet.setModifiedTime(new Date());
        if (warmSweet.getReadSum() == null){
            warmSweet.setReadSum(0);
        }
        this.update(warmSweet);
    }

    @Transactional
    public WarmSweet save(WarmSweetReq warmSweetReq) {
        WarmSweet warmSweet = new WarmSweet();
        ObjectConvertUtil.objectCopy(warmSweet, warmSweetReq);
        warmSweet.setCreatedTime(new Date());
        warmSweet.setModifiedTime(new Date());
        if (warmSweet.getReadSum() == null){
            warmSweet.setReadSum(0);
        }
        return this.save(warmSweet);
    }


}
