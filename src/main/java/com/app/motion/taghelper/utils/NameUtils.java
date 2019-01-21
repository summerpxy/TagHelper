package com.app.motion.taghelper.utils;

public class NameUtils {
    public static final String APPLICATION_SUFFIX = "$Bind";
    public static final String ACTIVITY_SUFFIX = "$TagHelper";


    public static class ApplicationUtils {
        public static final String APPLICATION_NAME = "application";
        public static final String ANONYMOUS_PACKAGE = "android.app";
        public static final String ACTIVITY = "Activity";
        public static final String ANONYMOUS_NAME = "ActivityLifecycleCallbacks";
        public static final String ACTIVITY_CREATE = "onActivityCreated";
        public static final String ACTIVITY_START = "onActivityStarted";
        public static final String ACTIVITY_RESUME = "onActivityResumed";
        public static final String ACTIVITY_PAUSE = "onActivityPaused";
        public static final String ACTIVITY_STOP = "onActivityStopped";
        public static final String ACTIVITY_SAVEINSTANCE = "onActivitySaveInstanceState";
        public static final String ACTIVITY_DESTORY = "onActivityDestroyed";
    }


    public static class InnerClassUtils {
        public static final String INNER_CLASS_SUFFIX = "$TagBind";
    }

}
