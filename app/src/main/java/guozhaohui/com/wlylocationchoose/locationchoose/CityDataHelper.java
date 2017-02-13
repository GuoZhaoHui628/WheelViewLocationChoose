package guozhaohui.com.wlylocationchoose.locationchoose;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import guozhaohui.com.wlylocationchoose.locationchoose.model.CityModel;
import guozhaohui.com.wlylocationchoose.locationchoose.model.DistrictModel;
import guozhaohui.com.wlylocationchoose.locationchoose.model.ProvinceModel;


/**
 * Created by xuan on 16/1/7.
 */
public class CityDataHelper {
    public static String DATABASES_DIR;//数据库目录路径
    public static String DATABASE_NAME="city.db";//要复制的数据库名
    private static CityDataHelper dataHelper;
    private CityDataHelper(Context context){
        DATABASES_DIR="/data/data/"+context.getPackageName()+"/databases/";
    }
    public static CityDataHelper getInstance(Context context){
        if(dataHelper==null){
            dataHelper=new CityDataHelper(context);
        }
        return dataHelper;
    }
    /**
     *
     * @param inStream
     * @param fileNme 文件名
     * @param newPath 要复制到的文件夹路径
     */
    public void copyFile(InputStream inStream, String fileNme, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;

            File file = new File(newPath);
            //保证文件夹存在
            if (!file.exists()) {
                file.mkdir();
            }
            //如果文件存在覆盖
            File newFile=new File(newPath+ File.separator+fileNme);
            if(newFile.exists()){
                newFile.delete();
                newFile.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(newFile);
            byte[] buffer = new byte[1024 * 2];
            int length;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread; //字节数 文件大小
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
            inStream.close();
            fs.close();
        } catch (Exception e) {
            System.out.println("复制文件操作出错");
            e.printStackTrace();

        }
    }

    /**
     * 打开数据库文件
     * @return
     */
    public SQLiteDatabase openDataBase(){
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(
                DATABASES_DIR+DATABASE_NAME, null);
        return database;
    }

    /**
     *
     * @param db
     * @return 查询所有的省
     */
    public List<ProvinceModel> getProvice(SQLiteDatabase db){
        String sql="SELECT * FROM ChooseCityModel where level = 1 ORDER BY cityID";
        Cursor cursor = db.rawQuery(sql,null);
        List<ProvinceModel> list=new ArrayList<ProvinceModel>();
        if (cursor!=null&&cursor.getCount() > 0) {
            while (cursor.moveToNext()){
                ProvinceModel provinceModel=new ProvinceModel();
                provinceModel.setCityID(cursor.getInt(cursor.getColumnIndex("cityID")));
                provinceModel.setParentId(cursor.getInt(cursor.getColumnIndex("parentId")));
                provinceModel.setLevel(cursor.getInt(cursor.getColumnIndex("level")));
                provinceModel.setName(cursor.getString(cursor.getColumnIndex("name")));
                provinceModel.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
                list.add(provinceModel);
            }
        }
        return list;
    }

    /**
     * 根据parentId查询所有的市
     * @param db
     * @param code
     * @return
     */
    public List<CityModel> getCityByParentId(SQLiteDatabase db, String code){
        String sql="SELECT * FROM ChooseCityModel WHERE  level = 2  and parentId = ? ORDER BY cityID";
        Cursor cursor = db.rawQuery(sql,new String[]{code});
        List<CityModel> list=new ArrayList<CityModel>();

        if (cursor!=null&&cursor.getCount() > 0) {

            while (cursor.moveToNext()){
                CityModel cityModel=new CityModel();
                cityModel.setCityID(cursor.getInt(cursor.getColumnIndex("cityID")));
                cityModel.setParentId(cursor.getInt(cursor.getColumnIndex("parentId")));
                cityModel.setLevel(cursor.getInt(cursor.getColumnIndex("level")));
                cityModel.setName(cursor.getString(cursor.getColumnIndex("name")));
                cityModel.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
                list.add(cityModel);
            }
        }
        return list;
    }

    /**
     * 根据parentId查询所有的区
     * @param db
     * @param code
     * @return
     */
    public List<DistrictModel> getDistrictById(SQLiteDatabase db, String code){
        //注意这里的parentId其实就是上一级的cityID
        String sql="SELECT * FROM ChooseCityModel WHERE  level = 3  and parentId = ? ORDER BY cityID";
        Cursor cursor = db.rawQuery(sql,new String[]{code});
        List<DistrictModel> list=new ArrayList<DistrictModel>();
        if (cursor!=null&&cursor.getCount() > 0) {
            while (cursor.moveToNext()){
                DistrictModel districtModel=new DistrictModel();
                districtModel.setCityID(cursor.getInt(cursor.getColumnIndex("cityID")));
                districtModel.setParentId(cursor.getInt(cursor.getColumnIndex("parentId")));
                districtModel.setLevel(cursor.getInt(cursor.getColumnIndex("level")));
                districtModel.setName(cursor.getString(cursor.getColumnIndex("name")));
                districtModel.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
                list.add(districtModel);
            }
        }
        return list;
    }
}
