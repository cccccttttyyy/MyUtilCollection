package expressionAnalysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Stack;

/**
 * 
 * 逻辑表达式计算工具类
 * 100w次需要7.7s 10w次1.7s
 * 
 *
 */
public class LogicalExpression {
    private Logger log = LoggerFactory.getLogger(LogicalExpression.class);
	private static final String AND = "&&";
	private static final String OR = "||";
	private static final String UNEQUAL = "!=";
	private static final String EQUAL = "==";
	private static final String BIG = ">";
	private static final String BIGM = ">=";
	private static final String LIT = "<";
	private static final String LITM = "<=";

	public boolean isEnable(String enable) {
	    //log.info("isenable处理"+enable);
		if (StringUtils.isEmpty(enable)) {
			return true;
		}
		Stack<String> operation = new Stack<String>();
		int tmpEnd = enable.length();
		for (int i = enable.length(); i > 1; i--) {
			String twoChatter = enable.substring(i - 2, i);
			if (twoChatter.equals(AND) || twoChatter.equals(OR)) {
				operation.add(enable.substring(i, tmpEnd).trim().replaceAll(" ", ""));
				operation.add(twoChatter);
				tmpEnd = i - 2;
			}
		}
		operation.add(enable.substring(0, tmpEnd).replaceAll(" ", ""));
		return judge(operation);
	}

	private boolean judge(Stack<String> operation) {
		if (operation.size() == 1) {
			return isTrue(operation.pop());
		} else {
			boolean value1 = isTrue(operation.pop());
			String releation = operation.pop();
			boolean value2 = isTrue(operation.pop());
			if (releation.equals(AND)) {
				boolean operationResult = value1 && value2;
				operation.add(String.valueOf(operationResult));
				return judge(operation);
			} else if (releation.equals(OR)) {
				boolean operationResult = value1 || value2;
				operation.add(String.valueOf(operationResult));
				return judge(operation);
			} else {
				// LOG.error("the logical is wrong")
				return true;
			}
		}

	}

	private boolean isTrue(String condition) {
		if (condition.toLowerCase().equals("true")) {
			return true;
		} else if (condition.toLowerCase().equals("false")) {
			return false;
		} else {
			if (condition.contains(EQUAL)) {
				int index = condition.indexOf(EQUAL);
				String stringOfValue1 = condition.substring(0, index);
				String stringOfValue2 = condition.substring(index + 2, condition.length());

				if (stringOfValue1 == null || stringOfValue2 == null) {
					// LOG.error();
					return false;
				}
				return stringOfValue2.equals(stringOfValue1);
			} else if (condition.contains(UNEQUAL)) {
				int index = condition.indexOf(UNEQUAL);
				String stringOfValue1 = condition.substring(0, index);
				String stringOfValue2 = condition.substring(index + 2, condition.length());

				if (stringOfValue1 == null || stringOfValue2 == null) {
					// LOG.error();
					return false;
				}
				return stringOfValue2.equals(stringOfValue1);
			}  else if (condition.contains(BIGM)) {
				int index = condition.indexOf(BIGM);
				String stringOfValue1 = condition.substring(0, index);
				String stringOfValue2 = condition.substring(index + 2, condition.length());

				if (stringOfValue1 == null || stringOfValue2 == null) {
					// LOG.error();
					return false;
				}
				return (Double.parseDouble(stringOfValue1)>=(Double.parseDouble(stringOfValue2)));
			}else if (condition.contains(LITM)) {
				int index = condition.indexOf(LITM);
				String stringOfValue1 = condition.substring(0, index);
				String stringOfValue2 = condition.substring(index + 2, condition.length());

				if (stringOfValue1 == null || stringOfValue2 == null) {
					// LOG.error();
					return false;
				}
				return (Double.parseDouble(stringOfValue1)<=(Double.parseDouble(stringOfValue2)));
			}else if (condition.contains(BIG)) {
				int index = condition.indexOf(BIG);
				String stringOfValue1 = condition.substring(0, index);
				String stringOfValue2 = condition.substring(index + 1, condition.length());

				if (stringOfValue1 == null || stringOfValue2 == null) {
					// LOG.error();
					return false;
				}
				return (Double.parseDouble(stringOfValue1)>(Double.parseDouble(stringOfValue2)));
			}else if (condition.contains(LIT)) {
				int index = condition.indexOf(LIT);
				String stringOfValue1 = condition.substring(0, index);
				String stringOfValue2 = condition.substring(index + 1, condition.length());

				if (stringOfValue1 == null || stringOfValue2 == null) {
					return false;
				}
				return (Double.parseDouble(stringOfValue1)<(Double.parseDouble(stringOfValue2)));
			}else {
			    log.debug("基本表达式istrue()出错:"+condition);
				return false;
			}
		}

	}

	public boolean caculate(String judeStr) {
	    //这里处理存在且后面不跟（的！
	    judeStr = completeTrim(judeStr);
	   if(isExistNon(judeStr)) {
	       while(isExistNon(judeStr)) {//目的 去掉所有单！
	           int firstIndex = charNon(judeStr,0);
	           StringBuffer waitToCal = new StringBuffer();
	           int templeng = 0;
	           for(int i = firstIndex+1;i<judeStr.length();i++) {
	               if(judeStr.charAt(i)!='|'&&judeStr.charAt(i)!='&'&&judeStr.charAt(i)!=')') {
	                   waitToCal.append(judeStr.charAt(i));
	                   templeng++;
	               }else {
	                   break;
	               }
	           }
	           boolean nonChildJudge = !isEnable(waitToCal.toString());
	           judeStr = judeStr.substring(0, firstIndex)+nonChildJudge+judeStr.substring(firstIndex+templeng+1,judeStr.length());
	       }
	   }
	   //log.info("单!处理后的字符串为："+judeStr);
	    return bracketMatch(judeStr);
	}
	
	//查找仅！的位置
	private int charNon(String judeStr,int startIndex) {
	    if(startIndex>=judeStr.length()) {
	        return -1;
	    }
	    for (int i =startIndex;i<judeStr.length()-1;i++) {
            if(judeStr.charAt(i)=='!'&&judeStr.charAt(i+1)!='(') {
                return i;
            }
        }
	    return -1;
	}
	//判断是否存在仅！的情况
	private boolean isExistNon(String judeStr) {
	    for(int i =0;i<judeStr.length()-1;i++)
	    {
	        if(judeStr.charAt(i)=='!'&&judeStr.charAt(i+1)!='(') {
	            return true;
	        }
	    }
	    return false;
	}
	//括号匹配
	private boolean bracketMatch(String judeStr) {
	    //log.info("括号匹配要处理的字符串为:"+judeStr);
	    if(judeStr.contains("(")&&judeStr.contains(")")) {
	      int start = judeStr.indexOf("(");
	      int end = getRightbracket(judeStr, start+1);
	      boolean child = bracketMatch(judeStr.substring(start+1,end));
	      if(start>0) {
	          if(judeStr.charAt(start-1)=='!') {
	              child = !child;
	              return  bracketMatch(judeStr.substring(0,start-1)+child+judeStr.substring(end+1,judeStr.length()));
	          }
	      }
	      return bracketMatch(judeStr.substring(0,start)+child+judeStr.substring(end+1,judeStr.length()));
	    }else {
	        return isEnable(judeStr);
	    }
	}

	private int getRightbracket(String judeStr,int index) {
	    if(index>=judeStr.length()) {
	        return -1;
	    }
	    int flag =1;
	    //遇到（ +1  遇到）-1
	    for(int i=index;i<judeStr.length();i++) {
	        if(judeStr.charAt(i)=='(') {
	            flag++;
	            if(flag==0) {
                    return i;
                }else if(flag<0) {
                    return -1;
                }
	        }else if(judeStr.charAt(i)==')') {
	            flag--;
	            if(flag==0) {
	                return i;
	            }else if(flag<0) {
	                return -1;
	            }
	        }
	    }
	    return -1;
	}
	private String completeTrim(String strOldString) {
        int length = strOldString.length();
        char chrString[] = new char[length];
        int iStartPos = 0;
        for (int i = 0; i < length; i++) {
            if (strOldString.charAt(i) != ' ') {
                chrString[iStartPos++] = strOldString.charAt(i);
            }
        }

        String strNewString = new String(chrString);
        strNewString = strNewString.substring(0, iStartPos);
        return strNewString;
    }
	
	public static void main(String[] args) {
	    long time = System.currentTimeMillis();
    	LogicalExpression get = new LogicalExpression();
    	for(int i=0;i<1000000;i++) {
    	    boolean a = get.caculate("!((!1.3247823647863<2.3246732657832)&&(!3.23468321231>4.2142141241314))||!(1.375823658223<1375823658223)");
//    	    System.out.print(a);
    	}
//    	boolean a = get.isEnable("1<0||575.0<=1518.8000000000002&&575.0>2175.2");
//    	boolean b = get.caculate("1<0||575.0<=1518.8000000000002&&575.0>2175.2");
//    	boolean a = get.caculate("!((!1<2)&&(!3>4))||!(2<1)");
//    	System.out.println(get.isExistNon("!1>0||(575.0>=1518.8000000000002&&575.0<2175.2)"));
//    	System.out.println(a+","+b);
//    	System.out.println(get.chatNon("s!das!afdasf!d",0));
//    	System.out.println(get.chatNon("s!das!afdasfd",10));
//    	System.out.println(get.getRightbracket("1<9&&((8>7))", 6));
    	System.out.println("共花费"+(System.currentTimeMillis()-time));
	}
	
}
