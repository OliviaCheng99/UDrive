package com.capstone.udrive.service.impl;

import com.capstone.udrive.component.RedisComponent;
import com.capstone.udrive.config.AppConfig;
import com.capstone.udrive.constants.Constants;
import com.capstone.udrive.entity.dto.SysSettingsDto;
import com.capstone.udrive.entity.enums.PageSize;
import com.capstone.udrive.entity.po.EmailCode;
import com.capstone.udrive.entity.po.UserInfo;
import com.capstone.udrive.entity.query.EmailCodeQuery;
import com.capstone.udrive.entity.query.SimplePage;
import com.capstone.udrive.entity.query.UserInfoQuery;
import com.capstone.udrive.entity.vo.PaginationResultVO;
import com.capstone.udrive.exception.BusinessException;
import com.capstone.udrive.mappers.EmailCodeMapper;
import com.capstone.udrive.mappers.UserInfoMapper;
import com.capstone.udrive.service.EmailCodeService;
import com.capstone.udrive.utils.StringTools;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Log4j2
@Service("emailCodeService")
public class EmailCodeServiceImpl implements EmailCodeService {

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private EmailCodeMapper<EmailCode, EmailCodeQuery> emailCodeMapper;

    @Resource
    private AppConfig appConfig;

    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    @Resource
    private RedisComponent redisComponent;


    @Override
    public List<EmailCode> findListByParam(EmailCodeQuery param) {
        return this.emailCodeMapper.selectList(param);
    }


    @Override
    public Integer findCountByParam(EmailCodeQuery param) {
        return this.emailCodeMapper.selectCount(param);
    }


    @Override
    public PaginationResultVO<EmailCode> findListByPage(EmailCodeQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<EmailCode> list = this.findListByParam(param);
        PaginationResultVO<EmailCode> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }


    @Override
    public Integer add(EmailCode bean) {
        return this.emailCodeMapper.insert(bean);
    }


    @Override
    public Integer addBatch(List<EmailCode> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.emailCodeMapper.insertBatch(listBean);
    }


    @Override
    public Integer addOrUpdateBatch(List<EmailCode> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.emailCodeMapper.insertOrUpdateBatch(listBean);
    }


    @Override
    public EmailCode getEmailCodeByEmailAndCode(String email, String code) {
        return this.emailCodeMapper.selectByEmailAndCode(email, code);
    }


    @Override
    public Integer updateEmailCodeByEmailAndCode(EmailCode bean, String email, String code) {
        return this.emailCodeMapper.updateByEmailAndCode(bean, email, code);
    }


    @Override
    public Integer deleteEmailCodeByEmailAndCode(String email, String code) {
        return this.emailCodeMapper.deleteByEmailAndCode(email, code);
    }

    private void sendEmailCode(String toEmail, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(appConfig.getSendUserName());
            message.setTo(toEmail);

            SysSettingsDto sysSettingsDto = redisComponent.getSysSettingsDto();

            message.setSubject(sysSettingsDto.getRegisterEmailTitle());
            message.setText(String.format(sysSettingsDto.getRegisterEmailContent(), code));
            message.setSentDate(new Date());
// TODO: still not able to use Gmail to send email
//            mailSender.send(message);
            log.warn("Email code: " + code);
        } catch (Exception e) {
            log.error("Send error", e);
            throw new BusinessException("Email send error");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendEmailCode(String toEmail, Integer type) {
        if (!Constants.ZERO.equals(type)) {
            UserInfo userInfo = userInfoMapper.selectByEmail(toEmail);
            if (userInfo == null) {
                throw new BusinessException("Email doesn't exist");
            }
        }

        String code = StringTools.getRandomNumber(Constants.LENGTH_5);
        sendEmailCode(toEmail, code);

        emailCodeMapper.disableEmailCode(toEmail);
        EmailCode emailCode = new EmailCode();
        emailCode.setCode(code);
        emailCode.setEmail(toEmail);
        emailCode.setStatus(Constants.ZERO);
        emailCode.setCreateTime(new Date());
        emailCodeMapper.insert(emailCode);
    }

    @Override
    public void checkCode(String email, String code) {
        EmailCode emailCode = emailCodeMapper.selectByEmailAndCode(email, code);
        if (null == emailCode) {
            throw new BusinessException("Code error");
        }
        if (emailCode.getStatus() == 1 || System.currentTimeMillis() - emailCode.getCreateTime().getTime() > Constants.LENGTH_15 * 1000 * 60) {
            throw new BusinessException("Code expired");
        }
        emailCodeMapper.disableEmailCode(email);
    }
}