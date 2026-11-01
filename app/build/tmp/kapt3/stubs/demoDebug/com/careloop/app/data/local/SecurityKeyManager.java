package com.careloop.app.data.local;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/careloop/app/data/local/SecurityKeyManager;", "", "()V", "KEY_DB_PASSPHRASE", "", "PREFS_FILE", "getOrCreatePassphrase", "", "context", "Landroid/content/Context;", "app_demoDebug"})
public final class SecurityKeyManager {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREFS_FILE = "careloop_secure_keys";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_DB_PASSPHRASE = "careloop_db_passphrase_bytes";
    @org.jetbrains.annotations.NotNull()
    public static final com.careloop.app.data.local.SecurityKeyManager INSTANCE = null;
    
    private SecurityKeyManager() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final byte[] getOrCreatePassphrase(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
}