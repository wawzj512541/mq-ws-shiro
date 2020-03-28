package com.example.web.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统日志
 *
 * @author lee
 * @email wawzj512541@gmail.com
 * @date 2019-10-28 11:29:08
 */
@Data
public class SysLogEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 操作类型
     */
    private String type;
    /**
     * 用户操作
     */
    private String operation;
    /**
     * 请求方法
     */
    private String method;
    /**
     * 请求参数
     */
    private String params;
    /**
     * 执行时长(毫秒)
     */
    private Long time;
    /**
     * IP地址
     */
    private String ip;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

}
