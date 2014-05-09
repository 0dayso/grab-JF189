var findPwdUrl = "/service/passmanage/init.action";

var uplimitTime = true;//随机码发送是否超过限制的时间，默认是

var checkWayRadio = 1;//手机登录方式的密码类型，默认是手机密码


//图形验证码
function check_img(url){
    if (url.indexOf("random") < 0){
    	url += "?random=" + Math.random();
    }
    $("#vImg").attr("src",url);
}

/*
 *根据cookie信息初始化登录form
 *@param change 是否初始化登录方式 
 */	
function initLoginFormCookie(change){
	if(cookie_loginType != "" && cookie_loginType !=undefined) {
		if(change){
			$("#loginType").val(cookie_loginType);
			$("#loginType").change();
			//存在本地网，绑定本地网变更事件
			if(cookie_loginType != "4" && cookie_loginType != "5"){
				$("#thislatnId").bind("change",function(){
						if($("#loginType").val() == cookie_loginType && cookie_thislatnId == $(this).val()){
							initLoginNameAndPwd();
						}else{
							$("#loginName").val("");
							$("#passwd").val("");
							$("input[name='remPwd']").removeAttr("checked");
							return false;
						}
					});
			}
			
			$("#loginName").bind("blur",function(){
				if($("#loginType").val() == cookie_loginType && cookie_thislatnId == $("#thislatnId").val() && $("#loginName").val()==cookie_loginName){
					$("#passwd").val(cookie_passwd);
				}else{
					$("#passwd").val("");
				}
			});
		}else {
			//登录方式改变，同步变更本地网及登录账号， 密码信息
			if(cookie_loginType != "4" && cookie_loginType != "5" && cookie_loginType != "10"){
				$("#thislatnId").val(cookie_thislatnId);
				$("#thislatnId").change();
			}
			initLoginNameAndPwd();
		}
		
	}
}

function changeLoginName(){
	// $("#passWord").val("");
}

//初始化页面 登录账号， 密码信息
function initLoginNameAndPwd(){
	$("input[id='loginName']").val(cookie_loginName);
	$("#loginName").css("color","#000");
	if($("#errorMsg").html() == ""){
		$("#passWord").val(cookie_passwd);
	}
	$("input[name='remPwd']").attr("checked", "checked");
	$("#remPwdContent").find("span").addClass("zogo-form-checkbox-checked");
}



function getDefaultValue(){
	var loginType=$("#loginType").val();
	if("4" == loginType){
		return "请输入11位手机号码";
	}else if("2" == loginType || "3" == loginType){
		var latnid = $("#thislatnId").val();
		if(latnid =='290' || latnid =='910'){
			return "请输入8位电话号码";
		}else{
			return "请输入7位电话号码";
		}
	}else if("5" == loginType){
		return "请输入拨号账号";
	}else if("15" == loginType){
		return "请输入天翼会员卡号";
	}else if("1"==loginType){
		return "请输入客户标识码";
	}else if("10"==loginType){
		return "请输入注册账号";
	}else{
		return "";
	}
	
}



//点击密码类型需要切换显示
function seleCheckWay(v){
	checkWayRadio = v;   
	var display = "";
	//如果选的是手机密码
	if(v==1){
		//显示为手机密码
		$("#login_passWord").html("手机密码:");
		$("#passWord_reset").html("找回密码");
		$("#passWord_reset").attr("disabled", false);
		$("#passWord_reset").attr("href",findPwdUrl);
		if($("#loginType").val() == getCookieValue("loginType")){
			$("#passwd").val(getCookieValue("passwd"));
		}
	}
	//如果选的是随机码
	else{
		//显示为随机密码
		$("#login_passWord").html("随机密码:");
		$("#passWord_reset").html("点击获取");
		$("#passWord_reset").attr("href","javascript:sedSmsCode();");
		if(!uplimitTime){
			$("#passWord_reset").attr("disabled", true);
			$("#passWord_reset").attr("href","javascript:void(0);");
		}	
		display = "none";
		$("#passwd").val("");
	}
	document.getElementById('remPwdContent').style.display=display;
}

//短信发送限制超时后回调方法
function sendTimeOut(){
	uplimitTime = true;
	$("#passWord_reset").attr("disabled", false);
	$("#passWord_reset").attr("href","javascript:sedSmsCode();");
	$("#errorMsg").css("display","none");
	$("#errorMsg").html('');
}

//发送短信随机码
function sedSmsCode()
{
	//获取手机号码
	var mobilNbr = $("#loginName").val();
	var defaultValue = getDefaultValue();
	if(mobilNbr==null||mobilNbr=="" || mobilNbr==defaultValue){
		$("#errorMsg").css("display","block");
		$("#errorMsg").html('请输入手机号码!');
		return;
	}
	 
	//手机号码格式为1，第二位为3,5,8,后接9个数字
	var reg = /^1[3,5,8]\d{9}$/;
  	if(!reg.test(mobilNbr)){
  		$("#errorMsg").css("display","block");
		$("#errorMsg").html('手机号码格式错误!');
		return;
	}

  	var url = ctxPath + "/SendSms?";
  	var params = "phone=" + mobilNbr;
	$.ajax({
		url: url,  //后台处理程序
	    type: 'post',    //数据发送方式
	    dataType: 'json',   //接受数据格式
	    data: params,   //要传递的数据
	    cache:false,
	    success:function(data){
	    	var limitTime = 60;
			if(data.result == "1"){
		  		$("#errorMsg").css("display","block");
				$("#errorMsg").html(data.msg);
				$("#passWord_reset").attr("disabled", false);
				$("#passWord_reset").attr("href","javascript:sedSmsCode();");	
			}else{
				$("#errorMsg").css("display","block");
				$("#errorMsg").html('随机码已发送'+ limitTime +'秒后可重发!');
				setTimeout("sendTimeOut()",limitTime * 1000);
				countDown(60);
			}
	    },
	    error: function(){
	  		$("#errorMsg").css("display","block");
			$("#errorMsg").html('随机码发送失败，请稍候再试!');	
			setTimeout("sendTimeOut()",limitTime * 1000);
	    }
	});
  	 	
  	$("#passWord_reset").attr("disabled", true);
  	$("#passWord_reset").attr("href", "javascript:void(0);");
  	uplimitTime = false;
}

function trim(str) {
	return str.replace( /(^[\s|　]*)|([\s|　]*$)/g , '' );
}

//登录提交验证
function checkSubmit(obj)
{
		//登录名
		var loginName = trim($('#loginName').val());
	   	loginName = loginName.replace(/(^\s*)|(\s*$)/g, "");
	   	var pwd = $.trim($('#passWord').val());
	   	document.getElementById("loginName").value = loginName;
	   	//验证号码是否为空
	   	if("" == loginName||loginName==getDefaultValue())
	   	{	
	   		$("#loginMsgDiv").css("display","block");
	 		$("#loginMsg").html('请输入登录号码!');
	   		return;
	   	}
	   	//手机登录
	   	if($("#loginType").val() == "4"){
	   		var reg = /^1[3,5,8]\d{9}$/;
	   		
	   		//验证手机号码输入是否正确
	   		if(!reg.test(loginName)){
				$("#loginMsgDiv").css("display","block");
	 			$("#loginMsg").html('手机号码格式错误!');
				return;
			}
	 	}else if($("#loginType").val() == "2"){
	 		var latnid = $("#thislatnId").val();
	 		var reg =  /^[0-9]{7}$/;
	 		if(latnid =='290' || latnid =='910'){
	 			var reg =  /^[0-9]{8}$/;
	 		}
	  		if(!reg.test($("#loginName").val())){
			$("#loginMsgDiv").css("display","block");
 			$("#loginMsg").html('电话号码格式错误!');
			return;
			}
	 	}	
	 	//验证密码是否为空
	 	if(pwd == null || pwd =="")
	 	{	
	 		$("#loginMsgDiv").css("display","block");
	 		$("#loginMsg").html('请输入密码!');
	   		return;
	 	}
	 	//判断验证码是否为空
	 	if($("#valicodeshow").css("display") == "block"){
		 	var validCode = document.getElementById("validCode").value;
		 	if(validCode == null || validCode ==""){
		 		$("#loginMsgDiv").css("display","block");
	 			$("#loginMsg").html('请输入验证码!');
		   		return;
		 	}
	 	}
		//对密码进行加密
		if(getCookieValue("passwd") != pwd){
			document.getElementById("passWord").value = encryptedString(key, pwd);
		}else if(getCookieValue("passwd") == pwd){
			$("#remPwd").val("13");
		}
		
		var remPwd = $("input[name='remPwd']").attr("checked");
		deleteCookie("loginType", "/");
		deleteCookie("loginName", "/");
		deleteCookie("passwd", "/");
		deleteCookie("thislatnId", "/");
		if(remPwd && document.getElementById('remPwdContent').style.display !="none"){
			//手机号码
			setCookie("loginType", $("#loginType").val(), 2400, "/");
			setCookie("loginName", $("#loginName").val(), 2400, "/");
			setCookie("passwd", $("#passWord").val(), 2400, "/");
			//电话号码
			setCookie("thislatnId", $("#thislatnId").val(), 2400, "/");
		}
		var form1 = document.getElementById("LOGIN_F");
		// Feely
		form1.action=ctxPath + "/GetLoginServlet";
		$("#loginButton").attr("disabled","disabled");
		form1.submit();
}

//倒计时
function countDown(cnt){
	var maxNum = 60;
	var num = parseInt(cnt);
	if(num == maxNum){
		$("#errorMsg").css("display","block");
		$("#errorMsg").html('随机码已发送'+ num +'秒后可重发!');
	}
	num=num-1;
	if(num <= 1){
		$("#errorMsg").css("display","none");
	}else{
		$("#errorMsg").html('随机码已发送'+ num +'秒后可重发!');
		setTimeout("countDown(" + num +")",1000);
	}
}

//根据所选地市提示语变动
function changeLatn(){
	var loginType = $("#loginType").val();
	var latnid = $("#thislatnId").val();
	if(loginType=='2' || loginType=='3'){//固话、小灵通
		if(latnid =='290' || latnid =='910'){
			$("#loginName").val("请输入8位电话号码");
		}else{
			$("#loginName").val("请输入7位电话号码");
		}
	}
}

//改变登录方式时需要对页面文字修改
function changeLoginType(loginType)
{
	//显示记住密码
	document.getElementById('remPwdContent').style.display="";
	$("#loginType").val(loginType);
	
	//如果是手机类型
	if("4" == loginType)
	{
		//隐藏本地网选择
		$("#selectLatn").hide();
		$("#loginAccount").text("手机号码:");
		$("#seleCheck").show();
		$("#login_passWord").html("手机密码:");
		$("#loginName").val("请输入11位手机号码");
		$("#loginName").css("color","#999");
		$("#passWord_reset").html("找回密码");
		$("#passWord_reset").attr("href",findPwdUrl);
		$("#passWord_reset").attr("disabled", false);
		//如果选择的是随机密码并且随机密码的时间限制还没超过
		if(!uplimitTime && checkWayRadio != 1){
			$("#passWord_reset").html("点击获取");
			$("#passWord_reset").attr("disabled", true);
			$("#passWord_reset").attr("href","javascript:void(0);");
		}
		//当随机密码选中的时候不显示记住密码
		if($("input[name='selCheckWay']:checked").val() == "18"){
			document.getElementById('remPwdContent').style.display="none";
		}
	}
	//如果是小灵通或固话
	else if(loginType == "2" || loginType == "3")
	{	$("#s11").attr("checked",'checked'); 
		//显示本地网选择
		$("#selectLatn").show();
		$("#loginAccount").text("电话号码:");
		$("#seleCheck").hide();
		$("#login_passWord").html("登录密码:");
		changeLatn();//loginName根据地市显示提示语
		$("#passWord_reset").html("找回密码");
		$("#loginName").val("请输入8位电话号码");
		$("#loginName").css("color","#999");
		$("#passWord_reset").attr("href",findPwdUrl);
		$("#passWord_reset").attr("disabled", false);
	}
	//如果是客户标识码
	else if(loginType == "1")
	{	$("#s11").attr("checked",'checked'); 
		//显示本地网选择
		$("#selectLatn").show();
		$("#loginAccount").html("标&nbsp;识&nbsp;码:");
		$("#seleCheck").hide();
		$("#login_passWord").html("登录密码:");
		$("#loginName").val("请输入客户标识码");
		$("#loginName").css("color","#999");
		$("#passWord_reset").html("找回密码");
		$("#passWord_reset").attr("href",findPwdUrl);
		$("#passWord_reset").attr("disabled", false);
	}
	//如果是宽带帐号
	else if(loginType == "5")
	{
		$("#s11").attr("checked",'checked'); 
		//隐藏本地网选择
		//$("#selectLatn").hide();
		$("#selectLatn").show();
		$("#loginAccount").text("宽带帐号:");
		$("#seleCheck").hide();
		$("#loginName").val("请输入拨号账号");
		$("#login_passWord").html("登录密码:");
		$("#loginName").css("color","#999");
		$("#passWord_reset").html("找回密码");
		$("#passWord_reset").attr("href",findPwdUrl);
		$("#passWord_reset").attr("disabled", false);
	}
	//注册用户登录
	else if(loginType == "10")
	{	$("#s11").attr("checked",'checked'); 
		//隐藏本地网选择
		$("#selectLatn").hide();
		$("#loginAccount").text("注册帐号:");
		$("#seleCheck").hide();
		$("#login_passWord").html("登录密码:");
		$("#loginName").val("请输入注册账号");
		$("#loginName").css("color","#999");
		$("#passWord_reset").html("找回密码");
		$("#passWord_reset").attr("href",findPwdUrl);
		$("#passWord_reset").attr("disabled", false);
	}	
	//天翼会员卡号
	else if(loginType == "15")
	{	$("#s11").attr("checked",'checked'); 
		//显示本地网选择
		$("#selectLatn").show();
		
		$("#loginAccount").text("会员卡号:");
		
		$("#seleCheck").hide();
		$("#loginName").val("请输入天翼会员卡号");
		$("#loginName").css("color","#999");
		$("#login_passWord").html("会员密码:");
		
		$("#passWord_reset").html("");

	}
	$("input[name='remPwd']").removeAttr("checked");
	$("#passwd").val("");
	if(loginType == cookie_loginType){
		initLoginFormCookie(false);
	}
	

}



// 一点通
var ydt_flg = true;

function  autoYDTLogin(ch){
    //alert(ch.checked);
    if(ch.checked){
    	setCookie("_onePointAutoLogin","1",24*30,"/");// 延长cookie保存时间30天;
    	submitYDT();
    }else {
        setCookie("_onePointAutoLogin","2",24*30,"/");
        submitYDT();
    }
}

function getUserAccount(ip){
	var url = "/freeAccountLogin";
  	var params = "ip="+ip;
	$.ajax({
		url: url,  //后台处理程序
	    type: 'post',    //数据发送方式
	    dataType: 'json',   //接受数据格式
	    data: params,   //要传递的数据
	    cache:false,
	    success:function(data){
	    	if(data.account != null && data.latnId != null)
	    	{
	    		$("#YDTdiv").css("display","block");// 显示一点通登录模块
	    		$("#loginSwitch").css("display","block");
				$("#logindiv").css("display","none");// 隐藏登录模块	
	    		$("#account").html(data.account);// 帐号
				$("#ydtLoginName").val(data.account);
				$("#ydtLatnId").val(data.latnId);
	    	}
	    },
	    error: function(){
	    }
	});
}		
function submitYDT()
{
		var form1 = document.getElementById("LOGIN_F");
		form1.action=ctxPath + "/LoginServlet";
		$("#loginButton").attr("disabled","disabled");
		form1.submit();
}
function switchDiv(){
	if($("#YDTdiv").css("display")=="none" ){
		$("#YDTdiv").css("display","block");
		$("#logindiv").css("display","none");
	}else{
		$("#YDTdiv").css("display","none");
		$("#logindiv").css("display","block");
	}
}

function YDTlogin(){
	
	var form1 = document.getElementById("YDTform");
	form1.action=ctxPath + "/LoginServlet";
	form1.submit();
	
}

