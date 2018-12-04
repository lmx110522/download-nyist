$(function () {
    $(".choose_item li").click(function () {
        if($(this).hasClass('hasCheck')){
            $(this).removeClass("hasCheck");
            ajaxGetDownLoad();
        }
        else{
            $(this).addClass("hasCheck");
            ajaxGetDownLoad();
        }

    })

    $(".layui-icon-right").click(function () {
        var all_page = $(".all_page").text();
        var page = $(".current_page").text();
        if(parseInt(all_page) <= parseInt(page)){
            layer.alert("已经是尾页了")
        }
        else{
            ajaxGetDownLoad((parseInt(page)+1)+"");
            $(".current_page").text(parseInt(page)+1);
        }
    })

    $(".layui-icon-left").click(function () {
        var page = $(".current_page").text();
        if(parseInt(page) <= 1){
            layer.alert("已经是第一页了")
        }
        else{
            ajaxGetDownLoad((parseInt(page)-1)+"");
            $(".current_page").text(parseInt(page)-1);
        }

    })

    $("#page_num").change(function () {
        var page = $(this).val();
        if(isNaN(page)){
            layer.msg("查找的页数输入不合法");
        }else{
            if(page.trim().length == 0){
                $(this).val("1");
            }
            ajaxGetDownLoad();
        }
    })

    $(".tier_ul li").click(function () {
        var $that = $(this);
        $that.addClass('active_cate').siblings().removeClass('active_cate');
        var page =  $(".current_page").text();
        if(isNaN(page)){
            layer.msg("查找的页数输入不合法");
        }else{
            if(page.trim().length == 0){
                $(this).val("1");
            }
            ajaxGetDownLoad();
            $that.css({
                "background":"white",
            }).siblings("li").css({"background":"#2F85FC"})
            $that.find("a").css({
                "color":"#2F85FC"
            }).parent("li").siblings("li").find("a").css({ "color":"white"})
        }
    })

})

function ajaxGetDownLoad(page) {
    var cid = $(".tier_ul li.active_cate").attr('id');
    var condition ="";
    if(page == null){
        page = $("#page_num").val();
    }
    $(".choose_item li.hasCheck").each(function () {
        var name = $(this).attr("id");
        condition += name+"@";
    })
    $.ajax({
        type:"GET",
        url:"/download/findCondition",
        data: "condition="+condition+"&page="+page+"&cid="+cid,
        success: function (result) {
            console.log(result);
            if(result.status != 200){
                layer.alert(result.msg);
            }
            else{
                $(".new_content_ul").empty();
                var htmls="";
                var resultData = result.data.content;
                if(resultData.length != 0){
                    for(var i = 0; i< resultData.length;i++){
                        var html="<li>\n" +
                            "                        <img class=\"source_img\" src=\""+resultData[i].tUserByUid.headImg+"\">\n" +
                            "                        <div class=\"go_link_div\">\n" +
                            "                            <a href=\"/download/detail/"+resultData[i].id+"\" id=\""+resultData[i].id+"\">去下载</a>\n" +
                            "                        </div>\n" +
                            "                        <div class=\"content_detail\">\n" +
                            "                            <p class=\"detail_title\"><a href=\"/download/detail/"+resultData[i].id+"\">"+resultData[i].title+"</a></p>\n" +
                            "                            <span class=\"pdesc\">\n" +
                            "                          "+resultData[i].dDesc+"\n" +
                            "                       </span>\n" +
                            "                            <p class=\"content_source\">\n" +
                            "                                <span class=\"source_name\">13193801071</span>.\n" +
                            "                                <span class=\"source_name\">2018-12-23 08:22</span>\n" +
                            "                            </p>\n" +
                            "                        </div>\n" +
                            "                    </li>"
                        htmls += html;
                    }
                }
                else{
                    htmls=" <li><img src=\"/img/null.png\"></li>";
                }
                $(".new_content_ul").append(htmls);
                $(".all_page").text(result.data.totalPages);
                $(".current_page").text(result.data.number+1);
                $(".choose_nums").text(result.data.totalElements);
                if(cid != ''){

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
}