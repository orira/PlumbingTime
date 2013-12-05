package com.rsd.plumbing.async;

import android.os.AsyncTask;

import com.rsd.plumbing.activity.MainActivity;
import com.rsd.plumbing.callback.PipesCallBack;
import com.rsd.plumbing.domain.Pipe;
import com.rsd.plumbing.service.PipeService;
import com.rsd.plumbing.service.stub.PipeServiceStub;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by wadereweti on 27/11/13.
 */
public class GetPipesTask extends AsyncTask<Void, Boolean, List<Pipe>> {

    @Inject
    PipeService mPipeService;

    private PipesCallBack mCallBack;

    public GetPipesTask(PipesCallBack callBack) {
        mCallBack = callBack;
    }

    @Override
    protected List<Pipe> doInBackground(Void... params) {
        //return mPipeService.findAll();

        return new PipeServiceStub().getAll();
    }

    @Override
    protected void onPostExecute(List<Pipe> pipes) {
        super.onPostExecute(pipes);

        mCallBack.pipesRetrieved();
    }
}
