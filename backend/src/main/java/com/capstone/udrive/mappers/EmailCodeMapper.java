package com.capstone.udrive.mappers;

import org.apache.ibatis.annotations.Param;

public interface EmailCodeMapper<T, P> extends BaseMapper<T, P> {

    Integer updateByEmailAndCode(@Param("bean") T t, @Param("email") String email, @Param("code") String code);

    Integer deleteByEmailAndCode(@Param("email") String email, @Param("code") String code);

    T selectByEmailAndCode(@Param("email") String email, @Param("code") String code);

    void disableEmailCode(@Param("email") String email);

}
