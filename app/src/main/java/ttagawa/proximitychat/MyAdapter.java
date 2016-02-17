package ttagawa.proximitychat;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tyler on 2/14/16.
 */
public class MyAdapter extends ArrayAdapter<ResultList> {
    int resource;
    Context context;

    public MyAdapter(Context _context, int _resource, List<ResultList> items) {
        super(_context, _resource, items);
        resource = _resource;
        context = _context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout newView;

        ResultList w = getItem(position);

        // Inflate a new view if necessary.
        if (convertView == null) {
            newView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource,  newView, true);
        } else {
            newView = (LinearLayout) convertView;
        }
        TextView tv = (TextView) newView.findViewById(R.id.rowTextView);
        Log.i("nickname","nickname:"+w.nickname);
        tv.setText(Html.fromHtml(w.message+"<br>"+"<small>"+w.nickname+"</small>"));
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        Log.i("uid","userid:"+settings.getString("user_id",""));
        Log.i("uid","resuld id:"+w.userId);
        if(w.userId.equals(settings.getString("user_id",""))){
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(400,0,0,50);
            tv.setLayoutParams(lp);
            tv.setText(Html.fromHtml(w.message + "<br>" + "<small>" + w.nickname +"(you)" + "</small>"));
        }else{
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 400, 50);
            tv.setLayoutParams(lp);
        }
        return newView;
    }
}
