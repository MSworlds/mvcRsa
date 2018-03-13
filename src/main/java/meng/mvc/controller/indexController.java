package meng.mvc.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import meng.mvc.util.RSAUtil;

@Controller
public class indexController {
@RequestMapping("/index")
public String index(){
	return "index";
}
@RequestMapping("/logintest")
public  void logintest(String  name, String password){
System.out.println(name);
}


          
        @RequestMapping(value = "/getkey")    
    @ResponseBody   
        public Object getPublicKey(HttpServletRequest request)throws Exception{  
                 Map<String,String> result = new HashMap<String,String>();   
                 KeyPair kp = RSAUtil.generateKeyPair();  
                 RSAPublicKey pubk = (RSAPublicKey) kp.getPublic();// ���ɹ�Կ  
                 RSAPrivateKey prik = (RSAPrivateKey) kp.getPrivate();// ����˽Կ  
                   
                 String publicKeyExponent = pubk.getPublicExponent().toString(16);// 16����  
                 String publicKeyModulus = pubk.getModulus().toString(16);// 16����  
                 request.getSession().setAttribute("prik", prik);  
                 result.put("pubexponent", publicKeyExponent);  
                 result.put("pubmodules", publicKeyModulus);  
                 return result;    
        }  
  
        @RequestMapping(value = "/login")    
        @ResponseBody   
            public Map login(String  name, String password,HttpServletRequest request){
        	 Map<String,String> result = new HashMap<String,String>();
        	 System.out.println("���ܺ�,name:"+name+",password:"+password);
        	 HttpSession session = request.getSession();
        	 RSAPrivateKey prik = (RSAPrivateKey) session.getAttribute("prik");
        	   try {
				   byte[] en_password = new BigInteger(password, 16).toByteArray();    

				   byte[] de_password = RSAUtil.decrypt(prik, en_password);    

				   StringBuffer sb1 = new StringBuffer();    

				   sb1.append(new String(de_password));    

				   password= sb1.reverse().toString();
				   System.out.println("���ܺ�,name:"+name+",password:"+password);
				   result.put("msg","��½�ɹ�");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				result.put("msg","��½ʧ��");
			} catch (Exception e) {
				e.printStackTrace();
				result.put("msg","��½ʧ��");
			}
				return result;
        	
        }

}
