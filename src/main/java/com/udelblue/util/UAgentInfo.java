package com.udelblue.util;

/* *******************************************
//LICENSE INFORMATION
//The code, "Detecting Smartphones Using PHP" 
//by Anthony Hand, is licensed under a Creative Commons 
//Attribution 3.0 United States License.
//
//Updated 01 March 2010 by Bryan J Swift
//- Remove un-needed if statements instead just returning the boolean
//  inside the if clause
//
//Updated 14 December 2009 by A Hand
//- Added the method for detecting BlackBerry Touch
//		devices like the Storm 1 and 2.
//
//Updated 5 December 2009 by A Hand
//- Fixed the DetectPalmOS method. It should filter
//	   out WebOS devices.
//
//Updated 8 November 2009 by A Hand
//- Added the deviceWebOS variable.
//- Added Palm's WebOS to the DetectPalmOS method.
//- Created a new method to check for Palm's WebOS devices.
//- Added Palm's WebOS to the DetectTierIphone method.
//
//Updated 4 April 2009 by A Hand
//- Changed the name of the class from DetectSmartPhone to UAgentInfo.
//	   New name is more consistent with PHP and JavaScript classes.
//- Added a method to detect Opera Mobile and Mini. Opera Mobile is new.
//- Updated the algorithm for detecting a Sony Mylo.
//	 - Added Android to the DetectTierIphone method.
//- Updated comments for Detect Tier methods. 
//
//Updated 22 January 2009
//- Ported to Java from the PHP code by 
//  Satish Kumar Nookala, javasatish@yahoo.com
//
//Anthony Hand, ahand@hand-interactive.com
//Web: www.hand-interactive.com
//
//License info: http://creativecommons.org/licenses/by/3.0/us/
//
//This code is provided AS IS with no expressed or implied warranty.  
//You have the right to use this code or any portion of it 
//so long as you provide credit toward myself as the original author.
//
//*******************************************
*/

import javax.servlet.http.HttpServletRequest;

/**
 * The DetectSmartPhone class encapsulates information about a browser's
 * connection to your web site. You can use it to find out whether the browser
 * asking for your site's content is probably running on a mobile device. The
 * methods were written so you can be as granular as you want. For example,
 * enquiring whether it's as specific as an iPod Touch or as general as a
 * smartphone class device. The object's methods return true, or false.
 */
public class UAgentInfo {
	public static final String chrome = "chrome";
	public static final String deviceAndroid = "android";

	public static final String deviceArchos = "archos";
	public static final String deviceBB = "blackberry";
	// emulate IE or Firefox
	public static final String deviceBBStorm = "blackberry95"; // Storm 1 and 2
	// Initialize variables for other random devices and mobile browsers.
	public static final String deviceBrew = "brew";
	public static final String deviceDanger = "danger";
	public static final String deviceHiptop = "hiptop";
	public static final String deviceIeMob = "iemobile";
	public static final String deviceIphone = "iphone";
	public static final String deviceIpod = "ipod";
	// OpenWave server
	public static final String deviceMidp = "midp"; // a mobile Java technology
	public static final String deviceNintendo = "nintendo";
	public static final String deviceNintendoDs = "nitro";
	public static final String devicePalm = "palm";
	public static final String devicePda = "pda"; // some devices report
	public static final String devicePlaystation = "playstation";
	public static final String deviceS60 = "series60";
	public static final String deviceS70 = "series70";
	public static final String deviceS80 = "series80";

	public static final String deviceS90 = "series90";
	public static final String deviceSymbian = "symbian";

	public static final String deviceWebOS = "webos"; // For Palm's new WebOS
														// devices
	public static final String deviceWii = "wii";

	public static final String deviceWindows = "windows";
	public static final String deviceWinMob = "windows ce";
	public static final String deviceXbox = "xbox";
	public static final String engineBlazer = "blazer"; // Old Palm
	public static final String engineNetfront = "netfront"; // Common embedded
	// phones
	public static final String engineOpenWeb = "openweb"; // Transcoding by
	public static final String engineOpera = "opera"; // Popular browser
	public static final String enginePie = "wm5 pie"; // An old Windows Mobile
	// OS browser
	public static final String engineUpBrowser = "up.browser"; // common on some

	// Initialize some initial smartphone string variables.
	public static final String engineWebKit = "webkit";
	public static final String engineXiino = "xiino"; // Another old Palm
	public static final String firefox = "firefox";
	public static final String linux = "linux";
	// Use Maemo, Tablet, and Linux to test for Nokia"s Internet Tablets.
	public static final String maemo = "maemo";
	public static final String maemoTablet = "tablet";

	public static final String manuericsson = "ericsson";
	public static final String manuSamsung1 = "sec-sgh";
	public static final String manuSony = "sony";
	// In some UserAgents, the only clue is the manufacturer.
	public static final String manuSonyEricsson = "sonyericsson";

	// themselves as PDAs
	public static final String mini = "mini"; // Some mobile browsers put "mini"
	// "mobile" in their user
	// agent strings.
	public static final String mobi = "mobi"; // Some mobile browsers put "mobi"
												// in their user agent strings.
	// in their names.
	public static final String mobile = "mobile"; // Some mobile browsers put
	// Standard desktop browser detection strings
	public static final String msie = "msie";
	public static final String msie60 = "msie 6.0";

	public static final String msie61 = "msie 6.1";
	public static final String msie7 = "msie 7.0";
	public static final String msie8 = "msie 8.0";
	public static final String msie9 = "msie 9.0";

	public static final String mylocom2 = "com2"; // for Sony Mylo also
	public static final String opera = "presto";
	public static final String qtembedded = "qt embedded"; // for Sony Mylo

	public static final String safari = "apple";
	// In some UserAgents, the only clue is the operator.
	public static final String svcDocomo = "docomo";
	public static final String svcKddi = "kddi";
	public static final String svcVodafone = "vodafone";
	public static final String uplink = "up.link";
	public static final String vndRIM = "vnd.rim"; // Detectable when BB devices
	// Initialize variables for mobile-specific content.
	public static final String vndwap = "vnd.wap";
	// OS Detection
	public static final String windows = "windows";
	public static final String wml = "wml";
	private String httpAccept = "";

	// User-Agent and Accept HTTP request headers
	private String userAgent = "";

	/**
	 * Initialize the userAgent and httpAccept variables by getting the headers
	 * from the HttpServletRequest
	 *
	 * @param request
	 *            the HttpServletRequest to get the header information from
	 */
	public UAgentInfo(HttpServletRequest request) {
		this(request.getHeader("User-Agent"), request.getHeader("Accept"));
	}

	/**
	 * Initialize the userAgent and httpAccept variables
	 *
	 * @param userAgent
	 *            the User-Agent header
	 * @param httpAccept
	 *            the Accept header
	 */
	public UAgentInfo(String userAgent, String httpAccept) {
		if (userAgent != null) {
			this.userAgent = userAgent.toLowerCase();
		}
		if (httpAccept != null) {
			this.httpAccept = httpAccept.toLowerCase();
		}

	}

	/**
	 * Detects if the current device is an Android OS-based device.
	 */
	public boolean detectAndroid() {
		return userAgent.indexOf(deviceAndroid) != -1;
	}

	/**
	 * Detects if the current device is an Android OS-based device and the
	 * browser is based on WebKit.
	 */
	public boolean detectAndroidWebKit() {
		return detectAndroid() && detectWebkit();
	}

	/**
	 * Detects if the current device is an Archos media player/Internet tablet.
	 */
	public boolean detectArchos() {
		return userAgent.indexOf(deviceArchos) != -1;
	}

	/**
	 * Detects if the current browser is a BlackBerry of some sort.
	 */
	public boolean detectBlackBerry() {
		return userAgent.indexOf(deviceBB) != -1 || httpAccept.indexOf(vndRIM) != -1;
	}

	/**
	 * Detects if the current browser is a BlackBerry Touch device, such as the
	 * Storm
	 */
	public boolean detectBlackBerryTouch() {
		return userAgent.indexOf(deviceBBStorm) != -1;
	}

	/**
	 * Detects whether the device is a Brew-powered device.
	 */
	public boolean detectBrewDevice() {
		return userAgent.indexOf(deviceBrew) != -1;
	}

	public boolean detectChrome() {
		return userAgent.indexOf(chrome) != -1;
	}

	/**
	 * Detects the Danger Hiptop device.
	 */
	public boolean detectDangerHiptop() {
		return userAgent.indexOf(deviceDanger) != -1 || userAgent.indexOf(deviceHiptop) != -1;
	}

	public boolean detectFirefox() {
		return userAgent.indexOf(firefox) != -1;
	}

	/**
	 * Detects if the current device is an Internet-capable game console.
	 */
	public boolean detectGameConsole() {
		return detectSonyPlaystation() || detectNintendo() || detectXbox();
	}

	/**
	 * Detects if the current device is an iPhone.
	 */
	public boolean detectIphone() {
		// The iPod touch says it's an iPhone! So let's disambiguate.
		return userAgent.indexOf(deviceIphone) != -1 && !detectIpod();
	}

	/**
	 * Detects if the current device is an iPhone or iPod Touch.
	 */
	public boolean detectIphoneOrIpod() {
		// We repeat the searches here because some iPods may report themselves
		// as an iPhone, which would be okay.
		return userAgent.indexOf(deviceIphone) != -1 || userAgent.indexOf(deviceIpod) != -1;
	}

	/**
	 * Detects if the current device is an iPod Touch.
	 */
	public boolean detectIpod() {
		return userAgent.indexOf(deviceIpod) != -1;
	}

	/**
	 * Detects if the current device is on one of the Maemo-based Nokia Internet
	 * Tablets.
	 */
	public boolean detectMaemoTablet() {
		return (userAgent.indexOf(maemo) != -1
				|| (userAgent.indexOf(maemoTablet) != -1 && userAgent.indexOf(linux) != -1));
	}

	/**
	 * Detects if the current device supports MIDP, a mobile Java technology.
	 */
	public boolean detectMidpCapable() {
		return userAgent.indexOf(deviceMidp) != -1 || httpAccept.indexOf(deviceMidp) != -1;
	}

	/**
	 * The longer and more thorough way to detect for a mobile device. Will
	 * probably detect most feature phones, smartphone-class devices, Internet
	 * Tablets, Internet-enabled game consoles, etc. This ought to catch a lot
	 * of the more obscure and older devices, also -- but no promises on
	 * thoroughness!
	 */
	public boolean detectMobileLong() {
		return detectMobileQuick() || detectMaemoTablet() || detectGameConsole();
	}

	/**
	 * The quick way to detect for a mobile device. Will probably detect most
	 * recent/current mid-tier Feature Phones as well as smartphone-class
	 * devices.
	 */
	public boolean detectMobileQuick() {
		// Ordered roughly by market share, WAP/XML > Brew > Smartphone.
		if (detectWapWml()) {
			return true;
		}
		if (detectBrewDevice()) {
			return true;
		}

		// Updated by AHand
		if (detectOperaMobile()) {
			return true;
		}

		if (userAgent.indexOf(engineUpBrowser) != -1) {
			return true;
		}
		if (userAgent.indexOf(engineOpenWeb) != -1) {
			return true;
		}
		if (userAgent.indexOf(deviceMidp) != -1) {
			return true;
		}

		if (detectSmartphone()) {
			return true;
		}
		if (detectDangerHiptop()) {
			return true;
		}

		if (detectMidpCapable()) {
			return true;
		}

		if (userAgent.indexOf(devicePda) != -1) {
			return true;
		}
		if (userAgent.indexOf(mobile) != -1) {
			return true;
		}

		// detect older phones from certain manufacturers and operators.
		if (userAgent.indexOf(uplink) != -1) {
			return true;
		}
		if (userAgent.indexOf(manuSonyEricsson) != -1) {
			return true;
		}
		if (userAgent.indexOf(manuericsson) != -1) {
			return true;
		}
		if (userAgent.indexOf(manuSamsung1) != -1) {
			return true;
		}
		if (userAgent.indexOf(svcDocomo) != -1) {
			return true;
		}
		if (userAgent.indexOf(svcKddi) != -1) {
			return true;
		}
		if (userAgent.indexOf(svcVodafone) != -1) {
			return true;
		}

		return false;
	}

	// *****************************
	// For Desktop Browsers
	// *****************************
	public boolean detectMSIE() {
		return userAgent.indexOf(msie) != -1;
	}

	public boolean detectMSIE6() {
		return userAgent.indexOf(msie60) != -1 && userAgent.indexOf(msie61) != -1;
	}

	public boolean detectMSIE7() {
		return userAgent.indexOf(msie7) != -1;
	}

	public boolean detectMSIE8() {
		return userAgent.indexOf(msie8) != -1;
	}

	public boolean detectMSIE9() {
		return userAgent.indexOf(msie9) != -1;
	}

	/**
	 * Detects if the current device is a Nintendo game device.
	 */
	public boolean detectNintendo() {
		return userAgent.indexOf(deviceNintendo) != -1 || userAgent.indexOf(deviceWii) != -1
				|| userAgent.indexOf(deviceNintendoDs) != -1;
	}

	public boolean detectOpera() {
		return userAgent.indexOf(opera) != -1;
	}

	/**
	 * Detects Opera Mobile or Opera Mini. Added by AHand
	 */
	public boolean detectOperaMobile() {
		return userAgent.indexOf(engineOpera) != -1 && (userAgent.indexOf(mini) != -1 || userAgent.indexOf(mobi) != -1);
	}

	/**
	 * Detects if the current browser is on a PalmOS device.
	 */
	public boolean detectPalmOS() {
		// Most devices nowadays report as 'Palm', but some older ones reported
		// as Blazer or Xiino.
		if (userAgent.indexOf(devicePalm) != -1 || userAgent.indexOf(engineBlazer) != -1
				|| userAgent.indexOf(engineXiino) != -1 && !detectPalmWebOS()) {
			// Make sure it's not WebOS first
			if (detectPalmWebOS()) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * Detects if the current browser is on a Palm device running the new WebOS.
	 */
	public boolean detectPalmWebOS() {
		return userAgent.indexOf(deviceWebOS) != -1;
	}

	/**
	 * Detects if the current browser is the S60 Open Source Browser.
	 */
	public boolean detectS60OssBrowser() {
		// First, test for WebKit, then make sure it's either Symbian or S60.
		return detectWebkit() && (userAgent.indexOf(deviceSymbian) != -1 || userAgent.indexOf(deviceS60) != -1);
	}

	public boolean detectSafari() {
		return userAgent.indexOf(safari) != -1;
	}

	/**
	 * Check to see whether the device is any device in the 'smartphone'
	 * category.
	 */
	public boolean detectSmartphone() {
		return (detectIphoneOrIpod() || detectS60OssBrowser() || detectSymbianOS() || detectWindowsMobile()
				|| detectBlackBerry() || detectPalmOS() || detectPalmWebOS() || detectAndroid());
	}

	/**
	 * Detects if the current browser is a Sony Mylo device. Updated by AHand
	 */
	public boolean detectSonyMylo() {
		return userAgent.indexOf(manuSony) != -1
				&& (userAgent.indexOf(qtembedded) != -1 || userAgent.indexOf(mylocom2) != -1);
	}

	/**
	 * Detects if the current device is a Sony Playstation.
	 */
	public boolean detectSonyPlaystation() {
		return userAgent.indexOf(devicePlaystation) != -1;
	}

	/**
	 *
	 * Detects if the current device is any Symbian OS-based device, including
	 * older S60, Series 70, Series 80, Series 90, and UIQ, or other browsers
	 * running on these devices.
	 */
	public boolean detectSymbianOS() {
		return userAgent.indexOf(deviceSymbian) != -1 || userAgent.indexOf(deviceS60) != -1
				|| userAgent.indexOf(deviceS70) != -1 || userAgent.indexOf(deviceS80) != -1
				|| userAgent.indexOf(deviceS90) != -1;
	}

	/**
	 * The quick way to detect for a tier of devices. This method detects for
	 * devices which can display iPhone-optimized web content. Includes iPhone,
	 * iPod Touch, Android, Palm WebOS, etc.
	 */
	public boolean detectTierIphone() {
		return detectIphoneOrIpod() || detectPalmWebOS() || detectAndroid() || detectAndroidWebKit();
	}

	/**
	 * The quick way to detect for a tier of devices. This method detects for
	 * all other types of phones, but excludes the iPhone and Smartphone Tier
	 * devices.
	 */
	public boolean detectTierOtherPhones() {
		return detectMobileQuick() && (!detectTierIphone()) && (!detectTierSmartphones());
	}

	/**
	 * The quick way to detect for a tier of devices. This method detects for
	 * all smartphones, but excludes the iPhone Tier devices.
	 */
	public boolean detectTierSmartphones() {
		return detectSmartphone() && (!detectTierIphone());
	}

	/**
	 * Detects whether the device supports WAP or WML.
	 */
	public boolean detectWapWml() {
		return httpAccept.indexOf(vndwap) != -1 || httpAccept.indexOf(wml) != -1;
	}

	/**
	 * Detects if the current browser is based on WebKit.
	 */
	public boolean detectWebkit() {
		return userAgent.indexOf(engineWebKit) != -1;
	}

	public boolean detectWindows() {
		return userAgent.indexOf(windows) != -1;
	}

	/**
	 * Detects if the current browser is a Windows Mobile device.
	 */
	public boolean detectWindowsMobile() {
		// Most devices use 'Windows CE', but some report 'iemobile'
		// and some older ones report as 'PIE' for Pocket IE.
		return userAgent.indexOf(deviceWinMob) != -1 || userAgent.indexOf(deviceIeMob) != -1
				|| userAgent.indexOf(enginePie) != -1 || (detectWapWml() && userAgent.indexOf(deviceWindows) != -1);
	}

	// *****************************
	// For Mobile Web Site Design
	// *****************************

	/**
	 * Detects if the current device is a Microsoft Xbox.
	 */
	public boolean detectXbox() {
		return userAgent.indexOf(deviceXbox) != -1;
	}

	/**
	 * Return the lower case HTTP_ACCEPT
	 */
	public String getHttpAccept() {
		return httpAccept;
	}

	/**
	 * Return the lower case HTTP_USER_AGENT
	 */
	public String getUserAgent() {
		return userAgent;
	}
}
