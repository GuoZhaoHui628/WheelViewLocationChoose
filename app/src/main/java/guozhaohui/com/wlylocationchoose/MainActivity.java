package guozhaohui.com.wlylocationchoose;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import guozhaohui.com.wlylocationchoose.locationchoose.CityDataHelper;
import guozhaohui.com.wlylocationchoose.locationchoose.OnWheelChangedListener;
import guozhaohui.com.wlylocationchoose.locationchoose.WheelView;
import guozhaohui.com.wlylocationchoose.locationchoose.adapters.AreaAdapter;
import guozhaohui.com.wlylocationchoose.locationchoose.adapters.CitysAdapter;
import guozhaohui.com.wlylocationchoose.locationchoose.adapters.ProvinceAdapter;
import guozhaohui.com.wlylocationchoose.locationchoose.model.CityModel;
import guozhaohui.com.wlylocationchoose.locationchoose.model.DistrictModel;
import guozhaohui.com.wlylocationchoose.locationchoose.model.ProvinceModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,OnWheelChangedListener {

    //popupwindow
    private PopupWindow mPopupWindow;
    private WheelView provinceView;
    private WheelView cityView;
    private WheelView districtView;
    private List<ProvinceModel> provinceDatas = new ArrayList<>();
    private List<CityModel> cityDatas = new ArrayList<>();
    private List<DistrictModel> districtDatas = new ArrayList<>();
    private String mCurrentProvince;
    private String mCurrentCity;
    private String mCurrentDistrict;
    private TextView btn_myinfo_sure,btn_myinfo_cancel;
    private ProvinceAdapter provinceAdapter;
    private CitysAdapter citysAdapter;
    private AreaAdapter areaAdapter;
    private SQLiteDatabase db;
    private CityDataHelper dataHelper;
    private final int TEXTSIZE=17;//选择器的字体大小
    private Button bt;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = LayoutInflater.from(this).inflate(R.layout.activity_main,null);
        bt = (Button) this.findViewById(R.id.bt);
        bt.setOnClickListener(this);

    }


    public void initPopupWindow(){

        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_locationchoose, null);
        mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(R.style.popup_locationchoose_bottom);

        //  pickText = (TextView)popupView.findViewById(R.id.tv_pickText);
        provinceView = (WheelView)popupView.findViewById(R.id.provinceView);
        cityView = (WheelView)popupView.findViewById(R.id.cityView);
        districtView = (WheelView)popupView.findViewById(R.id.districtView);

        //确定或者取消
        btn_myinfo_sure = (TextView)popupView.findViewById(R.id.btn_myinfo_sure);
        btn_myinfo_cancel = (TextView) popupView.findViewById(R.id.btn_myinfo_cancel);
        btn_myinfo_cancel.setOnClickListener(this);
        btn_myinfo_sure.setOnClickListener(this);

        // 设置可见条目数量
        provinceView.setVisibleItems(7);
        cityView.setVisibleItems(7);
        districtView.setVisibleItems(7);

        // 添加change事件
        provinceView.addChangingListener(this);
        // 添加change事件
        cityView.addChangingListener(this);
        // 添加change事件
        districtView.addChangingListener(this);

        initpopData();

    }

    private void initpopData() {
        //初始化数据
        dataHelper = CityDataHelper.getInstance(this);
        db = dataHelper.openDataBase();
        provinceDatas = dataHelper.getProvice(db);
        if (provinceDatas.size() > 0) {

            //弹出popup时，省wheelview中当前的省其实就是省集合的第一个
            mCurrentProvince = provinceDatas.get(0).getName();

            //根据省cityid查询到第一个省下面市的集合
            cityDatas = dataHelper.getCityByParentId(db, provinceDatas.get(0).getCityID()+"");
        }
        if (cityDatas.size() > 0) {
            //根据市cityid查询到第一个市集合下面区的集合
            districtDatas = dataHelper.getDistrictById(db, cityDatas.get(0).getCityID()+"");

        }
        //wheelview的适配器代码
        provinceAdapter = new ProvinceAdapter(this, provinceDatas);
        provinceAdapter.setTextSize(TEXTSIZE);//设置字体大小
        provinceView.setViewAdapter(provinceAdapter);

        updateCitys();
        updateAreas();
    }

    private void updateCitys() {
        int pCurrent = provinceView.getCurrentItem();
        if (provinceDatas.size() > 0) {
            //这里是必须的的，上面得到的集合只是第一个省下面所有市的集合及第一个市下面所有区的集合
            //这里得到的是相应省下面对应市的集合
            cityDatas = dataHelper.getCityByParentId(db, provinceDatas.get(pCurrent).getCityID()+"");
        } else {
            cityDatas.clear();
        }
        citysAdapter = new CitysAdapter(this, cityDatas);
        citysAdapter.setTextSize(TEXTSIZE);
        cityView.setViewAdapter(citysAdapter);
        if (cityDatas.size() > 0) {
            //默认省下面 市wheelview滑动第一个，显示第一个市
            cityView.setCurrentItem(0);
            mCurrentCity = cityDatas.get(0).getName();
        } else {
            mCurrentCity = "";
        }
        updateAreas();
    }


    private void updateAreas() {
        int cCurrent = cityView.getCurrentItem();
        if (cityDatas.size() > 0) {
            districtDatas = dataHelper.getDistrictById(db, cityDatas.get(cCurrent).getCityID()+"");
        } else {
            districtDatas.clear();
        }
        areaAdapter = new AreaAdapter(this, districtDatas);
        areaAdapter.setTextSize(TEXTSIZE);
        districtView.setViewAdapter(areaAdapter);
        if (districtDatas.size() > 0) {
            mCurrentDistrict = districtDatas.get(0).getName();
            districtView.setCurrentItem(0);
        } else {
            mCurrentDistrict = "";
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.bt:
                initPopupWindow();
                mPopupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
                break;

            case R.id.btn_myinfo_cancel:

                mPopupWindow.dismiss();

                break;

            case R.id.btn_myinfo_sure:
                Toast.makeText(this,mCurrentProvince+"-"+mCurrentCity+"-"+mCurrentDistrict,Toast.LENGTH_SHORT).show();
                mPopupWindow.dismiss();
                break;
        }


    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {

        if (wheel == provinceView) {
            mCurrentProvince = provinceDatas.get(newValue).getName();
            updateCitys();
        }
        if (wheel == cityView) {
            mCurrentCity = cityDatas.get(newValue).getName();
            updateAreas();
        }
        if (wheel == districtView) {
            mCurrentDistrict = districtDatas.get(newValue).getName();
        }

    }
}
