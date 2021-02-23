package com.pplingo.common_push;

/**
 * Created by ZhqoQi on 2021/2/23 17:19
 * E-Mail Address：550655294@qq.com
 */

public class ReturnPayResult {
    private String status;
    public ReturnPayResult(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}