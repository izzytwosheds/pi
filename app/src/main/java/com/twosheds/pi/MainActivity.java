package com.twosheds.pi;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends ListActivity {
    private List<ResolveInfo> resolveInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent("com.twosheds.pi.action.PI");
        intent.addCategory("com.twosheds.pi.category.PI");
        resolveInfoList = packageManager.queryIntentActivities(intent, 0);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        for (ResolveInfo resolveInfo: resolveInfoList) {
            adapter.add(getString(resolveInfo.activityInfo.labelRes));
        }

        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ResolveInfo resolveInfo = resolveInfoList.get(position);

        ComponentName name = new ComponentName(resolveInfo.activityInfo.applicationInfo.packageName,
                resolveInfo.activityInfo.name);

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(name);

        startActivity(intent);
    }

}
