package com.mizan.wealth;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.RemoteViews;

import org.json.JSONArray;

public class MizanWidget extends AppWidgetProvider {
    public static final String ACTION_REFRESH = "com.mizan.wealth.WIDGET_REFRESH";
    private static final int[] CELL = { R.id.d0, R.id.d1, R.id.d2, R.id.d3, R.id.d4, R.id.d5, R.id.d6 };
    private static final String[] LETTERS = { "M", "T", "W", "T", "F", "S", "S" };

    @Override
    public void onUpdate(Context ctx, AppWidgetManager mgr, int[] ids) {
        for (int id : ids) render(ctx, mgr, id);
    }

    @Override
    public void onReceive(Context ctx, Intent intent) {
        super.onReceive(ctx, intent);
        if (ACTION_REFRESH.equals(intent.getAction())) {
            AppWidgetManager mgr = AppWidgetManager.getInstance(ctx);
            int[] ids = mgr.getAppWidgetIds(new ComponentName(ctx, MizanWidget.class));
            if (ids != null) {
                for (int id : ids) render(ctx, mgr, id);
            }
        }
    }

    private void render(Context ctx, AppWidgetManager mgr, int id) {
        RemoteViews v = new RemoteViews(ctx.getPackageName(), R.layout.widget_mizan);
        String[] st = { "x", "x", "x", "x", "x", "x", "x" };
        try {
            SharedPreferences sp = ctx.getSharedPreferences("CapacitorStorage", Context.MODE_PRIVATE);
            String data = sp.getString("mizan.week", null);
            if (data != null) {
                JSONArray a = new JSONArray(data);
                for (int i = 0; i < 7 && i < a.length(); i++) st[i] = a.optString(i, "x");
            }
        } catch (Exception e) {
            // keep defaults
        }
        for (int i = 0; i < 7; i++) {
            int bg, fg;
            if ("g".equals(st[i])) { bg = R.drawable.day_green; fg = Color.WHITE; }
            else if ("r".equals(st[i])) { bg = R.drawable.day_red; fg = Color.WHITE; }
            else { bg = R.drawable.day_gray; fg = Color.parseColor("#6B7280"); }
            v.setInt(CELL[i], "setBackgroundResource", bg);
            v.setTextViewText(CELL[i], LETTERS[i]);
            v.setTextColor(CELL[i], fg);
        }
        Intent open = ctx.getPackageManager().getLaunchIntentForPackage(ctx.getPackageName());
        if (open != null) {
            PendingIntent pi = PendingIntent.getActivity(ctx, 0, open,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            v.setOnClickPendingIntent(R.id.widget_root, pi);
        }
        mgr.updateAppWidget(id, v);
    }
}
