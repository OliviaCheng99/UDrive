package com.capstone.udrive.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class CopyTools {
    public static <T, S> List<T> copyList(List<S> sList, Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        for (S s : sList) {
            T t = null;
            try {
                t = clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            assert t != null;
            BeanUtils.copyProperties(s, t);
            list.add(t);
        }
        return list;
    }

    public static <T, S> T copy(S s, Class<T> clazz) {
        T t = null;
        try {
            t = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        assert t != null;
        BeanUtils.copyProperties(s, t);
        return t;
    }
}
