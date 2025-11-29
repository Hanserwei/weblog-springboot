package com.hanserwei.jwt.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanserwei.common.utils.Response;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.PrintWriter;

public class ResultUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 成功响应
     *
     * @param response HttpServletResponse对象
     * @param result   响应数据
     * @throws IOException IO异常
     */
    public static void ok(HttpServletResponse response, Response<?> result) throws IOException {
        writeResponse(response, HttpStatus.OK.value(), result);
    }

    /**
     * 失败响应
     *
     * @param response HttpServletResponse对象
     * @param result   响应数据
     * @throws IOException IO异常
     */
    public static void fail(HttpServletResponse response, Response<?> result) throws IOException {
        writeResponse(response, HttpStatus.OK.value(), result);
    }

    /**
     * 失败响应
     *
     * @param response HttpServletResponse对象
     * @param status   HTTP状态码
     * @param result   响应数据
     * @throws IOException IO异常
     */
    public static void fail(HttpServletResponse response, int status, Response<?> result) throws IOException {
        writeResponse(response, status, result);
    }

    /**
     * 写入响应数据
     *
     * @param response HttpServletResponse对象
     * @param status   HTTP状态码
     * @param result   响应数据
     * @throws IOException IO异常
     */
    private static void writeResponse(HttpServletResponse response, int status, Response<?> result) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);
        response.setContentType("application/json");

        try (PrintWriter writer = response.getWriter()) {
            writer.write(objectMapper.writeValueAsString(result));
            writer.flush();
        }
    }
}
