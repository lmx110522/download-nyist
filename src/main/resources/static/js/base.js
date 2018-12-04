$(function () {
    huojian()
    layui.define(['layer', 'form'], function(){
        var layer = layui.layer
            ,form = layui.form;
    });
    $('.bar3').slideToUnlock({
        text : '先滑动我才可以登录哦~',
        succText : '解锁成功，您可以登录'
    });
    $("#open_login").click(function (e) {
        e.preventDefault()
        $('.login_modal').modal('show')
    })
    $(".get_msg").click(function (e) {
        e.preventDefault();
        var phone = $("#message_phone").val();
        if(!(/^[1][3,4,5,7,8][0-9]{9}$/.test(phone))){
            layer.msg("手机号不存在")
            $("#message_phone").val("");
            $("#message_phone").focus()
            return false;
        }
        else{
            $(".get_msg").html("<span class='djs'>120</span> 秒可重发")
            var time = 120;
            var my_interval = setInterval(function () {
                if(time > 0){
                    time--;
                    $(".djs").text(time);
                    $(".get_msg").css("pointer-events","none");
                }
                else{
                    clearInterval(my_interval)
                    $(".get_msg").html("获取验证码");
                    $(".get_msg").css("pointer-events","all");
                }
            },1000)
            $.getJSON("/user/buildCode","phone="+phone,function (result) {
                if(result.status !=200){
                    layer.msg(result.msg);
                }
                else{
                    layer.msg('验证码已发送，请查收', {
                        offset: 't',
                        anim: 6
                    });
                }
            })
        }
    })
    $(".submit_btn").click(function (e) {
        e.preventDefault();
        var flag = 0;
        $(this).parents(".contact").find(".must_input").each(function () {
            if($(this).val().trim().length == 0){
                $(this).focus();
                flag++;
                return false;
            }
        })
        if(flag != 0){
            return false;
        }
        var phone = $(this).parents(".contact").find("input[name=phone]").val();
        if(!(/^[1][3,4,5,7,8][0-9]{9}$/.test(phone))){
            flag++;
            layer.msg("手机号不存在");
            $(this).parents(".contact").find("input[name=phone]").focus();
            $(this).parents(".contact").find("input[name=phone]").val("");
            return false;
        }

        if(flag == 0){
            var formId = $(this).attr("tid");
            var serialize = $("#"+formId).serialize();
            if(formId == "pass_my"){
                var phone = $(this).parents("#pass").find("#login_phone").val();
                var password = $(this).parents("#pass").find("#password").val();
                serialize = $.param({phone:phone,password:password});
            }
            $.post("/user/login",serialize,function (result) {
                layer.msg(result.msg, {
                    offset: 't',
                    anim: 6
                });
                if(result.status == 200){
                    setTimeout(function(){
                        window.location.reload();
                    },1000);
                }
            })
        }

    })
})

function huojian() {

    var e = $("#rocket-to-top"),
        t = $(document).scrollTop(),
        n,
        r,
        i = !0;
    $(window).scroll(function() {
        var t = $(document).scrollTop();
        t == 0 ? e.css("background-position") == "0px 0px" ? e.fadeOut("slow") : i && (i = !1, $(".level-2").css("opacity", 1), e.delay(100).animate({
                marginTop: "-1000px"
            },
            "normal",
            function() {
                e.css({
                    "margin-top": "-125px",
                    display: "none"
                }),
                    i = !0
            })) : e.fadeIn("slow")
    }),
        e.hover(function() {
                $(".level-2").stop(!0).animate({
                    opacity: 1
                })
            },
            function() {
                $(".level-2").stop(!0).animate({
                    opacity: 0
                })
            }),
        $(".level-3").click(function() {
            function t() {
                var t = e.css("background-position");
                if (e.css("display") == "none" || i == 0) {
                    clearInterval(n),
                        e.css("background-position", "0px 0px");
                    return
                }
                switch (t){
                    case "0px 0px":
                        e.css("background-position", "-298px 0px");
                        break;
                    case "-298px 0px":
                        e.css("background-position", "-447px 0px");
                        break;
                    case "-447px 0px":
                        e.css("background-position", "-596px 0px");
                        break;
                    case "-596px 0px":
                        e.css("background-position", "-745px 0px");
                        break;
                    case "-745px 0px":
                        e.css("background-position", "-298px 0px");
                }
            }
            if (!i) return;
            n = setInterval(t, 50),
                $("html,body").animate({scrollTop: 0},"slow");
        });
}