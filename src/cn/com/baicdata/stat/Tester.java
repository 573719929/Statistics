package cn.com.baicdata.stat;

import java.util.HashMap;

public class Tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap<String, String[]> d = new HashMap<String, String[]>();
//		long len = Long.parseLong(args[0])*10000;
//		int sec = Integer.parseInt(args[1])*1000;
		long len = 100*10000;
		int sec = 10*1000;
		for (int i = 0; i < len; i++) {
			String t[] = new String[6];
			t[0] = "dff290dc1f6cfaf98b8a22172af2d24";
			t[1] = "www.juyouqu.com";
			t[2] = "000000000";
			t[3] = "xxxx";
			t[4] = "sss";
			t[5] = "132";
			d.put(String.format("%40d", i), null);
//			System.out.println(String.format("%40d", i));
		}
		System.out.println(String.format("sleep"));
		try {
			Thread.sleep(sec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
