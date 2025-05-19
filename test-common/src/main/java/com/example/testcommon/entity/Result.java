package com.example.testcommon.entity;


import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class Result<E> implements Serializable {

    public final static int CODE_SUCCESS = 200;

    public int code = CODE_SUCCESS;

    public String msg;

    protected E data;

    private Map other = new HashMap<String, Object>();

    public Result() {
        this.code = CODE_SUCCESS;
        this.msg = "成功";
    }

    public Result(E data) {
        System.out.println(JSON.toJSONString(data));
        this.code = CODE_SUCCESS;
        this.msg = "成功";
        this.data = data;
    }

    public Result<E> setMessage(int code, String errorMsg) {
        this.code = code;
        this.msg = errorMsg;
        return this;
    }

    public Result<E> setErrorMessage(int code, String errorMsg) {
        this.code = code;
        this.msg = errorMsg;
        return this;
    }


    public boolean judgeFail() {
        return this.code != CODE_SUCCESS;
    }

    public void setDefaulErrorMessage() {
        this.code = 169;
        this.msg = "服务器开小差了";
    }

    public void setParaErrorMessage() {
        this.code = 168;
        this.msg = "请检查输入的参数是否符合规定";
    }

    public boolean isFail() {
        return CODE_SUCCESS != this.code;
    }

    /**
     * 成功
     *
     * @return
     */
    public static <E> Result<E> success() {
        return new Result<E>();
    }

    /**
     * 成功
     *
     * @return
     */
    public static <E> Result<E> success(E data) {
        return new Result<E>(data);
    }

    /**
     * 自定义返回
     *
     * @param code    返回码
     * @param message 返回信息
     * @return
     */
    public static <E> Result<E> fail(int code, String message) {
        Result<E> result = new Result<E>();
        result.setMessage(code, message);
        return result;
    }

    /**
     * 返回错误信息
     *
     * @return
     */
    public static <E> Result<E> fail() {
        Result<E> result = new Result<E>();
        result.setDefaulErrorMessage();
        return result;
    }
}
