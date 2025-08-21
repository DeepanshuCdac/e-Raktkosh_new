package in.cdac.eraktkosh.utility;

public class HisUtil {

	public static String getParameterFromHisPathXML(String _Param) {
		String osType = null;
		String paramValue = null;
		try {
			osType = System.getProperties().getProperty("os.name");
			if (osType.startsWith("Win")) {
				_Param += "_WIN";
				paramValue = getParameterFromXML("c:/BBNW1/eRaktKosh/hisPath.xml", _Param);// c:/Telangana/eRaktKosh/hisPath.xml
			} else {
				_Param += "_LIN";
				paramValue = getParameterFromXML("/opt/BBNW1/eRaktKosh/hisPath.xml", _Param);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paramValue;
	}

	public static String getParameterFromXML(String _XMLpath, String _Param) {
		String strResult = "";
		try {
			SaxHandler sax = new SaxHandler();
			strResult = sax.getParameter(_XMLpath, _Param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;
	}

}
