package com.mizan.wealth;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.activity.result.ActivityResult;
import androidx.documentfile.provider.DocumentFile;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Lets the user pick any folder the device exposes through the system file
 * picker (Storage Access Framework) — including a Google Drive folder, since
 * the Drive app registers itself as a document provider. Once a folder is
 * chosen, writeFile() overwrites a single file inside it directly, with no
 * share-sheet prompt, so backups can update in place automatically.
 */
@CapacitorPlugin(name = "MizanBackup")
public class MizanBackupPlugin extends Plugin {

    @PluginMethod
    public void pickFolder(PluginCall call) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION |
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
            Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        );
        startActivityForResult(call, intent, "onFolderPicked");
    }

    @ActivityCallback
    private void onFolderPicked(PluginCall call, ActivityResult result) {
        if (call == null) return;
        if (result.getResultCode() != Activity.RESULT_OK || result.getData() == null) {
            call.reject("cancelled");
            return;
        }
        Uri treeUri = result.getData().getData();
        if (treeUri == null) {
            call.reject("no folder returned");
            return;
        }
        try {
            getContext().getContentResolver().takePersistableUriPermission(
                treeUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            );
        } catch (Exception e) {
            // Some providers don't support persistable permissions; still usable this session.
        }
        String name = treeUri.getLastPathSegment();
        try {
            DocumentFile dir = DocumentFile.fromTreeUri(getContext(), treeUri);
            if (dir != null && dir.getName() != null) name = dir.getName();
        } catch (Exception e) {
            // fall back to the raw path segment
        }
        JSObject ret = new JSObject();
        ret.put("uri", treeUri.toString());
        ret.put("name", name);
        call.resolve(ret);
    }

    @PluginMethod
    public void writeFile(PluginCall call) {
        String uriStr = call.getString("uri");
        String filename = call.getString("filename", "mizan-backup");
        String data = call.getString("data", "");
        if (uriStr == null) {
            call.reject("missing uri");
            return;
        }
        try {
            Uri treeUri = Uri.parse(uriStr);
            DocumentFile dir = DocumentFile.fromTreeUri(getContext(), treeUri);
            if (dir == null || !dir.canWrite()) {
                call.reject("no write access — folder permission may have been revoked");
                return;
            }
            // Remove any prior copy (whatever extension the provider settled on) so we
            // always end up with exactly one backup file, truly overwritten in place.
            DocumentFile[] existing = dir.listFiles();
            for (DocumentFile f : existing) {
                String n = f.getName();
                if (n != null && n.startsWith(filename)) {
                    f.delete();
                }
            }
            DocumentFile file = dir.createFile("application/json", filename + ".json");
            if (file == null) {
                call.reject("could not create backup file");
                return;
            }
            OutputStream out = getContext().getContentResolver().openOutputStream(file.getUri(), "wt");
            if (out == null) {
                call.reject("could not open output stream");
                return;
            }
            out.write(data.getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
            call.resolve();
        } catch (Exception e) {
            call.reject("write failed: " + e.getMessage());
        }
    }
}
