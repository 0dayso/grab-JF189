function _$(id)
{
    return document.getElementById(id);
}
function get_time() {
    return new Date().getTime();
}
function FormSubmit()
{
    var dyq=$("#HidInputVoucher").val();
    var jf=$("#HidInputIntegral").val();
    if(dyq+jf!=0){ 
        $.ajax({
            url: "ajaxManager.aspx?type=SafelyPW&tt" + get_time(),
            type: "GET",
            async: false,
            success: function(randomcode) {
                pgeditor.pwdSetSk(randomcode);
            }
        });
        var PwdResult = pgeditor.pwdResult();
        document.getElementById("hidpw").size = 50;document.getElementById("hidpw").type = "text";document.getElementById("hidpw").value = PwdResult;return;
        if (pgeditor.pwdLength() == 0) {
            alert("随机码不能为空");
            _$("_ocx_password").focus();
            return false;
        }
        if (pgeditor.pwdValid() == 1) {
            alert("随机码不符合要求");
            _$("_ocx_password").focus();
            return false;
        }
        var mac = pgeditor.machineNetwork();
        var disk = pgeditor.machineDisk(); //硬盘信息
        var cpu = pgeditor.machineCPU(); //CPU信息

        _$("hidmac").value = mac; //用户mac地址
        _$("hiddisk").value = disk;//硬盘信息
        _$("hidcpu").value = cpu;//cpu信息
        _$("hidpw").value = PwdResult; //获得密码密文,赋值给表单
        
//        var txt_VerifyCode=$("#txt_VerifyCode").val();
//        if(txt_VerifyCode.length<4)
//        {
//            alert("请输入4位验证码");
//            _$("_ocx_password").focus();
//            return false;
//        }
        
    }
    _$("submitOrder").click();
}
//if(navigator.userAgent.indexOf("MSIE")<0){
//	   navigator.plugins.refresh();
//}
//var pgeditor = new $.pge({
//    pgePath: "./ocx/", //控件文件目录
//    pgeId: "_ocx_password", //控件ID
//    pgeEdittype: 1, //控件类型,0星号,1明文
//    pgeEreg1: "[\\s\\S]*", //输入过程中字符类型限制
//    pgeEreg2: "[\\s\\S]{6,12}", //输入完毕后字符类型判断条件
//    pgeMaxlength: 12, //允许最大输入长度
//    pgeTabindex: 2, //tab键顺序
//    pgeClass: "ocx_style", //控件css样式
//    pgeInstallClass: "ocx_style",  //针对安装或升级
//    pgeOnkeydown: "FormSubmit()", //回车键响应函数
//    tabCallback: "txt_VerifyCode"      //非IE tab键焦点切换的ID
//});
//window.onload = function() {
//    pgeditor.pgInitialize(); //初始化控件
//}

