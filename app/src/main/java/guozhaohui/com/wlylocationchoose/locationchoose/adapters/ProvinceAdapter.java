package guozhaohui.com.wlylocationchoose.locationchoose.adapters;

import android.content.Context;


import java.util.List;

import guozhaohui.com.wlylocationchoose.locationchoose.model.ProvinceModel;

/**
 * Created by xuan on 16/1/7.
 */
public class ProvinceAdapter extends AbstractWheelTextAdapter {
    public List<ProvinceModel> mList;
    private Context mContext;
    public ProvinceAdapter(Context context, List<ProvinceModel> list) {
        super(context);
        mList=list;
        mContext=context;
    }

    @Override
    protected CharSequence getItemText(int index) {
        ProvinceModel provinceModel=mList.get(index);
        return provinceModel.getName();
    }

    @Override
    public int getItemsCount() {
        return mList.size();
    }
}
