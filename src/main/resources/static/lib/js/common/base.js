$(function() {
    function shadeBlend(p,c0,c1) {
        var n=p<0?p*-1:p,u=Math.round,w=parseInt;
        if(c0.length>7){
            var f=c0.split(","),t=(c1?c1:p<0?"rgb(0,0,0)":"rgb(255,255,255)").split(","),R=w(f[0].slice(4)),G=w(f[1]),B=w(f[2]);
            return "rgb("+(u((w(t[0].slice(4))-R)*n)+R)+","+(u((w(t[1])-G)*n)+G)+","+(u((w(t[2])-B)*n)+B)+")"
        }else{
            var f=w(c0.slice(1),16),t=w((c1?c1:p<0?"#000000":"#FFFFFF").slice(1),16),R1=f>>16,G1=f>>8&0x00FF,B1=f&0x0000FF;
            return "#"+(0x1000000+(u(((t>>16)-R1)*n)+R1)*0x10000+(u(((t>>8&0x00FF)-G1)*n)+G1)*0x100+(u(((t&0x0000FF)-B1)*n)+B1)).toString(16).slice(1)
        }
    }
    var initMenu = function(){
        var  module = $('.super-setting-left ul li')[0];
        $('.super-west').panel({title : module.innerText});
        module.click();

    }
    var initTheme = function() {
        var theme = localStorage.getItem('theme');
        if(theme != null){
            changeTheme(theme);
        }
        var color = theme.split(',')[0].replace("#",'');
        // 添加勾选状态
        $(".themeItem ul li").removeClass('themeActive');
        $('.themeItem ul li .' + color).parent().addClass('themeActive');
    }
    var changeTheme = function(theme){
        var root = document.querySelector(':root');
        root.style.setProperty('--themeColor', theme.split(',')[0]);
        root.style.setProperty('--selectBackground', theme.split(',')[1]);
        root.style.setProperty('--logoBackgroud', theme.split(',')[2]);
    }
    // 初始化主题
    initTheme();
    // 点击模块事件
    $('.super-setting-left ul li').on('click', function() {
        var module = $(this)[0];
        $.ajax({
            url:'./menu/list/' + module.id,
            type:"get",
            success:function(menus){
                var a = $('#menu').accordion('panels').length;
                for(var i = 0;i<a;i++){
                    $('#menu').accordion('remove',0);
                }
                for(var i = 0;i< menus.length;i++){
                    var children = menus[i].children;
                    var html = "<ul>";
                    for (var j = 0;j< children.length;j++){
                        html += "<li data-url='" + children[j].entity + "'>" + children[j].text + "</li>";
                    }
                    html += "</ul>";
                    $('#menu').accordion('add',{
                        title:menus[i].text,
                        content:html,
                        iconCls:menus[i].iconCls,
                        selected:false
                    });
                }
                $('.super-west').panel({title : module.innerText});
                // 左侧导航分类选中样式
                $(".panel-body .accordion-body>ul").on('click', 'li', function() {
                    $(this).siblings().removeClass('super-accordion-selected');
                    $(this).addClass('super-accordion-selected');
                    //新增一个选项卡
                    var tabUrl = "index/" + $(this).data('url');
                    var tabTitle = $(this).text();
                    //tab是否存在
                    if($("#tt").tabs('exists', tabTitle)) {
                        $("#tt").tabs('select', tabTitle);
                    } else {
                        var content = '<iframe  src=' + tabUrl + ' style="width:100%;height:99%;"></iframe>';
                        $('#tt').tabs('add', {
                            title: tabTitle,
                            content: content,
                            closable: true
                        });
                    }
                });
            },
            error : function(data) {
                alert(data.responseJSON.message);
            }
        })
    });
    initMenu();
    // 设置按钮的下拉菜单
    $('.super-setting-icon').on('click', function() {
        $('#mm').menu('show', {
            top: 50,
            left: document.body.scrollWidth - 160
        });
    });
    // 修改主题
    $('#themeSetting').on('click', function() {
        var themeWin = $('#win').dialog({
            width: 575,
            height: 260,
            modal: true,
            title: '主题设置',
            buttons: [{
                text: '保存',
                id: 'btn-sure',
                handler: function() {
                    themeWin.panel('close');
                    var themeColor = '#'+ $(".themeItem ul li.themeActive>div").attr('class');
                    var selectBackground = shadeBlend(-0.05,themeColor);
                    var logoBackgroud = shadeBlend(-0.15,themeColor);
                    var theme = [themeColor,selectBackground,logoBackgroud];
                    localStorage.setItem('theme', theme);
                    changeTheme(localStorage.getItem('theme'));
                }
            }, {
                text: '关闭',
                handler: function() {
                    themeWin.panel('close');
                }
            }],
            onOpen: function() {
                $(".themeItem").show();
            }
        });
    });

    // 勾选主题
    $(".themeItem ul li").on('click', function() {
        $(".themeItem ul li").removeClass('themeActive');
        $(this).addClass('themeActive');
    });

    // 退出系统
    $("#logout").on('click', function() {
        $.messager.confirm('提示', '确定退出系统？', function(r) {
            if(r) {
                window.location.href=("/logout");
            }
        });
    });


});
