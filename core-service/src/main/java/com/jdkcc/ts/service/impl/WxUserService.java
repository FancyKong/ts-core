package com.jdkcc.ts.service.impl;

import com.jdkcc.ts.common.util.ObjectConvertUtil;
import com.jdkcc.ts.dal.entity.WxUser;
import com.jdkcc.ts.dal.repository.IBaseDAO;
import com.jdkcc.ts.dal.repository.WxUserDAO;
import com.jdkcc.ts.service.dto.request.BasicSearchReq;
import com.jdkcc.ts.service.dto.request.wxuser.WxUserSearchReq;
import com.jdkcc.ts.service.dto.request.wxuser.WxUserUpdateReq;
import com.jdkcc.ts.service.dto.response.WxUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WxUserService extends ABaseService<WxUser, Long> {

    private final WxUserDAO wxUserDAO;

    @Autowired
    public WxUserService(WxUserDAO wxUserDAO) {
        this.wxUserDAO = wxUserDAO;
    }

    @Override
    protected IBaseDAO<WxUser, Long> getEntityDAO() {
        return wxUserDAO;
    }

    public WxUser findByOpenid(String openid) {
        return wxUserDAO.findByOpenid(openid);
    }

    public boolean exist(String openid) {
        return wxUserDAO.findByOpenid(openid) != null;
    }

    public Page<WxUserDTO> findAll(BasicSearchReq basicSearchReq, WxUserSearchReq wxUserSearchReq) {
        int pageNumber = basicSearchReq.getStartIndex() / basicSearchReq.getPageSize() + 1;
        Page<WxUser> wxUserPage = super.findAllBySearchParams(
                buildSearchParams(wxUserSearchReq), pageNumber, basicSearchReq.getPageSize());
        return wxUserPage.map(this::getWxUserDTO);
    }

    private WxUserDTO getWxUserDTO(WxUser source) {
        WxUserDTO wxUserDTO = new WxUserDTO();
        ObjectConvertUtil.objectCopy(wxUserDTO, source);
        return wxUserDTO;
    }

    public void update(WxUserUpdateReq wxUserUpdateReq) {
        WxUser wxUser = wxUserDAO.findOne(wxUserUpdateReq.getId());
        ObjectConvertUtil.objectCopy(wxUser, wxUserUpdateReq);
        wxUserDAO.save(wxUser);
    }


}
