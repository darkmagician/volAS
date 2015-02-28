/**
 * 
 */
package com.vol.mgmt.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vol.auth.AuthenticationServiceHolder;
import com.vol.auth.IdentityChangeListener;

/**
 * @author scott
 *
 */
public class CredentialUtil {
	
	private static final Logger log = LoggerFactory.getLogger(CredentialUtil.class);
	
	private final static String chars = "abcdefghijklmnopqrstuvwxyz"+"ABCDEFGHIJKLMNOPQRSTUVWXYZ"+"0123456789";
	
	private final static Random rand = new Random();
	
	private static MessageDigest __md;
	
	private static final Object lock = new Object();
	   
	public static String generatePass(){
		int range = chars.length();
		char [] pass = new char [8];
		for(int i=0;i<pass.length;i++){
			pass[i]=chars.charAt(rand.nextInt(range));
		}
		return new String(pass);
	}
	
    public static String digest(String password)
    {
        try
        {
            byte[] digest;
            synchronized (lock)
            {
                if (__md == null)
                {
                    try
                    {
                        __md = MessageDigest.getInstance("MD5");
                    }
                    catch (Exception e)
                    {
                    	log.warn("failed to generate MD5",e);
                        return null;
                    }
                }

                __md.reset();
                __md.update(password.getBytes(StandardCharsets.ISO_8859_1));
                digest = __md.digest();
            }

            return com.vol.auth.AuthenticationService.MD5_PREFIX + toString(digest, 16);
        }
        catch (Exception e)
        {
        	log.warn("failed to digest",e);
            return null;
        }
    }
    
    private static String toString(byte[] bytes, int base)
    {
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes)
        {
            int bi=0xff&b;
            int c='0'+(bi/base)%base;
            if (c>'9')
                c= 'a'+(c-'0'-10);
            buf.append((char)c);
            c='0'+bi%base;
            if (c>'9')
                c= 'a'+(c-'0'-10);
            buf.append((char)c);
        }
        return buf.toString();
    }
    
    
    public static void revoke(String name){
    	 Set<IdentityChangeListener> listeners = AuthenticationServiceHolder.getInstance().getListeners();
    	 for(IdentityChangeListener listener: listeners){
    		 listener.onChange(name);
    	 }
    }
    
}
