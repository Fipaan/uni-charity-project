public class Global {
	public static boolean compareStrings(String s1, String s2) {
		int len = s1.length();
		if(len != s2.length()) return false;
		for(int i = 0; i < len; ++i) {
			if(s1.charAt(i) != s2.charAt(i)) return false;
		}
		return true;
	}
}
