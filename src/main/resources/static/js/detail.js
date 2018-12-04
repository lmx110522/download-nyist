$(function () {
    var page = 1;
    $('.modal').modal('hide')
    $('#mpanel3').codeVerify({
        type : 2,
        figure : 100,	//位数，仅在type=2时生效
        arith : 0,	//算法，支持加减乘，不填为随机，仅在type=2时生效
        width : '200px',
        height : '50px',
        fontSize : '30px',
        btnId : 'check-btn2',
        ready : function() {
        },
        success : function() {
            var did = $(".btn_download").attr('id');
            $.ajax({
                type:"GET",
                url:"/download/downFile/"+did,
                success: function (result) {
                    if(result.status != 200){
                        layer.alert(result.msg);
                    }
                    else{
                        window.location.href=result.data;
                        $(".varify_modal").modal('hide');
                        layer.alert(result.msg, {
                            icon: 1,
                            offset: 'm',
                            anim: 6
                        },function () {
                            window.location.reload();
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
        },
        error : function() {
            layer.msg('验证码不匹配', {
                offset: 't',
                anim: 6
            });
            $(".verify-change-code").click();
            $(".varify-input-code").focus()
        }
    });
    $(".btn_download").click(function () {
        $('.varify_modal').modal('show')
    })
    $(".click_love").click(function () {
        var $that = $(this)
        var did = $("#did").val();
        var len = $(this).find(".cancel_thumb").length;
        var flag = 0;
        if(len == 0){
            flag = 1;
        }
        else{
            flag = -1;
        }
        $.ajax({
            type:"GET",
            url:"/download/thumb",
            data: "flag="+parseInt(flag)+"&did="+did,
            success: function (result) {
                if(result.status != 200){
                   layer.alert(result.msg);
                }
                else{
                   if(flag == 1){
                       $that.find(".layui-icon").removeClass("layui-icon-praise").addClass("layui-icon-tread").addClass("cancel_thumb")
                       $that.find("span:last-child").text(parseInt(  $that.find("span:last-child").text())+1);
                       $that.find(".thumb_word").text("取消赞")
                   }
                   else{
                       $that.find(".layui-icon").addClass("layui-icon-praise").removeClass("layui-icon-tread").removeClass("cancel_thumb")
                       $that.find("span:last-child").text(parseInt( parseInt($that.find("span:last-child").text())-1));
                       $that.find(".thumb_word").text("点赞")
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
    $("#comment_login").click(function (e) {
        e.preventDefault();
        $('.login_modal').modal('show');
    })
    $(".comment-submit").click(function (e) {
        e.preventDefault();
        var content = $("#comment-textarea").val();
        if(content.trim().length != 0){
            $.ajax({
                type:"POST",
                url:"/comment/sendComment",
                data: $("#comment-form").serialize(),
                success: function (result) {
                    $("#comment_counts").text(parseInt( $("#comment_counts").text())+1);
                    if(result.status != 200){
                        layer.alert(result.msg);
                    }
                    else{

                        var len = $(".null_comment").length;
                        var resultData = result.data;
                        var my_html = "<span class=\"source_name his_size removeComment\" id=\""+resultData.id+"\" title=\"删除\"><span class=\"layui-icon layui-icon-delete\"></span></span>"
                        var html = "<li>  <img class=\"source_img\" src=\""+resultData.tUserByUid.headImg+"\">\n" +
                            "                                      <span class=\"floor_num\">1</span>\n" +
                            "                                      <div class=\"content_msg\">\n" +
                            "                                          <p class=\"detail_title\"><a href=\"#\"></a></p>\n" +
                            "                                          <span class=\"pdesc\">\n" +
                            "                               "+resultData.content+"\n" +
                            "                              </span>\n" +
                            "                                          <p class=\"content_source\">" +
                            " \n" +
                            "                                              <span class=\"source_name his_size\">"+resultData.tUserByUid.username+"</span>.\n" +
                            "                                              <span class=\"source_name his_size\">"+resultData.cDate+"</span>\n" +
                            "                                         "+my_html+" </p>\n" +
                            "                                      </div></li>";
                        if(len != 0){
                            $(".null_comment").remove();
                            var htmls = "<ul>";

                            htmls+=(html+"</ul>")
                            $(".show_comments h4").after(htmls);
                        }
                        else{

                            $(".show_comments ul").prepend(html);
                        }


                    }

                    $(".floor_num").each(function (i,val) {
                        $(this).text("#"+(i+1));
                    })
                    $("#comment-textarea").val("")
                    $("#comment-textarea").focus();

                },
                beforeSend: function () {
                    this.layerIndex = layer.load(0, { shade: [0.01, '#fff'] });
                },
                complete: function () {
                    layer.close(this.layerIndex); //完成加载后关闭loading
                }
            })

        }
        else{
            $("#comment-textarea").focus();
            $("#comment-textarea").val('');
            layer.msg('评论不能为空', {
                offset: 't',
                anim: 6
            });
        }
    })

    $(".watch_more_comment").delegate(".more_btn","click",function (e) {
        e.preventDefault();
        $(this).remove();
        $(".watch_more_comment").append(" <h3  class=\"dot-floating my_wait\"></h3>");
        var did = $("#did").val()
        $.getJSON("/comment/getComments/"+did+"/"+parseInt(page),function (result) {
            $(".my_wait").remove();
            if(result.status != 200){
                layer.msg(result.msg);
            }
            else{
                var resultData = result.data;
                if(resultData.length == 0){
                    $(".watch_more_comment").append("<h4 class='no_comment'>评论显示完了呦~</h4>");
                }
                else{
                    var htmls = "";
                    var my_id = $("#no_use_id").text();
                    for(var i = 0;i < resultData.length;i++){
                        var my_html = "";
                        if(my_id == resultData[i].tUserByUid.id){
                            my_html = "<span class=\"source_name his_size removeComment\" id=\""+resultData[i].id+"\" title=\"删除\"><span class=\"layui-icon layui-icon-delete\"></span></span>"
                        }

                        var html = "<li>  <img class=\"source_img\" src=\""+resultData[i].tUserByUid.headImg+"\">\n" +
                            "                                      <span class=\"floor_num\">1</span>\n" +
                            "                                      <div class=\"content_msg\">\n" +
                            "                                          <p class=\"detail_title\"><a href=\"#\"></a></p>\n" +
                            "                                          <span class=\"pdesc\">\n" +
                            "                               "+resultData[i].content+"\n" +
                            "                              </span>\n" +
                            "                                          <p class=\"content_source\">" +
                            " \n" +
                            "                                              <span class=\"source_name his_size\">"+resultData[i].tUserByUid.username+"</span>.\n" +
                            "                                              <span class=\"source_name his_size\">"+resultData[i].cDate+"</span>\n" +
                            "                                         "+my_html+" </p>\n" +
                            "                                      </div></li>";
                            htmls += html;
                    }
                    $(".show_comments ul").append(htmls);
                    $(".watch_more_comment").append("  <a class=\"more_btn\" href=\"#\"><span class=\"layui-icon layui-icon-form\"></span> 查看更多</a>");
                    page++;
                    $(".floor_num").each(function (i,val) {
                        $(this).text("#"+(i+1));
                    })
                }
            }
        })
    })
    $(".layui-layer-btn0").click(function () {
        window.location.reload();
    })

    $("body").delegate(".removeComment","click",function (e) {
        var $that = $(this);
        layer.confirm('确定删除这条精彩的评论吗？', {
            btn: ['删除','取消'] //按钮
        }, function(){
            var cid = $that.attr('id');
            $.ajax({
                type:"GET",
                url:"/comment/delete/"+cid,
                success: function (result) {
                    if(result.status != 200){
                        layer.alert(result.msg);
                    }
                    else{
                        layer.msg('删除成功', {
                            offset: 't',
                            anim: 6
                        });
                        $that.parents("li").fadeOut(500);
                        var my_inter = setInterval(function () {
                            $that.parents("li").remove();
                            clearInterval(my_inter);
                            $("#comment_counts").text(parseInt( $("#comment_counts").text())-1)
                            var len = $(".show_comments>ul li").length;
                             if(len == 0){
                                 $(".show_comments>ul").remove();
                                 $(".show_comments").append("<ul class=\"null_comment\"><li><img src=\"/img/null.png\"></li></ul>")
                                 $(".watch_more_comment").remove();
                             }
                             else{
                                 $(".floor_num").each(function (i,val) {
                                     $(this).text("#"+(i+1));
                                 });
                             }

                        },500)
                    }
                },
                beforeSend: function () {
                    this.layerIndex = layer.load(0, { shade: [0.01, '#fff'] });
                },
                complete: function () {
                    layer.close(this.layerIndex); //完成加载后关闭loading
                }
            })
        }, function(){

        });
    })
})