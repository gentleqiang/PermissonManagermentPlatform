package com.debug.pmp.server.CommonExceptionHandler;

import com.debug.pmp.common.response.BaseResponse;
import com.debug.pmp.common.response.StatusCode;
import com.debug.pmp.common.utils.Constant;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局同意捕获异常，并返回给前段
 * @author Administrator
 */
@RestControllerAdvice
public class CommonExceptionHanler {

    private static final Logger log = LoggerFactory.getLogger(CommonExceptionHanler.class);

    @ExceptionHandler(AuthorizationException.class)
    public BaseResponse exceptionHandle(AuthorizationException e){
        log.info("访问了没有操作权限的路径：{}",e);
        return new BaseResponse(StatusCode.CurrUserHasNotPermission);
    }

}
