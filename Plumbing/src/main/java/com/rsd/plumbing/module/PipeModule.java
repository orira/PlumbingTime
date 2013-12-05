package com.rsd.plumbing.module;

import android.content.Context;

import com.rsd.plumbing.application.PlumbingApplication;
import com.rsd.plumbing.async.GetPipesTask;
import com.rsd.plumbing.service.PipeService;
import com.rsd.plumbing.service.stub.PipeServiceStub;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wadereweti on 28/11/13.
 */
@Module(
    library = true,
    injects = GetPipesTask.class
)
public class PipeModule {

    private static final boolean useStubs = true;

    @Provides @Singleton
    public static PipeService providePipeService() {
        if (useStubs) {
            return new PipeServiceStub();
        }

        return null;
    }
}
