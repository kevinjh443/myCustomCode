package com.hogee.bootprof;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <p><b>The total boot time</b> = BOOT_Animation:END + preloader</p>
 * @param     1046: preloader
 * @param     105584.713236 : BOOT_Animation:END
 *
 * <br></br>
 * <br><b>Kernel init time</b> = timestamp of Kernel_init_done</br>
 * @param     15876.315692 : Kernel_init_done
 *
 * <br><b>Mount Partition time</b> = the time interval between “INIT: on init start”and "INIT:Mount_END"</br>
 * @param     21947.338847 : INIT: on init start
 * @param     23242.357078 : INIT:PROTECT:Mount_END
 * <br></br>
 *
 * <br><b>Android Boot:</b></br>
 * <br>  <b>pre-load classed time</b> = timestamp of [zygote:Preload End] – [Zygote:Preload Start]</br>
 * @param     28017.176616 : Zygote:Preload Start
 * @param     32254.015616 : Zygote:Preload End
 * <br>  <b>package scanning time</b> = [PMS_READY] – [PackageManagerService_Start]</br>
 * @param     66568.221080 : Android:PMS_READY</br>
 * @param     33563.512001 : Android:PackageManagerService_Start
 * <br>  <b>odex analysis time = the first of [AP_Init:] - [PMS_READY]</br>
 * @param     66568.221080 : Android:PMS_READY</br>
 * @param     43473.957925 : AP_Init:[broadcast]:[com.android.providers.calendar]...
 * <br>  <b>App initializations</b> = [BOOT_Animation:END] – [AP_Init:]
 * <br></br>
 *
 * <br>  How many applications are launched during boot UP:
 *       - AP_Init:[service|added|broadcast|content provider]:[AP]:pid:[pid]</br>
 * @param  43473.957925 : AP_Init:[broadcast]:[com.android.providers.calendar]:[com.android.providers.calendar/
 *            .CalendarUpgradeReceiver]:pid:983
 */
public class BootAnalyzer {
    protected final String TAG = "BootAnalyzer";

    public static final String TOTAL_BOOT_TIME = "totalBootTime";
    public static final String KERNEL_INIT_TIME = "kernelInitTime";
    public static final String MOUNT_PARTI_TIME = "mountPartitionTime";
    public static final String PRELOAD_CLASS_TIME = "preloadClassTime";
    public static final String PKG_SCAN_TIME = "pkgScanTime";
    public static final String ODEX_PARSER_TIME = "odex";
    public static final String APP_INIT_TIME = "appInitTime";
    public static final String ANDROID_TIME = "androidBootTime";

    private static final String TIME_PATTERN = "(\\d+)\\.\\d+ :";// pixi : "(\\d+)\\.\\d+ :"  \\s+\\S+\\s+:
    private static final String PRELOAD_PATTERN = "(\\d+)\\s+: preloader";
    private static final String BOOT_ANIMATION_END_PATTERN = TIME_PATTERN + " BOOT_Animation:END";
    private static final String KERNEL_INIT_PATTERN = TIME_PATTERN + " Kernel_init_done";

    private static final String MOUNT_PARTITION_START_PATTERN = TIME_PATTERN + " INIT: on init start";
    private static final String MOUNT_PARTITION_END_PATTERN = TIME_PATTERN + " INIT:Mount_END";

    private static final String PRELOAD_CLASS_START_PATTERN = TIME_PATTERN + " Zygote:Preload Start";
    private static final String PRELOAD_CLASS_END_PATTERN = TIME_PATTERN + " Zygote:Preload End";

    private static final String PACKAGE_SCANNING_START_PATTERN = TIME_PATTERN + " Android:PackageManagerService_Start";
    private static final String PACKAGE_SCANNING_READY_PATTERN = TIME_PATTERN + " Android:PMS_READY";

    private static final String APP_INIT_PATTERN = TIME_PATTERN + " AP_Init:\\[";

    private final Pattern mPreloadPattern;
    private final Pattern mBootAnimationEndPattern;
    private final Pattern mKernelInitPattern;
    private final Pattern mMountPartitionStartPattern;
    private final Pattern mMountPartitionEndPattern;
    private final Pattern mPreloadClassStartPattern;
    private final Pattern mPreloadClassEndPattern;
    private final Pattern mPackageScanningStartPattern;
    private final Pattern mPackageScanningEndPattern;
    private final Pattern mAppInitPattern;

    private final Map<String, Integer> mBootTimeChart = new HashMap<String, Integer>();

    public BootAnalyzer() {
        mPreloadPattern = Pattern.compile(PRELOAD_PATTERN);
        mBootAnimationEndPattern = Pattern.compile(BOOT_ANIMATION_END_PATTERN);
        mKernelInitPattern = Pattern.compile(KERNEL_INIT_PATTERN);
        mMountPartitionStartPattern = Pattern.compile(MOUNT_PARTITION_START_PATTERN);
        mMountPartitionEndPattern = Pattern.compile(MOUNT_PARTITION_END_PATTERN);
        mPreloadClassStartPattern = Pattern.compile(PRELOAD_CLASS_START_PATTERN);
        mPreloadClassEndPattern = Pattern.compile(PRELOAD_CLASS_END_PATTERN);
        mPackageScanningStartPattern = Pattern.compile(PACKAGE_SCANNING_START_PATTERN);
        mPackageScanningEndPattern = Pattern.compile(PACKAGE_SCANNING_READY_PATTERN);
        mAppInitPattern = Pattern.compile(APP_INIT_PATTERN);
    }
    
    static class Logger {
        public static void v(String tag, String msg) {
            System.out.println(tag+" V : "+msg);
        }
        
        public static void e(String tag, String msg) {
            System.out.println(tag+" E : "+msg);
        }
    }
    
    static class Log {
        public static void e(String tag, String msg) {
            System.out.println(tag+" E : "+msg);
        }
    }

    public Map<String, Integer> matches(String file) {
        if (!mBootTimeChart.isEmpty()) {
            Logger.v(TAG, "mBootTimeChart is not empty, so reuse it.");
            return mBootTimeChart;
        }

        Map<String, Integer> timeStampMap = scanFile(file);
        if (timeStampMap == null || timeStampMap.isEmpty()) {
            return null;
        }

        // The total boot time = BOOT_Animation:END + preloader
        if (timeStampMap.containsKey("preload") || timeStampMap.containsKey("bootAnimationEnd")) {
            int totalBootTime = 0;
            if (timeStampMap.containsKey("preload")) {
                totalBootTime = timeStampMap.get("preload") + timeStampMap.get("bootAnimationEnd");
            } else {
                totalBootTime = timeStampMap.get("bootAnimationEnd");
            }
            mBootTimeChart.put(TOTAL_BOOT_TIME, totalBootTime);
        }

        // Kernel init time = timestamp of Kernel_init_done
        if (timeStampMap.containsKey("kernelInit"))
            mBootTimeChart.put(KERNEL_INIT_TIME, timeStampMap.get("kernelInit"));

        // Mount Partition time = [INIT:Mount_END] - [INIT: on init start]
        if (timeStampMap.containsKey("mountPartitionStart")
                && timeStampMap.containsKey("mountPartitionEnd")) {
            int mountTime = timeStampMap.get("mountPartitionEnd") - timeStampMap.get("mountPartitionStart");
            mBootTimeChart.put(MOUNT_PARTI_TIME, mountTime);
        }

        // Android Boot:
        int preloadTime = 0, scanTime = 0, appTime = 0, odexTime = 0;

        // pre-load classed time = timestamp of [zygote:Preload End] – [Zygote:Preload Start]
        if (timeStampMap.containsKey("preloadClassStart")
                && timeStampMap.containsKey("preloadClassEnd")) {
            preloadTime = timeStampMap.get("preloadClassEnd") - timeStampMap.get("preloadClassStart");
            mBootTimeChart.put(PRELOAD_CLASS_TIME, preloadTime);
        }

        // package scanning time = [PMS_READY] – [PackageManagerService_Start]
        if (timeStampMap.containsKey("packageScanningStart")
                && timeStampMap.containsKey("packageScanningEnd")) {
            scanTime = timeStampMap.get("packageScanningEnd") - timeStampMap.get("packageScanningStart");
            mBootTimeChart.put(PKG_SCAN_TIME, scanTime);
        }

        // odex analysis time = the first of [AP_Init:] - [PMS_READY]
        if (timeStampMap.containsKey("packageScanningEnd")
                && timeStampMap.containsKey("appInit")) {
            odexTime = timeStampMap.get("appInit") - timeStampMap.get("packageScanningEnd");
            mBootTimeChart.put(ODEX_PARSER_TIME, odexTime);
        }

        // App initializations = [BOOT_Animation:END] – the first of [AP_Init:]
        if (timeStampMap.containsKey("appInit")
                && timeStampMap.containsKey("bootAnimationEnd")) {
            appTime = timeStampMap.get("bootAnimationEnd") - timeStampMap.get("appInit");
            mBootTimeChart.put(APP_INIT_TIME, appTime);
        }

        // Android Boot Time:
        if (preloadTime != 0 || scanTime != 0 || appTime != 0)
            mBootTimeChart.put(ANDROID_TIME, preloadTime + scanTime + appTime);

        Logger.v(TAG, "The boot time chart is: " + mBootTimeChart);
        return mBootTimeChart;
    }

    public Map<String, Integer> getBootTimeChart() {
        return mBootTimeChart;
    }

    private Map<String, Integer> scanFile(String name) {
        if (!mBootTimeChart.isEmpty()) {
            return mBootTimeChart;
        }

        BufferedReader br = null;
        Map<String, Integer> matchingMap = new HashMap<String, Integer>();
        try {
            String currentLine;
            boolean getAppInit = false;
            br = new BufferedReader(new FileReader(name));
            while ((currentLine = br.readLine()) != null) {
                Matcher preload = mPreloadPattern.matcher(currentLine);
                Matcher bootAnimationEnd = mBootAnimationEndPattern.matcher(currentLine);
                Matcher kernelInit = mKernelInitPattern.matcher(currentLine);
                Matcher mountPartitionStart = mMountPartitionStartPattern.matcher(currentLine);
                Matcher mountPartitionEnd = mMountPartitionEndPattern.matcher(currentLine);
                Matcher preloadClassStart = mPreloadClassStartPattern.matcher(currentLine);
                Matcher preloadClassEnd = mPreloadClassEndPattern.matcher(currentLine);
                Matcher packageScanningStart = mPackageScanningStartPattern.matcher(currentLine);
                Matcher packageScanningEnd = mPackageScanningEndPattern.matcher(currentLine);
                Matcher appInit = mAppInitPattern.matcher(currentLine);

                if (preload.find()) {
                    Logger.v(TAG, "preload: " + preload.group(1));
                    matchingMap.put("preload", Integer.valueOf(preload.group(1)));
                }
                else if (bootAnimationEnd.find()) {
                    Logger.v(TAG, "bootAnimationEnd: " + bootAnimationEnd.group());
                    matchingMap.put("bootAnimationEnd", Integer.valueOf(bootAnimationEnd.group(1)));
                }
                else if (kernelInit.find()) {
                    Logger.v(TAG, "kernelInit: " + kernelInit.group(1));
                    matchingMap.put("kernelInit", Integer.valueOf(kernelInit.group(1)));
                }
                else if (mountPartitionStart.find()) {
                    Logger.v(TAG, "mountPartitionStart: " + mountPartitionStart.group(1));
                    matchingMap.put("mountPartitionStart", Integer.valueOf(mountPartitionStart.group(1)));
                }
                else if (mountPartitionEnd.find()) {
                    Logger.v(TAG, "mountPartitionEnd: " + mountPartitionEnd.group(1));
                    matchingMap.put("mountPartitionEnd", Integer.valueOf(mountPartitionEnd.group(1)));
                }
                else if (preloadClassStart.find()) {
                    Logger.v(TAG, "preloadClassStart: " + preloadClassStart.group(1));
                    matchingMap.put("preloadClassStart", Integer.valueOf(preloadClassStart.group(1)));
                }
                else if (preloadClassEnd.find()) {
                    Logger.v(TAG, "preloadClassEnd: " + preloadClassEnd.group(1));
                    matchingMap.put("preloadClassEnd", Integer.valueOf(preloadClassEnd.group(1)));
                }
                else if (packageScanningStart.find()) {
                    Logger.v(TAG, "packageScanningStart: " + packageScanningStart.group(1));
                    matchingMap.put("packageScanningStart", Integer.valueOf(packageScanningStart.group(1)));
                }
                else if (packageScanningEnd.find()) {
                    Logger.v(TAG, "packageScanningEnd: " + packageScanningEnd.group(1));
                    matchingMap.put("packageScanningEnd", Integer.valueOf(packageScanningEnd.group(1)));
                }
                else if (!getAppInit && appInit.find()) {
                    Logger.v(TAG, "appInit: " + appInit.group(1));
                    getAppInit = true;
                    matchingMap.put("appInit", Integer.valueOf(appInit.group(1)));
                }
            }
        } catch (IOException ex) {
            Log.e(TAG, "read " + name + " file fail!");
            ex.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                Log.e(TAG, "close BufferedReader fail!");
                ex.printStackTrace();
            }
        }
        return matchingMap;
    }
}
