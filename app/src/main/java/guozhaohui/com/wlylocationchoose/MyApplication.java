package guozhaohui.com.wlylocationchoose;

import android.app.Application;

import java.io.InputStream;

import guozhaohui.com.wlylocationchoose.locationchoose.CityDataHelper;

/**
 * Created by ${GuoZhaoHui} on 2017/2/13.
 * Abstract:
 */

public class MyApplication extends Application {

    private CityDataHelper dataHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * 放在application中，让app一启动就把raw中文件copy到 "/data/data/"+context.getPackageName()+"/databases/"
         * 这是app读取数据的方法，不管是将数据库文件放在raw或者assets中都是一样
         */
        dataHelper=CityDataHelper.getInstance(this);
        InputStream in = this.getResources().openRawResource(R.raw.city);
        dataHelper.copyFile(in,CityDataHelper.DATABASE_NAME,CityDataHelper.DATABASES_DIR);

    }
}
