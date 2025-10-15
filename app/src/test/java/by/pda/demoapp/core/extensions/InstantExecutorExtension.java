package by.pda.demoapp.android.utils;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.arch.core.executor.TaskExecutor;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * A JUnit 5 extension that swaps the background executor used by the Architecture Components
 * with a different one which executes each task synchronously.
 * <p>
 * You can use this rule for your host side tests that use Architecture Components.
 */
public class InstantExecutorExtension implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        ArchTaskExecutor.getInstance().setDelegate(new TaskExecutor() {
            @Override
            public void executeOnDiskIO(@NonNull Runnable runnable) { runnable.run(); }
            @Override
            public void postToMainThread(@NonNull Runnable runnable) { runnable.run(); }
            @Override
            public boolean isMainThread() { return true; }
        });
    }

    @Override
    public void afterEach(ExtensionContext context) {
        ArchTaskExecutor.getInstance().setDelegate(null);
    }
}