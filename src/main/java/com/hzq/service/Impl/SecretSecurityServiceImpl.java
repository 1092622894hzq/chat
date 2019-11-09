package com.hzq.service.Impl;

import com.hzq.vo.ServerResponse;
import com.hzq.dao.SecretSecurityDao;
import com.hzq.domain.SecretSecurity;
import com.hzq.enums.ResponseCodeEnum;
import com.hzq.execption.CustomGenericException;
import com.hzq.service.SecretSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: blue
 * @Date: 2019/11/1
 * @Description: com.hzq.service.Impl
 * @version: 1.0
 */
@Service("secretSecurityService")
public class SecretSecurityServiceImpl implements SecretSecurityService {

    @Autowired
    private SecretSecurityDao secretSecurityDao;

    @Override
    public ServerResponse<String> insert(SecretSecurity security) {
        if (secretSecurityDao.checkById(security.getUserId()) > 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.USER_ERROR.getCode(),"已经设置过密保问题");
        }
        if (secretSecurityDao.insert(security) == 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"插入密保问题出错");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<SecretSecurity> select(Integer userId) {
        SecretSecurity security = secretSecurityDao.selectByUserId(userId);
        if (security == null) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.USER_ERROR.getCode(),"查询不到密保问题");
        }
        return ServerResponse.createBySuccess(security);
    }

    @Override
    public ServerResponse<String> update(SecretSecurity security) {
        if (secretSecurityDao.update(security) == 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"更新密保问题出错");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> verify(SecretSecurity security) {
        SecretSecurity s = secretSecurityDao.selectByUserId(security.getUserId());
        if (s == null) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.USER_ERROR.getCode(),"该用户没设定密保问题");
        }
        if (s.getAnswer1().equals(security.getAnswer1()) && s.getAnswer2().equals(security.getAnswer2())){
          return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("答案不对");
    }
}
