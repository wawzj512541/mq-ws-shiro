package com.example.shiro.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeMsg {


    public static CodeMsg USER_ALREADY_EXISTS = new CodeMsg(500100, "用户已存在,请重新输入");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常:%s");
    public static CodeMsg REGISTER_ERROR = new CodeMsg(500102, "注册失败");
    public static CodeMsg LOGIN_ERROR = new CodeMsg(500103, "登录失败");
    public static CodeMsg PHONENUMBER_ERROR = new CodeMsg(500104, "该手机号已注册");
    public static CodeMsg ACCOUNT_ERROR = new CodeMsg(500105, "用户不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500106, "密码错误");
    public static CodeMsg MESSAGE_ERROR = new CodeMsg(500107, "验证码错误");
    public static CodeMsg AUTHCODE_ERROR = new CodeMsg(500108, "验证码已发送，请勿重复提交");
    public static CodeMsg PWD_ERROR = new CodeMsg(500109, "密码不能为空");
    public static CodeMsg USER_unallowable_ERROR = new CodeMsg(500111, "该账号已锁定");
    public static CodeMsg UPDATE_ERROR = new CodeMsg(500112, "修改失败");
    public static CodeMsg HASEXPERT_ERROR = new CodeMsg(500113, "该账号以认证过，无需重复认证");
    public static CodeMsg AUC_ERROR = new CodeMsg(500114, "认证失败");
    public static CodeMsg ADD_ERROR = new CodeMsg(500115, "添加失败");
    public static CodeMsg TYPE_ERROR = new CodeMsg(500116, "对不起您的账号类型不符合");
    public static CodeMsg USER_LOGIN_ERROR = new CodeMsg(500117, "对不起，请先登录");
    public static CodeMsg USER_TYPE_ERROR = new CodeMsg(500118, "账号类型不符");
    public static CodeMsg AFFIRM_ERROR = new CodeMsg(500119, "认证失败");
    public static CodeMsg MUST_LOGIN_ERROP = new CodeMsg(500120, "请先登录");
    public static CodeMsg SED_MESSAGE_ERROP = new CodeMsg(500121, "发送失败");
    public static CodeMsg PHONEORCOMPANYNAMENUMBER_ERROR = new CodeMsg(500122, "该手机号或公司名已注册");
    public static CodeMsg NUMBER_ERROP = new CodeMsg(500123, "请输入数字");
    public static CodeMsg RESENDMESSAGE_ERROP = new CodeMsg(500124, "请勿频繁发送短信, 请1分钟之后再咨询.");
    public static CodeMsg COMPANCY_ERROP = new CodeMsg(500125, "该公司名已注册");

    public static CodeMsg DATA_EMPTY = new CodeMsg(500201, "没有对应的数据");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500204, "服务器未知异常");
    public static CodeMsg PASSWORDWORD_ERROP = new CodeMsg(500205, "密码长度必须大于 6 位且小于 8位");
    public static CodeMsg NOINFORMATION_ERROP = new CodeMsg(500206, "查询的信息不存在");
    public static CodeMsg DELECT_ERROP = new CodeMsg(500207, "删除失败");


    public static CodeMsg AUTH_ERROR = new CodeMsg(500400, "抱歉！没有足够权限");
    public static CodeMsg SESSION_ERROR = new CodeMsg(500404, "session验证失败,请重新登录");
    public static CodeMsg LOGOUT_ERROR = new CodeMsg(500405, "您已掉线,请重新登录");


    public static CodeMsg MENU_ERROR = new CodeMsg(500501, "菜单名称不能为空");

    public static CodeMsg PAY_MALL_ENABLE = new CodeMsg(500601, "商品未上架");
    public static CodeMsg PAY_MALL_STOCKFAIL = new CodeMsg(500602, "商品库存不足");
    public static CodeMsg JSON_ERROR = new CodeMsg(500502, "错误的JSON格式");

    private Integer code;
    private String msg;


    //返回一个带参数的错误码
    public CodeMsg fillArgs(Object... args) {//变参
        int code = this.code;
        //message是填充了参数的message
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }
}
