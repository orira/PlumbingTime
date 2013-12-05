package com.rsd.plumbing.service.stub;

import com.rsd.plumbing.domain.Pipe;
import com.rsd.plumbing.service.PipeService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wadereweti on 27/11/13.
 */

public class PipeServiceStub implements PipeService{

    private String imageNameBase = "pipes";
    int[] sizes = {150, 100, 80, 65, 50, 40, 0};

    @Override
    public List<Pipe> getAll() {

        List<Pipe> existingPipes = Pipe.findAll();

        if (existingPipes.size() == 0) {
            List<Pipe> pipes = new ArrayList<Pipe>();

            for (int i=0; i<sizes.length; i++) {

                Pipe pipe = new Pipe();
                pipe.size = sizes[i];
                pipe.imageName = imageNameBase + (i + 1);
                pipe.save();
                pipes.add(pipe);
            }

            return pipes;
        } else {
            return existingPipes;
        }
    }
}
