package com.mizan.wealth;

import android.content.Context;
import android.content.Intent;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "MizanWidget")
public class MizanWidgetPlugin extends Plugin {
    @PluginMethod
    public void refresh(PluginCall call) {
        Context ctx = getContext();
        Intent i = new Intent(ctx, MizanWidget.class);
        i.setAction(MizanWidget.ACTION_REFRESH);
        ctx.sendBroadcast(i);
        call.resolve();
    }
}
