package com.lc.common.error;

import com.lc.common.utils.ObjectUtil;
import com.lc.common.pojo.ResponseInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.dao.DataAccessException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.net.ConnectException;
import java.sql.SQLException;

/**
 * 全局错误处理
 * @author l5990
 */
@Log4j2
@RestControllerAdvice
public class DefaultExceptionHandler {


    /**
     * 文件上传错误异常的捕获
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MultipartException.class)
    @ResponseBody
    public ResponseInfo uploadFile(MultipartException e) throws Exception {
        log.error("MultipartException ", e);
        return new ResponseInfo(BaseErrorEnums.FILEUPLOAD);
    }


    /**
     * 请求参数类型错误异常的捕获
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseInfo badRequest(BindException e) throws Exception {
        log.error("BindException: ", e);
        return new ResponseInfo(BaseErrorEnums.BAD_REQUEST);
    }

    /**
     * 404错误异常的捕获
     *
     * @param e
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public ResponseInfo badRequestNotFound(NoHandlerFoundException e) throws Exception {
        log.error("NoHandlerFoundException ", e);
        return new ResponseInfo(BaseErrorEnums.NOT_FOUND);

    }


    /**
     * 自定义异常的捕获
     * 自定义抛出异常。统一的在这里捕获返回JSON格式的友好提示。
     *
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(value = {BaseException.class})
    @ResponseBody
    public ResponseInfo sendError(BaseException exception, HttpServletRequest request) throws Exception {
        String requestURI = request.getRequestURI();
        log.error(exception);
        log.error("occurs error when execute url ={} ,message {}", requestURI, exception);
        return new ResponseInfo(ObjectUtil.getInteger(exception.getErr_code()), exception.getMessage());
    }

    /**
     * 数据库操作出现异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = {SQLException.class, DataAccessException.class})
    @ResponseBody
    public ResponseInfo systemError(Exception e) throws Exception {
        log.error("SQLException,DataAccessException:", e);
        return new ResponseInfo(BaseErrorEnums.ERROR_SYS);
    }

    /**
     * 网络连接失败！
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = {ConnectException.class})
    @ResponseBody
    public ResponseInfo connect(Exception e) throws Exception {
        log.error("ConnectException ", e);
        return new ResponseInfo(BaseErrorEnums.CONNECTION_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseInfo notAllowed(Exception e) {
        log.error("Exception :", e);
        return new ResponseInfo(BaseErrorEnums.ERROR_SYS);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResponseInfo notAllowedMethod(Exception e) throws Exception {
        log.error("HttpRequestMethodNotSupportedException {}", e);
        return new ResponseInfo(BaseErrorEnums.BAD_REQUEST_TYPE);
    }
}
