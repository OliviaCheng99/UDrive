package com.capstone.udrive.controllers;

import com.capstone.udrive.constants.Constants;
import com.capstone.udrive.entity.dto.SessionShareDto;
import com.capstone.udrive.entity.dto.SessionWebUserDto;
import com.capstone.udrive.entity.enums.ResponseCodeEnum;
import com.capstone.udrive.entity.vo.PaginationResultVO;
import com.capstone.udrive.entity.vo.ResponseVO;
import com.capstone.udrive.utils.CopyTools;
import com.capstone.udrive.utils.StringTools;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Log4j2
public class CommonController {

    protected static final String STATUS_SUCCESS = "success";
    protected static final String STATUS_ERROR = "error";

    protected <T> ResponseVO<T> getSuccessResponseVO(T t) {
        ResponseVO<T> responseVO = new ResponseVO<>();
        responseVO.setStatus(STATUS_SUCCESS);
        responseVO.setCode(ResponseCodeEnum.CODE_200.getCode());
        responseVO.setInfo(ResponseCodeEnum.CODE_200.getMsg());
        responseVO.setData(t);
        return responseVO;
    }

    protected <S, T> PaginationResultVO<T> convert2PaginationVO(PaginationResultVO<S> result, Class<T> classz) {
        PaginationResultVO<T> resultVO = new PaginationResultVO<>();
        resultVO.setList(CopyTools.copyList(result.getList(), classz));
        resultVO.setPageNo(result.getPageNo());
        resultVO.setPageSize(result.getPageSize());
        resultVO.setPageTotal(result.getPageTotal());
        resultVO.setTotalCount(result.getTotalCount());
        return resultVO;
    }

    protected SessionWebUserDto getUserInfoFromSession(HttpSession session) {
        return (SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);
    }


    protected SessionShareDto getSessionShareFromSession(@Validated HttpSession session, String shareId) {
        return (SessionShareDto) session.getAttribute(Constants.SESSION_SHARE_KEY + shareId);
    }


    protected void readFile(HttpServletResponse response, String filePath) {
        if (!StringTools.pathIsOk(filePath)) {
            return;
        }

        Path file = Path.of(filePath);
        if (!Files.exists(file)) {
            return;
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getFileName());

        try (OutputStream out = response.getOutputStream()) {
            Files.copy(file, out);
        } catch (IOException e) {
            log.error("IOException", e);
        }
    }
}
