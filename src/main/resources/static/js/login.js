/*
 * @作者: Hewitt  邮箱:qiaohewei@126.com  网站:www.hwcyxk.com 
 * @日期: 2018-10-17 17:23:35 
 * @最后修改: Hewitt  邮箱:qiaohewei@126.com  网站:www.hwcyxk.com 
 * @最后修改时间: 2018-10-17 17:23:35 
 */

// 登录按钮切换

function PwdLogin() {
    var login = document.getElementsByClassName("login_con");
    login[0].classList.remove("hidden");
    login[0].classList.add("show");
    login[1].classList.remove("show");
    login[1].classList.add("hidden");
    var tags = document.getElementsByClassName("top_tag");
    tags[0].classList.add("active");
    tags[1].classList.remove("active");
    var ad = document.getElementById("AdImg");
    ad.style.height = "558px";
    // ad.style.backgroundImage='url(https://static.zcool.cn/v1.1.43/passport4.0/images/login-ground.jpg)';
}
function QrcodeLogin() {
    var login = document.getElementsByClassName("login_con");
    login[0].classList.remove("show");
    login[0].classList.add("hidden");
    login[1].classList.remove("hidden");
    login[1].classList.add("show");
    var tags = document.getElementsByClassName("top_tag");
    tags[1].classList.add("active");
    tags[0].classList.remove("active");
    var ad = document.getElementById("AdImg");
    ad.style.height = "407px";
}

// 用户输入错误提示

var inputs = document.getElementsByTagName('input');
var tips = document.getElementsByClassName('tips');

function InputNull(a){
    tips[a].classList.add('show');
    tips[a].classList.remove('hidden');
}
function InputNoNull(a){
    tips[a].classList.add('hidden');
    tips[a].classList.remove('show');
}
var show_num = [];
draw(show_num);
$("#canvas").on('click',function(){
    draw(show_num);
})
var UserName     = inputs[0];
var UserPwd      = inputs[1];
var AuthCode     = inputs[2];
var UserNameTips = tips[0];
var UserPwdTips  = tips[1];
var AuthCodeTips = tips[2];

UserName.oninput = function () {
    if (UserName.value == '') {
        InputNull(0);
        UserNameTips.innerHTML="请检查用户名，用户名不能为空";
    } else{
        InputNoNull(0);
    }
}
UserName.onblur = function () {
    if (UserName.value == '') {
        InputNull(0);
        UserNameTips.innerHTML="请检查用户名，用户名不能为空";

    } 
}

UserPwd.oninput = function () {
    if (UserPwd.value == '') {
        InputNull(1);
        UserPwdTips.innerHTML="请检查您的密码，密码不能为空";
    } else{
        InputNoNull(1);
    }
}
UserPwd.onblur = function () {
    if (UserPwd.value == '') {
        InputNull(1);
        UserPwdTips.innerHTML="请检查您的密码，密码不能为空";
    } 
}

AuthCode.oninput = function () {

    var num = show_num.join("");
    if (AuthCode.value == ''|| AuthCode.value.toLowerCase() !== num) {
        InputNull(2);
        AuthCodeTips.innerHTML="请检查验证码，验证码错误";
    } else if(AuthCode.value.toLowerCase() == num){
        InputNull(2);
        AuthCodeTips.innerHTML="验证码正确";
    }else{
        InputNoNull(2);
    }
}
AuthCode.onblur = function () {
    if (AuthCode.value == '') {
        InputNull(2);
        AuthCodeTips.innerHTML="请检查验证码，验证码错误";
    }
}


function draw(show_num) {
    var canvas_width=$('#canvas').width();
    var canvas_height=$('#canvas').height();
    var canvas = document.getElementById("canvas");//获取到canvas的对象，演员
    var context = canvas.getContext("2d");//获取到canvas画图的环境，演员表演的舞台
    canvas.width = canvas_width;
    canvas.height = canvas_height;
    var sCode = "A,B,C,E,F,G,H,J,K,L,M,N,P,Q,R,S,T,W,X,Y,Z,1,2,3,4,5,6,7,8,9,0";
    var aCode = sCode.split(",");
    var aLength = aCode.length;//获取到数组的长度

    for (var i = 0; i <= 3; i++) {
        var j = Math.floor(Math.random() * aLength);//获取到随机的索引值
        var deg = Math.random() * 30 * Math.PI / 180;//产生0~30之间的随机弧度
        var txt = aCode[j];//得到随机的一个内容
        show_num[i] = txt.toLowerCase();
        var x = 10 + i * 20;//文字在canvas上的x坐标
        var y = 20 + Math.random() * 8;//文字在canvas上的y坐标
        context.font = "bold 23px 微软雅黑";

        context.translate(x, y);
        context.rotate(deg);

        context.fillStyle = randomColor();
        context.fillText(txt, 0, 0);

        context.rotate(-deg);
        context.translate(-x, -y);
    }
    for (var i = 0; i <= 5; i++) { //验证码上显示线条
        context.strokeStyle = randomColor();
        context.beginPath();
        context.moveTo(Math.random() * canvas_width, Math.random() * canvas_height);
        context.lineTo(Math.random() * canvas_width, Math.random() * canvas_height);
        context.stroke();
    }
    for (var i = 0; i <= 30; i++) { //验证码上显示小点
        context.strokeStyle = randomColor();
        context.beginPath();
        var x = Math.random() * canvas_width;
        var y = Math.random() * canvas_height;
        context.moveTo(x, y);
        context.lineTo(x + 1, y + 1);
        context.stroke();
    }
}

function randomColor() {//得到随机的颜色值
    var r = Math.floor(Math.random() * 256);
    var g = Math.floor(Math.random() * 256);
    var b = Math.floor(Math.random() * 256);
    return "rgb(" + r + "," + g + "," + b + ")";
}



// inputs[1].onmouseout = function () {

//     if (inputs[1].value == 0) {
//         tips[1].classList.add('show');
//         tips[1].classList.remove('hidden');

//     } else {
//         tips[1].classList.add('hidden');
//         tips[1].classList.remove('show');
//     }
// }
// inputs[2].onmouseout = function () {

//     if (inputs[2].value == 0) {
//         tips[2].classList.add('show');
//         tips[2].classList.remove('hidden');

//     } else {
//         tips[2].classList.add('hidden');
//         tips[2].classList.remove('show');
//     }
// }
