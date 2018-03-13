<%@ page language="java" contentType="text/html; charset=Utf-8"
	pageEncoding="Utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Utf-8">
<title>Insert title here</title>
</head>
<body>
	<form action="login" method="post">
		<input type="text" name="name" id="name"> <input
			type="password" name="password" id="password">
		<button type="button" onClick="login()" value="提交">登录</button>
			
	</form>
</body>
<script type="text/javascript" src="js/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="js/BigInt.js"></script>
<script type="text/javascript" src="js/Barrett.js"></script>
<script type="text/javascript" src="js/RSA.js"></script>
<script>
	function login() {
		$.post("getkey",
				function(data) {
					//data为获取到的公钥数据  
					var pubexponent = data.pubexponent;
					var pubmodules = data.pubmodules;
					setMaxDigits(200);
					var key = new RSAKeyPair(pubexponent, "", pubmodules);
					var password = $("#password").val();
					var encrypedPwd = encryptedString(key,
							encodeURIComponent(password));
					$("#password").val(encrypedPwd);
					var formData = {
						"name" : $("form").find("#name").val(),
						"password" : $("form").find("#password").val(),
					};
					//登录开始  
					$.ajax({
						url : 'login',
						type : 'POST',
						dataType : 'json',
						data : formData,
						success : function(data) {
							//window.location.href= "sysLogin";  
						alert(data.msg)
						}
					});
					//登录结束  
				});

	}
</script>
</html>