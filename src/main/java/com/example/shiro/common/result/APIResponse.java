package com.example.shiro.common.result;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author huachen created on 13-12-2 下午4:59
 * @version $Id$
 */
@Data
@ApiModel(value = "包装类", description = "包装类")
public class APIResponse<T> implements Serializable {
    private static final long serialVersionUID = 5241526151768786394L;

    @ApiModelProperty(example = "1.0", name = "版本号", dataType = "float")
    private final String ver = "1.0";
    @ApiModelProperty(example = "true|false", name = "是否正常返回", dataType = "boolean")
    private boolean ret;
    @ApiModelProperty(example = "***异常", name = "错误信息", dataType = "string")
    private String errmsg;
    @ApiModelProperty(example = "1000", name = "错误码", dataType = "int")
    private int errcode;
    @ApiModelProperty(name = "返回对象", dataType = "object")
    private T data;

    public APIResponse() {
        this.ret = true;
        this.errmsg = "成功";
        this.errcode = 0;
    }


    private APIResponse(String errmsg, T t) {
        this.ret = false;
        this.errmsg = errmsg;
        this.data = t;
        this.errcode = -1;
    }

    private APIResponse(CodeMsg codeMsg) {
        this.ret = false;
        this.errmsg = codeMsg.getMsg();
        this.errcode = codeMsg.getCode();
    }

    private APIResponse(int errcode, String errmsg, T t) {
        this.ret = false;
        this.errmsg = errmsg;
        this.errcode = errcode;
        this.data = t;
    }


    private APIResponse(T t) {
        this.ret = true;
        this.data = t;
        this.errcode = 0;
    }

    public static <T> APIResponse<T> returnSuccess() {
        return new APIResponse<T>();
    }

    public static <T> APIResponse<T> returnSuccess(T t) {
        return new APIResponse<T>(t);
    }

    public static <T> APIResponse<T> returnFail(String errmsg) {
        return new APIResponse<T>(errmsg, null);
    }

    public static <T> APIResponse<T> returnFail(String errmsg, T t) {
        return new APIResponse<T>(errmsg, t);
    }

    public static APIResponse returnFail(CodeMsg codeMsg) {
        return new APIResponse(codeMsg);
    }

    public static <T> APIResponse<T> returnFail(int errcode, String errmsg) {
        return new APIResponse<T>(errcode, errmsg, null);
    }

    public static <T> APIResponse<T> returnFail(int errcode, String errmsg, T t) {
        return new APIResponse<T>(errcode, errmsg, t);
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + ((errmsg == null) ? 0 : errmsg.hashCode());
        result = prime * result + (ret ? 1231 : 1237);
        result = prime * result + errcode;
        result = prime * result + ((ver == null) ? 0 : ver.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        APIResponse that = (APIResponse) o;

        if (errcode != that.errcode)
            return false;
        if (ret != that.ret)
            return false;
        if (data != null ? !data.equals(that.data) : that.data != null)
            return false;
        if (errmsg != null ? !errmsg.equals(that.errmsg) : that.errmsg != null)
            return false;
        if (ver != null ? !ver.equals(that.ver) : that.ver != null)
            return false;

        return true;
    }

    @Override
    public String toString() {
        return "APIResponse [" +
                "ver='" + ver + '\'' +
                ", ret=" + ret +
                ", errmsg='" + errmsg + '\'' +
                ", errcode=" + errcode +
                ", data=" + data +
                ']';
    }
}