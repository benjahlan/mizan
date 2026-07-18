# Mizan — get your Android APK (no software to install)

This bundle builds your **Mizan** app into an installable Android APK using GitHub's free cloud builder.
Everything happens in your web browser. No Android Studio, no coding. About 10 minutes.

---

## Step 1 — Make a free GitHub account
Go to **https://github.com** and sign up (skip if you already have one).

## Step 2 — Create a new repository
1. Click the **+** at the top-right → **New repository**.
2. Repository name: `mizan`
3. Choose **Private**.
4. Click **Create repository**.

## Step 3 — Upload these files
1. On the new empty repo page, click the link **“uploading an existing file”**
   (or **Add file → Upload files**).
2. **Unzip** this bundle on your computer first, then **drag ALL of its contents**
   into the browser window — the whole set at once:
   - `www` (folder)
   - `.github` (folder)   ← important, this is what builds the APK
   - `package.json`
   - `capacitor.config.json`
   - `README.md`

   > If your file manager hides folders that start with a dot, turn on “show hidden files”
   > so you can see and drag the **`.github`** folder. It must be included.
3. Click **Commit changes**.

## Step 4 — Let it build
1. Click the **Actions** tab at the top of your repo.
2. You’ll see a run called **“Build Mizan APK”** working (yellow dot).
3. Wait until it turns into a **green check** (~5–7 minutes the first time).

## Step 5 — Download your APK
1. Click the finished (green) run.
2. Scroll to the bottom to the **Artifacts** section.
3. Download **Mizan-APK** — it downloads as a zip.
4. Unzip it → you get **`app-debug.apk`**.

## Step 6 — Install on your phone
1. Send `app-debug.apk` to your phone (email it to yourself, or copy via USB/Google Drive).
2. Tap the file on your phone.
3. Android will ask to allow **“install unknown apps”** for the app you tapped from
   (your browser or Files app) — allow it once.
4. Install. **Mizan** now appears in your app drawer. 🎉

Your data lives on the phone and persists between opens. Use **Settings → Back up to device / Back up to Google Drive** to keep copies, and **Restore** to load a backup.

---

## Updating later
Change a file in the repo (or re-upload) → GitHub rebuilds automatically → download the new APK from **Actions → latest run → Artifacts**.

## If the build shows a red ✗
Open the failed run, click the red step, and copy the last ~30 lines of the log back to me — I’ll fix it. (It compiles in a standard way, so this is usually a quick tweak.)

## What’s in this version
Full Mizan: multi-currency balances, income by shift, expenses, transfers, checkpoints, zakat, goals, monthly statement, wealth split, edit history, dark mode, English/Arabic, Hijri dates — plus **local + Google Drive backup**, a **weekly reminder notification**, and an in-app **“good day / bad day” week strip**.

**Coming in the next build (once you confirm this one installs):** real **fingerprint/face unlock** and a **home-screen widget** showing the week’s good/bad days.
