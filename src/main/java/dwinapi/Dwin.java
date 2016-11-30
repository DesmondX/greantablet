package dwinapi;

import android.app.Activity;
import android.os.Build;
import android.view.WindowManager;

import org.apache.http.util.EncodingUtils;

import java.io.DataOutputStream;
import java.io.FileInputStream;

/**
 * DWIN
 * 
 * ʹ�ñ���ǰϵͳ����ROOT
 * 
 * @author F
 * 
 */
public class Dwin {

	private static Dwin dwin;

	/**
	 * 
	 * 
	 * @return Dwin
	 */
	public static Dwin getInstance() {
		return dwin = dwin == null ? new Dwin() : dwin;
	}

	/**
	 * 
	 * 
	 * ������Ļ���ȣ�ʹ�ñ������������Թرձ��⣬�����߹���ʱ����ֻ�ñ�����
	 * 
	 * @param bright
	 *            ��0<=bright<=255��
	 * 
	 * @return �������ɹ����򷵻�true
	 */
	public boolean setBrightness(int bright) {
		try {
			String[] command = new String[] {
					"su",
					"-c",
					"echo "
							+ bright
							+ " > /sys/devices/platform/pwm-backlight.0/backlight/pwm-backlight.0/brightness" };
			exec(command);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
     * ��������
     * 
     * @param activity
     * @param brightness
     */
    public static void setBrightness(Activity activity, int brightness) {
        // Settings.System.putInt(activity.getContentResolver(),
        // Settings.System.SCREEN_BRIGHTNESS_MODE,
        // Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
        activity.getWindow().setAttributes(lp);

    }	
	
	/**
	 * ���ص���
	 * 
	 * @return �������ɹ��򷵻�ture.
	 * 
	 */
	public boolean hideNavigation() {
		try {
			Build.VERSION_CODES vc = new Build.VERSION_CODES();
			Build.VERSION vr = new Build.VERSION();
			String procId = vr.SDK_INT > vc.ICE_CREAM_SANDWICH ? "42" : "79";
			String[] command = new String[] {
					"su",
					"-c",
					"service call activity " + procId
							+ " s16 com.android.systemui" };
			exec(command);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * ��ʾ����
	 * 
	 * @return �������ɹ�������true.
	 */
	public boolean showNavigation() {

		try {
			String[] command = new String[] { "am", "startservice", "-n",
					"com.android.systemui/.SystemUIService" };
			exec(command);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * ��̬����IP��������ʹ��ǰϵͳ����ROOT�����޸���ɶϵ粻����
	 * 
	 * @param IP
	 * @return ������óɹ�������true
	 */
	public boolean setIp(String IP) {
		try {
			String[] command = new String[] { "su", "-c",
					"ifconfig eth0 " + IP + " netmask 255.255.255.0 up" };
			exec(command);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * ��ȡ��ĻΨһ��ʶChipID
	 * 
	 * @return chipid
	 */
	public String getChipID() {
		String res = null;
		String fileName = "/sys/devices/platform/jz4780-efuse/chip_id";

		try {
			FileInputStream fin = new FileInputStream(fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8").toString().trim();
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * �ػ�
	 */
	public void shutDown() {
		execCommand("input keyevent 26");
	}

	/**
	 * ����
	 */
	public void reboot() {
		execCommand("reboot");
	}

	/**
	 * ���� ִ�з��ؼ��²���
	 */
	public void back() {
		execCommand("input keyevent 4");
	}

	/**
	 * Home ִ��Home���²���
	 */
	public void home() {
		execCommand("input keyevent 3");
	}

	/**
	 * execute command
	 * 
	 * @param command
	 * @throws Exception
	 */
	private void exec(String[] command) throws Exception {
		Process proc;
		proc = Runtime.getRuntime().exec(command);
		proc.waitFor();
	}

	/**
	 * execute command, the phone must be root,it can exctue the adb command
	 * 
	 * @param command
	 */
	public void execCommand(String command) {
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");//
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
