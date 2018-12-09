/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guillermovallespir.ultradbscript.core;

import java.nio.ByteBuffer;
import java.security.SecureRandom;

/**
 *
 * @author guille
 */
public class Utils {
    public static String uniqid(String prefix,boolean more_entropy)
	{
		long time = System.currentTimeMillis();
		//String uniqid = String.format("%fd%05f", Math.floor(time),(time-Math.floor(time))*1000000);
		//uniqid = uniqid.substring(0, 13);
		String uniqid = "";
		if(!more_entropy)
		{
			uniqid = String.format("%s%08x%05x", prefix, time/1000, time);
		}else
		{
			SecureRandom sec = new SecureRandom();
			byte[] sbuf = sec.generateSeed(8);
			ByteBuffer bb = ByteBuffer.wrap(sbuf);

			uniqid = String.format("%s%08x%05x", prefix, time/1000, time);
			uniqid += "." + String.format("%.8s", ""+bb.getLong()*-1);
		}


		return uniqid ;
	}
}
