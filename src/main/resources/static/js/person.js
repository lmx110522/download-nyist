$(function () {

    selectLi()
    crop()
    tab()
    $(".change_btn").click(function () {
        $(this).hide()
        $("#crop_img").fadeIn(1000)
        $('html,body').animate({scrollTop:$('#crop_img').offset().top}, 1000)
    })
    $(".change_cancel").click(function () {
        $("#crop_img").fadeOut(1000)
       var hide_inter = setInterval(function () {
           $(".change_btn").show()
           clearInterval(hide_inter)
       },1000)
    })
    $(".edit_msg").click(function (e) {
        var $that = $(this);
        e.preventDefault()
        if($(this).hasClass("has_click")){
            var flag = 0;
            $(".can_change").each(function () {
                if($(this).val().trim().length == 0){
                    if($(this).attr("name") != "password"){
                        $(this).focus();
                        flag = 1;
                        return false;
                    }

                }
            })
            if(flag ==1){
                layer.msg("请完善好信息再点击确认修改！");
            }
            else{
                console.log($("#myForm").serialize());
                console.log(JSON.stringify($("#myForm").serialize()));
                $.ajax({
                    type:"POST",
                    url:"/user/edit",
                    data: $("#myForm").serialize(),
                    success: function (result) {
                        if(result.status != 200){
                            layer.alert(result.msg);
                        }
                        else{
                            $that.removeClass("has_click")
                            $that.text("修改")
                            $(".can_change").css("pointer-events","none");
                            $("input[name=password]").val("******");
                            layer.msg("成功修改", {
                                offset: 't',
                                anim: 6
                            });
                        }
                    },
                    beforeSend: function () {
                        this.layerIndex = layer.load(0, { shade: [0.01, '#fff'] });
                    },
                    complete: function () {
                        layer.close(this.layerIndex); //完成加载后关闭loading
                    }
                })
            }
        }
        else{
            $("input[name=password]").val("");
            $(this).addClass("has_click")
            $(this).text("确认修改")
            $(".can_change").css("pointer-events","all")
        }

    })
    $("#must_use").click(function () {
        var len = $(".cropped img").length;
        if(len < 3){
            layer.alert("请先选择照片并且裁剪好再点击确认使用！");
        }
        else{
           var img_base64 = $(".cropped img:eq(1)").attr("src");
            $.ajax({
                type:"POST",
                url:"/user/changeHeadImg",
                data:{"img_base64":img_base64},
                success: function (result) {
                    console.log(result.data);
                    $("#use_head_img").attr("src",result.data);
                    $(".user_msg img").attr("src",result.data);
                    $(".change_cancel").click();
                },
                beforeSend: function () {
                    this.layerIndex = layer.load(0, { shade: [0.01, '#fff'] });
                },
                complete: function () {
                    layer.close(this.layerIndex); //完成加载后关闭loading
                }
            })
        }
    })
    $("#reset").click(function () {
        window.location.reload();
    })
    $(".choose_read").click(function () {
        var length = $(".choose_read:checked").length;
        if(length != 0){
            $(".msg_table .read_msg").css({
                "display":"block",
                "pointer-events":"all",
            })
            $(".msg_table .read_msg").removeAttr("disabled")
        }else{
            $(".msg_table .read_msg").css({
                "display":"none",
                "pointer-events":"none",
            })
            $(".msg_table .read_msg").attr("disabled","disabled")
        }

    })
    $(".read_msg").click(function () {
        var checkedId = "";
        $(".msg_table .choose_read").each(function () {
            if($(this).is(":checked")){
                checkedId += $(this).attr('id')+"@"
            }
        })
        $.ajax({
            type:"GET",
            url:"/user/hasRead",
            data: "checkedId="+checkedId,
            success: function (result) {
                if(result.status != 200){
                    layer.msg(result.msg);
                }
                else{
                  var resultData =  result.data;
                  var splitIds = resultData.split("@");
                  for(var i = 0;i < splitIds.length;i++){
                      $(".msg_table .choose_read").each(function () {
                          if($(this).attr("id") == splitIds[i]){
                              $(this).parents('tr').find(".layui-badge").addClass('layui-bg-blue');
                              $(this).parents('tr').find(".layui-badge").text("已读");
                              $(this).remove();
                          }
                      })
                  }
                  $("#only_msg").text(result.msg);
                    $(".msg_table .read_msg").css({
                        "display":"none",
                        "pointer-events":"none",
                    })
                    $(".msg_table .read_msg").attr("disabled","disabled")
                }
            },
            beforeSend: function () {
                this.layerIndex = layer.load(0, { shade: [0.01, '#fff'] });
            },
            complete: function () {
                layer.close(this.layerIndex); //完成加载后关闭loading
            }
        })

    })

    $(".delete_msg").click(function () {
        var $that = $(this);
        var mid = $(this).attr('mid');
        $.ajax({
            type:"GET",
            url:"/user/deleteMsg",
            data: "mid="+mid,
            success: function (result) {
                if(result.status != 200){
                    layer.msg(result.msg);
                }
                else{
                    $that.parents('tr').remove();
                    $("#only_msg").text(result.data);
                    var length = $(".msg_table tr").length;
                    $(".msg_table .read_msg").css({
                        "display":"none",
                        "pointer-events":"none",
                    })
                    $(".msg_table .read_msg").attr("disabled","disabled")
                    if(length <= 1){
                        $(".msg_table").empty();
                        $("#msg_notice").after("<span style=\"color: firebrick\">暂无消息</span>");
                    }
                }
            },
            beforeSend: function () {
                this.layerIndex = layer.load(0, { shade: [0.01, '#fff'] });
            },
            complete: function () {
                layer.close(this.layerIndex); //完成加载后关闭loading
            }
        })
    })
})
function selectLi() {
   var url = window.location.href;
   var index = url.indexOf("index");
   if( index != -1){
     var $index =  url.substring(index+6);
     if(!isNaN($index)&& parseInt($index) <= 5){
          $(".tabs-vertical ul li").find("a").removeClass("tab-active");

          $(".tabs-vertical ul li:eq('"+(parseInt($index)+1)+"')").find("a").addClass("tab-active");
          $("div.tabs-content-placeholder>div").siblings("div").removeClass('tab-content-active');
          $("div.tabs-content-placeholder>div:eq('"+parseInt($index)+"')").addClass('tab-content-active');
     }
   }
}
function tab() {
    var widget = $('.tabs-vertical');

    var tabs = widget.find('ul a'),
        content = widget.find('.tabs-content-placeholder > div');

    tabs.on('click', function (e) {

        e.preventDefault();

        // Get the data-index attribute, and show the matching content div

        var index = $(this).data('index');

        tabs.removeClass('tab-active');
        content.removeClass('tab-content-active');

        $(this).addClass('tab-active');
        content.eq(index).addClass('tab-content-active');
    })
}
function crop() {
    var options =
        {
            thumbBox: '.thumbBox',
            spinner: '.spinner',
            imgSrc: ''
        }
    var cropper = $('.imageBox').cropbox(options);
    $('#upload-file').on('change', function(){
        var reader = new FileReader();
        reader.onload = function(e) {
            options.imgSrc = e.target.result;
            cropper = $('.imageBox').cropbox(options);
        }
        reader.readAsDataURL(this.files[0]);
        this.files = [];
    })
    $('#btnCrop').on('click', function(){
        var img = cropper.getDataURL();
        $('.cropped').html('');
        $('.cropped').append('<img src="'+img+'" align="absmiddle" style="width:64px;margin-top:4px;border-radius:64px;box-shadow:0px 0px 12px #7E7E7E;" ><p>64px*64px</p>');
        $('.cropped').append('<img src="'+img+'" align="absmiddle" style="width:128px;margin-top:4px;border-radius:128px;box-shadow:0px 0px 12px #7E7E7E;"><p>128px*128px</p>');
        $('.cropped').append('<img src="'+img+'" align="absmiddle" style="width:180px;margin-top:4px;border-radius:180px;box-shadow:0px 0px 12px #7E7E7E;"><p>180px*180px</p>');
    })
    $('#btnZoomIn').on('click', function(){
        cropper.zoomIn();
    })
    $('#btnZoomOut').on('click', function(){
        cropper.zoomOut();
    })
    $(".head_img").click(function () {
        $(".gray_div").css("display","block");
    })
    $(".use_btn_close").click(function () {
        $(".gray_div").css("display","none");
    })
    $(".use_btn_use").click(function () {
        var img_base64 = $(".cropped img").eq(1).attr("src");
        if(img_base64 == undefined){
            layer.msg("请选择照片且裁切后再确认使用！！！")
        }
        else{
            var index = layer.load(1, {
                shade: [0.1,'#a2a2a2cc'] //0.1透明度的白色背景
            });
            $.post("/changeHeadImg",{imgSrc:img_base64},function (result) {
                if(result.data=""){
                    layer.msg("修改头像失败！请稍后再试！");
                }
                else{
                    window.location.reload();
                }
            })
        }

    })
}