$(function () {
    $(".modal").modal({backdrop: "static", keyboard: false});
    $(".modal").modal("hide");

    $(".page_order li a").click(function (e) {
        e.preventDefault();
        var $that = $(this);
        if(!$(this).parents('li').hasClass('active')){
            var page = $(this).text();
            $.ajax({
                type:"GET",
                url:"/admin/findByPage/"+page,
                success: function (result) {
                    if(result.status != 200){
                        layer.alert(result.msg)
                    }else{
                        $("nav").hide()
                        $(".layui-table tbody").fadeOut(1000);
                        var pageData = result.data;
                        var htmls = "";
                        for(var i = 0;i < pageData.content.length;i++){
                            var my_data = pageData.content[i];
                            var oneHtml = "";
                            if(my_data.isPass == 1){
                                 oneHtml = "<button class=\"layui-btn layui-bg-blue\">已经审核通过</button>";
                                 a_url = "<a target='_blank' href='/download/detail/"+my_data.id+"'>"+my_data.title+"</a>"
                            }else{
                                oneHtml="  <button class=\"layui-btn layui-btn-sm allow\">点我审核</button>\n";
                                a_url=my_data.title
                            }
                            var html = " <tr id='"+my_data.id+"'>\n" +
                        "                                        <td style=\"width: 15%\">"+my_data.dwDate+"</td>\n" +
                        "                                        <td style=\"width: 30%\">"+a_url+"</td>\n" +
                        "                                        <td style=\"width: 10%\">"+my_data.tUserByUid.username+"</td>\n" +
                        "                                        <td style=\"width: 10%\">"+my_data.downCounts+"</td>\n" +
                        "                                        <td style=\"width: 10%\">"+my_data.thumbCounts+"</td>\n" +
                        "                                        <td style=\"width: 10%\">"+my_data.commentCounts+"</td>\n" +
                        "                                        <td style=\"width: 15%\">"+oneHtml+"</td>\n" +
                        "                                        <td style=\"width: 15%\"><button class=\"layui-btn layui-btn-danger\">删除</button></td>\n" +
                        "                                    </tr>";
                            htmls += html;
                        }
                        var my_inter = setInterval(function () {
                            $(".layui-table tbody").empty();
                            $(".layui-table tbody").append(htmls);
                            $(".layui-table tbody").fadeIn(1000);
                            $("nav").show()
                            $that.parents('li').addClass('active').siblings().removeClass('active')
                            clearInterval(my_inter)
                        },1000);
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
    })

    $("body").delegate(".allow","click",function () {
        var did = $(this).parents('tr').attr('id');
        $.ajax({
            type:"GET",
            url:"/admin/findById",
            data:"did="+did,
            success: function (result) {
                if(result.status !=200){
                    layer.alert(result.msg);
                }else{
                    var resultData = result.data;
                    $("#time").val(resultData.dwDate);
                    $("#username").val(resultData.tUserByUid.username)
                    $("#title").val(resultData.title)
                    $("#desc").val(resultData.dDesc)
                    $("#downloadBtn").attr("did",resultData.id);
                    $(".modal").modal('show');
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
    $("#downloadBtn").click(function (e) {
        e.preventDefault()
        var did = $(this).attr('did');
        $.ajax({
            type:"GET",
            url:"/admin/downloadFile",
            data:"did="+did,
            success: function (result) {
              if(result.status == 200){
                  window.location.href=result.data;
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
    
    $(".refuse,.pass").click(function () {
        var desc = $("#my_desc").val();

        if(desc.trim().length == 0){
            $("#my_desc").val('');
            $("#my_desc").focus();
            layer.msg("您的理由很重要，请填写后再做出决定");
        }else{
            var did = $("#downloadBtn").attr('did');
            var flag = $(this).attr('tid');
            $.ajax({
                type:"GET",
                url:"/admin/doTask",
                data:"did="+did+"&desc="+desc+"&flag="+flag,
                success: function (result) {
                    if(result.status != 200){
                        layer.alert(result.msg);
                    }else{
                        layer.alert('审核结束', {
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
        }
    })
})