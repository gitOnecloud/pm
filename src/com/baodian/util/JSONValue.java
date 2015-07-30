package com.baodian.util;

public class JSONValue {
	/**
	 * Escape quotes, \, /, \r, \n, \b, \f, \t and other control characters (U+0000 through U+001F).
	 * @param s
	 * @return
	 */
	public static String escape(String s){
		if(s==null)
			return null;
        StringBuffer sb = new StringBuffer();
        escape(s, sb);
        return sb.toString();
    }
	/**
     * 在原基础上，将 " 回车换行 < > 也全部转换
     */
	public static String escapeHTML(String s){
		if(s==null)
			return null;
        StringBuffer sb = new StringBuffer();
        escapeHTML(s, sb);
        return sb.toString();
    }
	/**
     * @param s - Must not be null.
     * @param sb
     */
    static void escape(String s, StringBuffer sb) {
		for(int i=0;i<s.length();i++){
			char ch=s.charAt(i);
			switch(ch){
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b'://退格BS
				sb.append("\\b");
				break;
			case '\f'://换页FF
				sb.append("\\f");
				break;
			case '\n'://换行LF
				sb.append("\\n");
				break;
			case '\r'://回车CR
				sb.append("\\r");
				break;
			case '\t'://水平制表HT
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
                //Reference: http://www.unicode.org/versions/Unicode5.1.0/
				if((ch>='\u0000' && ch<='\u001F') || (ch>='\u007F' && ch<='\u009F') || (ch>='\u2000' && ch<='\u20FF')){
					String ss=Integer.toHexString(ch);
					sb.append("\\u");
					for(int k=0;k<4-ss.length();k++){
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				}
				else{
					sb.append(ch);
				}
			}
		}//for
	}
    static void escapeHTML(String s, StringBuffer sb) {
    	for(int i=0;i<s.length();i++){
    		char ch=s.charAt(i);
    		switch(ch){
    		case '"':
    			sb.append("&quot;");
    			break;
    		case '\\':
    			sb.append("\\\\");
    			break;
    		case '\b': sb.append(' ');break;
    		case '\f': sb.append(' ');break;
    		case '\n': sb.append(' ');break;
    		case '\r': sb.append(' ');break;
    		case '\t': sb.append(' ');break;
    		case '/':
    			sb.append("\\/");
    			break;
    		case '<':
    			sb.append("&lt;");
    			break;
    		case '>':
    			sb.append("&gt;");
    			break;
    		default:
    			//Reference: http://www.unicode.org/versions/Unicode5.1.0/
    			if((ch>='\u0000' && ch<='\u001F') || (ch>='\u007F' && ch<='\u009F') || (ch>='\u2000' && ch<='\u20FF')){
    				String ss=Integer.toHexString(ch);
    				sb.append("\\u");
    				for(int k=0;k<4-ss.length();k++){
    					sb.append('0');
    				}
    				sb.append(ss.toUpperCase());
    			}
    			else{
    				sb.append(ch);
    			}
    		}
    	}//for
    }
}
