package com.capstone.udrive.service;

import com.capstone.udrive.entity.dto.SessionWebUserDto;
import com.capstone.udrive.entity.po.UserInfo;
import com.capstone.udrive.entity.query.UserInfoQuery;
import com.capstone.udrive.entity.vo.PaginationResultVO;

import java.util.List;

public interface UserInfoService {

    List<UserInfo> findListByParam(UserInfoQuery param);

    Integer findCountByParam(UserInfoQuery param);

    PaginationResultVO<UserInfo> findListByPage(UserInfoQuery param);

    Integer add(UserInfo bean);

    Integer addBatch(List<UserInfo> listBean);

    Integer addOrUpdateBatch(List<UserInfo> listBean);

    UserInfo getUserInfoByUserId(String userId);

    Integer updateUserInfoByUserId(UserInfo bean, String userId);

    Integer deleteUserInfoByUserId(String userId);

    UserInfo getUserInfoByEmail(String email);

    Integer updateUserInfoByEmail(UserInfo bean, String email);

    Integer deleteUserInfoByEmail(String email);

    UserInfo getUserInfoByNickName(String nickName);

    Integer updateUserInfoByNickName(UserInfo bean, String nickName);

    Integer deleteUserInfoByNickName(String nickName);

    SessionWebUserDto login(String email, String password);

    void register(String email, String nickName, String password, String emailCode);

    void resetPwd(String email, String password, String emailCode);

    void updateUserStatus(String userId, Integer status);

    void changeUserSpace(String userId, Integer changeSpace);
}