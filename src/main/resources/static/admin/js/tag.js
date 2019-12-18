function tagcl(title,url,pd) {
    if (pd){
        alert("添加成功！");
    }else{
        alert("添加失败！");
    }
    var $tabs = parent.$(parent.document).data('multitabs');
    if (!$tabs) return;
    var $navPanelList = $tabs.$element.navPanelList;
    $navTab2 = $navPanelList.find('a[data-url="' + url + '"]:first');
    if($navTab2.length!=0){
        $tabs.close($navTab2);
    }
    $navTab = $navPanelList.find('li.active:first a');
    parent.$(parent.document).data('multitabs').create({
        iframe : true,                                // 指定为iframe模式，当值为false的时候，为智能模式，自动判断（内网用ajax，外网用iframe）。缺省为false。
        title : title,                     // 标题（可选），没有则显示网址
        url : url                    // 链接（必须），如为外链，强制为info页
    }, true); // true 则激活新增的tab页
    $tabs.close($navTab);
}