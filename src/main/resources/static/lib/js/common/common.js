/**
 * layout方法扩展
 * @param {Object} jq
 * @param {Object} region
 */
$.extend($.fn.layout.methods, {
    /**
     * 面板是否存在和可见
     * @param {Object} jq
     * @param {Object} params
     */
    isVisible: function(jq, params) {
        var panels = $.data(jq[0], 'layout').panels;
        var pp = panels[params];
        if(!pp) {
            return false;
        }
        if(pp.length) {
            return pp.panel('panel').is(':visible');
        } else {
            return false;
        }
    },
    /**
     * 隐藏除某个region，center除外。
     * @param {Object} jq
     * @param {Object} params
     */
    hidden: function(jq, params) {
        return jq.each(function() {
            var opts = $.data(this, 'layout').options;
            var panels = $.data(this, 'layout').panels;
            if(!opts.regionState){
                opts.regionState = {};
            }
            var region = params;
            function hide(dom,region,doResize){
                var first = region.substring(0,1);
                var others = region.substring(1);
                var expand = 'expand' + first.toUpperCase() + others;
                if(panels[expand]) {
                    if($(dom).layout('isVisible', expand)) {
                        opts.regionState[region] = 1;
                        panels[expand].panel('close');
                    } else if($(dom).layout('isVisible', region)) {
                        opts.regionState[region] = 0;
                        panels[region].panel('close');
                    }
                } else {
                    panels[region].panel('close');
                }
                if(doResize){
                    $(dom).layout('resize');
                }
            };
            if(region.toLowerCase() == 'all'){
                hide(this,'east',false);
                hide(this,'north',false);
                hide(this,'west',false);
                hide(this,'south',true);
            }else{
                hide(this,region,true);
            }
        });
    },
    /**
     * 显示某个region，center除外。
     * @param {Object} jq
     * @param {Object} params
     */
    show: function(jq, params) {
        return jq.each(function() {
            var opts = $.data(this, 'layout').options;
            var panels = $.data(this, 'layout').panels;
            var region = params;

            function show(dom,region,doResize){
                var first = region.substring(0,1);
                var others = region.substring(1);
                var expand = 'expand' + first.toUpperCase() + others;
                if(panels[expand]) {
                    if(!$(dom).layout('isVisible', expand)) {
                        if(!$(dom).layout('isVisible', region)) {
                            if(opts.regionState[region] == 1) {
                                panels[expand].panel('open');
                            } else {
                                panels[region].panel('open');
                            }
                        }
                    }
                } else {
                    panels[region].panel('open');
                }
                if(doResize){
                    $(dom).layout('resize');
                }
            };
            if(region.toLowerCase() == 'all'){
                show(this,'east',false);
                show(this,'north',false);
                show(this,'west',false);
                show(this,'south',true);
            }else{
                show(this,region,true);
            }
        });
    }
});
var add = function(){
    $('#currentTab').layout('show','east');
    $('#currentTab').layout();
    $('#detailForm').form('clear');
    $('#detailForm').form('disableValidation');
}
var closeRightPanel = function(){
    $('#currentTab').layout('hidden','east');
    $('#currentTab').layout();
}
var mod = function(){
    var selectRows = $("#mainTable").datagrid("getSelections");
    if(selectRows.length != 1){
        alert("请选择一条需要编辑的信息");
        return;
    }
    var data = selectRows[0];
    add();
    $('#detailForm').form('load',data);
}
var del = function(){
    alert("delete");
}
var filter = "enableFilter";
var search = function (){
    var dg = $('#mainTable').datagrid({
        filterBtnIconCls:'fa fa-search',
        filterMenuIconCls:'fa fa-check',
        remoteFilter:true
    });
    dg.datagrid('enableFilter', [{
        field:'id',
        type:'textbox',
        options:{precision:1},
        op:['equal','notequal','less','greater','beginwith','endwith','range']
    },{
        field:'userName',
        type:'datebox',
        options:{precision:1},
        op:['equal','notequal','less','greater','range']
    },{
        field:'realName',
        type:'combobox',
        options:{
            panelHeight:'auto',
            data:[{value:'',text:'All'},{value:'P',text:'P'},{value:'N',text:'N'}],
            onChange:function(value){
                if (value == ''){
                    dg.datagrid('removeFilterRule', 'status');
                } else {
                    dg.datagrid('addFilterRule', {
                        field: 'status',
                        op: 'equal',
                        value: value
                    });
                }
                dg.datagrid('doFilter');
            }
        }
    }]);
    dg.datagrid(filter);
    if(filter == 'enableFilter'){
        filter = "disableFilter";
    }else{
        filter = "enableFilter";

    }

}
var save = function(){
    $('#detailForm').form('submit',{
        onSubmit:function(){
            return $(this).form('enableValidation').form('validate');
        }
    });
}

var initMainTable = function(){
    $('#currentTab').layout();
    $('#currentTab').layout('hidden','east');
    $('#mainTable').datagrid({
        //url: url,
        //method:"get",
        //fit:true,
        //rownumbers:true,
        //striped:'true',
        toolbar: toolbar,
        // toolbar: "#toolbar",
        //singleSelect: true,
        //pagination:true,
        //pageNumber:1,
        //pageSize:20,
        //pageList:[20,50,100,500,1000],
        //columns:[columns],
        onLoadSuccess :function(data){

        },
        onHeaderContextMenu: function(e, field){
            e.preventDefault();
            if (!cmenu){
                createColumnMenu();
            }
            cmenu.menu('show', {
                left:e.pageX,
                top:e.pageY
            });
        }
    });
    var cmenu;
    function createColumnMenu(){
        cmenu = $('<div/>').appendTo('body');
        cmenu.menu({
            onClick: function(item){
                if (item.iconCls == 'fa fa-circle'){
                    $('#mainTable').datagrid('hideColumn', item.name);
                    cmenu.menu('setIcon', {
                        target: item.target,
                        iconCls: 'fa fa-circle-o'
                    });
                } else {
                    $('#mainTable').datagrid('showColumn', item.name);
                    cmenu.menu('setIcon', {
                        target: item.target,
                        iconCls: 'fa fa-circle'
                    });
                }
            }
        });
        var fields = $('#mainTable').datagrid('getColumnFields');
        for(var i=0; i<fields.length; i++){
            var field = fields[i];
            var col = $('#mainTable').datagrid('getColumnOption', field);
            if(col.title){
                cmenu.menu('appendItem', {
                    text: col.title,
                    name: field,
                    iconCls: 'fa fa-circle'
                });
            }
        }
    }
};