package jp.ac.titech.itpro.sdl.tabra.Activity.ThemeList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import jp.ac.titech.itpro.sdl.tabra.R;

/**
 * Created by hskk1120551 on 15/07/17.
 */
public class ThemeListAdapter extends ArrayAdapter<ThemeListItem> {
    static final int RESOURCE_ID = R.layout.theme_list_item;
    private Context mContext;
    private List<ThemeListItem> mItems;
    private LayoutInflater mInflater;

    public ThemeListAdapter(Context context, List<ThemeListItem> items) {
        super(context, RESOURCE_ID, items);

        mContext = context;
        mItems = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<ThemeListItem> getItems() { return mItems; }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = mInflater.inflate(RESOURCE_ID, null);
        }

        final ThemeListItem item = mItems.get(position);

        // テキストビュー
        ((TextView) view.findViewById(R.id.theme_list_item_title)).setText(item.getTitle());

        return view;
    }
}
