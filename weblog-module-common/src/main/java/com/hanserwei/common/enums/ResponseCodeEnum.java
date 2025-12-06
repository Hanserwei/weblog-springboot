package com.hanserwei.common.enums;

import com.hanserwei.common.exception.BaseExceptionInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCodeEnum implements BaseExceptionInterface {

    // ----------- 通用异常状态码 -----------
    SYSTEM_ERROR("10000", "出错啦，后台小哥正在努力修复中..."),
    PARAM_NOT_VALID("10001", "参数错误"),

    // ----------- 业务异常状态码 -----------
    LOGIN_FAIL("20000", "登录失败"),
    USERNAME_OR_PWD_ERROR("20001", "用户名或密码错误"),
    UNAUTHORIZED("20002", "无访问权限，请先登录！"),
    FORBIDDEN("20004", "演示账号仅支持查询操作！"),
    USER_NOT_EXIST("2005", "有户不存在！"),
    CATEGORY_NAME_IS_EXISTED("20005", "该分类已存在，请勿重复添加！"),
    TAG_NOT_EXIST("20006", "标签不存在！"),
    CATEGORY_NOT_EXIST("20007", "分类不存在！"),
    FILE_UPLOAD_FAILED("20008", "上传文件失败！"),
    CATEGORY_NOT_EXISTED("20009", "提交的分类不存在！"),
    TAG_NOT_EXISTED("20010", "标签ID不存在或已删除！"),
    ARTICLE_NOT_EXIST("20011", "文章不存在！"),
    TAG_HAS_ARTICLE("20012", "该标签有关联文章，无法删除！"),
    CATEGORY_HAS_ARTICLE("20013","该分类下有关联文章，无法删除！" );

    // 异常码
    private final String errorCode;
    // 错误信息
    private final String errorMsg;

}