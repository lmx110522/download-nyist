$(function () {
    // Basic
    $('.dropify').dropify();

    // Translated
    $('.dropify-fr').dropify({
        messages: {
            'default': '点击或拖拽文件到这里',
            'replace': '点击或拖拽文件到这里来替换文件',
            'remove':  '移除文件',
            'error':   '对不起，你上传的文件太大了'
        }
    });

    // Used events
    var drEvent = $('.dropify-event').dropify();

    drEvent.on('dropify.beforeClear', function(event, element){
        return confirm("Do you really want to delete \"" + element.filename + "\" ?");
    });

    drEvent.on('dropify.afterClear', function(event, element){
        alert('File deleted');
    });

    $(".upload_btn").click(function () {
        var flag = 0;

        $(".must_input").each(function () {
            if($(this).val().trim().length == 0){
                flag = 1;
                $(this).focus();
                return false;
            }
        })
        if($("#select option:selected").val() == ''){
            layer.msg("请上传的文件类型");
            return false;
        }
        if(flag == 1){
            layer.msg("请填写完整再点击立即提交");
            return false;
        }
        $('#myForm').ajaxForm({
            beforeSend: function () {
                this.layerIndex = layer.load(0, { shade: [0.01, '#fff'] });
            },
            complete: function () {
                layer.close(this.layerIndex); //完成加载后关闭loading
            },
            success: function (result) {
                if(result.status != 200){
                    layer.alert(result.msg);
                }
                else{
                    var id = $("#no_use_id").text();
                    layer.confirm('文件上传成功,审核通过后会给你推送消息的~，请问现在您想查看您的上传记录吗', {
                        btn: ['要看','留在本页'] //按钮
                    }, function(){
                        window.location.href="/user/mainPage/"+id+"?index=4";
                    }, function(){
                        window.location.reload();
                    });
                }
            }
        });
    })

})

function filesize(ele) {
    // 返回 KB，保留小数点后两位
    var upload_size = (ele.files[0].size / 1024).toFixed(2)
    if(Math.ceil(upload_size) > 10240){
        $(".dropify-clear").click();
        layer.alert("文件大小不能超过10MB");
    }
}
