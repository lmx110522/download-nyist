$(function () {
    $(".tier_ul li").click(function (e) {
        e.preventDefault();
        var $that = $(this)
        var cid = $(this).attr("id");
        $.ajax({
            type:"GET",
            url:"/download/findByCate",
            data: "cid="+cid,
            success: function (result) {
                if(result.status != 200){
                    layer.alert(result.msg);
                }
                else{
                    $(".new_content_ul").empty();
                    var htmls="";
                    var resultData = result.data;
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
                    $(".new_content_ul").append(htmls);
                    $that.css({
                        "background":"white",
                    }).siblings("li").css({"background":"#2F85FC"})
                    $that.find("a").css({
                        "color":"#2F85FC"
                    }).parent("li").siblings("li").find("a").css({ "color":"white"})
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