package guozhaohui.com.wlylocationchoose.locationchoose.adapters;

import android.content.Context;


import java.util.List;

import guozhaohui.com.wlylocationchoose.locationchoose.model.CityModel;

/**
 * Created by xuan on 16/1/7.
 */
public class CitysAdapter extends AbstractWheelTextAdapter {
    public List<CityModel> mList;
    private Context mContext;
    public CitysAdapter(Context context, List<CityModel> list) {
        super(context);
        mList=list;
        mContext=context;
    }

    @Override
    protected CharSequence getItemText(int index) {
        CityModel cityModel=mList.get(index);
        return cityModel.getName();
    }

    @Override
    public int getItemsCount() {
        return mList.size();
    }
}
