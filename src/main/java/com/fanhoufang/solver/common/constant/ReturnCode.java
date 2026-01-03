package com.fanhoufang.solver.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReturnCode {

    /**帐号密码不匹配**/
    CD100(100,"帐号密码不匹配"),
    /**操作失败**/
    CD999(999,"操作失败"),
    /**不支持的认证模式 */
    UNSUPPORTED_GRANT_TYPE(1003, "不支持的认证模式"),
    /**操作成功**/
    OK(0,"操作成功");



    /**自定义状态码**/
    private final int code;
    /**自定义描述**/
    private final String msg;
}
