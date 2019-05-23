package pri.crm.util;

//128bit
//8-4-4-4-12
//time-random-nanoTime-hashCode
public class UUIDUtil {
	
	static public String getUUID() {
		String UUID=new String("");
		String time=Long.toHexString(System.currentTimeMillis()).concat("000000000").substring(0, 8);
		String random=Integer.toHexString((int)(Math.random()*10000));
		String nanoTime=Long.toHexString(System.nanoTime()).concat("0000").substring(0, 4); 
		String hashCode=Integer.toHexString(Thread.currentThread().hashCode()).concat("00000000000").substring(0,12);
		UUID=time+random+nanoTime+hashCode;
		return UUID;
	};
}
