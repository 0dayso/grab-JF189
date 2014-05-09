/*!
 * Date: 2012/10/30 09:20:00 bata1
 */
$(document).ready(function(){ //document begin
	$(".ph-label").click(function(){
		$(this).parent().find(".login-text").focus();
		});
	$(".login-box .hd li").click(function(){
		var login_box = $(this).index();
		$(this).addClass("current").siblings().removeClass("current");
		$(this).parent().parent().parent().find(".bd").filter(":visible").hide().parent().children().eq(login_box).show();
		});
	$(".long-login").hover(function(){
		$(this).addClass("long-login-hover");
		$(this).parent().find(".login-tips").show();
	},function(){
		$(this).removeClass("long-login-hover");
		$(this).parent().find(".login-tips").hide();
		});
	$(".login-text").keyup(function(e){
		$(this).parent().addClass("ph-hide");
		});
	$(".login-text").mousedown(function(e){
		if(3 == e.which){
			$(this).parent().addClass("ph-hide");
			};
		});
	$(".login-text").focus(function(){
		$(this).parent().addClass("ph-focus");
		});
	$(".login-text").blur(function(){
		if($(this).val() == ""){
			$(this).parent().removeClass("ph-hide").removeClass("ph-focus");
			};
		});
});
function changVal(that){
	(that.v=function(){that.value=that.value.replace(/[^0-9-]+/,'');}).call(that);
	 if(that.value.length == 11){
		 var thisValue = that.value.substring(0, 3) + " " + that.value.substring(3, 7) + " " + that.value.substring(7, that.value.length);
		 //$("#pay_dues .telecom").attr("maxlength","13");
		 //$("#pay_dues .telecom").val(thisValue);
		 that.value = thisValue;
		 }else if(that.value.length > 11){
			 var _thisValue = that.value.substring(0, 3) + " " + that.value.substring(3, 7) + " " + that.value.substring(8, 12);
			 that.value = _thisValue
			 //$("#pay_dues .telecom").val(_thisValue);
			 };
	};