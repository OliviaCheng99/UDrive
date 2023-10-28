package com.capstone.udrive.service;


import com.capstone.udrive.entity.po.EmailCode;
import com.capstone.udrive.entity.query.EmailCodeQuery;
import com.capstone.udrive.entity.vo.PaginationResultVO;

import java.util.List;


public interface EmailCodeService {

    List<EmailCode> findListByParam(EmailCodeQuery param);

    Integer findCountByParam(EmailCodeQuery param);

    PaginationResultVO<EmailCode> findListByPage(EmailCodeQuery param);

    Integer add(EmailCode bean);

    Integer addBatch(List<EmailCode> listBean);

    Integer addOrUpdateBatch(List<EmailCode> listBean);

    EmailCode getEmailCodeByEmailAndCode(String email, String code);

    Integer updateEmailCodeByEmailAndCode(EmailCode bean, String email, String code);

    Integer deleteEmailCodeByEmailAndCode(String email, String code);

    void sendEmailCode(String toEmail, Integer type);

    void checkCode(String email, String code);
}